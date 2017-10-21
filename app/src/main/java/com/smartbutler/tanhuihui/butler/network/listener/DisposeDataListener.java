package com.smartbutler.tanhuihui.butler.network.listener;

/**
 * 创建者：   TANHUIHUI
 * 项  目：   Butler
 * 包  名：   com.smartbutler.tanhuihui.butler.network.listener
 * 创建日期： 2017/10/7
 * 描  述：   Dispose部署，用于请求回调处理
 */

public interface DisposeDataListener {
    //请求成功时回调事件处理
    public void onSuccess(Object responseObj);
    //请求失败时回调事件处理
    public void onFailure(Object reasonObj);
}
