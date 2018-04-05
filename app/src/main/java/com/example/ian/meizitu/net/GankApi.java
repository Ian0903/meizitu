package com.example.ian.meizitu.net;

import com.example.ian.meizitu.data.Datedata;
import com.example.ian.meizitu.data.Meizidata;
import com.example.ian.meizitu.data.Videodata;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;


/**
 * Created by Ian on 2017/10/26.
 */

public interface GankApi {
    @GET("data/福利/10/{page}")
    Observable<Meizidata>  getMeiziData(@Path("page") int page );

    @GET("data/休息视频/10/{page}")
    Observable<Videodata>  getVideoData(@Path("page") int page );

    @GET("day/{year}/{month}/{day}")
    Observable<Datedata> getContent(@Path("year") String year, @Path("month") String month, @Path("day") String day);
}
