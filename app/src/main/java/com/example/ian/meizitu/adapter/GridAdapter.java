package com.example.ian.meizitu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.ian.meizitu.bean.Meizis;
import com.example.ian.meizitu.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Ian on 2017/10/22.
 */

public class GridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{

    private Context context;

    private List<Meizis.Meizi> meizis;

    //初始化adapter
    public GridAdapter(List<Meizis.Meizi> meizis, Context context){
        this.meizis=meizis;
        this.context = context;
    }

    //自定义监听事件
    public static interface onReyclerViewItemClickListener{
        void onItemClick(View view);
    }

    private onReyclerViewItemClickListener itemClickListener = null;

    public void setOnItemClickListener(onReyclerViewItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //加载item布局
        View view = LayoutInflater.from(context).inflate(R.layout.grid_item,parent,false);

        MyViewHolder viewHolder = new MyViewHolder(view);

        view.setOnClickListener(this);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        //绑定item图片
       Picasso.with(context).load(meizis.get(position).getUrl()).into(((MyViewHolder) holder).grid_photo);
        //绑定item标题
        ((MyViewHolder)holder).grid_title.setText(meizis.get(position).getDesc());
    }

    @Override
    public int getItemCount() {
        return meizis.size();
    }

    //item点击事件回调
    public void onClick(View v){
        if(itemClickListener!=null){
            itemClickListener.onItemClick(v);
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView grid_title;

        private ImageButton grid_photo;

        public MyViewHolder(View view){
            super(view);
            grid_photo = (ImageButton)view.findViewById(R.id.grid_photo);
            grid_title = (TextView)view.findViewById(R.id.grid_title);

        }
    }
}
