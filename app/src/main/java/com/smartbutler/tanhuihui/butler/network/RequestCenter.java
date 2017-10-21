package com.smartbutler.tanhuihui.butler.network;

import com.smartbutler.tanhuihui.butler.network.Request.CommonRequest;
import com.smartbutler.tanhuihui.butler.network.Request.RequestParams;
import com.smartbutler.tanhuihui.butler.network.listener.DisposeDataHandle;
import com.smartbutler.tanhuihui.butler.network.listener.DisposeDataListener;
import com.smartbutler.tanhuihui.butler.tinker.BasePatch;

/**
 * 创建者：   TANHUIHUI
 * 项  目：   Butler
 * 包  名：   com.smartbutler.tanhuihui.butler.network
 * 创建日期： 2017/10/7
 * 描  述：   请求发送中心
 */

public class RequestCenter {
    //根据参数发送所有post请求
    public static void postRequest(String url, RequestParams params , DisposeDataListener listener , Class<?> clazz){
        CommonOkHttpClient.get(CommonRequest.createGetResquest(url,params),new DisposeDataHandle(listener,clazz));
    }

    /**
     * 询问是否有patch可以更新
     * @param listener
     */
    public static void requestPatchUpdateInfo(DisposeDataListener listener){
        RequestCenter.postRequest(HttpConstant.UPDATE_PATCH_URL , null , listener, BasePatch.class);
    }

    /**
     * 文件下载
     * @param url
     * @param path
     * @param listener
     */
    public static void downloadFile(String url, String path ,
                                    DisposeDataListener listener){
        CommonOkHttpClient.downloadFile(CommonRequest.createGetResquest(url,null),
                new DisposeDataHandle(listener,path));
    }
}
