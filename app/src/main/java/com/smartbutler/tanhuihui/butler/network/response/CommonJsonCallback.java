package com.smartbutler.tanhuihui.butler.network.response;

import android.os.Handler;
import android.os.Looper;

import com.smartbutler.tanhuihui.butler.network.ResponseEntityToModule;
import com.smartbutler.tanhuihui.butler.network.exception.OkHttpException;
import com.smartbutler.tanhuihui.butler.network.listener.DisposeDataHandle;
import com.smartbutler.tanhuihui.butler.network.listener.DisposeDataListener;
import com.smartbutler.tanhuihui.butler.network.listener.DisposeHandleCookieListener;
import com.smartbutler.tanhuihui.butler.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.Response;

/**
 * 创建者：   TANHUIHUI
 * 项  目：   Butler
 * 包  名：   com.smartbutler.tanhuihui.butler.network.response
 * 创建日期： 2017/10/7
 * 描  述：   专门处理JSON的回调
 */

public class CommonJsonCallback implements Callback{
    //有返回则对于http请求是成功的，但业务逻辑可能是错误的
    protected final String RESULT_CODE = "ecode";
    protected final int RESULT_CODE_VALUE = 0;
    protected final String ERROR_MSG = "emsg";
    protected final String EMPTY_MSG = "";
    protected final String COOKIE_STORE = "Set-Cookie";

    // can has the value of
    // set-cookie2

    /**
     * the java layer exception, do not same to the logic error
     */
    protected final int NETWORK_ERROR = -1; // the network relative error
    protected final int JSON_ERROR = -2; // the JSON relative error
    protected final int OTHER_ERROR = -3; // the unknow error


    /**
     * 将其他线程的数据转发到UI线程
     */
    private Handler mDeliveryHandler;
    private DisposeDataListener mListener;
    private Class<?> mClass;
    public CommonJsonCallback(DisposeDataHandle handle){
        this.mListener = handle.mListener;
        this.mClass = handle.mClass;
        this.mDeliveryHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * 此时还在非UI线程，因此要转发
     * @param call
     * @param e
     */
    @Override
    public void onFailure(Call call, final IOException e) {
        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                mListener.onFailure(new OkHttpException(NETWORK_ERROR,e));
            }
        });
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        final String result = response.body().string();
        LogUtils.i("test:" + result);
        final ArrayList<String> cookieLists = handleCookie(response.headers());
        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                handleResponse(result);
                if(mListener instanceof DisposeHandleCookieListener)
                    ((DisposeHandleCookieListener)mListener).onCookie(cookieLists);
            }
        });
    }

    private ArrayList<String> handleCookie(Headers headers){
        ArrayList<String> tempList = new ArrayList<>();
        for (int i = 0; i < headers.size(); i++) {
            if(headers.name(i).equalsIgnoreCase(COOKIE_STORE)){
                tempList.add(headers.value(i));
            }
        }
        return tempList;
    }

    private void handleResponse(Object responseObj){
        if(responseObj == null || responseObj.toString().trim().equals("")){
            mListener.onFailure(new OkHttpException(NETWORK_ERROR,EMPTY_MSG));
            return;
        }
        /**
         * 协议确定后看这里如何修改
         */
        try {
            JSONObject result = new JSONObject(responseObj.toString());
            LogUtils.i("str:"+responseObj.toString());
            if(mClass == null){
                mListener.onSuccess(result);
            }else {
                Object obj = ResponseEntityToModule.parseJsonObjectToModule(result,mClass);
                if(obj != null)
                    mListener.onSuccess(obj);
                else
                    mListener.onFailure(new OkHttpException(JSON_ERROR,EMPTY_MSG));
            }
        } catch (JSONException e) {
            mListener.onFailure(new OkHttpException(OTHER_ERROR , e.getMessage()));
            e.printStackTrace();
        }
    }

}
