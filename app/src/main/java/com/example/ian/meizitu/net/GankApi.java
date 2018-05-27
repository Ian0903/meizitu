package com.example.ian.meizitu.net;

import com.example.ian.meizitu.data.Categorydata;
import com.example.ian.meizitu.data.Datedata;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;


/**
 * Created by Ian on 2017/10/26.
 */

public interface GankApi {

    @GET("data/{type}/{count}/{page}")
    Observable<Categorydata>  getCategoryData(@Path("type") String type, @Path("count") int count, @Path("page") int page);

    @GET("day/{year}/{month}/{day}")
    Observable<Datedata> getContent(@Path("year") String year, @Path("month") String month, @Path("day") String day);

    @GET("search/query/{queryText}/category/all/count/{count}/page/{page}")
    Observable<Categorydata> getQueryData(@Path("queryText") String queryText,@Path("count") int count,@Path("page") int page);
}
