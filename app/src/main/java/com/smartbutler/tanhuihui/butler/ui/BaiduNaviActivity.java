package com.smartbutler.tanhuihui.butler.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.navisdk.adapter.BNCommonSettingParam;
import com.baidu.navisdk.adapter.BNOuterLogUtil;
import com.baidu.navisdk.adapter.BNOuterTTSPlayerCallback;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNRoutePlanNode.CoordinateType;
import com.baidu.navisdk.adapter.BNaviBaseCallbackModel;
import com.baidu.navisdk.adapter.BNaviSettingManager;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.baidu.navisdk.adapter.BaiduNaviManager.NaviInitListener;
import com.baidu.navisdk.adapter.BaiduNaviManager.RoutePlanListener;
import com.smartbutler.tanhuihui.butler.R;
import com.smartbutler.tanhuihui.butler.utils.StaticClass;
import com.smartbutler.tanhuihui.butler.utils.UtilTools;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

/**
 * 创建者：   TANHUIHUI
 * 项  目：   Butler
 * 包  名：   com.smartbutler.tanhuihui.butler.ui
 * 创建日期： 2017/7/10
 * 描  述：   导航页
 */

public class BaiduNaviActivity extends AppCompatActivity {

	public static List<Activity> activityList = new LinkedList<Activity>();

	private String APP_FOLDER_NAME = null;
	private String mSDCardPath = null;

	public static final String ROUTE_PLAN_NODE = "routePlanNode";
	public static final String SHOW_CUSTOM_ITEM = "showCustomItem";
	public static final String RESET_END_NODE = "resetEndNode";
	public static final String VOID_MODE = "voidMode";

	private final static String authBaseArr[] =
			{ Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION };
	private final static String authComArr[] = { Manifest.permission.READ_PHONE_STATE };
	private final static int authBaseRequestCode = 1;
	private final static int authComRequestCode = 2;

	private boolean hasInitSuccess = false;
	private boolean hasRequestComAuth = false;

	//出发点经纬度
	private double fromlongitude;
	private double fromlatitude;
	//终点经纬度
	private double tolongitude;
	private double tolatitude;
	//出发点
	private String fromLocation;
	//终点
	private String toLocation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		activityList.add(this);

		setContentView(R.layout.activity_baidunavi);

		Handler h = new Handler();
		h.postDelayed(new Runnable() {

			@Override
			public void run() {
				String name = Thread.currentThread().getName();
				Log.i("crug", name);
				delayTest();
			}
		}, 500);

		initData();

		BNOuterLogUtil.setLogSwitcher(true);

		if (BaiduNaviManager.isNaviInited()) {
			routeplanToNavi(CoordinateType.BD09LL);
		}

		if (initDirs()) {
			initNavi();
		}

