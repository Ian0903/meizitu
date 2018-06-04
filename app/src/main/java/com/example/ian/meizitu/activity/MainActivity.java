package com.example.ian.meizitu.activity;

import android.content.Intent;
import android.content.SharedPreferences;
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

import com.example.ian.meizitu.R;
import com.example.ian.meizitu.adapter.GridAdapter;
import com.example.ian.meizitu.data.Categorydata;
import com.example.ian.meizitu.data.entity.Gank;
import com.example.ian.meizitu.listener.MeizhiTouchListener;
import com.example.ian.meizitu.net.ApiService;
import com.example.ian.meizitu.util.MyApp;
import com.example.ian.meizitu.util.Share;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.litesuits.orm.db.model.ConflictAlgorithm;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final int PRELOAD_SIZE = 6;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.grid_view)
    public RecyclerView recyclerView;
    @BindView(R.id.coordinatorLayout)
    public CoordinatorLayout coordinatorLayout;
    private List<Gank> ganks = new ArrayList<>();
    @BindView(R.id.grid_swipe_refresh)
    public SwipeRefreshLayout swipeRefreshLayout;
    private StaggeredGridLayoutManager mLayoutManager;
    private int page = 1;
    private GridAdapter adapter;
    private boolean isFirstTimeTouchBottom = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isFirstStart = isFirstOpen();
        //如果第一次启动则进入引导页面
        if(isFirstStart){
            Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        //否则直接进入主页面
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getCatch();
        initToolbar();
        initRecycler();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        new Handler().postDelayed(() -> setRefresh(true),350);
        GetData(true);

    }


    private void initRecycler(){
        //设置swipeRefreshLayout位置
        swipeRefreshLayout.setProgressViewOffset(false, 0,  (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
        //设置RecyclerView为瀑布流布局
        mLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter = new GridAdapter(ganks,MainActivity.this) );
        adapter.setMeizhiTouchListener(getMeiziTouchListener());

        //设置上拉刷新监听
        swipeRefreshLayout.setOnRefreshListener(() -> {
            setRefresh(true);
            page = 1;
            GetData(true);
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

    }

    private void initToolbar(){
        toolbar.setTitle(R.string.app_name);
        toolbar.inflateMenu(R.menu.menu_main);

        toolbar.setOnMenuItemClickListener(item -> {

            switch (item.getItemId()){
                case R.id.gank_search:
                    startActivity(new Intent(MainActivity.this,SearchActivity.class));
                    break;
                case R.id.gank_share:
                    Share.shareApp(MainActivity.this);
                    break;
                case R.id.gank_about:
                    startActivity(new Intent(MainActivity.this,AboutActivity.class));
                    break;
                case R.id.my_save:
                    startActivity(new Intent(MainActivity.this, SaveActivity.class));
                    break;
            }
            return false;
        });

        toolbar.setOnClickListener(v -> recyclerView.smoothScrollToPosition(0));
    }

    public void GetData(boolean isCleanDB){

        Observable.zip(ApiService.getService().getCategoryData("福利",10,page),ApiService.getService().getCategoryData("休息视频",10,page),
                (meizidata, videodata) -> createPhotoWithVideo(meizidata,videodata))
                    .doOnNext(gankList -> {
                        //将请求的数据保存进数据库
                        MyApp.liteOrm.insert(gankList, ConflictAlgorithm.Replace);
                    })
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<List<Gank>>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            Snackbar.make(coordinatorLayout,"数据请求失败",Snackbar.LENGTH_LONG).show();
                        }

                        @Override
                        public void onNext(List<Gank> gankList) {
                            if(isCleanDB) ganks.clear();
                            ganks.addAll(gankList);
                            adapter.notifyDataSetChanged();
                            setRefresh(false);
                        }
                    });

    }

    private MeizhiTouchListener getMeiziTouchListener(){
        return (v, photoView, titleView, meizi) -> {
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
        };
    }

    private List<Gank> createPhotoWithVideo(Categorydata meiziData,Categorydata videoData){
        List<Gank> temp1 = videoData.getResults();
        List<Gank> temp2 = meiziData.getResults();
        for(int i = 0;i<temp1.size();i++)
            temp2.get(i).setDesc(temp1.get(i).getDesc());
        return temp2;
    }


    private void setRefresh(boolean isRefresh){
        if(swipeRefreshLayout == null)
            return;
        if(!isRefresh){
            //防止刷新图标消失太快
            swipeRefreshLayout.postDelayed(() -> swipeRefreshLayout.setRefreshing(false),1000);
        }else{
            swipeRefreshLayout.setRefreshing(true);
        }
    }


    private boolean isFirstOpen(){
        SharedPreferences pref = getSharedPreferences("data",MODE_PRIVATE);
        return pref.getBoolean("isFirstOpen",true);
    }

    private void getCatch(){
        QueryBuilder query = new QueryBuilder(Gank.class);
        query.appendOrderDescBy("publishedAt");
        query.limit(0, 10);
        ganks.addAll(MyApp.liteOrm.query(query));
    }
}
