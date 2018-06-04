package com.example.ian.meizitu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ContentActivity extends AppCompatActivity {

    @BindView(R.id.contentLayout) public CoordinatorLayout contentLayout;
    @BindView(R.id.content_list) public RecyclerView contentRecyler;
    @BindView(R.id.header_image) public ImageView headerImage;
    @BindView(R.id.header_toolbar) public Toolbar headerToolbar;
    @BindView(R.id.collToolbar) public CollapsingToolbarLayout collapsingToolbarLayout;

    private List<Gank> contentList = new ArrayList<>();
    private ContentAdapter contentAdapter;
    private LinearLayoutManager contentLayoutManager;
    private String year,month,day;
    private String videoUrl,photoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_content);
        ButterKnife.bind(this);
        getDate();
        initToolbar();
        initRecycler();
        getContent();
    }

    private void initToolbar(){
        collapsingToolbarLayout.setTitle(year+"-"+month+"-"+day);
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.white));
        Glide.with(ContentActivity.this).load(photoUrl).into(headerImage);
        headerToolbar.setNavigationOnClickListener(v -> finish());


    }

    private void initRecycler(){
        contentLayoutManager = new LinearLayoutManager(ContentActivity.this,LinearLayoutManager.VERTICAL,false);
        contentRecyler.setLayoutManager(contentLayoutManager);
        contentRecyler.setAdapter(contentAdapter = new ContentAdapter(ContentActivity.this,contentList));
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
                .map(dateData -> dateData.getResults())
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
        if(results.appList!=null) contentList.addAll(results.appList);
        if(results.休息视频List!=null){
            contentList.addAll(results.休息视频List);
            videoUrl = results.休息视频List.get(0).getUrl();
        }
        if(results.妹纸List!=null) contentList.addAll(results.妹纸List);
        return contentList;
    }








}
