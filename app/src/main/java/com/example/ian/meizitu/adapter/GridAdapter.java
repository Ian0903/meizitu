package com.example.ian.meizitu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ian.meizitu.R;
import com.example.ian.meizitu.data.entity.Gank;
import com.example.ian.meizitu.listener.MeizhiTouchListener;
import com.example.ian.meizitu.widget.RatioImageView;

import java.util.List;

import static com.example.ian.meizitu.R.id.grid_photo;
import static com.example.ian.meizitu.R.id.grid_title;

/**
 * Created by Ian on 2017/10/22.
 */

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.MyViewHolder>{

    private Context context;
    private List<Gank> ganks;
    private MeizhiTouchListener mMeizhiTouchListener;

    //初始化adapter
    public GridAdapter(List<Gank> ganks, Context context){
        this.ganks = ganks;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //加载item布局
        View view = LayoutInflater.from(context).inflate(R.layout.grid_item,parent,false);

        MyViewHolder viewHolder = new MyViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Gank gank = ganks.get(position);
        holder.gank = gank;

        Glide.with(context).load(gank.getUrl()).placeholder(R.mipmap.ic_placeholder).into(holder.gridPhoto);
        holder.gridTitle.setText(gank.getDesc());
    }

    @Override
    public int getItemCount() {
        return ganks.size();
    }

    public void setMeizhiTouchListener(MeizhiTouchListener mMeizhiTouchListener){
        this.mMeizhiTouchListener = mMeizhiTouchListener;
    }

   class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView gridTitle;

        private RatioImageView gridPhoto;

        Gank gank;


        public MyViewHolder(View view){
            super(view);
            gridPhoto = (RatioImageView) view.findViewById(grid_photo);
            gridTitle = (TextView)view.findViewById(grid_title);
            gridPhoto.setOnClickListener(this);
            gridTitle.setOnClickListener(this);
        }

       @Override
       public void onClick(View v) {
            int position = getLayoutPosition();
            mMeizhiTouchListener.onTouch(v,gridPhoto,gridTitle,gank);
       }
   }
}
