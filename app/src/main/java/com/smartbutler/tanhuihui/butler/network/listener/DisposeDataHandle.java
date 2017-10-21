package com.smartbutler.tanhuihui.butler.network.listener;


/**
 * 创建者：   TANHUIHUI
 * 项  目：   Butler
 * 包  名：   com.smartbutler.tanhuihui.butler.network.listener
 * 创建日期： 2017/10/7
 * 描  述：   部署处理事件类
 */

public class DisposeDataHandle {
    public DisposeDataListener mListener = null;
    public Class<?> mClass = null;
    public String mSource = null;
    public DisposeDataHandle(DisposeDataListener listener){
        this.mListener = listener;
    }
    public DisposeDataHandle(DisposeDataListener listener , Class<?> clazz){
        this.mListener = listener;
        this.mClass = clazz;
    }
    public DisposeDataHandle(DisposeDataListener listener,String mSource){
        this.mListener = listener;
        this.mSource = mSource;
    }
}
