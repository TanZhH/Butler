package com.smartbutler.tanhuihui.butler.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.smartbutler.tanhuihui.butler.MainActivity;
import com.smartbutler.tanhuihui.butler.R;
import com.smartbutler.tanhuihui.butler.entity.UserEntity;
import com.smartbutler.tanhuihui.butler.utils.UtilTools;

import java.util.Calendar;
import java.util.Date;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * 创建者：   TANHUIHUI
 * 项  目：   Butler
 * 包  名：   com.smartbutler.tanhuihui.butler.ui
 * 创建日期： 2017/6/8
 * 描  述：   注册类
 */

public class LogupActivity extends BaseActivity implements View.OnClickListener {

    private EditText musername;
    private EditText mpassword;
    private EditText mcompassword;
    private EditText mEmail1;
    private RadioGroup mSex;
    private TextView mbrith;
    //日历选择
    private ImageView mcalendar;
    private Button mlogup;
    private Calendar calendar;

    //性别
    private Boolean man = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logup);
        initView();
        setListener();

    }

    private void setListener() {
        mcalendar.setOnClickListener(this);
        mSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(checkedId == R.id.mman)
                    man = true;
                else
                    man = false;
            }
        });
        mlogup.setOnClickListener(this);
    }

    private void initView() {
        musername = (EditText) findViewById(R.id.musername);
        mpassword = (EditText) findViewById(R.id.mpassword2);
        mcompassword = (EditText) findViewById(R.id.mpassword3);
        mEmail1 = (EditText) findViewById(R.id.mEmail1);
        mSex = (RadioGroup) findViewById(R.id.msexGroup);
        mcalendar = (ImageView) findViewById(R.id.mcalendar);
        mlogup = (Button) findViewById(R.id.mlogup);
        mbrith = (TextView) findViewById(R.id.mBrith);

        /**
         * 设置出生日期
         */
        calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        mbrith.setText(calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //选择出生日期
            case R.id.mcalendar:
                //弹出日期选择器
                DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        mbrith.setText(year+"-"+(month+1)+"-"+dayOfMonth);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                dpd.show();
                break;
            //注册
            case R.id.mlogup:
                String username = String.valueOf(musername.getText()).trim();
                String password = String.valueOf(mpassword.getText()).trim();
                String compass = String.valueOf(mcompassword.getText()).trim();
                String email = String.valueOf(mEmail1.getText()).trim();
                String birth = String.valueOf(mbrith.getText()).trim();

                //输入框不能为空
                if(!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password) &&!TextUtils.isEmpty(compass) &&
                        !TextUtils.isEmpty(email) &&!TextUtils.isEmpty(birth)){
                    if(password.equals(compass)){
                        UserEntity user = new UserEntity();
                        user.setUsername(username);
                        user.setPassword(password);
                        user.setEmail(email);
                        user.setBirth(birth);
                        user.setMan(man);
                        //默认邮箱验证为false
                        user.setEmailVerified(false);
                        user.signUp(new SaveListener<UserEntity>() {
                            @Override
                            public void done(UserEntity userEntity, BmobException e) {
                                if(e==null) {
                                    startActivity(new Intent(LogupActivity.this, LoginActivity.class));
                                    finish();
                                }
                                else if(e.getErrorCode()==202)
                                    UtilTools.toash(LogupActivity.this,getResources().getString(R.string.userex));
                                else if(e.getErrorCode() == 203)
                                    UtilTools.toash(LogupActivity.this,getResources().getString(R.string.emailex));
                                else
                                    UtilTools.toash(LogupActivity.this,e.getMessage());
                            }
                        });
                    }else
                        UtilTools.toash(this,getResources().getString(R.string.notsame));
                }else {
                    UtilTools.toash(this,getResources().getString(R.string.null_str));
                }
                break;
        }
    }
}
