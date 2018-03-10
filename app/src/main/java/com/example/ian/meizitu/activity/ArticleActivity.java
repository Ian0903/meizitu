package com.example.ian.meizitu.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.ian.meizitu.R;

public class ArticleActivity extends AppCompatActivity {

    private WebView articleWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_artical);
        articleWeb = (WebView)findViewById(R.id.articleWeb);

        //获取文章URL
        String url = getIntent().getStringExtra("url");


        //配置WebView
        WebSettings webSettings = articleWeb.getSettings();
        webSettings.setLoadWithOverviewMode(true);//缩放至屏幕大小
        webSettings.setJavaScriptEnabled(true);//设置JS交互
        webSettings.setUseWideViewPort(true);//设置图片自适应窗口大小

        //加载url
        articleWeb.setWebViewClient(new WebViewClient());
        articleWeb.loadUrl(url);
    }
}
