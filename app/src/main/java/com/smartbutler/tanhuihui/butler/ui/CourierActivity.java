package com.smartbutler.tanhuihui.butler.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.smartbutler.tanhuihui.butler.R;
import com.smartbutler.tanhuihui.butler.adapter.ComAdapter;
import com.smartbutler.tanhuihui.butler.adapter.CourierAdapter;
import com.smartbutler.tanhuihui.butler.entity.ComEntity;
import com.smartbutler.tanhuihui.butler.entity.CourierEntity;
import com.smartbutler.tanhuihui.butler.utils.LogUtils;
import com.smartbutler.tanhuihui.butler.utils.StaticClass;
import com.smartbutler.tanhuihui.butler.utils.UtilTools;
import com.smartbutler.tanhuihui.butler.view.CustomDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 创建者：   TANHUIHUI
 * 项  目：   Butler
 * 包  名：   com.smartbutler.tanhuihui.butler.ui
 * 创建日期： 2017/6/16
 * 描  述：   查询快递
 */

public class CourierActivity extends BaseActivity implements View.OnClickListener {
    //快递公司下拉选择框
    private Spinner spinner;
    //订单号
    private EditText order;
    //时间查询结果
    private ListView listView;
    //查询按钮
    private Button button;
    //查询能查询的快递公司的Url
    private String comurl;
    //快递公司集合
    private List<ComEntity> comList;
    //快递公司加载
//    private ComAsynTask comAsynTask;
    //spinner下拉框适配器
    private ComAdapter comadapter;
    //快递公司代号
    private String code = "sf";
    //查询订单url
    private String URL;
    //快递物流信息
    private List<CourierEntity> courierEntityList;

    private CustomDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier);
        initView();
        getCourier();
        setListener();
    }

    //获取快递公司，初始化spinner
    private void getCourier() {
        comurl = "http://v.juhe.cn/exp/com?key=" + StaticClass.ORDOR_APP_KEY;
//        comAsynTask = new ComAsynTask();
//        comAsynTask.execute(comurl);
        comList = new ArrayList<>();
        RxVolley.get(comurl, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                JSONComArray(t);
                comadapter = new ComAdapter(CourierActivity.this, comList);
                spinner.setAdapter(comadapter);
            }
        });
    }

    private void setListener() {
        //查询按钮监听
        button.setOnClickListener(this);
        //spinner下拉框选中监听
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ComEntity com = (ComEntity) comadapter.getItem(position);
                code = com.getCode();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //初始化
    private void initView() {
        spinner = (Spinner) findViewById(R.id.sp_courier);
        order = (EditText) findViewById(R.id.ed_order);
        listView = (ListView) findViewById(R.id.lv_result);
        button = (Button) findViewById(R.id.bt_select);
    }

    //解析JSON数据获取快递公司，将其加入到comList集合中
    public void JSONComArray(String t){
        JSONObject object = null;
        JSONArray comArray = null;
        try {
            object = new JSONObject(t);
            comArray = object.getJSONArray("result");
            ComEntity comEntity = null;
            for (int i = 0; i < comArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) comArray.get(i);
                comEntity = new ComEntity();
                comEntity.setName(String.valueOf(jsonObject.get("com")));
                comEntity.setCode(String.valueOf(jsonObject.get("no")));
                comList.add(comEntity);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.i(comList.size()+"asd");
    }


    @Override
    public void onClick(View v) {
        dialog = new CustomDialog(CourierActivity.this,0,0,R.layout.dialog_courier,R.style.Theme_dialog, Gravity.CENTER,R.style.pop_anin_style);
        dialog.show();
        switch (v.getId()){
            case R.id.bt_select:
                String text = String.valueOf(order.getText());
                if(!TextUtils.isEmpty(text)){
                    URL = "http://v.juhe.cn/exp/index?key="+StaticClass.ORDOR_APP_KEY+"&com="+code+"&no="+text;
                    LogUtils.i(URL);
//                    new CourierAsynTask().execute(URL);
                    courierEntityList = new ArrayList<>();
                    RxVolley.get(URL, new HttpCallback() {
                        @Override
                        public void onSuccess(String t) {
                            JSONCourierMessage(t);
                            CourierAdapter adapter = new CourierAdapter(CourierActivity.this,courierEntityList);
                            listView.setAdapter(adapter);
                            dialog.dismiss();
                        }
                    });
                }else{
                    dialog.dismiss();
                    UtilTools.toash(this,"请填写订单号");
                }
                break;
        }
    }

    private void JSONCourierMessage(String s) {
        CourierEntity entity = null;
        JSONObject object = null;
        JSONObject result   = null;
        JSONArray jsonList = null;
        try {
            object = new JSONObject(s);
            result = object.getJSONObject("result");
            jsonList = result.getJSONArray("list");
            for (int i = 0; i < jsonList.length(); i++) {
                JSONObject o = (JSONObject) jsonList.get(i);
                entity = new CourierEntity();
                entity.setDatetime(String.valueOf(o.get("datetime")));
                entity.setRemark(String.valueOf(o.get("remark")));
                courierEntityList.add(entity);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //List倒序
        Collections.reverse(courierEntityList);
    }

   /* //快递公司下拉框加载
    class ComAsynTask extends AsyncTask<String, Void, List<ComEntity>> {
        String s = "";
        @Override
        protected List<ComEntity> doInBackground(String... params) {
            comList = new ArrayList<ComEntity>();
            RxVolley.get(params[0], new HttpCallback() {
                @Override
                public void onSuccessInAsync(byte[] t) {
                    s = new String(t);
                    comList = JSONComArray(s);
                    LogUtils.i("快递公司数目："+comList.size());
                }
            });
            LogUtils.i("快递公司数目："+comList.size());
            return comList;
        }

        @Override
        protected void onPostExecute(List<ComEntity> comEntityList) {
            super.onPostExecute(comEntityList);
            //spinner设置适配器
            LogUtils.i("快递公司数目2："+comEntityList.size());
            comadapter = new ComAdapter(CourierActivity.this, comEntityList);
            spinner.setAdapter(comadapter);
        }
    }

    //查询快递
    class CourierAsynTask extends AsyncTask<String, Void, List<CourierEntity>> {
        String s = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
            LogUtils.i("begin");
        }

        @Override
        protected List<CourierEntity> doInBackground(String... params) {
            courierEntityList = new ArrayList<>();
            RxVolley.get(params[0], new HttpCallback() {
                @Override
                public void onSuccessInAsync(byte[] t) {
                    s = new String(t);
                    LogUtils.i(s);
                    courierEntityList = JSONCourierMessage(s);
                }
            });
            LogUtils.i("物流数目："+courierEntityList.size());
            return courierEntityList;
        }

        @Override
        protected void onPostExecute(List<CourierEntity> courierEntities) {
            super.onPostExecute(courierEntities);
            CourierAdapter adapter = new CourierAdapter(CourierActivity.this,courierEntities);
            LogUtils.i("物流数目2："+courierEntities.size());
            listView.setAdapter(adapter);
            dialog.dismiss();
        }
    }*/

}
