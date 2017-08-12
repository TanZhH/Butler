package com.smartbutler.tanhuihui.butler.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Transformation;

import java.io.IOException;

/**
 * 创建者：   TANHUIHUI
 * 项  目：   Butler
 * 包  名：   com.smartbutler.tanhuihui.butler.utils
 * 创建日期： 2017/6/30
 * 描  述：   PicassoUtil工具类
 */

public class PicassoUtil {
    private static int test = 0;

    //普通加载方式
    public static void loadImage(Context mcontext , String url , ImageView view){
        Picasso.with(mcontext).load(url).into(view);
    }

    //获取BitMap
    public static Bitmap getBitmap(Context mcontext , String url){
        Bitmap bitmap = null;
        try {
            bitmap = Picasso.with(mcontext).load(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }


    //加载指定大小的图片,Bitmap.Config RGB_565：每个像素占四位，即R=5，G=6，B=5，没有透明度，那么一个像素点占5+6+5=16位
    public static void loadSizeImage(Context mcontext, String url , ImageView view , int wide , int height){
        Picasso.with(mcontext).load(url).config(Bitmap.Config.RGB_565).resize(wide,height).centerCrop().into(view);
    }

    //默认加载方式,且按指定大小加载图片
    public static void defalutLoadImage(Context mcontext , String url , ImageView view , int placeHolder, int errorView,
                                        int wide , int height){
        Picasso.with(mcontext)
                .load(url).config(Bitmap.Config.RGB_565)
                .resize(wide,height)
                .placeholder(placeHolder)
                .error(errorView)
                .into(view);
    }


    //按比例裁剪图片
    public static void loadImageWithCrop(Context mcontext , String url , ImageView view){
        Picasso.with(mcontext).load(url).transform(new CropSquareTransformation()).into(view);
    }

    //按比例裁剪图片
    public static class CropSquareTransformation implements Transformation {
        @Override public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;
            Bitmap result = Bitmap.createBitmap(source, x, y, size, size);
            if (result != source) {
                //回收
                source.recycle();
            }
            return result;
        }

        @Override public String key() { return "lgl"; }
    }

}
