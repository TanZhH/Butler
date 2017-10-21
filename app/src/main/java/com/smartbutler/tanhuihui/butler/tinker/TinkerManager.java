package com.smartbutler.tanhuihui.butler.tinker;

import android.content.Context;

import com.tencent.tinker.lib.tinker.Tinker;
import com.tencent.tinker.lib.tinker.TinkerInstaller;
import com.tencent.tinker.loader.app.ApplicationLike;

/**
 * 创建者：   TANHUIHUI
 * 项  目：   Butler
 * 包  名：   com.smartbutler.tanhuihui.butler.tinker
 * 创建日期： 2017/10/7
 * 描  述：   对Tinker的api封装
 */

public class TinkerManager {
    //是否已完成Tinker的初始化
    private static boolean isInstalled = false;
    private static ApplicationLike mAppLike;

    /**
     * 完成Tinker的初始化
     * @param applicationLike
     */
    public static void installedTinker(ApplicationLike applicationLike){
        mAppLike = applicationLike;
        if(isInstalled){
            return;
        }
        TinkerInstaller.install(applicationLike);
        isInstalled = true;
    }

    /**
     * 完成patch文件的加载
     * @param path Patch文件的地址
     */
    public static void loadPatch(String path){
        //如果Tinker已完成安装
        if(Tinker.isTinkerInstalled()){
            TinkerInstaller.onReceiveUpgradePatch(getApplicationContext(),path);
        }
    }

    /**
     * 通过ApplicationLike 获取 Context
     * @return 当前的ApplicationContext
     */
    private static Context getApplicationContext(){
        if(mAppLike != null){
            return mAppLike.getApplication().getApplicationContext();
        }
        return null;
    }

}
