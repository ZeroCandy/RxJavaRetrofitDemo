package com.github.zerocandy.rxredemo.net;

import com.github.zerocandy.rxredemo.bean.HttpResult;
import com.github.zerocandy.rxredemo.net.exception.EmptyResultException;

import rx.functions.Func1;

/**
 * 统一处理返回数据并对主数据进行分离
 * Created by 帅阳 on 2017/1/19.
 */

public class HttpResultFunc<T> implements Func1<HttpResult<T>, T>{
    @Override
    public T call(HttpResult<T> httpResult) {
        // 空数据则抛出异常，交由Subscriber的onError()方法进行统一处理
        if (httpResult.getStart() == httpResult.getTotal()) {
            throw new EmptyResultException("木有数据啦！");
        }
        // 分离出主数据
        return httpResult.getSubjects();
    }
}
