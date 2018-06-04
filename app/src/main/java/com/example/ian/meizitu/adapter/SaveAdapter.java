package com.example.ian.meizitu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ian.meizitu.R;
import com.example.ian.meizitu.data.entity.Save;
import com.example.ian.meizitu.listener.SaveItemTouchListener;

import java.util.List;

/**
 * Created by Ian on 2018/5/28.
 */

public class SaveAdapter extends RecyclerView.Adapter<SaveAdapter.MyViewHolder> {

    private List<Save> list;
    private Context context;
    private SaveItemTouchListener saveItemTouchListener;

    public SaveAdapter(List<Save> list,Context context){
        this.list = list;
        this.context = context;
    }

    public void setSaveItemTouchListener(SaveItemTouchListener saveItemTouchListener){
        this.saveItemTouchListener = saveItemTouchListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.save_item,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Save save = list.get(position);
        holder.save = save;
        holder.title.setText(save.getDesc());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView title;
        private ImageView delete;
        public Save save;
        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.save_item);
            delete = (ImageView) itemView.findViewById(R.id.save_delete_item);
            title.setOnClickListener(this);
            delete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            saveItemTouchListener.onTouch(v,title,delete,save,getLayoutPosition());
        }
    }
}
