package com.example.ian.meizitu;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private CoordinatorLayout coordinatorLayout;
    private List<Meizis.Meizi> meizis;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int lastVisibleItem;
    private StaggeredGridLayoutManager mLayoutManager;
    private int page;
    private GridAdapter adapter;
    private String lastestId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar)findViewById(R.id.toolbar);

        init();
        setListener();
        GetData(10,1);
    }

    public void init(){
        //初始化toolbar
        toolbar.inflateMenu(R.menu.toolbar_menu);
        toolbar.setTitleTextColor(getResources().getColor(R.color.actionMenuColor));//设置标题颜色

        recyclerView = (RecyclerView)findViewById(R.id.grid_view);
        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinatorLayout);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.grid_swipe_refresh);
        



        //设置swipeRefreshLayout位置
       swipeRefreshLayout.setProgressViewOffset(false, 0,  (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));

        //设置RecyclerView为瀑布流布局
        mLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);



    }

    public void setListener(){
        //设置上拉刷新监听
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){

            @Override
            public void onRefresh() {
                page = 1;
                GetData(10,1);
            }
        });


        //设置下拉加载监听
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //当滑动状态停止且剩余少于两个item时，加载下一页
                if(newState==RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem+2>mLayoutManager.getItemCount()){
                    GetData(10,++page);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //获取加载的最后一个可见视图在适配器的位置
                int[] positions = mLayoutManager.findLastVisibleItemPositions(null);
                lastVisibleItem = Math.max(positions[0],positions[1]);
            }
        });
    }



    public void GetData(int count,int page){



            ApiService.getService().getData("福利",count,page)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(Schedulers.newThread())
                    .map(new Func1<Meizis, List<Meizis.Meizi>>() {
                        @Override
                        public List<Meizis.Meizi> call(Meizis meizis) {
                            return meizis.getResults();
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<List<Meizis.Meizi>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Snackbar.make(coordinatorLayout,"数据请求失败",Snackbar.LENGTH_LONG).show();
                        }

                        @Override
                        public void onNext(List<Meizis.Meizi> resultMeizis) {

                                swipeRefreshLayout.setRefreshing(true);

                                if(meizis == null || meizis.size()==0){
                                    meizis=resultMeizis;
                                    lastestId = meizis.get(0).get_id();
                                }else{
                                    if(!(lastestId.equals(resultMeizis.get(0).get_id()))){
                                        meizis.addAll(resultMeizis);
                                        lastestId=meizis.get(0).get_id();
                                    }
                                }
                                if(adapter==null){
                                    recyclerView.setAdapter(adapter = new GridAdapter(meizis,MainActivity.this) );

                                    //适配器实现点击监听
                                    adapter.setOnItemClickListener(new GridAdapter.onReyclerViewItemClickListener() {
                                        @Override
                                        public void onItemClick(View view) {
                                            int position = recyclerView.getChildAdapterPosition(view);
                                            SnackbarUtil.ShortSnackbar(coordinatorLayout,"点击第"+position+"个",SnackbarUtil.Info).show();
                                        }
                                    });
                                }
                                else{
                                    adapter.notifyDataSetChanged();
                                }
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    });

    }

}
