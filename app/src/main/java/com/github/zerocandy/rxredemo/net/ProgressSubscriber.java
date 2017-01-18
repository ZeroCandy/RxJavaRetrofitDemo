package com.github.zerocandy.rxredemo.net;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import rx.Subscriber;

/**
 * Created by 帅阳 on 2017/1/18.
 */

public class ProgressSubscriber<T> extends Subscriber<T> implements ProgressDialogHandler.OnCancelListener {
    private static final String TAG = ProgressSubscriber.class.getName();

    private Context mContext;
    private OnNextListener<T> mListener;
    private ProgressDialogHandler mProgressDialogHandler;

    public ProgressSubscriber(Context context, OnNextListener<T> listener) {
        this.mContext = context;
        this.mListener = listener;
        mProgressDialogHandler = new ProgressDialogHandler(context, this, true);
    }

    @Override
    public void onCompleted() {
        dismissProgressDialog();
        Toast.makeText(mContext, "Completed", Toast.LENGTH_SHORT).show();
        Log.i(TAG,"completed线程" + Thread.currentThread().getName());
    }

    @Override
    public void onError(Throwable e) {
        dismissProgressDialog();
        Toast.makeText(mContext, "Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        Log.i(TAG,"error线程" + Thread.currentThread().getName());
    }

    @Override
    public void onNext(T t) {
        mListener.onNext(t);
        Log.i(TAG,"next线程" + Thread.currentThread().getName());
    }

    @Override
    public void onStart() {
        showProgressDialog();
        Log.i(TAG,"start线程" + Thread.currentThread().getName());
    }

    @Override
    public void onCancel() {
        if(!this.isUnsubscribed()){
            this.unsubscribe();
        }
    }

    private void showProgressDialog(){
        if(mProgressDialogHandler != null){
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.SHOW_DIALOG).sendToTarget();
        }
    }

    private void dismissProgressDialog(){
        if(mProgressDialogHandler != null){
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.DISMISS_DIALOG).sendToTarget();
            mProgressDialogHandler = null;
        }
    }

    public interface OnNextListener<T>{
        void onNext(T t);
    }
}
