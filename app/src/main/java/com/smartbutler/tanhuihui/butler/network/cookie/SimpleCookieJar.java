package com.smartbutler.tanhuihui.butler.network.cookie;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * 创建者：   TANHUIHUI
 * 项  目：   Butler
 * 包  名：   com.smartbutler.tanhuihui.butler.network.cookie
 * 创建日期： 2017/10/7
 * 描  述：
 */

public class SimpleCookieJar implements CookieJar {
    private final List<Cookie> allCookies = new ArrayList<>();
    @Override
    public void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {
        allCookies.addAll(list);
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl httpUrl) {
        List<Cookie> result = new ArrayList<>();
        for (Cookie cookie : allCookies){
            if(cookie.matches(httpUrl)){
                result.add(cookie);
            }
        }
        return result;
    }
}
