package com.example.ian.meizitu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ian.meizitu.R;
import com.example.ian.meizitu.data.entity.Meizi;
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
    private List<Meizi> meizis;
    private MeizhiTouchListener mMeizhiTouchListener;

    //初始化adapter
    public GridAdapter(List<Meizi> meizis, Context context){
        this.meizis = meizis;
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
        Meizi meizi = meizis.get(position);
        holder.meizi = meizi;
        //绑定item图片
        Glide.with(context).load(meizis.get(position).getUrl()).into(holder.gridPhoto);
        //绑定item标题
        holder.gridTitle.setText(meizi.getDesc());
    }

    @Override
    public int getItemCount() {
        return meizis.size();
    }

    public void setMeizhiTouchListener(MeizhiTouchListener mMeizhiTouchListener){
        this.mMeizhiTouchListener = mMeizhiTouchListener;
    }

   class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView gridTitle;

        private RatioImageView gridPhoto;

        Meizi meizi;


        public MyViewHolder(View view){
            super(view);
            gridPhoto = (RatioImageView) view.findViewById(grid_photo);
            gridTitle = (TextView)view.findViewById(grid_title);
            gridPhoto.setOnClickListener(this);
            gridTitle.setOnClickListener(this);
        }

       @Override
       public void onClick(View v) {
            mMeizhiTouchListener.onTouch(v,gridPhoto,gridTitle,meizi);
       }
   }
}
