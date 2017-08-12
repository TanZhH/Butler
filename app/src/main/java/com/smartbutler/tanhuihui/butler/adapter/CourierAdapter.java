package com.smartbutler.tanhuihui.butler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.smartbutler.tanhuihui.butler.R;
import com.smartbutler.tanhuihui.butler.entity.CourierEntity;

import java.util.List;

/**
 * 创建者：   TANHUIHUI
 * 项  目：   Butler
 * 包  名：   com.smartbutler.tanhuihui.butler.adapter
 * 创建日期： 2017/6/17
 * 描  述：   快递适配器
 */

public class CourierAdapter extends BaseAdapter {

    private Context mcontext;
    private List<CourierEntity> courierEntityList;
    private LayoutInflater inflater;

    public CourierAdapter(Context mcontext, List<CourierEntity> courierEntityList) {
        this.mcontext = mcontext;
        this.courierEntityList = courierEntityList;
        inflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return courierEntityList.size();
    }

    @Override
    public Object getItem(int position) {
        return courierEntityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_courier,null);
            holder.tv_remark = (TextView) convertView.findViewById(R.id.tv_remark);
            holder.tv_datetime = (TextView) convertView.findViewById(R.id.tv_datetime);
            convertView.setTag(holder);
        }else
            holder = (ViewHolder) convertView.getTag();
        holder.tv_remark.setText(courierEntityList.get(position).getRemark());
        holder.tv_datetime.setText(courierEntityList.get(position).getDatetime());
        return convertView;
    }

    private class ViewHolder{
        private TextView tv_remark;
        private TextView tv_datetime;
    }
}
