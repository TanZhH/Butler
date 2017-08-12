package com.smartbutler.tanhuihui.butler.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.smartbutler.tanhuihui.butler.R;

/**
 * 创建者：   TANHUIHUI
 * 项  目：   Butler
 * 包  名：   com.smartbutler.tanhuihui.butler.ui
 * 创建日期： 2017/6/29
 * 描  述：
 */

public class NewActivity extends BaseActivity {
    private String URL;
    private String title;
    private WebView wv_new;
    private ProgressBar pb_new;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取出URL
        setContentView(R.layout.activity_new);
        Intent intent = getIntent();
        URL = intent.getStringExtra("URL");
        //取出title
        title = intent.getStringExtra("title");
        initView();
    }

    private void initView() {
        pb_new = (ProgressBar) findViewById(R.id.pb_new);
        wv_new = (WebView) findViewById(R.id.wv_new);
        //配置webView
        //设置标题
        getSupportActionBar().setTitle(title);

        //进行加载网页的逻辑
        WebSettings settings = wv_new.getSettings();
        //支持JS
        settings.setJavaScriptEnabled(true);
        //支持缩放
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        //接口回调
        wv_new.setWebChromeClient(new MyWebChromeClient());

        //加载网页
        wv_new.loadUrl(URL);
        //本地显示，不打开新的浏览器
        wv_new.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(URL);
                //接受事件
                return true;
            }
        });
    }

    private class MyWebChromeClient extends WebChromeClient{
        //进度条变化监听
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if(newProgress == 100)
                pb_new.setVisibility(View.GONE);
            else
                pb_new.setProgress(newProgress);
            super.onProgressChanged(view, newProgress);
        }
    }
}
