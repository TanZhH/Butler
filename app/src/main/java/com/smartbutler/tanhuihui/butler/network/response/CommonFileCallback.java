package com.smartbutler.tanhuihui.butler.network.response;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.smartbutler.tanhuihui.butler.network.exception.OkHttpException;
import com.smartbutler.tanhuihui.butler.network.listener.DisposeDataHandle;
import com.smartbutler.tanhuihui.butler.network.listener.DisposeDownLoadListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * 创建者：   TANHUIHUI
 * 项  目：   Butler
 * 包  名：   com.smartbutler.tanhuihui.butler.network.response
 * 创建日期： 2017/10/7
 * 描  述：   专门处理文件下载回调
 */

public class CommonFileCallback implements Callback {
    protected final int NETWORK_ERROR = -1; // the network relative error
    protected final int IO_ERROR = -2; // the JSON relative error
    protected final String EMPTY_MSG = "";

    /**
     * 将其他线程的数据转发到UI线程
     */
    private static final int PROGRESS_MESSAGE = 0x01;
    private Handler mDeliveryHandler;
    private DisposeDownLoadListener mListener;
    private String mFilePath;
    private int mProgress;

    public CommonFileCallback(DisposeDataHandle handle){
        this.mListener = (DisposeDownLoadListener) handle.mListener;
        this.mFilePath = handle.mSource;
        this.mDeliveryHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case PROGRESS_MESSAGE:
                        mListener.onProgress((int)msg.obj);
                        break;
                }
            }
        };
    }


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
        final File file = handleResponse(response);
        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                if(file!=null)
                    mListener.onSuccess(file);
                else
                    mListener.onFailure(new OkHttpException(IO_ERROR,EMPTY_MSG));
            }
        });
    }

    /**
     * 此时还在子线程中，不则调用回调接口
     * @param response
     * @return
     */
    private File handleResponse(Response response){
        if(response == null){
            return null;
        }
        InputStream inputStream = null;
        File file = null;
        FileOutputStream fos = null;
        byte[] buffer = new byte[2048];
        int length;
        int currentLength = 0;
        double sumLength;
        try{
            checkLocalFilePath(mFilePath);
            file = new File(mFilePath);
            fos = new FileOutputStream(file);
            inputStream = response.body().byteStream();
            sumLength = (double) response.body().contentLength();

            while ((length = inputStream.read(buffer)) != -1){
                fos.write(buffer,0,length);
                currentLength += length;
                mProgress = (int)(currentLength/sumLength * 100);
                mDeliveryHandler.obtainMessage(PROGRESS_MESSAGE,mProgress).sendToTarget();
            }
            fos.flush();
        }catch (Exception e){
            file = null;
            e.printStackTrace();
        }finally {
            try {
                if(fos != null)
                    fos.close();
                if(inputStream != null)
                    inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * 检查当地文件是否存在，不存在则创建
     * @param localFilePath
     */
    private void checkLocalFilePath(String localFilePath){
        File path = new File(localFilePath.substring(0,localFilePath.lastIndexOf("/")+1));
        File file = new File(localFilePath);
        if(!path.exists()){
            path.mkdir();
        }
        if(!file.exists()){
            try {
                file.createNewFile();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
