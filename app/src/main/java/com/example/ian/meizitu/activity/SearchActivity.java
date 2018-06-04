package com.example.ian.meizitu.activity;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.ian.meizitu.R;
import com.example.ian.meizitu.adapter.QueryAdapter;
import com.example.ian.meizitu.data.Categorydata;
import com.example.ian.meizitu.data.entity.Gank;
import com.example.ian.meizitu.net.ApiService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SearchActivity extends AppCompatActivity {

    public static final int DEFAULT_COUNT = 27;
    public static final int DEFAULT_PAGE = 1;

    @BindView(R.id.search_view) public SearchView searchView;
    @BindView(R.id.search_toolbar) public Toolbar toolbar;
    @BindView(R.id.search_list) public RecyclerView recyclerView;
    @BindView(R.id.empty_view) public TextView emptyQuery;
    @BindView(R.id.search_layout_id) public CoordinatorLayout coordinatorLayout;

    private LinearLayoutManager linearLayoutManager;
    private List<Gank> searchResults = new ArrayList<>();
    private QueryAdapter queryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        initToolbar();
        initRecyclerView();
        initSearchView();

    }

    private void initRecyclerView(){
        linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(queryAdapter = new QueryAdapter(searchResults,this));
    }

    private void initSearchView(){
        searchView.onActionViewExpanded();
        searchView.setQueryHint("请输入文章关键字");
        searchView.setSubmitButtonEnabled(true);
        searchView.requestFocus();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                queryData(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void initToolbar(){
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void queryData(String queryText){
        ApiService.getService().getQueryData(queryText,DEFAULT_COUNT,DEFAULT_PAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Categorydata>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Snackbar.make(coordinatorLayout,"搜索失败,请重试",Snackbar.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(Categorydata categorydata) {
                        searchResults.clear();
                        List<Gank> temp = categorydata.getResults();
                        if(temp == null || temp.size() == 0){
                            emptyQuery.setVisibility(View.VISIBLE);
                        }else{
                            if(emptyQuery.getVisibility() == View.VISIBLE){
                                emptyQuery.setVisibility(View.INVISIBLE);
                            }
                            searchResults.addAll(temp);
                        }
                        queryAdapter.notifyDataSetChanged();
                    }
                });
    }

}
