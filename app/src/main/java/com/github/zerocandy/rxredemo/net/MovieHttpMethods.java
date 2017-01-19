package com.github.zerocandy.rxredemo.net;

import com.github.zerocandy.rxredemo.bean.Subject;
import com.github.zerocandy.rxredemo.net.service.MovieService;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;

/**
 * Created by 帅阳 on 2017/1/17.
 */

public class MovieHttpMethods {
    public static final String BASE_URL = "https://api.douban.com/v2/movie/";

    private static final int DEFAULT_TIMEOUT = 5;// 默认超时时间为5秒
    private Retrofit mRetrofit;
    private MovieService mMovieService;

    private MovieHttpMethods(){
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS) // 设置连接超时时间
                .build();

        mRetrofit = new Retrofit.Builder()
                .client(client) // 设置OkHttpClient
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        mMovieService = mRetrofit.create(MovieService.class);
    }

    private static class SingletonHolder{
        private static final MovieHttpMethods INSTANCE = new MovieHttpMethods();
    }

    public static MovieHttpMethods getInstance(){
        return SingletonHolder.INSTANCE;
    }

    public void getTopMoive(Subscriber<List<Subject>> subscriber, int start, int count){
        mMovieService.getTop250Movie(start, count)
                .compose(new LiftTransformer())
                .subscribe(subscriber);
    }
}
