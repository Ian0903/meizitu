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
 * Created by Ian on 2018/5/27.
 */

public class QueryAdapter extends RecyclerView.Adapter<QueryAdapter.MyViewHolder> {
    private List<Gank> ganks;
    private Context context;

    public QueryAdapter(List<Gank> ganks,Context context){
        this.ganks = ganks;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.query_item,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Gank gank = ganks.get(position);
        SpannableStringBuilder ssb = new SpannableStringBuilder(gank.getDesc());
        ssb.append(StringStyles.format(holder.title.getContext(),
                "（分类:" + gank.getType() + "）",R.style.ViaTextAppearance));
        CharSequence titleText = ssb.subSequence(0,ssb.length());
        holder.title.setText(titleText);
    }

    @Override
    public int getItemCount() {
        return ganks.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView title;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.query_item_title);
            title.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Gank gank = ganks.get(getLayoutPosition());
            Intent intent = new Intent(v.getContext(), WebActivity.class);
            intent.putExtra("webUrl",gank.getUrl());
            intent.putExtra("webTitle",gank.getDesc());
            v.getContext().startActivity(intent);
        }
    }
}
