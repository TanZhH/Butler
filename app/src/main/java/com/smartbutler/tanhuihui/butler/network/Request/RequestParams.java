package com.smartbutler.tanhuihui.butler.network.Request;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 创建者：   TANHUIHUI
 * 项  目：   Butler
 * 包  名：   com.smartbutler.tanhuihui.butler.network.Request
 * 创建日期： 2017/10/7
 * 描  述：   请求参数
 */

public class RequestParams {
    public ConcurrentHashMap<String , String> urlParams = new ConcurrentHashMap<>();
    public ConcurrentHashMap<String , Object> fileParams = new ConcurrentHashMap<>();

    public RequestParams(){
        this((Map<String,String>)null);
    }

    public RequestParams(Map<String , String> source){
        if(source != null){
            for (Map.Entry<String,String> entry : source.entrySet()){
                put(entry.getKey(),entry.getValue());
            }
        }
    }

    public RequestParams(final String key , final String value){
        this(new HashMap<String, String>(){
            {
                put(key,value);
            }
        });
    }
    /**
     * 将key/value 字符串加入到request
     * @param key
     * @param value
     */
    public void put(String key , String value){
        if(key!=null && value != null){
            urlParams.put(key,value);
        }
    }
    public void put(String key , Object object) throws FileNotFoundException{
        if (key != null) {
            fileParams.put(key,object);
        }
    }

    public boolean hasParams(){
        if(urlParams.size() > 0 || fileParams.size() > 0){
            return true;
        }
        return false;
    }

}
