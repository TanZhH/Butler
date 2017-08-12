package com.smartbutler.tanhuihui.butler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.smartbutler.tanhuihui.butler.entity.ComEntity;

import java.util.List;

/**
 * 创建者：   TANHUIHUI
 * 项  目：   Butler
 * 包  名：   com.smartbutler.tanhuihui.butler.adapter
 * 创建日期： 2017/6/16
 * 描  述：   查询快递公司适配器
 */

public class ComAdapter extends BaseAdapter {

    private Context mcontext;
    private List<ComEntity> comEntityList;
    private LayoutInflater inflater;

    public ComAdapter(Context mcontext, List<ComEntity> comEntityList) {
        this.mcontext = mcontext;
        this.comEntityList = comEntityList;
        inflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return comEntityList.size();
    }

    @Override
    public Object getItem(int position) {
        return comEntityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(android.R.layout.simple_spinner_item, null);
            viewHolder.text = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(viewHolder);
        }else
            viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.text.setText(comEntityList.get(position).getName());
        return convertView;
    }

    private class ViewHolder{
        private TextView text;
    }

}
