package com.smartbutler.tanhuihui.butler.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.smartbutler.tanhuihui.butler.R;
import com.smartbutler.tanhuihui.butler.entity.UserEntity;
import com.smartbutler.tanhuihui.butler.ui.BaiduMapActivity;
import com.smartbutler.tanhuihui.butler.ui.BaiduNaviActivity;
import com.smartbutler.tanhuihui.butler.ui.CourierActivity;
import com.smartbutler.tanhuihui.butler.ui.LoginActivity;
import com.smartbutler.tanhuihui.butler.ui.QRBitMapActivity;
import com.smartbutler.tanhuihui.butler.ui.QRCodeActivity;
import com.smartbutler.tanhuihui.butler.ui.UpdateMessage;
import com.smartbutler.tanhuihui.butler.ui.UpdatePassword;
import com.smartbutler.tanhuihui.butler.utils.LogUtils;
import com.smartbutler.tanhuihui.butler.utils.SharedUtils;
import com.smartbutler.tanhuihui.butler.utils.StaticClass;
import com.smartbutler.tanhuihui.butler.utils.UtilTools;
import com.smartbutler.tanhuihui.butler.view.CustomDialog;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.io.File;

import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 创建者：   TANHUIHUI
 * 项  目：   Butler
 * 包  名：   com.smartbutler.tanhuihui.butler.fragment
 * 创建日期： 2017/6/5
 * 描  述：
 */

public class UserSettingFragment extends Fragment implements View.OnClickListener {

    //退出登录
    private TextView text_exit;
    //编辑资料
    private TextView text_update_user_message;
    //修改密码
    private TextView tv_update_user_password;
    //头像
    private CircleImageView circleImageView;
    //弹出窗口
    private CustomDialog dialog;
    //弹窗取消
    private TextView cancel;
    //相册
    private TextView picture;
    //照相
    private TextView Camera;
    //储存的图片
    private File temFile;
    //物流查询
    private TextView courier;

    //设置语音播报功能
    private Switch sw_Speek;
    //是否开启语音播报功能
    private boolean isSpeek;

    //扫一扫二维码
    private TextView tv_QRCode;
    //个人二维码
    private TextView tv_getQR;

