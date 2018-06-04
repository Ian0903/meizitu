package com.example.ian.meizitu.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.ian.meizitu.R;
import com.example.ian.meizitu.data.entity.Save;
import com.example.ian.meizitu.util.MyApp;
import com.example.ian.meizitu.util.Share;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.litesuits.orm.db.model.ConflictAlgorithm;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebActivity extends AppCompatActivity {

    @BindView(R.id.website) public WebView website;
    @BindView(R.id.toolbar) public Toolbar webToolbar;
    private String url;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);
        getUrlAndTitle();
        initToolbar();
        initWebView();
    }

    private void initToolbar(){
        webToolbar.setTitle(title);
        webToolbar.inflateMenu(R.menu.menu_web);
        webToolbar.setNavigationIcon(R.mipmap.ic_back_white_24dp);
        webToolbar.setNavigationOnClickListener(v -> finish());
        webToolbar.setOnMenuItemClickListener(item -> {
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
            }else if(itemId == R.id.save_website){
                saveWebsite();
            }
            return false;
        });
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

    private void getUrlAndTitle(){
        url = getIntent().getStringExtra("webUrl");
        title = getIntent().getStringExtra("webTitle");
    }

    private void shareWebsite(){
        Share.shareUrl(this,url,title);
    }

    private void saveWebsite(){
        Save save = new Save();
        //检测是否重复收藏
        List<Save> isSorted = MyApp.liteOrm.query(new QueryBuilder<>(Save.class)
                .where("desc = ? ",new String[]{title})
        );
        if(isSorted.isEmpty() || isSorted.size() == 0){
            save.setDesc(title);
            save.setUrl(url);
            save.setSaveTime(new Date().getTime());//保存收藏时间，用于查询的排序
            MyApp.liteOrm.insert(save, ConflictAlgorithm.Replace);
            Toast.makeText(WebActivity.this,"收藏成功",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(WebActivity.this,"文章已存在", Toast.LENGTH_SHORT).show();
        }
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
