package com.smartbutler.tanhuihui.butler.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.smartbutler.tanhuihui.butler.R;

/**
 * 创建者：   TANHUIHUI
 * 项  目：   Butler
 * 包  名：   com.smartbutler.tanhuihui.butler.view
 * 创建日期： 2017/6/13
 * 描  述：   登录弹出等待框
 */

public class CustomDialog extends Dialog{
    //定义模板
    public CustomDialog(Context context,int Layout,int style){
        this(context, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                Layout,style, Gravity.CENTER);
    }

    //定义属性
    public CustomDialog(Context context , int width , int hight,
                        int layout , int style , int gravity ,
                        int anim){
        super(context,style);
        //设置属性
        setContentView(layout);
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = gravity;
        window.setAttributes(layoutParams);
        window.setWindowAnimations(anim);
    }

    //实例
    public CustomDialog(Context context , int width,int heigh,
                        int layout,int style,int gravity){
        this(context,width,heigh,layout,style,gravity, R.style.pop_anin_style);
    }
}
