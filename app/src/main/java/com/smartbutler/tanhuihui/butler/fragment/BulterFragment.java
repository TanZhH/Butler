package com.smartbutler.tanhuihui.butler.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.smartbutler.tanhuihui.butler.R;
import com.smartbutler.tanhuihui.butler.adapter.ChatAdapter;
import com.smartbutler.tanhuihui.butler.entity.ChatEntity;
import com.smartbutler.tanhuihui.butler.utils.SharedUtils;
import com.smartbutler.tanhuihui.butler.utils.StaticClass;
import com.smartbutler.tanhuihui.butler.utils.UtilTools;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建者：   TANHUIHUI
 * 项  目：   Butler
 * 包  名：   com.smartbutler.tanhuihui.butler.fragment
 * 创建日期： 2017/6/5
 * 描  述：   问答机器人
 */

public class BulterFragment extends Fragment implements View.OnClickListener {

    private ListView lv_chat;
    private EditText et_text;
    private Button bt_send;

    private List<ChatEntity> chatEntityList = new ArrayList<>();
    //ListView适配器
    private ChatAdapter adapter;
    //URL接口
    private String URL;

    //TTS
    private SpeechSynthesizer mTts;

    //是否开启语音播报功能
    private boolean isSpeek;

    //返回内容
    private String text;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bulter, null);
        initview(view);
        setListener();
        addLeftItem("你好，很高兴为你服务");
        return view;
    }

    private void setListener() {
        bt_send.setOnClickListener(this);
    }

    private void initview(View view) {
        lv_chat = (ListView) view.findViewById(R.id.lv_chat);
        et_text = (EditText) view.findViewById(R.id.et_text);
        bt_send = (Button) view.findViewById(R.id.bt_send);

        //初始化提示信息
        adapter = new ChatAdapter(getActivity(), chatEntityList);
        lv_chat.setAdapter(adapter);

        //初始化TTS

        //1.创建SpeechSynthesizer对象, 第二个参数：本地合成时传InitListener
        mTts = SpeechSynthesizer.createSynthesizer(getActivity(), null);
        //2.合成参数设置，详见《科大讯飞MSC API手册(Android)》SpeechSynthesizer 类
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaomei");//设置发音人
        mTts.setParameter(SpeechConstant.SPEED, "50");//设置语速
        mTts.setParameter(SpeechConstant.VOLUME, "80");//设置音量，范围0~100
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); //设置云端
        //设置合成音频保存位置（可自定义保存位置），保存在“./sdcard/iflytek.pcm”
        //保存在SD卡需要在AndroidManifest.xml添加写SD卡权限
        //如果不需要保存合成音频，注释该行代码
        //mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, "./sdcard/iflytek.pcm");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_send:
                String str = et_text.getText().toString();
                if (!TextUtils.isEmpty(str)) {
                    addRightItem(str);
                    et_text.setText("");
                    URL = "http://op.juhe.cn/robot/index?info=" + str + "&key=" + StaticClass.CHAT_APP_KEY;
                    RxVolley.get(URL, new HttpCallback() {
                        @Override
                        public void onSuccessInAsync(byte[] s) {
                            String t = new String(s);
                            text = parsingJSON(t);
                        }

                        @Override
                        public void onFinish() {
                            addLeftItem(text);
                        }
                    });
                } else
                    UtilTools.toash(getActivity(), "内容不能为空");
                break;
        }
    }

    //加入左边
    public void addLeftItem(final String str) {
        ChatEntity chatEntity = new ChatEntity();
        chatEntity.setText(str);
        chatEntity.setType(ChatAdapter.VALUE_LEFT_ITEM);
        chatEntityList.add(chatEntity);
        //获取是否开启自动播报功能
        isSpeek = SharedUtils.getBool(getActivity(),"isSpeek",false);
        /**
         * 延时100ms更新适配器
         */
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //通知Adapter进行更新，延时刷新
                adapter.notifyDataSetChanged();
                if(isSpeek)
                    startSpeak(str);
            }
        },100);
        //滚动到最后
        lv_chat.setSelection(lv_chat.getBottom());
    }

    //加入右边
    public void addRightItem(String str) {
        ChatEntity chatEntity = new ChatEntity();
        chatEntity.setText(str);
        chatEntity.setType(ChatAdapter.VALUE_RIGHT_ITEM);
        chatEntityList.add(chatEntity);
        //通知Adapter进行更新
        adapter.notifyDataSetChanged();
        //滚动到最后
        lv_chat.setSelection(lv_chat.getBottom());
    }

    //解析JSON
    public String parsingJSON(String t) {
        JSONObject object = null;
        JSONObject result = null;
        String text = null;
        try {
            object = new JSONObject(t);
            result = (JSONObject) object.get("result");
            text = result.getString("text");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return text;
    }


    //开始说话
    private void startSpeak(String text) {
        //3.开始合成
        mTts.startSpeaking(text, mSynListener);
    }
    //合成监听器
    private SynthesizerListener mSynListener = new SynthesizerListener() {
        //会话结束回调接口，没有错误时，error为null
        public void onCompleted(SpeechError error) {
        }

        //缓冲进度回调
        //percent为缓冲进度0~100，beginPos为缓冲音频在文本中开始位置，endPos表示缓冲音频在文本中结束位置，info为附加信息。
        public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
        }

        //开始播放
        public void onSpeakBegin() {
        }

        //暂停播放
        public void onSpeakPaused() {
        }

        //播放进度回调
        //percent为播放进度0~100,beginPos为播放音频在文本中开始位置，endPos表示播放音频在文本中结束位置.
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
        }

        //恢复播放回调接口
        public void onSpeakResumed() {
        }

        //会话事件回调接口
        public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
        }
    };
}
