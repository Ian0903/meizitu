package com.example.ian.meizitu.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.ian.meizitu.R;
import com.example.ian.meizitu.adapter.SaveAdapter;
import com.example.ian.meizitu.data.entity.Save;
import com.example.ian.meizitu.util.MyApp;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.litesuits.orm.db.assit.WhereBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SaveActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) public Toolbar toolbar;
    @BindView(R.id.save_list) public RecyclerView recyclerView;

    private LinearLayoutManager linearLayoutManager;
    private List<Save> saves = new ArrayList<>();
    private SaveAdapter saveAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);
        ButterKnife.bind(this);
        initToolbar();
        queryDatabase();
        initRecyclerView();
    }

    private void queryDatabase(){
        QueryBuilder<Save> queryBuilder = new QueryBuilder<>(Save.class)
                .appendOrderAscBy("saveTime");
        saves = MyApp.liteOrm.query(queryBuilder);

    }

    private void initToolbar(){
        toolbar.setNavigationIcon(R.mipmap.ic_back_white_24dp);
        toolbar.setTitle(R.string.save_title);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void initRecyclerView(){
        recyclerView = (RecyclerView) findViewById(R.id.save_list);
        recyclerView.setAdapter(saveAdapter = new SaveAdapter(saves,this));
        linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        saveAdapter.setSaveItemTouchListener((v, titleView, deleteView, save, position) -> {
            if(save == null) return;
            if(v == titleView){
                Intent intent = new Intent(SaveActivity.this,WebActivity.class);
                intent.putExtra("webTitle",save.getDesc());
                intent.putExtra("webUrl",save.getUrl());
                startActivity(intent);
            }
            else if(v == deleteView){
                new AlertDialog.Builder(SaveActivity.this)
                        .setMessage("确认删除文章？")
                        .setPositiveButton("确定", (dialog, which) -> {
                            saves.remove(position);
                            MyApp.liteOrm.delete(new WhereBuilder(Save.class)
                                    .where("desc = ? ",new String[]{save.getDesc()})
                            );
                            saveAdapter.notifyDataSetChanged();
                        })
                        .setNegativeButton("取消", (dialog, which) -> {
                            return;
                        }).show();

            }
        });
    }
}
