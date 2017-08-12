package com.smartbutler.tanhuihui.butler.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * 创建者：   TANHUIHUI
 * 项  目：   Butler
 * 包  名：   com.smartbutler.tanhuihui.butler.utils
 * 创建日期： 2017/6/5
 * 描  述：   工具统一类
 */

public class UtilTools {

    //设置字体
    public static void setFontType(Context mcontext , String uri , TextView view){
        Typeface fromAsset = Typeface.createFromAsset(mcontext.getAssets(), uri);
        view.setTypeface(fromAsset);
    }

    //设置土司
    public static void toash(Context mcontext , String text){
        Toast.makeText(mcontext,text,Toast.LENGTH_LONG).show();
    }

    //将裁剪的图片保存在Shared中，BitMap压缩转换成String，将其保存在Shared
    public static void saveImageToShared(Context mcontext , ImageView imageView){
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        //1、将bitmap压缩成字节数组输出流
        ByteArrayOutputStream byoutputStream = new ByteArrayOutputStream();
        //将bitmap压缩，压缩率为20%
        bitmap.compress(Bitmap.CompressFormat.PNG , 80 , byoutputStream);

        //2、利用Base64将字节数组输出流转换成String，保存在Shared中
        byte[] bytes = byoutputStream.toByteArray();
        String imageString = Base64.encodeToString(bytes,Base64.DEFAULT);
        SharedUtils.putString(mcontext,StaticClass.IMAGE_TOUXIANG,imageString);
    }

    //从shared中读取String类型的头像并且将其转换成Bitmap类型，并设置成头像
    public static void getBitmapFromShared(Context mcontext, ImageView imageView){
        //1、从Shared中取出头像的String
        String imageString = SharedUtils.getString(mcontext,StaticClass.IMAGE_TOUXIANG,"");
        if(!"".equals(imageString)){
            //2、将 ImageString 转换成 Bitmap，并设置成头像
            //利用Base64反解码
            byte[] bytes = Base64.decode(imageString,Base64.DEFAULT);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            //3、生成Bitmap
            Bitmap bitmap = BitmapFactory.decodeStream(byteArrayInputStream);
            imageView.setImageBitmap(bitmap);
        }
    }
}
