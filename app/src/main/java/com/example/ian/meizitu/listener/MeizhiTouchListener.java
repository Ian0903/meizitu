package com.example.ian.meizitu.listener;

import android.view.View;

import com.example.ian.meizitu.data.entity.Meizi;

/**
 * Created by Ian on 2018/3/27.
 */

public interface MeizhiTouchListener {
     void onTouch(View v,View photoView, View titleView, Meizi meizi);
}
