package com.smartbutler.tanhuihui.butler.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.smartbutler.tanhuihui.butler.MainActivity;
import com.smartbutler.tanhuihui.butler.R;
import com.smartbutler.tanhuihui.butler.entity.UserEntity;
import com.smartbutler.tanhuihui.butler.utils.LogUtils;
import com.smartbutler.tanhuihui.butler.utils.UtilTools;
import com.smartbutler.tanhuihui.butler.view.CustomDialog;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * 创建者：   TANHUIHUI
 * 项  目：   Butler
 * 包  名：   com.smartbutler.tanhuihui.butler.ui
 * 创建日期： 2017/6/8
 * 描  述：   登录类
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_username;
    private EditText et_password;
    //登录按钮
    private Button mlogin;
    //注册按钮
    private Button mlogup;
    //忘记密码
//    private TextView text_forget;
    //弹出登录
    private CustomDialog dialog;
    //

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        setListener();
    }

    //设置监听器
    private void setListener() {
        mlogup.setOnClickListener(this);
        mlogin.setOnClickListener(this);
//        text_forget.setOnClickListener(this);
    }

    //初始化
    private void initView() {
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        mlogin = (Button) findViewById(R.id.mlogin);
        mlogup = (Button) findViewById(R.id.mlogup);
//        text_forget = (TextView) findViewById(R.id.text_forget);

        //初始化弹出框
        dialog = new CustomDialog(this,150,150,R.layout.dialog_loding_waiting,R.style.Theme_dialog, Gravity.CENTER,R.style.pop_anin_style);
        //取消屏幕外焦点
        dialog.setCancelable(false);
    }

    @Override
    public void onClick(View v) {
        final Intent intent;
        switch (v.getId()) {
            //选择注册
            case R.id.mlogup:
                intent = new Intent(this, LogupActivity.class);
                startActivity(intent);
                break;
            /**
             * 登录页面忘记密码功能取消，改到用户设置页面
             */
//            case R.id.text_forget:
//                intent = new Intent(this, UpdatePassword.class);
//                startActivity(intent);
//                break;
            //登录
            case R.id.mlogin:
                dialog.show();
                String username = String.valueOf(et_username.getText());
                String password = String.valueOf(et_password.getText());
                //用户名和密码不能为空
                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
                    final UserEntity user = new UserEntity();
                    user.setUsername(username);
                    user.setPassword(password);
                    user.login(new SaveListener<UserEntity>() {
                        @Override
                        public void done(UserEntity o, BmobException e) {
                            dialog.dismiss();
                            if (e == null) {
                                //判断用户是否进行了邮箱验证
                                if (user.getEmailVerified()) {
                                    Intent intent1 = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent1);
                                    finish();
                                } else
                                    UtilTools.toash(LoginActivity.this, "请先前往邮箱验证");
                            } else if (e.getErrorCode() == 101)
                                UtilTools.toash(LoginActivity.this, "用户名或者密码错误");
                            else
                                UtilTools.toash(LoginActivity.this, e.getMessage());
                        }
                    });
                } else
                    UtilTools.toash(this, "用户名和密码不能为空");
                break;
        }
    }
}
