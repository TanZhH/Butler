package com.smartbutler.tanhuihui.butler.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.smartbutler.tanhuihui.butler.R;
import com.smartbutler.tanhuihui.butler.adapter.ChatNewAdapter;
import com.smartbutler.tanhuihui.butler.entity.ChatEntity;
import com.smartbutler.tanhuihui.butler.entity.ChatNewEntity;
import com.smartbutler.tanhuihui.butler.ui.NewActivity;
import com.smartbutler.tanhuihui.butler.utils.LogUtils;
import com.smartbutler.tanhuihui.butler.utils.StaticClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建者：   TANHUIHUI
 * 项  目：   Butler
 * 包  名：   com.smartbutler.tanhuihui.butler.fragment
 * 创建日期： 2017/6/5
 * 描  述：   微信精选
 */

public class WechatFragment extends Fragment {

    //微信精选ListView
    private ListView lv_chatnew;
    //当前页数
    private int curPage = 1;
    //微信精选接口
    private String URL = "http://v.juhe.cn/weixin/query?key=" + StaticClass.WECHAT_NEW_APP_KEY +
            "&pno=" + curPage;

    //微信精选集合
    private List<ChatNewEntity> chatNewEntityList = new ArrayList<>();

    //脚布局加载
    private View lv_foot;
    //脚布局高度
    private int footHight;
    //是否到达了底部
    private boolean isScrollBottom = false;
    //是否进行刷新
    private boolean isfreshing = false;
    //适配器
    private ChatNewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wechat, null);
        initView(view);
        setListener();
        return view;
    }

    private void setListener() {
        //listView上拉刷新监听
        lv_chatnew.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //滑动结束或者手指离开屏幕
                if (scrollState == SCROLL_STATE_IDLE || scrollState == SCROLL_STATE_FLING) {
                    //当滑到最底，且没在刷新
                    if (isScrollBottom && !isfreshing) {
                        curPage++;
                        URL = "http://v.juhe.cn/weixin/query?key=" + StaticClass.WECHAT_NEW_APP_KEY +
                                "&pno=" + curPage;
                        //加载数据
                        RxVolley.get(URL, new HttpCallback() {

                            @Override
                            public void onPreStart() {
                                //上拉显示，脚布局可见
                                lv_foot.setPadding(0, 0, 0, 0);
                                lv_chatnew.setSelection(lv_chatnew.getCount());
                                isfreshing = true;
                            }

                            @Override
                            public void onSuccessInAsync(byte[] s) {
                                String t = new String(s);
                                parsingJSON(t);
                            }

                            @Override
                            public void onFinish() {
                                //隐藏脚布局
                                lv_foot.setPadding(0, -footHight, 0, 0);
                                //是否正在进行刷新
                                isfreshing = false;
                                //刷新adapter
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //滚动条是否到达了最后
                if (view.getLastVisiblePosition() == totalItemCount - 1) {
                    isScrollBottom = true;
                } else
                    isScrollBottom = false;
            }
        });

        //选中item点击监听
        lv_chatnew.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), NewActivity.class);
                //选中item
                ChatNewEntity chatEntity = chatNewEntityList.get(position);
                intent.putExtra("URL", chatEntity.getUrl());
                intent.putExtra("title", chatEntity.getTitle());
                startActivity(intent);
            }
        });

    }

    //初始化
    private void initView(View view) {
        lv_chatnew = (ListView) view.findViewById(R.id.lv_chatnew);

        //初始化ListView脚布局
        lv_foot = View.inflate(getActivity(), R.layout.lv_chatnews_foot, null);
        //脚布局高度自适合
        lv_foot.measure(0, 0);
        footHight = lv_foot.getMeasuredHeight();
        //脚布局不可见
        lv_foot.setPadding(0, -footHight, 0, 0);
        //添加脚布局
        lv_chatnew.addFooterView(lv_foot);

        //解析第一页的JSON信息
        RxVolley.get(URL, new HttpCallback() {
            @Override
            public void onSuccessInAsync(byte[] s) {
                String t = new String(s);
                parsingJSON(t);
            }

            @Override
            public void onFinish() {
                //加载适配器
                adapter = new ChatNewAdapter(chatNewEntityList, getActivity());
                lv_chatnew.setAdapter(adapter);
            }
        });
    }

    //解析JSON数据信息
    private void parsingJSON(String t) {
        JSONObject object = null;
        JSONObject result = null;
        JSONArray array = null;
        try {
            object = new JSONObject(t);
            result = object.getJSONObject("result");
            array = result.getJSONArray("list");
            ChatNewEntity chatNewEntity;
            //遍历array
            for (int i = 0; i < array.length(); i++) {
                JSONObject o = (JSONObject) array.get(i);
                chatNewEntity = new ChatNewEntity();
                //设置标题
                chatNewEntity.setTitle(o.getString("title"));
                //设置出处
                chatNewEntity.setSource(o.getString("source"));
                //设置图片URL
                chatNewEntity.setImgURL(o.getString("firstImg"));
                //设置URL
                chatNewEntity.setUrl(o.getString("url"));
                chatNewEntityList.add(chatNewEntity);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
