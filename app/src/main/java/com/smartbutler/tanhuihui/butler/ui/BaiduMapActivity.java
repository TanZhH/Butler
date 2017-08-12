package com.smartbutler.tanhuihui.butler.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;

import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.smartbutler.tanhuihui.butler.R;
import com.smartbutler.tanhuihui.butler.utils.LogUtils;
import com.smartbutler.tanhuihui.butler.utils.UtilTools;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建者：   TANHUIHUI
 * 项  目：   Butler
 * 包  名：   com.smartbutler.tanhuihui.butler.ui
 * 创建日期： 2017/7/11
 * 描  述：   百度地图定位页面
 */

public class BaiduMapActivity extends AppCompatActivity implements View.OnClickListener {

    private MapView mv_baidu;
    private BaiduMap map;

    //从某地出发
    private EditText et_fromlocation;
    //终点
    private EditText et_tolocation;
    //交换
    private ImageView iv_swin;
    //导航
    private ImageView iv_search;


    //定位
    private LocationClient mLocationClient = null;
    private BDLocationListener myListener = new MyLocationListener();

    //得到一个地理编码
    private GeoCoder mGeoCoder;
    //声明一个标记
    private Marker marker;
    //当前位置
    private BDLocation mlocation;

    Intent intent;
    Bundle bundle = new Bundle();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baidumap);
        initMap();
        initView();
        setListener();
    }

    //设置监听器
    private void setListener() {
        iv_swin.setOnClickListener(this);
        iv_search.setOnClickListener(this);
    }

    //地点搜索
    private void search(final String location, final boolean isStart) {
        //通过GeoCoder的实例得到GerCoder的对象
        mGeoCoder = GeoCoder.newInstance();
        //得到GenCoderOption对象
        GeoCodeOption mGenCoderOption = new GeoCodeOption();
        //搜索位置
        mGenCoderOption.address(location);
        //设置当前城市
        mGenCoderOption.city(mlocation.getCity());
        //为GenCoder设置监听事件
        mGeoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
                                                    //将具体的地址变化成坐标
                                                    @Override
                                                    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
                                                        //健壮性判断
                                                        if (geoCodeResult == null
                                                                || geoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
                                                            UtilTools.toash(BaiduMapActivity.this, "无结果");
                                                        } else {
                                                            //得到具体的地址
                                                            LatLng pos = geoCodeResult.getLocation();
                                                            if (isStart) {
                                                                intent = new Intent(BaiduMapActivity.this, BaiduNaviActivity.class);
                                                                bundle.putDouble("tolongitude", pos.longitude);
                                                                bundle.putDouble("tolatitude", pos.latitude);
                                                                bundle.putString("fromLocation", String.valueOf(et_fromlocation.getText()));
                                                                bundle.putString("toLocation", String.valueOf(et_tolocation.getText()));
                                                                intent.putExtra("location", bundle);
                                                                startActivity(intent);
                                                            } else {
                                                                bundle.putDouble("fromlongitude", pos.longitude);
                                                                bundle.putDouble("fromlatitude", pos.latitude);

                                                            }

//                    //得到一个标记的控制器
//                    MarkerOptions markerOptions = new MarkerOptions();
//                    // 我们设置标记的时候需要传入的参数
//                    BitmapDescriptor mbitmapDescriptor = BitmapDescriptorFactory
//                            .fromResource(R.drawable.ic_action_location);
//                    // 设置标记的图标
//                    markerOptions.icon(mbitmapDescriptor);
//                    // 设置标记的坐标
//                    markerOptions.position(pos);
//                    // 添加标记
//                    map.addOverlay(markerOptions);
//                    // 设置地图跳转的参数
//                    MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
//                            .newLatLngZoom(pos, 15);
//                    // 设置进行地图跳转
//                    map.setMapStatus(mMapStatusUpdate);
                                                        }
                                                    }

                                                    //这个方法是将坐标转化成具体地址
                                                    @Override
                                                    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {

                                                    }

                                                }
        );


        // 这句话必须写，否则监听事件里面的都不会执行
        mGeoCoder.geocode(mGenCoderOption);

    }

    //初始化控件
    private void initView() {
        et_fromlocation = (EditText) findViewById(R.id.et_fromlocation);
        et_tolocation = (EditText) findViewById(R.id.et_tolocation);
        iv_swin = (ImageView) findViewById(R.id.iv_swin);
        iv_search = (ImageView) findViewById(R.id.iv_search);
    }

    //初始化地图
    private void initMap() {
        mv_baidu = (MapView) findViewById(R.id.mv_baidu);
        map = mv_baidu.getMap();

        //声明LocationClient类
        mLocationClient = new LocationClient(this);
        //注册监听函数
        mLocationClient.registerLocationListener(myListener);
        initLocation();
        //开启定位
        mLocationClient.start();
        LogUtils.e("开始定位");
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        ////可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认gcj02，设置返回的定位结果坐标系
        option.setCoorType("bd09ll");
        int span = 100;
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setScanSpan(span);
        //可选，设置是否需要地址信息，默认不需要
        option.setIsNeedAddress(true);
        //可选，默认false,设置是否使用gps
        option.setOpenGps(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setLocationNotify(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIsNeedLocationPoiList(true);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setIgnoreKillProcess(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集
        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        option.setEnableSimulateGps(false);
        mLocationClient.setLocOption(option);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mv_baidu.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mv_baidu.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mv_baidu.onPause();
    }

    @Override
    public void onClick(View v) {
        //目的地与出发点交换
        switch (v.getId()) {
            case R.id.iv_swin:
                if (TextUtils.isEmpty(et_fromlocation.getText()) || TextUtils.isEmpty(et_tolocation.getText())) {
                    UtilTools.toash(this, "出发点与终点不能为空");
                } else {
                    //交换
                    String t = String.valueOf(et_fromlocation.getText());
                    et_fromlocation.setText(et_tolocation.getText());
                    et_tolocation.setText(t);
                }
                break;
            case R.id.iv_search:
                if(!defaultFromLocation()) {
                    search(String.valueOf(et_fromlocation.getText()), false);
                }
                search(String.valueOf(et_tolocation.getText()), true);
                break;
        }
    }

    //判断出发点是否改变，默认为目前定位,
    public boolean defaultFromLocation() {
        String fromlocation = String.valueOf(et_fromlocation.getText());
        String mylocation = getResources().getString(R.string.et_mylocation);
        if (mylocation.equals(fromlocation)) {
            bundle.putDouble("fromlongitude", mlocation.getLongitude());
            bundle.putDouble("fromlatitude", mlocation.getLatitude());
            return true;
        }
        return false;
    }

    //定位的回调
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive Location
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            // GPS定位结果
            if (location.getLocType() == BDLocation.TypeGpsLocation) {
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());// 单位度
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");
                // 网络定位结果
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
                // 离线定位结果
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");
            // 位置语义化信息
            sb.append(location.getLocationDescribe());
            // POI数据
            List<Poi> list = location.getPoiList();
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
            //定位的结果
            LogUtils.i(sb.toString());
            LogUtils.e("结束定位");

            //移动到我的位置
            //设置缩放，确保屏幕内有我
            MapStatusUpdate mapUpdate = MapStatusUpdateFactory.zoomTo(18);
            map.setMapStatus(mapUpdate);

            //开始移动
            MapStatusUpdate mapLatlng = MapStatusUpdateFactory.
                    newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
            map.setMapStatus(mapLatlng);

            //绘制图层
            //定义Maker坐标点
            LatLng point = new LatLng(location.getLatitude(), location.getLongitude());
            //构建Marker图标
            BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.ic_action_location);
            //构建MarkerOption，用于在地图上添加Marker
            OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
            //在地图上添加Marker，并显示
            map.addOverlay(option);
            //获取当前位置
            mlocation = location;
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }
}
