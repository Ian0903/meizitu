package com.example.ian.meizitu.util;

import android.app.Application;
import android.content.Context;

import com.litesuits.orm.BuildConfig;
import com.litesuits.orm.LiteOrm;

/**
 * Created by Ian on 2018/4/4.
 */

public class MyApp extends Application {

    private static final String DB_NAME = "gank.db";
    public static LiteOrm liteOrm;
    public static Context context;

    @Override
    public void onCreate(){
        super.onCreate();
        context = this;
        liteOrm = LiteOrm.newSingleInstance(context,DB_NAME);
        if(BuildConfig.DEBUG){
            liteOrm.setDebugged(true);
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }


}
