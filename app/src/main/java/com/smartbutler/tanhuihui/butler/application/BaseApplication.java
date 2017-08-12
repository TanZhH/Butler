package com.smartbutler.tanhuihui.butler.application;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.smartbutler.tanhuihui.butler.utils.StaticClass;
import com.tencent.bugly.crashreport.CrashReport;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import cn.bmob.v3.Bmob;

/**
 * 创建者：    TANHUIHUI
 * 项  目：    Butler
 * 包  名：    com.smartbutler.tanhuihui.butler.application
 * 创建日期：  2017/6/5
 * 描  述：    Application
 */

public class BaseApplication extends Application {
    //创建
    @Override
    public void onCreate() {
        super.onCreate();

        //初始化 bugly 设置
        CrashReport.initCrashReport(getApplicationContext(), StaticClass.BUGLY_APP_ID, true);

        //初始化Bmob
        Bmob.initialize(this, StaticClass.BMOB_APP_ID);

        //科大讯飞，语音合成功能
        SpeechUtility.createUtility(getApplicationContext(), SpeechConstant.APPID +"="+StaticClass.TTS_KEY);

        //zxing二维码初始化
        ZXingLibrary.initDisplayOpinion(this);

        //百度地图定位
        SDKInitializer.initialize(getApplicationContext());
    }


}