    //百度地图
    private TextView tv_baiduNavi;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, null);
        initView(view);
        setListener();
        return view;
    }

    //设置监听
    private void setListener() {
        text_exit.setOnClickListener(this);
        text_update_user_message.setOnClickListener(this);
        tv_update_user_password.setOnClickListener(this);
        cancel.setOnClickListener(this);
        circleImageView.setOnClickListener(this);
        picture.setOnClickListener(this);
        Camera.setOnClickListener(this);
        courier.setOnClickListener(this);

        //sw_speek监听，是否开启语音播报功能
        sw_Speek.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedUtils.putBool(getActivity(),"isSpeek",isChecked);
                sw_Speek.setChecked(isChecked);
            }
        });
        //扫一扫监听
        tv_QRCode.setOnClickListener(this);
        //个人二维码
        tv_getQR.setOnClickListener(this);
        //百度地图
        tv_baiduNavi.setOnClickListener(this);
    }

    //初始化
    private void initView(View view) {
        text_exit = (TextView) view.findViewById(R.id.text_exit);
        text_update_user_message = (TextView) view.findViewById(R.id.text_update_user_message);
        tv_update_user_password = (TextView) view.findViewById(R.id.tv_update_user_password);
        circleImageView = (CircleImageView) view.findViewById(R.id.image_touxiang);
        //初始化弹窗
        dialog = new CustomDialog(getActivity(),0,0,R.layout.dialog_touxiang,R.style.Theme_dialog2, Gravity.BOTTOM,R.style.pop_anin_style);
        //设置弹窗外不可点击
        dialog.setCancelable(false);
        //弹窗的取消按钮
        cancel = (TextView) dialog.findViewById(R.id.text_cancel);
        //相册
        picture = (TextView) dialog.findViewById(R.id.text_picture);
        //拍照
        Camera = (TextView) dialog.findViewById(R.id.text_camera);
        //设置头像
        UtilTools.getBitmapFromShared(getActivity(),circleImageView);
        courier = (TextView) view.findViewById(R.id.text_selectcourier);

        //初始化语音播报功能
        sw_Speek = (Switch) view.findViewById(R.id.sw_speek);
        isSpeek = SharedUtils.getBool(getActivity(),"isSpeek",false);
        sw_Speek.setChecked(isSpeek);

        //扫一扫，个人二维码
        tv_QRCode = (TextView) view.findViewById(R.id.tv_QRCode);
        tv_getQR = (TextView) view.findViewById(R.id.tv_personQR);

        //百度地图
        tv_baiduNavi = (TextView) view.findViewById(R.id.tv_baidunavi);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            //退出登录
            case R.id.text_exit:
                SharedUtils.delete(getActivity(),StaticClass.IMAGE_TOUXIANG);
                UserEntity.logOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
                break;

            //修改用户信息
            case R.id.text_update_user_message:
                startActivity(new Intent(getActivity(), UpdateMessage.class));
                break;
            //修改密码
            case R.id.tv_update_user_password:
                startActivity(new Intent(getActivity(), UpdatePassword.class));
                break;
            //弹出弹窗
            case R.id.image_touxiang:
                dialog.show();
                break;
            //弹窗取消
            case R.id.text_cancel:
                dialog.dismiss();
                break;
            //从相册中选择
            case R.id.text_picture:
                toPicture();
                break;
            //拍照
            case R.id.text_camera:
                toCamera();
                break;
            //查询快递
            case R.id.text_selectcourier:
                startActivity(new Intent(getActivity(), CourierActivity.class));
                break;
            //扫一扫
            case R.id.tv_QRCode:
                //打开默认二维码扫描界面
                intent = new Intent(getActivity(), QRCodeActivity.class);
                startActivityForResult(intent, StaticClass.QR_REQUEST_CODE);
                break;
            //个人二维码
            case R.id.tv_personQR:
                intent = new Intent(getActivity(), QRBitMapActivity.class);
                startActivity(intent);
                break;
            //百度地图
            case R.id.tv_baidunavi:
                intent = new Intent(getActivity(), BaiduMapActivity.class);
                startActivity(intent);
                break;
        }
    }

    //跳转到相册
    private void toPicture() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,StaticClass.IMAGE_REQUEST_CODE);
        dialog.dismiss();
    }

    //跳转到相机
    private void toCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //判断内存卡是否能用，可用就进行储存
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(new File(Environment.getExternalStorageDirectory(),StaticClass.PHONE_IMAGE_FILE_NAME)));
        startActivityForResult(intent, StaticClass.CAMERA_REQUEST_CODE);
        dialog.dismiss();
    }

    //对返回照片或者图片进行裁剪，保存
    //扫一扫结果分析
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //返回码不为0,有结果
        if(resultCode!=getActivity().RESULT_CANCELED){
            switch (requestCode){
                //相册返回数据
                case StaticClass.IMAGE_REQUEST_CODE:
                    startPhotoZoom(data.getData());
                    break;
                //相机返回数据
                case StaticClass.CAMERA_REQUEST_CODE:
                    temFile = new File(Environment.getExternalStorageDirectory(),StaticClass.PHONE_IMAGE_FILE_NAME);
                    startPhotoZoom(Uri.fromFile(temFile));
                    break;
                //进行剪裁后返回结果
                case StaticClass.RESULT_REQUEST_CODE:
                    //中途放弃，则data为空
                    if(data!=null){
                        //图片设置
                        setImagetoView(data);
                        if(temFile!=null)
                            temFile.delete();
                        UtilTools.saveImageToShared(getActivity(),circleImageView);
                    }
                    break;
                //扫一扫返回结果
                case StaticClass.QR_REQUEST_CODE:
                    LogUtils.i("testdemo657");
                    //处理扫描结果（在界面上显示）
                    if (data != null) {
                        Bundle bundle = data.getExtras();
                        if (bundle == null) {
                            return;
                        }
                        else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                            String result = bundle.getString(CodeUtils.RESULT_STRING);
                            UtilTools.toash(getActivity(),"解析结果:" + result);
                        } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                            UtilTools.toash(getActivity(),"解析二维码失败");
                        }
                    }
                    break;
            }
        }
    }

    //设置头像
    private void setImagetoView(Intent data) {
        //Bundle 传输数据
        Bundle bundle = data.getExtras();
        if(bundle!=null){
            //获取出来的是 bitmap
            Bitmap bitmap = bundle.getParcelable("data");
            circleImageView.setImageBitmap(bitmap);
        }
    }

    //进行裁剪
    private void startPhotoZoom(Uri uri) {
        if(uri == null){
            LogUtils.e("uri == null");
            return;
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri,"image/*");
        //设置可以剪裁
        intent.putExtra("crop","true");
        //裁剪宽高比例
        intent.putExtra("aspectX",1);
        intent.putExtra("aspectY",1);
        //裁剪图片的质量，像素
        intent.putExtra("outputX",320);
        intent.putExtra("outputY",320);
        //发送数据
        intent.putExtra("return-data",true);
        startActivityForResult(intent,StaticClass.RESULT_REQUEST_CODE);
    }


}
