package com.smartbutler.tanhuihui.butler.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.smartbutler.tanhuihui.butler.R;
import com.smartbutler.tanhuihui.butler.entity.UserEntity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import cn.bmob.v3.BmobUser;

/**
 * 创建者：   TANHUIHUI
 * 项  目：   Butler
 * 包  名：   com.smartbutler.tanhuihui.butler.ui
 * 创建日期： 2017/7/8
 * 描  述：
 */

public class QRBitMapActivity extends BaseActivity {

    private ImageView iv_qrimage;
    //屏幕高
    private int wide;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrbitmap);
        initView();
    }

    private void initView() {
        iv_qrimage = (ImageView) findViewById(R.id.iv_qrimgage);
        //获取用户名称
        UserEntity user = BmobUser.getCurrentUser(UserEntity.class);
        String textContent = user.getUsername();
        if (TextUtils.isEmpty(textContent)) {
            return;
        }
        //获取屏幕宽
        wide = getResources().getDisplayMetrics().widthPixels;
        //生成二维码
        Bitmap mBitmap = CodeUtils.createImage(textContent, wide/2, wide/2, null);
        iv_qrimage.setImageBitmap(mBitmap);
    }
}
