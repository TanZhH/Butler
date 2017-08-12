package com.smartbutler.tanhuihui.butler.adapter;

import android.content.Context;
import android.text.Layout;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.smartbutler.tanhuihui.butler.R;
import com.smartbutler.tanhuihui.butler.entity.PictureEntity;
import com.smartbutler.tanhuihui.butler.utils.PicassoUtil;

import java.util.List;

/**
 * 创建者：   TANHUIHUI
 * 项  目：   Butler
 * 包  名：   com.smartbutler.tanhuihui.butler.adapter
 * 创建日期： 2017/7/1
 * 描  述：   图片精选适配器
 */

public class PictureAdapter extends BaseAdapter {

    private Context mcontext;
    private List<PictureEntity> pList;
    private LayoutInflater inflater;
    //屏幕宽,长
    private int wide;
    private int height;
    private PictureEntity pictureEntity;

    public PictureAdapter(Context mcontext, List<PictureEntity> pList) {
        this.mcontext = mcontext;
        this.pList = pList;
        inflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        WindowManager wm = (WindowManager) mcontext.getSystemService(Context.WINDOW_SERVICE);
        //获取屏幕像素密度
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        //获取屏幕宽度
        wide = dm.widthPixels;
        height = dm.heightPixels;
    }

    @Override
    public int getCount() {
        return pList.size();
    }

    @Override
    public Object getItem(int position) {
        return pList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_picture,null);
            holder = new ViewHolder();
            holder.iv_woman = (ImageView) convertView.findViewById(R.id.iv_woman);
            convertView.setTag(holder);
        }else
            holder = (ViewHolder) convertView.getTag();
        pictureEntity = pList.get(position);
        PicassoUtil.loadSizeImage(mcontext,pictureEntity.getImageURL(),holder.iv_woman,wide/2,height/5);
        return convertView;
    }

    private  class ViewHolder{
        private ImageView iv_woman;
    }
}
