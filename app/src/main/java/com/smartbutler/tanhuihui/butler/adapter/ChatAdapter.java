package com.smartbutler.tanhuihui.butler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.smartbutler.tanhuihui.butler.R;
import com.smartbutler.tanhuihui.butler.entity.ChatEntity;


import java.util.List;

/**
 * 创建者：   TANHUIHUI
 * 项  目：   Butler
 * 包  名：   com.smartbutler.tanhuihui.butler.adapter
 * 创建日期： 2017/6/18
 * 描  述 ：  聊天机器人适配器
 */

public class ChatAdapter extends BaseAdapter {

    private Context mcontext;
    private List<ChatEntity> chatEntityList;
    private LayoutInflater inflater;

    //左边的 item_layout_left type
    public static final int VALUE_LEFT_ITEM = 1;

    //右边的 item_layout_left type
    public static final int VALUE_RIGHT_ITEM = 2;

    public ChatAdapter(Context mcontext, List<ChatEntity> chatEntityList) {
        this.mcontext = mcontext;
        this.chatEntityList = chatEntityList;
        inflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return chatEntityList.size();
    }

    @Override
    public Object getItem(int position) {
        return chatEntityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LeftViewHolder leftHolder = null;
        RightViewHolder rightHolder = null;

        //类型 type
        int type = getItemViewType(position);

        //根据 type 判断加载
        if(convertView == null){
            switch (type){
                case VALUE_LEFT_ITEM:
                    leftHolder = new LeftViewHolder();
                    convertView = inflater.inflate(R.layout.item_chat_left,null);
                    leftHolder.tv_text = (TextView) convertView.findViewById(R.id.tv_robottext);
                    convertView.setTag(leftHolder);
                    break;
                case VALUE_RIGHT_ITEM:
                    rightHolder = new RightViewHolder();
                    convertView = inflater.inflate(R.layout.item_chat_right,null);
                    rightHolder.tv_text = (TextView) convertView.findViewById(R.id.tv_usertext);
                    convertView.setTag(rightHolder);
                    break;
            }
        }else {
            switch (type){
                case VALUE_LEFT_ITEM:
                    leftHolder = (LeftViewHolder) convertView.getTag();
                    break;
                case VALUE_RIGHT_ITEM:
                    rightHolder = (RightViewHolder) convertView.getTag();
                    break;
            }
            //赋值
            ChatEntity data = chatEntityList.get(position);
            switch (type){
                case VALUE_LEFT_ITEM:
                    leftHolder.tv_text.setText(data.getText());
                    break;
                case VALUE_RIGHT_ITEM:
                    rightHolder.tv_text.setText(data.getText());
                    break;
            }
        }
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return chatEntityList.get(position).getType();
    }

    //返回所有layout的数量
    @Override
    public int getViewTypeCount() {
        return 3;
    }

    private class LeftViewHolder{
        private TextView tv_text;
    }

    private class RightViewHolder{
        private TextView tv_text;
    }
}
