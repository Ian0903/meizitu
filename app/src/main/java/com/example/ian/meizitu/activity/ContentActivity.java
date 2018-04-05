package com.example.ian.meizitu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.ian.meizitu.R;
import com.example.ian.meizitu.adapter.ContentAdapter;
import com.example.ian.meizitu.data.Datedata;
import com.example.ian.meizitu.data.entity.Gank;
import com.example.ian.meizitu.net.ApiService;
import com.example.ian.meizitu.util.SnackbarUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class ContentActivity extends AppCompatActivity {

    private CoordinatorLayout contentLayout;
    private List<Gank> contentList = new ArrayList<>();
    private RecyclerView contentRecyler;
    private ContentAdapter contentAdapter;
    private LinearLayoutManager contentLayoutManager;
    private ImageView headerImage;
    private String year,month,day;
    private String videoUrl,photoUrl;
    private Toolbar headerToolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_content);
        headerToolbar = (Toolbar) findViewById(R.id.header_toolbar);
        contentLayout = (CoordinatorLayout)findViewById(R.id.contentLayout);
        contentRecyler = (RecyclerView)findViewById(R.id.content_list);
        headerImage = (ImageView)findViewById(R.id.header_image);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collToolbar);
        getDate();
        
        init();
        setListener();
        getContent();
    }

    public void init(){
        headerToolbar.inflateMenu(R.menu.menu_content);
        collapsingToolbarLayout.setTitle(year+"-"+month+"-"+day);
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.actionMenuColor));
        headerToolbar.setNavigationIcon(R.mipmap.ic_back_white_24dp);
        Glide.with(ContentActivity.this).load(photoUrl).into(headerImage);

        contentLayoutManager = new LinearLayoutManager(ContentActivity.this,LinearLayoutManager.VERTICAL,false);
        contentRecyler.setLayoutManager(contentLayoutManager);
        contentRecyler.setAdapter(contentAdapter = new ContentAdapter(ContentActivity.this,contentList));
    }

    public void setListener(){
        headerToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.video_share ){
                    shareVideo();
                }
                return false;
            }
        });

        headerToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    //获取选定日期
    public void getDate(){

        Intent dateIntent = getIntent();
        photoUrl = dateIntent.getStringExtra("photoUrl");

        String date = dateIntent.getStringExtra("date");
        String[] temp = date.split("-");
        year = temp[0];
        month = temp[1];
        day = temp[2].substring(0,2);

    }

    public void getContent(){
        ApiService.getService().getContent(year,month,day)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(new Func1<Datedata, Datedata.ResultsBean>() {
                    @Override
                    public Datedata.ResultsBean call(Datedata dateData) {
                        return dateData.getResults();
                    }
                })
                .map(this::addAllResult
                )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Gank>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        SnackbarUtil.ShortSnackbar(contentLayout,"获取数据失败",SnackbarUtil.Info).show();
                        e.printStackTrace();

                    }

                    @Override
                    public void onNext(List<Gank> ganks) {
                        if(ganks.isEmpty())
                        {
                            SnackbarUtil.ShortSnackbar(contentLayout,"获取数据失败",SnackbarUtil.Info).show();
                        }else{
                            contentAdapter.notifyDataSetChanged();
                        }
                    }
                });

    }

    private List<Gank> addAllResult(Datedata.ResultsBean results){
        if(results.androidList!=null) contentList.addAll(results.androidList);
        if(results.iOSList!=null) contentList.addAll(results.iOSList);
        if(results.拓展资源List!=null) contentList.addAll(results.拓展资源List);
        if(results.瞎推荐List!=null) contentList.addAll(results.瞎推荐List);
        if(results.休息视频List!=null){
            contentList.addAll(results.休息视频List);
            videoUrl = results.休息视频List.get(0).getUrl();
        }
        if(results.妹纸List!=null) contentList.addAll(results.妹纸List);
        return contentList;
    }

    private void shareVideo(){
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,"发现一个有趣的视频哦！\n"+videoUrl+"\n来自干货集中营");
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent,"分享给..."));
    }







}
