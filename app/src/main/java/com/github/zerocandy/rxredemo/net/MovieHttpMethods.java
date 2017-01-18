package com.github.zerocandy.rxredemo.net;

import com.github.zerocandy.rxredemo.bean.HttpResult;
import com.github.zerocandy.rxredemo.bean.Subject;
import com.github.zerocandy.rxredemo.net.service.MovieService;
import com.github.zerocandy.rxredemo.net.exception.EmptyResultException;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by 帅阳 on 2017/1/17.
 */

public class MovieHttpMethods {
    public static final String BASE_URL = "https://api.douban.com/v2/movie/";

    private static final int DEFAULT_TIMEOUT = 5;// 默认超时时间为5秒
    private Retrofit mRetrofit;
    private MovieService mMovieService;

    private MovieHttpMethods(){
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);// 设置连接超时时间

        mRetrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())                          // 设置OkHttpClient
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
                .map(new HttpResultFunc<List<Subject>>())// 转换返回结果数据
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io()) // 指定在IO线程执行取消订阅的逻辑
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 统一处理返回数据并对主数据进行分离
     * @param <T>
     */
    private class HttpResultFunc<T> implements Func1<HttpResult<T>, T>{

        @Override
        public T call(HttpResult<T> httpResult) {
            // 空数据则抛出异常，交由Subscriber的onError()方法进行统一处理
            if(httpResult.getStart() == httpResult.getTotal()) {
                throw new EmptyResultException("木有数据啦！");
            }
            // 分离出主数据
            return httpResult.getSubjects();
        }
    }
}
