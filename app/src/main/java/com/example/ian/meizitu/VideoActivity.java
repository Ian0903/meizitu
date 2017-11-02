package com.example.ian.meizitu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

public class VideoActivity extends AppCompatActivity {

    private WebView videoView;

    private LinearLayout videoLayout;

    private String videoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_video);
        videoLayout = (LinearLayout)findViewById(R.id.videoLayout);

        //获取videourl
        Intent videoUrlIntent = getIntent();
        videoUrl = videoUrlIntent.getStringExtra("videoUrl");


        //布局添加webview
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        videoView = new WebView(getApplicationContext());
        videoView.setLayoutParams(params);
        videoLayout.addView(videoView);


        //配置webview
        WebSettings webSettings = videoView.getSettings();
        webSettings.setJavaScriptEnabled(true);//配置JavaScript交互
        webSettings.setUseWideViewPort(true);//设置图片适合webview大小
        webSettings.setLoadWithOverviewMode(true);//缩放至屏幕大小

        //加载视频网站
        videoView.setWebViewClient(new WebViewClient());
        videoView.loadUrl(videoUrl);





    }

    @Override
    protected void onDestroy() {

        //销毁webview
        if(videoView != null){
            //加载空白页
            videoView.loadDataWithBaseURL(null,"","text/html","utf-8",null);
            videoView.clearHistory();

            //移除webview
            ((ViewGroup)videoView.getParent()).removeView(videoView);
            videoView.destroy();
            videoView=null;
        }
        super.onDestroy();
    }
}
