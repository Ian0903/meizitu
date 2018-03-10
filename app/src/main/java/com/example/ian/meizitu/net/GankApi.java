package com.example.ian.meizitu.net;

import com.example.ian.meizitu.bean.DateEnity;
import com.example.ian.meizitu.bean.Meizis;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;


/**
 * Created by Ian on 2017/10/26.
 */

public interface GankApi {
    @GET("data/{type}/{count}/{page}")
    Observable<Meizis>  getData(@Path("type") String type, @Path("count") int count, @Path("page") int page );

    @GET("day/{year}/{month}/{day}")
    Observable<DateEnity> getContent(@Path("year") String year, @Path("month") String month, @Path("day") String day);
}
