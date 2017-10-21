package com.smartbutler.tanhuihui.butler.network.Request;


import java.io.File;
import java.util.Map;
import java.util.Objects;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * 创建者：   TANHUIHUI
 * 项  目：   Butler
 * 包  名：   com.smartbutler.tanhuihui.butler.network.Request
 * 创建日期： 2017/10/7
 * 描  述：   请求命令
 */

public class CommonRequest {

    /**
     *  create the key-value Request 不带请求头的Post请求
     * @param url url
     * @param params 参数
     * @return Request请求
     */
    public static Request createPostRequest(String url , RequestParams params){
        return createPostRequest(url,params,null);
    }

    /**
     * 可以带请求头的Post请求
     * @param url  url
     * @param params 参数
     * @param headers 请求头
     * @return  Request
     */
    public static Request createPostRequest(String url , RequestParams params , RequestParams headers){
        //添加参数
        FormBody.Builder mFormBodyBuild = new FormBody.Builder();
        if(params != null){
            for(Map.Entry<String , String > entry : params.urlParams.entrySet()){
                mFormBodyBuild.add(entry.getKey(),entry.getValue());
            }
        }
        //添加请求头
        Headers.Builder mHeaderBuilder = new Headers.Builder();
        if(headers != null){
            for (Map.Entry<String,String> entry:headers.urlParams.entrySet()) {
                mHeaderBuilder.add(entry.getKey(),entry.getValue());
            }
        }

        FormBody mFormBody = mFormBodyBuild.build();
        Headers mHeader = mHeaderBuilder.build();
        //根据FormBody和Headers创建request
        Request request = new Request.Builder().url(url)
                .post(mFormBody).headers(mHeader).build();
        return request;
    }

    /**
     * 不带请求头的Get请求
     * @param url url
     * @param params 参数
     * @return Request Get请求
     */
    public static Request createGetResquest(String url , RequestParams params){
        return createGetResquest(url,params,null);
    }

    public static Request createGetResquest(String url , RequestParams params , RequestParams headers){
        StringBuilder urlBuilder = new StringBuilder(url).append("?");
        //参数
        if(params != null){
            for(Map.Entry<String , String > entry : params.urlParams.entrySet()){
                urlBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
        }

        //添加请求头
        Headers.Builder mHeaderBuilder = new Headers.Builder();
        if(headers != null){
            for (Map.Entry<String , String > entry: headers.urlParams.entrySet()) {
                mHeaderBuilder.add(entry.getKey(),entry.getValue());
            }
        }

        //将参数和请求头添加到request中
        Headers mHeader = mHeaderBuilder.build();
        Request request = new Request.Builder().url(urlBuilder.toString()).get().headers(mHeader).build();
        return request;
    }

    public static Request createMonitorRequest(String url , RequestParams params){
        StringBuilder urlBuilder = new StringBuilder(url).append("&");
        if(params != null && params.hasParams()){
            for (Map.Entry<String ,String> entry: params.urlParams.entrySet()) {
                urlBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
        }
        return new Request.Builder().url(urlBuilder.substring(0,urlBuilder.length()-1)).get().build();
    }

    /**
     * 文件上传请求
     */
    private static final MediaType FILE_TYPE = MediaType.parse("application/octet-stream");
    public static Request CreateMultiPostRequest(String url , RequestParams params){
        MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder();
        requestBodyBuilder.setType(MultipartBody.FORM);
        if(params!=null){
            for(Map.Entry<String,Object> entry : params.fileParams.entrySet()){
                if(entry.getValue() instanceof File){
                    requestBodyBuilder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + entry.getKey() + "\""),
                            RequestBody.create(FILE_TYPE,(File) entry.getValue()));
                }else if(entry.getValue() instanceof String){
                    requestBodyBuilder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + entry.getKey() + "\""),
                            RequestBody.create(null,(String)entry.getValue()));
                }
            }
        }
        return new Request.Builder().url(url).post(requestBodyBuilder.build()).build();
    }
}
