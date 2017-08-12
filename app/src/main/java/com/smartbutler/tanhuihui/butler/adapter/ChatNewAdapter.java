package com.smartbutler.tanhuihui.butler.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartbutler.tanhuihui.butler.R;
import com.smartbutler.tanhuihui.butler.entity.ChatNewEntity;
import com.smartbutler.tanhuihui.butler.utils.LogUtils;
import com.smartbutler.tanhuihui.butler.utils.PicassoUtil;

import java.util.List;

/**
 * 创建者：   TANHUIHUI
 * 项  目：   Butler
 * 包  名：   com.smartbutler.tanhuihui.butler.adapter
 * 创建日期： 2017/6/28
 * 描  述：   微信精选适配器
 */

public class ChatNewAdapter extends BaseAdapter {

    private List<ChatNewEntity> chatNewEntityList;
    private Context mcontext;
    private LayoutInflater inflater;
    private ChatNewEntity chatNew;
    //屏幕宽
    private int wide;
    //屏幕长
    private int height;

    public ChatNewAdapter(List<ChatNewEntity> chatNewEntityList, Context mcontext) {
        this.chatNewEntityList = chatNewEntityList;
        this.mcontext = mcontext;
        inflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        WindowManager wm = (WindowManager) mcontext.getSystemService(Context.WINDOW_SERVICE);
        //获取屏幕像素密度
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        //获取屏幕宽度以及长度
        wide = dm.widthPixels;
        height = dm.heightPixels;
    }

    @Override
    public int getCount() {
        return chatNewEntityList.size();
    }

    @Override
    public Object getItem(int position) {
        return chatNewEntityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_chatnew,null);
            holder.iv_chatnew = (ImageView) convertView.findViewById(R.id.iv_chatnew);
            holder.tv_chattitle = (TextView) convertView.findViewById(R.id.tv_chattitle);
            holder.tv_chatnewfrom = (TextView) convertView.findViewById(R.id.tv_chatnewfrom);
            convertView.setTag(holder);
        }else
            holder = (ViewHolder) convertView.getTag();

        chatNew = chatNewEntityList.get(position);
        holder.tv_chattitle.setText(chatNew.getTitle());
        holder.tv_chatnewfrom.setText(chatNew.getSource());
        //设置图片
        if(!TextUtils.isEmpty(chatNew.getImgURL())) {
            PicassoUtil.loadSizeImage(mcontext, chatNew.getImgURL(), holder.iv_chatnew, wide / 4, height/6);
        }
        return convertView;
    }

    private class ViewHolder{
        private ImageView iv_chatnew;
        private TextView tv_chattitle;
        private TextView tv_chatnewfrom;
    }

}
