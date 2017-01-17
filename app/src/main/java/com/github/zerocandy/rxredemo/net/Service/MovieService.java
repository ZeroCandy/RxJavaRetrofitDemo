package com.github.zerocandy.rxredemo.net.Service;


import com.github.zerocandy.rxredemo.bean.HttpResult;
import com.github.zerocandy.rxredemo.bean.Subject;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by 帅阳 on 2017/1/16.
 */

public interface MovieService {
    @GET("top250")
    Observable<HttpResult<List<Subject>>> getTop250Movie(@Query("start") int start, @Query("count") int count);
}
