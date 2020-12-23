package com.marten.pdd_sdk_demo.activity;

import android.os.Build;
import android.os.Bundle;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.marten.pdd_sdk_demo.R;

public class WebViewActivity extends AppCompatActivity {

    private WebView mWv1;
    private WebSettings mWs;
    private String url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        mWv1 = findViewById(R.id.wv_main);
        url = getIntent().getStringExtra("url");

        mWv1.setWebChromeClient(new WebChromeClient(){
//            @Override
//            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
//                return super.onJsAlert(view, url, message, result);
//            }
        });
        mWv1.setWebViewClient(new WebViewClient());


        mWs = mWv1.getSettings();
        // 在Android5.0以下，默认是采用的MIXED_CONTENT_ALWAYS_ALLOW模式，
        // 即总是允许WebView同时加载Https和Http；而从Android5.0开始，默认用MIXED_CONTENT_NEVER_ALLOW模式，
        // 即总是不允许WebView同时加载Https和Http。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mWs.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        //如果访问的页面中要与JavaScript交互，则WebView必须设置支持JavaScript
        mWs.setJavaScriptEnabled(true);
        //设置自适应屏幕，两者合用
        mWs.setUseWideViewPort(true); //将图片调整到适合webview的大小
        mWs.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        // 缩放操作
        mWs.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        mWs.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        mWs.setDisplayZoomControls(false); //隐藏原生的缩放控件
        //其他细节操作
        mWs.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
        mWs.setAllowFileAccess(true); //设置可以访问文件
        mWs.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        mWs.setLoadsImagesAutomatically(true); //支持自动加载图片
        mWs.setDefaultTextEncodingName("utf-8");//设置编码格式

        mWv1.loadUrl(url);
    }
}