		// BNOuterLogUtil.setLogSwitcher(true);
	}

	private void initData() {
		Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra("location");
		fromlongitude = bundle.getDouble("fromlongitude");
		fromlatitude = bundle.getDouble("fromlatitude");
		tolongitude = bundle.getDouble("tolongitude");
		tolatitude = bundle.getDouble("tolatitude");
		fromLocation = bundle.getString("fromLocation");
		toLocation = bundle.getString("toLocation");
	}


	@Override
	protected void onResume() {
		super.onResume();
	}

	public void delayTest() {
		// SDKInitializer.initialize(BaiduNaviActivity.this.getApplication());
//        new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                Looper.prepare();
//                SDKInitializer.initialize(BaiduNaviActivity.this.getApplication());
//            }
//        }).start();
	}


	private boolean initDirs() {
		APP_FOLDER_NAME = this.getPackageName();
		mSDCardPath = getSdcardDir();
		if (mSDCardPath == null) {
			return false;
		}
		File f = new File(mSDCardPath, APP_FOLDER_NAME);
		if (!f.exists()) {
			try {
				f.mkdir();
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	String authinfo = null;

	/**
	 * 内部TTS播报状态回传handler
	 */
	private Handler ttsHandler = new Handler() {
		public void handleMessage(Message msg) {
			int type = msg.what;
			switch (type) {
				case BaiduNaviManager.TTSPlayMsgType.PLAY_START_MSG: {
					// showToastMsg("Handler : TTS play start");
					break;
				}
				case BaiduNaviManager.TTSPlayMsgType.PLAY_END_MSG: {
					// showToastMsg("Handler : TTS play end");
					break;
				}
				default:
					break;
			}
		}
	};

	/**
	 * 内部TTS播报状态回调接口
	 */
	private BaiduNaviManager.TTSPlayStateListener ttsPlayStateListener = new BaiduNaviManager.TTSPlayStateListener() {

		@Override
		public void playEnd() {
			// showToastMsg("TTSPlayStateListener : TTS play end");
		}

		@Override
		public void playStart() {
			// showToastMsg("TTSPlayStateListener : TTS play start");
		}
	};

	public void showToastMsg(final String msg) {
		BaiduNaviActivity.this.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(BaiduNaviActivity.this, msg, Toast.LENGTH_SHORT).show();
			}
		});
	}

	private boolean hasBasePhoneAuth() {
		// TODO Auto-generated method stub

		PackageManager pm = this.getPackageManager();
		for (String auth : authBaseArr) {
			if (pm.checkPermission(auth, this.getPackageName()) != PackageManager.PERMISSION_GRANTED) {
				return false;
			}
		}
		return true;
	}

	private boolean hasCompletePhoneAuth() {
		// TODO Auto-generated method stub

		PackageManager pm = this.getPackageManager();
		for (String auth : authComArr) {
			if (pm.checkPermission(auth, this.getPackageName()) != PackageManager.PERMISSION_GRANTED) {
				return false;
			}
		}
		return true;
	}

	private void initNavi() {

		BNOuterTTSPlayerCallback ttsCallback =  mTTSCallback;
		// 申请权限
		if (android.os.Build.VERSION.SDK_INT >= 23) {

			if (!hasBasePhoneAuth()) {

				this.requestPermissions(authBaseArr, authBaseRequestCode);
				return;

			}
		}

		BaiduNaviManager.getInstance().init(this, mSDCardPath, APP_FOLDER_NAME, new NaviInitListener() {
			@Override
			public void onAuthResult(int status, String msg) {
				if (0 == status) {
					authinfo = "key校验成功!";
				} else {
					authinfo = "key校验失败, " + msg;
				}
				BaiduNaviActivity.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(BaiduNaviActivity.this, authinfo, Toast.LENGTH_LONG).show();
					}
				});
			}

			public void initSuccess() {
				hasInitSuccess = true;
				initSetting();
			}

			public void initStart() {
			}

			public void initFailed() {
			}

		}, null, ttsHandler, ttsPlayStateListener);

	}

	private String getSdcardDir() {
		if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
			return Environment.getExternalStorageDirectory().toString();
		}
		return null;
	}

	private CoordinateType mCoordinateType = null;

	private void routeplanToNavi(CoordinateType coType) {
		mCoordinateType = coType;
		if (!hasInitSuccess) {
			Toast.makeText(BaiduNaviActivity.this, "还未初始化!", Toast.LENGTH_SHORT).show();
		}
		// 权限申请
		if (android.os.Build.VERSION.SDK_INT >= 23) {
			// 保证导航功能完备
			if (!hasCompletePhoneAuth()) {
				if (!hasRequestComAuth) {
					hasRequestComAuth = true;
					this.requestPermissions(authComArr, authComRequestCode);
					return;
				} else {
					Toast.makeText(BaiduNaviActivity.this, "没有完备的权限!", Toast.LENGTH_SHORT).show();
				}
			}

		}
		BNRoutePlanNode sNode = null;
		BNRoutePlanNode eNode = null;
		switch (coType) {
			case BD09LL: {
				sNode = new BNRoutePlanNode(fromlongitude, fromlatitude, fromLocation, null, coType);
				eNode = new BNRoutePlanNode(tolongitude, tolatitude, toLocation, null, coType);
				break;
			}
			default:
				;
		}
		if (sNode != null && eNode != null) {
			List<BNRoutePlanNode> list = new ArrayList<BNRoutePlanNode>();
			list.add(sNode);
			list.add(eNode);

			// 开发者可以使用旧的算路接口，也可以使用新的算路接口,可以接收诱导信息等
			// BaiduNaviManager.getInstance().launchNavigator(this, list, 1, true, new DemoRoutePlanListener(sNode));
			BaiduNaviManager.getInstance().launchNavigator(this, list, 1, true, new DemoRoutePlanListener(sNode),
					eventListerner);
		}
	}

	BaiduNaviManager.NavEventListener eventListerner = new BaiduNaviManager.NavEventListener() {

		@Override
		public void onCommonEventCall(int what, int arg1, int arg2, Bundle bundle) {
			BNEventHandler.getInstance().handleNaviEvent(what, arg1, arg2, bundle);
		}
	};

	public class DemoRoutePlanListener implements RoutePlanListener {

		private BNRoutePlanNode mBNRoutePlanNode = null;

		public DemoRoutePlanListener(BNRoutePlanNode node) {
			mBNRoutePlanNode = node;
		}

		@Override
		public void onJumpToNavigator() {
            /*
             * 设置途径点以及resetEndNode会回调该接口
             */

			for (Activity ac : activityList) {

				if (ac.getClass().getName().endsWith("BaiduNaviGuideActivity")) {
					return;
				}
			}
			Intent intent = new Intent(BaiduNaviActivity.this, BaiduNaviGuideActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable(ROUTE_PLAN_NODE, (BNRoutePlanNode) mBNRoutePlanNode);
			intent.putExtras(bundle);
			startActivity(intent);

		}

		@Override
		public void onRoutePlanFailed() {
			// TODO Auto-generated method stub
			Toast.makeText(BaiduNaviActivity.this, "算路失败", Toast.LENGTH_SHORT).show();
		}
	}

	private void initSetting() {
		// BNaviSettingManager.setDayNightMode(BNaviSettingManager.DayNightMode.DAY_NIGHT_MODE_DAY);
		BNaviSettingManager
				.setShowTotalRoadConditionBar(BNaviSettingManager.PreViewRoadCondition.ROAD_CONDITION_BAR_SHOW_ON);
		BNaviSettingManager.setVoiceMode(BNaviSettingManager.VoiceMode.Veteran);
		// BNaviSettingManager.setPowerSaveMode(BNaviSettingManager.PowerSaveMode.DISABLE_MODE);
		BNaviSettingManager.setRealRoadCondition(BNaviSettingManager.RealRoadCondition.NAVI_ITS_ON);
		BNaviSettingManager.setIsAutoQuitWhenArrived(true);
		Bundle bundle = new Bundle();
		// 必须设置APPID，否则会静音
		bundle.putString(BNCommonSettingParam.TTS_APP_ID, StaticClass.TTS_KEY);
		BNaviSettingManager.setNaviSdkParam(bundle);
	}

	private BNOuterTTSPlayerCallback mTTSCallback = new BNOuterTTSPlayerCallback() {

		@Override
		public void stopTTS() {
			// TODO Auto-generated method stub
			Log.e("test_TTS", "stopTTS");
		}

		@Override
		public void resumeTTS() {
			// TODO Auto-generated method stub
			Log.e("test_TTS", "resumeTTS");
		}

		@Override
		public void releaseTTSPlayer() {
			// TODO Auto-generated method stub
			Log.e("test_TTS", "releaseTTSPlayer");
		}

		@Override
		public int playTTSText(String speech, int bPreempt) {
			// TODO Auto-generated method stub
			Log.e("test_TTS", "playTTSText" + "_" + speech + "_" + bPreempt);

			return 1;
		}

		@Override
		public void phoneHangUp() {
			// TODO Auto-generated method stub
			Log.e("test_TTS", "phoneHangUp");
		}

		@Override
		public void phoneCalling() {
			// TODO Auto-generated method stub
			Log.e("test_TTS", "phoneCalling");
		}

		@Override
		public void pauseTTS() {
			// TODO Auto-generated method stub
			Log.e("test_TTS", "pauseTTS");
		}

		@Override
		public void initTTSPlayer() {
			// TODO Auto-generated method stub
			Log.e("test_TTS", "initTTSPlayer");
		}

		@Override
		public int getTTSState() {
			// TODO Auto-generated method stub
			Log.e("test_TTS", "getTTSState");
			return 1;
		}
	};

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		// TODO Auto-generated method stub
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == authBaseRequestCode) {
			for (int ret : grantResults) {
				if (ret == 0) {
					continue;
				} else {
					Toast.makeText(BaiduNaviActivity.this, "缺少导航基本的权限!", Toast.LENGTH_SHORT).show();
					return;
				}
			}
			initNavi();
		} else if (requestCode == authComRequestCode) {
			for (int ret : grantResults) {
				if (ret == 0) {
					continue;
				}
			}
			routeplanToNavi(mCoordinateType);
		}

	}
}
