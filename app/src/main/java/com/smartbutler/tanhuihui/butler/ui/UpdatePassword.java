package com.smartbutler.tanhuihui.butler.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.smartbutler.tanhuihui.butler.R;
import com.smartbutler.tanhuihui.butler.entity.UserEntity;
import com.smartbutler.tanhuihui.butler.utils.UtilTools;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 创建者：   TANHUIHUI
 * 项  目：   Butler
 * 包  名：   com.smartbutler.tanhuihui.butler.ui
 * 创建日期： 2017/6/11
 * 描  述：
 */

public class UpdatePassword extends BaseActivity implements View.OnClickListener {

    private EditText et_now_password;
    private EditText et_new_password;
    private EditText et_new_pass;
    private EditText et_email;
    private Button bt_update_password;
    private Button bt_update_password_email;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatepasswod);

        initView();
        setListener();
    }

    private void setListener() {
        bt_update_password.setOnClickListener(this);
        bt_update_password_email.setOnClickListener(this);
    }

    private void initView() {

        et_now_password = (EditText) findViewById(R.id.nowPassword);
        et_new_password = (EditText) findViewById(R.id.newPassword);
        et_new_pass = (EditText) findViewById(R.id.newpass);
        et_email = (EditText) findViewById(R.id.et_emial);

        bt_update_password = (Button) findViewById(R.id.update_password);
        bt_update_password_email = (Button) findViewById(R.id.update_password_email);
    }

    @Override
    public void onClick(View v) {

        String now_password = String.valueOf(et_now_password.getText());
        String new_password = String.valueOf(et_new_password.getText());
        String new_pass = String.valueOf(et_new_pass.getText());
        final String email1 = String.valueOf(et_email.getText());

        switch (v.getId()) {
            case R.id.update_password:
                if (!TextUtils.isEmpty(now_password) && !TextUtils.isEmpty(new_password) && !TextUtils.isEmpty(new_pass)) {
                    if (!new_password.equals(new_pass))
                        UtilTools.toash(this, "两次密码不一致");
                    else {
                        UserEntity.updateCurrentUserPassword(now_password, new_password, new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    UtilTools.toash(UpdatePassword.this, "修改密码成功");
                                    startActivity(new Intent(UpdatePassword.this, LoginActivity.class));
                                    finish();
                                } else if (e.getErrorCode() == 210)
                                    UtilTools.toash(UpdatePassword.this, "密码不正确");
                                else
                                    UtilTools.toash(UpdatePassword.this, e.getMessage());
                            }
                        });
                    }
                } else
                    UtilTools.toash(this, "密码修改输入框不能为空");
                break;

            case R.id.update_password_email:
                if (!TextUtils.isEmpty(email1)) {
                    UserEntity.requestEmailVerify(email1, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                UtilTools.toash(UpdatePassword.this, "请到" + email1 + "邮箱中进行激活");
                                startActivity(new Intent(UpdatePassword.this, LoginActivity.class));
                                finish();
                            } else
                                UtilTools.toash(UpdatePassword.this, e.getMessage());
                        }
                    });
                } else
                    UtilTools.toash(this, "邮箱不能为空");
                break;
        }

    }
}
