package com.smartbutler.tanhuihui.butler.tinker;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.smartbutler.tanhuihui.butler.network.RequestCenter;
import com.smartbutler.tanhuihui.butler.network.listener.DisposeDataListener;
import com.smartbutler.tanhuihui.butler.network.listener.DisposeDownLoadListener;
import com.smartbutler.tanhuihui.butler.utils.LogUtils;
import com.smartbutler.tanhuihui.butler.utils.SharedUtils;

import java.io.File;

/**
 * 创建者：   TANHUIHUI
 * 项  目：   Butler
 * 包  名：   com.smartbutler.tanhuihui.butler.tinker
 * 创建日期： 2017/10/7
 * 描  述：   应用程序Tinker更新服务：
 *           1、从服务器下载patch文件
 *           2、使用TinkerManager完成patch文件加载
 *           3、patch文件会在下次进程启动时生效
 */

public class TinkerService extends Service {

    private static final String FILE_END = ".apk"; //文件后缀名
    private static final int DOWNLOAD_PATCH = 0x01; //下载patch文件信息
    private static final int UPDATE_PATCH = 0X02; //检查是否有patch更新

    private String mPatchFileDir; //patch要保存的文件名
    private String mFilePatch; //patch文件保存路径
    private BasePatch basePatch;//服务器Patch信息

    private Handler mHandler =
            new Handler(){

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case UPDATE_PATCH:
                    checkPatchInfo();
                    break;
                case DOWNLOAD_PATCH:
                    downloadPatch();
                    break;
            }
        }
    };

    //下载Patch文件
    private void downloadPatch() {
        mFilePatch = mPatchFileDir.concat(String.valueOf(System.currentTimeMillis())).concat(FILE_END);
        RequestCenter.downloadFile(basePatch.downloadUrl, mFilePatch
                , new DisposeDownLoadListener() {
                    @Override
                    public void onProgress(int progress) {

                    }

                    @Override
                    public void onSuccess(Object responseObj) {
                        LogUtils.i("patchload");
                        TinkerManager.loadPatch(mFilePatch);
                        SharedUtils.putString(getBaseContext(),"tinkerUpdate",basePatch.ecode);
                    }

                    @Override
                    public void onFailure(Object reasonObj) {
                        stopSelf();
                    }
                });
    }

    //检查服务器中是否有更新patch文件
    private void checkPatchInfo() {
        RequestCenter.requestPatchUpdateInfo(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                basePatch = (BasePatch)responseObj;
                LogUtils.i(basePatch.patchMessage);
                String ecode = SharedUtils.getString(getBaseContext(),"tinkerUpdate","0");
                LogUtils.i(ecode + "    " + basePatch.ecode + "  abb");
                if(!basePatch.ecode.equals(ecode)){
                    mHandler.sendEmptyMessage(DOWNLOAD_PATCH);
                }else {
                    stopSelf();
                    LogUtils.i("TinkerTest");
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
                stopSelf();
            }
        });
    }


    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    //初始化变量
    private void init() {
        mPatchFileDir = getExternalCacheDir().getAbsolutePath().concat("/tinkerpatch/");
        File patchFileDir = new File(mPatchFileDir);
        try {
            if(patchFileDir == null || !patchFileDir.exists()){
                patchFileDir.mkdir();  //文件夹不存在则创建
            }
        }catch (Exception e){
            e.printStackTrace();
            stopSelf(); //无法正常创建文件夹，则终止服务
        }
    }

    @Override
    public int onStartCommand(Intent intent,int flags, int startId) {
        //检查服务器中是否有patch更新
        mHandler.sendEmptyMessage(UPDATE_PATCH);
        return START_NOT_STICKY; //被系统回收不再重启
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.i("tinkerService is dead");
    }
}
