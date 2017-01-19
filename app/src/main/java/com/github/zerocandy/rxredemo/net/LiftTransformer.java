package com.github.zerocandy.rxredemo.net;

import com.github.zerocandy.rxredemo.bean.HttpResult;
import com.github.zerocandy.rxredemo.bean.Subject;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Actions;
import rx.schedulers.Schedulers;

/**
 * 对Observable统一的变换
 * Created by 帅阳 on 2017/1/19.
 */

public class LiftTransformer implements Observable.Transformer<HttpResult<List<Subject>>, List<Subject>> {
    @Override
    public Observable<List<Subject>> call(Observable<HttpResult<List<Subject>>> httpResultObservable) {
        return httpResultObservable.map(new HttpResultFunc<List<Subject>>())// 转换返回结果数据
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io()) // 指定在IO线程执行取消订阅的逻辑
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(Actions.empty())
                .subscribeOn(AndroidSchedulers.mainThread());
//                .onErrorReturn(new Func1<Throwable, List<Subject>>() { // 发生错误时则返回空集合且不调用onError()方法
//                    @Override
//                    public List<Subject> call(Throwable throwable) {
//                        return Collections.emptyList();
//                    }
//                });

    }
}
