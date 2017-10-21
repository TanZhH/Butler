package com.smartbutler.tanhuihui.butler.network.listener;

/**
 * 创建者：   TANHUIHUI
 * 项  目：   Butler
 * 包  名：   com.smartbutler.tanhuihui.butler.network.listener
 * 创建日期： 2017/10/7
 * 描  述：  监听下载进度
 */

public interface DisposeDownLoadListener extends DisposeDataListener{
    public void onProgress(int progress);
}
