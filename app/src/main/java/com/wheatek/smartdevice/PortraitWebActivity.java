package com.wheatek.smartdevice;

import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static com.wheatek.smartdevice.PointsMallShareFragment.SHOPLINKEXTR;

public class PortraitWebActivity extends AppCompatActivity {
    private Button mBack;
    private WebView mWebview;
    private ProgressBar progressbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.portraitweb_activity);
        initView();
    }

    private void initView() {
        mBack = findViewById(R.id.back);
        mWebview = findViewById(R.id.webview);
        mBack.setOnClickListener(v -> finish());
        mWebview.setWebChromeClient(new WebChromeClient());
        mWebview.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                runOnUiThread(() -> Toast.makeText(getApplicationContext(), "finish", Toast.LENGTH_LONG).show());
            }
        });
        String u = getUrl();
//        System.out.println(u);
        setWebView();
        mWebview.loadUrl(u);
        progressbar = findViewById(R.id.progress);
    }

    private void setWebView() {
        WebSettings webSettings = mWebview.getSettings();

        webSettings.setJavaScriptEnabled(true); // 是否开启JS支持
        webSettings.setPluginsEnabled(true); // 是否开启插件支持
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); // 是否允许JS打开新窗口

        webSettings.setUseWideViewPort(true); // 缩放至屏幕大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕大小
        webSettings.setSupportZoom(true); // 是否支持缩放
        webSettings.setBuiltInZoomControls(true); // 是否支持缩放变焦，前提是支持缩放
        webSettings.setDisplayZoomControls(false); // 是否隐藏缩放控件

        webSettings.setAllowFileAccess(true); // 是否允许访问文件
        webSettings.setDomStorageEnabled(true); // 是否节点缓存
        webSettings.setDatabaseEnabled(true); // 是否数据缓存
        webSettings.setAppCacheEnabled(true); // 是否应用缓存
//        webSettings.setAppCachePath(uri); // 设置缓存路径

//        webSettings.setMediaPlaybackRequiresUserGesture(false); // 是否要手势触发媒体
//        webSettings.setStandardFontFamily("sans-serif"); // 设置字体库格式
//        webSettings.setFixedFontFamily("monospace"); // 设置字体库格式
//        webSettings.setSansSerifFontFamily("sans-serif"); // 设置字体库格式
//        webSettings.setSerifFontFamily("sans-serif"); // 设置字体库格式
//        webSettings.setCursiveFontFamily("cursive"); // 设置字体库格式
//        webSettings.setFantasyFontFamily("fantasy"); // 设置字体库格式
//        webSettings.setTextZoom(100); // 设置文本缩放的百分比
//        webSettings.setMinimumFontSize(8); // 设置文本字体的最小值(1~72)
//        webSettings.setDefaultFontSize(16); // 设置文本字体默认的大小

//        webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN); // 按规则重新布局
//        webSettings.setLoadsImagesAutomatically(false); // 是否自动加载图片
        webSettings.setDefaultTextEncodingName("UTF-8"); // 设置编码格式
//        webSettings.setNeedInitialFocus(true); // 是否需要获取焦点
//        webSettings.setGeolocationEnabled(false); // 设置开启定位功能
//        webSettings.setBlockNetworkLoads(false); // 是否从网络获取资源

    }

    //浏览器

    private class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                progressbar.setVisibility(View.GONE);
            } else {
                if (progressbar.getVisibility() == View.GONE)
                    progressbar.setVisibility(View.VISIBLE);
                progressbar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }


    }


    private String getUrl() {
        if (getIntent() != null) {
            return getIntent().getStringExtra(SHOPLINKEXTR);
        }
        return null;
    }
}
