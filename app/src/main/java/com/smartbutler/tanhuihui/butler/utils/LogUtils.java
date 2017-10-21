package com.smartbutler.tanhuihui.butler.utils;

import android.util.Log;

/**
 * 创建者：   TANHUIHUI
 * 项  目：   Butler
 * 包  名：   com.smartbutler.tanhuihui.butler.utils
 * 创建日期： 2017/6/6
 * 描  述：   日志工具类
 */

public class LogUtils {
    private static final String TAG = "bulter";
    private static final boolean bool = false;

    public static void i(String text){
        if(bool)
            Log.i(TAG,text);
    }

    public static void d(String text){
        if(bool)
            Log.d(TAG,text);
    }
    public static void e(String text){
        if(bool)
            Log.e(TAG,text);
    }
    public static void v(String text){
        if(bool)
            Log.v(TAG,text);
    }

}
