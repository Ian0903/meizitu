package com.example.ian.meizitu.listener;

import android.view.View;

import com.example.ian.meizitu.data.entity.Save;

/**
 * Created by Ian on 2018/5/28.
 */

public interface SaveItemTouchListener {
    void onTouch(View v, View titleView, View deleteView, Save save,int position);
}
