package com.example.ian.meizitu;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class ContentActivity extends AppCompatActivity {

    private ImageButton imageButton;
    private CoordinatorLayout contentLayout;
    private List<Basebean> contentList;
    private RecyclerView contentRecycler;
    private ContentAdapter contentAdapter;
    private LinearLayoutManager contentLayoutManager;
    private String year,month,day;
    private String videoUrl;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_content);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        init();

        setListener();

        getDate();


    }

    public void init(){
        title = (TextView)findViewById(R.id.content_title);
        title.getPaint().setFakeBoldText(true);

        imageButton = (ImageButton)findViewById(R.id.video_photo);

        contentList = new ArrayList<>();

        contentLayout = (CoordinatorLayout)findViewById(R.id.contentLayout);

        contentRecycler = (RecyclerView)findViewById(R.id.content_recycler);

        contentLayoutManager = new LinearLayoutManager(ContentActivity.this,LinearLayoutManager.VERTICAL,false);

        contentRecycler.setLayoutManager(contentLayoutManager);

        contentRecycler.setAdapter(contentAdapter = new ContentAdapter(ContentActivity.this,contentList));
    }

    public void setListener(){
        //图片设置监听
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent videoIntent = new Intent(ContentActivity.this,VideoActivity.class);
                videoIntent.putExtra("videoUrl",videoUrl);
                startActivity(videoIntent);
            }
        });

        //文章列表设置监听
        contentAdapter.setOnItemClickListener(new ContentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view) {
                int position = contentRecycler.getChildAdapterPosition(view);
                String articleUrl = contentList.get(position).getUrl();
                Intent artUrlIntent = new Intent(ContentActivity.this,ArticleActivity.class);
                artUrlIntent.putExtra("articleUrl",articleUrl);
                startActivity(artUrlIntent);
            }
        });
    }

    //获取选定日期
    public void getDate(){

        Intent dateIntent = getIntent();
        String date = dateIntent.getStringExtra("date");
        String[] temp = date.split("-");
        year = temp[0];
        month = temp[1];
        day = temp[2].substring(0,2);
        getContent();

    }

    public void getContent(){

        ApiService.getService().getContent(year,month,day)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(new Func1<DateEnity, DateEnity.ResultsBean>() {
                    @Override
                    public DateEnity.ResultsBean call(DateEnity dateEnity) {
                        return dateEnity.getResults();
                    }
                })
                .map(this::addAllResult
                )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Basebean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                       // SnackbarUtil.ShortSnackbar(contentLayout,"获取数据失败",SnackbarUtil.Info).show();
                        e.printStackTrace();

                    }

                    @Override
                    public void onNext(List<Basebean> basebeen) {
                        if(basebeen.isEmpty())
                        {
                            SnackbarUtil.ShortSnackbar(contentLayout,"获取数据失败",SnackbarUtil.Info).show();
                        }else{
                            contentAdapter.notifyDataSetChanged();
                        }
                    }
                });

    }

    private List<Basebean> addAllResult(DateEnity.ResultsBean results){
        if(results.Android!=null) contentList.addAll(results.Android);
        if(results.iOS!=null) contentList.addAll(results.iOS);
        if(results.Other!=null) contentList.addAll(results.Other);
        if(results.Fore!=null) contentList.addAll(results.Fore);
        if(results.Recom!=null) contentList.addAll(results.Recom);
        if(results.Video!=null){
            contentList.addAll(results.Video);
            videoUrl = results.Video.get(0).getUrl();
        }
        if(results.Weal!=null) contentList.addAll(results.Weal);
        return contentList;
    }







}
