package com.example.ian.meizitu;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Ian on 2017/10/26.
 */

public class ApiService {

    private static GankApi SERVICE;

    private static final String BaseUrl = "http://gank.io/api/";

    /**
     * 请求超时时间
     */

    private static final int DEFALUT_TIMEOUT = 10000;

    public static GankApi getService(){

        //创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(DEFALUT_TIMEOUT, TimeUnit.SECONDS);

        SERVICE = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BaseUrl)
                .build().create(GankApi.class);

        return SERVICE;
    }
}
