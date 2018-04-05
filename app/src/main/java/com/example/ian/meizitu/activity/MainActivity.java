package com.example.ian.meizitu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.example.ian.meizitu.R;
import com.example.ian.meizitu.adapter.GridAdapter;
import com.example.ian.meizitu.data.Meizidata;
import com.example.ian.meizitu.data.Videodata;
import com.example.ian.meizitu.data.entity.Gank;
import com.example.ian.meizitu.data.entity.Meizi;
import com.example.ian.meizitu.listener.MeizhiTouchListener;
import com.example.ian.meizitu.net.ApiService;
import com.example.ian.meizitu.util.MyApp;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.litesuits.orm.db.model.ConflictAlgorithm;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final int PRELOAD_SIZE = 6;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private CoordinatorLayout coordinatorLayout;
    private List<Meizi> meizis = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private StaggeredGridLayoutManager mLayoutManager;
    private int page = 1;
    private GridAdapter adapter;
    private boolean isFirstTimeTouchBottom = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        QueryBuilder query = new QueryBuilder(Meizi.class);
        query.appendOrderDescBy("publishedAt");
        query.limit(0, 10);
        meizis.addAll(MyApp.liteOrm.query(query));
        init();
        setListener();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setRefresh(true);
            }
        },350);
        GetData(true);

    }

    public void init(){
        //初始化toolbar
        toolbar.inflateMenu(R.menu.menu_main);
        toolbar.setTitle("干货集中营");

        recyclerView = (RecyclerView)findViewById(R.id.grid_view);
        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinatorLayout);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.grid_swipe_refresh);

        //设置swipeRefreshLayout位置
        swipeRefreshLayout.setProgressViewOffset(false, 0,  (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));

        //设置RecyclerView为瀑布流布局
        mLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter = new GridAdapter(meizis,MainActivity.this) );


    }

    public void setListener(){
        adapter.setMeizhiTouchListener(getMeiziTouchListener());

        //设置上拉刷新监听
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                setRefresh(true);
                page = 1;
                GetData(true);
            }
        });


        //设置下拉加载监听
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //获取加载的最后一个可见视图在适配器的位置
                boolean isBottom =
                        mLayoutManager.findLastCompletelyVisibleItemPositions(new int[2])[1] >=
                                adapter.getItemCount() - PRELOAD_SIZE;
                if(!swipeRefreshLayout.isRefreshing() && isBottom && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if(isFirstTimeTouchBottom){
                        isFirstTimeTouchBottom = false;
                    }else{
                        setRefresh(true);
                        page++;
                        GetData(false);
                    }
                }
            }
        });

        //设置干货集中营按钮监听
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.gank_shareapp:
                        Intent sendIntent = new Intent(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT,"发现一个充满干货的App哦！\n 下载地址：");
                        sendIntent.setType("text/plain");
                        startActivity(Intent.createChooser(sendIntent,"分享给..."));
                        break;
                    case R.id.gank_about:
                        startActivity(new Intent(MainActivity.this,AboutActivity.class));
                        break;
                }
                return false;
            }
        });

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.smoothScrollToPosition(0);
            }
        });
    }



    public void GetData(boolean isCleanDB){

        Observable.zip(ApiService.getService().getMeiziData(page),ApiService.getService().getVideoData(page),
                new Func2<Meizidata,Videodata,List<Meizi>>(){
                    @Override
                    public List<Meizi> call(Meizidata meizidata, Videodata videodata) {
                        return createPhotoWithVideo(meizidata,videodata);
                    }
                })
                    .doOnNext(new Action1<List<Meizi>>() {
                        @Override
                        public void call(List<Meizi> meiziList) {
                            //将请求的数据保存进数据库
                            MyApp.liteOrm.insert(meiziList, ConflictAlgorithm.Replace);
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<List<Meizi>>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            Snackbar.make(coordinatorLayout,"数据请求失败",Snackbar.LENGTH_LONG).show();
                        }

                        @Override
                        public void onNext(List<Meizi> meiziList) {
                            if(isCleanDB) meizis.clear();
                            meizis.addAll(meiziList);
                            adapter.notifyDataSetChanged();
                            setRefresh(false);
                        }
                    });

    }

    private MeizhiTouchListener getMeiziTouchListener(){
        return new MeizhiTouchListener() {
            @Override
            public void onTouch(View v, View photoView, View titleView, Meizi meizi) {
                if(meizi == null) return;
                if(v == photoView){
                    Intent intent = new Intent(MainActivity.this,PictureActivity.class);
                    intent.putExtra("photoUrl",meizi.getUrl());
                    intent.putExtra("title",meizi.getDesc());
                    startActivity(intent);
                }
                else if(v == titleView){
                    Intent intent = new Intent(MainActivity.this,ContentActivity.class);
                    intent.putExtra("date",meizi.getPublishedAt());//用日期作为请求标志
                    intent.putExtra("photoUrl",meizi.getUrl());
                    startActivity(intent);
                }
            }
        };
    }

    private List<Meizi> createPhotoWithVideo(Meizidata meizidata,Videodata videodata){
        List<Gank> temp1 = videodata.getResults();
        List<Meizi> temp2 = meizidata.getResults();
        for(int i = 0;i<temp1.size();i++)
            temp2.get(i).setDesc(temp1.get(i).getDesc());
        return temp2;
    }


    private void setRefresh(boolean isRefresh){
        if(swipeRefreshLayout == null)
            return;
        if(!isRefresh){
            //防止刷新图标消失太快
            swipeRefreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(false);
                }
            },1000);
        }else{
            swipeRefreshLayout.setRefreshing(true);
        }
    }

}
