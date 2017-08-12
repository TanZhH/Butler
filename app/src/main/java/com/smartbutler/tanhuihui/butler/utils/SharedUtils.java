package com.smartbutler.tanhuihui.butler.utils;


import android.content.Context;
import android.content.SharedPreferences;

/**
 * 创建者：   TANHUIHUI
 * 项  目：   Butler
 * 包  名：   com.smartbutler.tanhuihui.butler.utils
 * 创建日期： 2017/6/6
 * 描  述：  SharedPreferences工具类
 */

public class SharedUtils {

    private static final String NAME="config";

    public static void putString(Context mcontext , String key , String value){
        SharedPreferences sp = mcontext.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        sp.edit().putString(key,value).apply();
    }

    public static String getString(Context mcontext , String key , String defValue){
        SharedPreferences sp = mcontext.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        return sp.getString(key,defValue);
    }

    public static void putInt(Context mcontext , String key , int value){
        SharedPreferences sp = mcontext.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        sp.edit().putInt(key,value).apply();
    }

    public static int getInt(Context mcontext , String key , int defValue){
        SharedPreferences sp = mcontext.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        return sp.getInt(key,defValue);
    }

    public static void putBool(Context mcontext , String key , Boolean value){
        SharedPreferences sp = mcontext.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        sp.edit().putBoolean(key,value).apply();
    }

    public static boolean getBool(Context mcontext , String key , Boolean defValue){
        SharedPreferences sp = mcontext.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        return sp.getBoolean(key,defValue);
    }


    //删除单个
    public static void delete(Context mcontext , String key){
        SharedPreferences sp = mcontext.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        sp.edit().remove(key).apply();
    }

    //删除全部
    public static void deleteAll(Context mcontext){
        SharedPreferences sp = mcontext.getSharedPreferences(NAME,Context.MODE_PRIVATE);
        sp.edit().clear();
    }
}
