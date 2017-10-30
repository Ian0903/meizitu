package com.example.ian.meizitu;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;


/**
 * Created by Ian on 2017/10/26.
 */

public interface GankApi {
    @GET("data/{type}/{count}/{page}")
    Observable<Meizis>  getData(@Path("type") String type,@Path("count") int count,@Path("page") int page );
}
