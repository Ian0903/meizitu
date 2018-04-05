package com.example.ian.meizitu.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.ian.meizitu.R;

public class WebActivity extends AppCompatActivity {

    private WebView website;
    private Toolbar webToolbar;
    private String url;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_web);
        webToolbar = (Toolbar)findViewById(R.id.toolbar);
        website = (WebView)findViewById(R.id.website);

        //获取文章URL
        url = getIntent().getStringExtra("webUrl");
        title = getIntent().getStringExtra("webTitle");
        webToolbar.setTitle(title);
        webToolbar.inflateMenu(R.menu.menu_web);
        webToolbar.setNavigationIcon(R.mipmap.ic_back_white_24dp);
        webToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        webToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if(itemId == R.id.copy_website){
                    ClipboardManager clm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText(null,url);
                    clm.setPrimaryClip(clipData);
                    Toast.makeText(WebActivity.this,"复制成功", Toast.LENGTH_SHORT).show();
                }else if(itemId == R.id.share_website){
                    shareWebsite();
                }else if(itemId == R.id.open_website){
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                    startActivity(intent);
                }
                return false;
            }
        });
        initWebView();
    }

    private void initWebView(){
        //配置WebView
        WebSettings webSettings = website.getSettings();
        webSettings.setLoadWithOverviewMode(true);//缩放至屏幕大小
        webSettings.setJavaScriptEnabled(true);//设置JS交互
        webSettings.setUseWideViewPort(true);//设置图片自适应窗口大小

        //加载url
        website.setWebViewClient(new WebViewClient());
        website.loadUrl(url);
    }

    private void shareWebsite(){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT,title+"\n"+url);
        shareIntent.setType("text/plain");
        startActivity(Intent.createChooser(shareIntent,"分享给..."));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(website!=null)
            website.destroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(website!=null)
            website.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(website!=null)
            website.onResume();
    }
}
