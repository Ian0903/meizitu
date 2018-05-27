package com.example.ian.meizitu.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ian.meizitu.R;
import com.example.ian.meizitu.activity.WebActivity;
import com.example.ian.meizitu.data.entity.Gank;
import com.example.ian.meizitu.util.StringStyles;

import java.util.List;

/**
 * Created by Ian on 2017/11/6.
 */

public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.MyViewHolder>{

    private Context context;

    private List<Gank> contents;

    public ContentAdapter(Context context,List<Gank> contents){
        this.context = context;
        this.contents = contents;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.content_item,parent,false);

        return new MyViewHolder(view);


    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Gank gank = contents.get(position);
        if(position == 0){
            showCategory(holder);
        }else{
            boolean theCategoryOfLastEqualsToThis =
                    contents.get(position - 1).getType().equals(contents.get(position).getType());
            if(theCategoryOfLastEqualsToThis){
                hideCategory(holder);
            }else{
                showCategory(holder);
            }
        }

        SpannableStringBuilder ssb = new SpannableStringBuilder(gank.getDesc());
        ssb.append(StringStyles.format(holder.title.getContext()," (via. "+gank.getWho()+")",
                R.style.ViaTextAppearance));
        CharSequence titleText = ssb.subSequence(0,ssb.length());

        holder.category.setText(gank.getType());
        holder.title.setText(titleText);
    }

    private void showCategory(MyViewHolder holder){
        if(!isVisibleOf(holder.category))holder.category.setVisibility(View.VISIBLE);
    }

    private void hideCategory(MyViewHolder holder){
        if(isVisibleOf(holder.category))holder.category.setVisibility(View.GONE);
    }

    private boolean isVisibleOf(View v){
        return v.getVisibility() == View.VISIBLE;
    }

    @Override
    public int getItemCount() {
        return contents.size();
    }


     class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView category;
        private TextView title;

        public MyViewHolder(View view){
            super(view);
            category = (TextView)view.findViewById(R.id.category);
            title = (TextView) view.findViewById(R.id.title);
            title.setOnClickListener(this);
        }


         @Override
         public void onClick(View v) {
             Gank gank = contents.get(getLayoutPosition());
             Intent intent  = new Intent(v.getContext(),WebActivity.class);
             intent.putExtra("webUrl",gank.getUrl());
             intent.putExtra("webTitle",gank.getDesc());
             v.getContext().startActivity(intent);
         }
     }
}
