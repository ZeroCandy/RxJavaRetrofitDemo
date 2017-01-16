package com.github.zerocandy.rxredemo.net;

import com.github.zerocandy.rxredemo.bean.MovieEntity;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by 帅阳 on 2017/1/16.
 */

public interface MovieService {
    @GET("top250")
    Call<MovieEntity> getTop250Movie(@Query("start") int start, @Query("count") int count);
}
