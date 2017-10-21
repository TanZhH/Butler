package com.smartbutler.tanhuihui.butler.tinker;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.baidu.mapapi.SDKInitializer;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.smartbutler.tanhuihui.butler.utils.StaticClass;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.tinker.anno.DefaultLifeCycle;
import com.tencent.tinker.loader.app.ApplicationLike;
import com.tencent.tinker.loader.shareutil.ShareConstants;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import cn.bmob.v3.Bmob;

/**
 * 创建者：   TANHUIHUI
 * 项  目：   Butler
 * 包  名：   com.smartbutler.tanhuihui.butler.tinker
 * 创建日期： 2017/10/7
 * 描  述：   自定义TinkerApplicationLike用于自动生成所需的Application,
 *           Tinker委托ApplicationLike监听Application的生命周期，代理的方式
 */

@DefaultLifeCycle(application = ".MyTinkerApplication",flags = ShareConstants.TINKER_ENABLE_ALL,
                    loadVerifyFlag = false)
public class CustomTinkerLike extends ApplicationLike {
    public CustomTinkerLike(Application application, int tinkerFlags, boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime, long applicationStartMillisTime, Intent tinkerResultIntent) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent);
    }

    //安装Tinker
    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);
        TinkerManager.installedTinker(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化 bugly 设置
        CrashReport.initCrashReport(getApplication().getApplicationContext(), StaticClass.BUGLY_APP_ID, true);

        //初始化Bmob
        Bmob.initialize(getApplication(), StaticClass.BMOB_APP_ID);

        //科大讯飞，语音合成功能
        SpeechUtility.createUtility(getApplication().getApplicationContext(), SpeechConstant.APPID +"="+StaticClass.TTS_KEY);

        //zxing二维码初始化
        ZXingLibrary.initDisplayOpinion(getApplication());

        //百度地图定位
        SDKInitializer.initialize(getApplication().getApplicationContext());
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void registerActivityLifecycleCallbacks(Application.ActivityLifecycleCallbacks callback) {
        getApplication().registerActivityLifecycleCallbacks(callback);
    }
}
