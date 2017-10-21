package com.smartbutler.tanhuihui.butler.ui;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.smartbutler.tanhuihui.butler.MainActivity;
import com.smartbutler.tanhuihui.butler.R;
import com.smartbutler.tanhuihui.butler.entity.UserEntity;
import com.smartbutler.tanhuihui.butler.tinker.TinkerService;
import com.smartbutler.tanhuihui.butler.utils.LogUtils;
import com.smartbutler.tanhuihui.butler.utils.SharedUtils;
import com.smartbutler.tanhuihui.butler.utils.StaticClass;
import com.smartbutler.tanhuihui.butler.utils.UtilTools;

import cn.bmob.v3.BmobUser;


/**
 * 创建者：   TANHUIHUI
 * 项  目：   Butler
 * 包  名：   com.smartbutler.tanhuihui.butler.ui
 * 创建日期： 2017/6/6
 * 描  述：   闪屏页
 */

public class FlashActivity extends AppCompatActivity {

    private TextView app_name;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case StaticClass.HANDLER_SFLASH:
                    if(isfirst()) {
                        Intent intent = new Intent(FlashActivity.this, GuideActivity.class);
                        startActivity(intent);
                    }
                    else {
                        //获取当前缓存的对象进行判断是否自动登录
                        UserEntity currentuser =  BmobUser.getCurrentUser(UserEntity.class);
                        if(currentuser == null){
                            Intent intent = new Intent(FlashActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }else {
                            //判断是否进行了邮箱验证
                            Boolean verified = currentuser.getEmailVerified();
                            LogUtils.i(verified+"");
                            if(verified) {
                                Intent intent = new Intent(FlashActivity.this, MainActivity.class);
                                startActivity(intent);
                            }else {
                                Intent intent = new Intent(FlashActivity.this, LoginActivity.class);
                                startActivity(intent);
                                UtilTools.toash(FlashActivity.this, "请先前往邮箱验证");
                            }
                        }
                    }
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_flash);
        initview();
        startTinkerService();
    }

    //初始化
    private void initview() {
//        app_name = (TextView) findViewById(R.id.mfalshText);
//        //设置字体
//        UtilTools.setFontType(this,"fonts/FONT.TTF",app_name);
        handler.sendEmptyMessageDelayed(StaticClass.HANDLER_SFLASH,1000);
    }

    //判断是否为第一次运行
    private boolean isfirst() {
        //默认是第一次登录
        boolean bool = SharedUtils.getBool(this,StaticClass.SHARE_IS_FIRST,true);
        if(bool){
            SharedUtils.putBool(this,StaticClass.SHARE_IS_FIRST,false);
            return true;
        }
        return false;
    }

    //开启tinker服务
    private void startTinkerService() {
        Intent intent = new Intent(this, TinkerService.class);
        startService(intent);
    }
}
