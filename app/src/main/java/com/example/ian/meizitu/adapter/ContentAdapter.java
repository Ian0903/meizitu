package com.example.ian.meizitu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ian.meizitu.bean.Basebean;
import com.example.ian.meizitu.R;

import java.util.List;

/**
 * Created by Ian on 2017/11/6.
 */

public class ContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{

    private Context context;

    private List<Basebean> listBean;

    public ContentAdapter(Context context,List<Basebean> listBean){
        this.context = context;
        this.listBean = listBean;
    }

    public interface OnItemClickListener{
        void onItemClick(View view);
    }

    OnItemClickListener onItemClickListener = null;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.content_item,parent,false);

        MyViewHolder myViewHolder = new MyViewHolder(view);

        view.setOnClickListener(this);

        return myViewHolder;


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MyViewHolder)holder).article.setText(listBean.get(position).getDesc());
    }

    @Override
    public int getItemCount() {
        return listBean.size();
    }

    @Override
    public void onClick(View v){
        if(onItemClickListener!=null){
            onItemClickListener.onItemClick(v);
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView article;

        public MyViewHolder(View view){
            super(view);
            article = (TextView)view.findViewById(R.id.content_item_article);
        }

    }
}
