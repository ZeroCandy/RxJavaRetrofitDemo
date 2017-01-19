package com.github.zerocandy.rxredemo.net.subscriber;

import android.content.Context;
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
    }

    @Override
    public void onError(Throwable e) {
        dismissProgressDialog();
        Toast.makeText(mContext, "Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNext(T t) {
        mListener.onNext(t);
    }

    @Override
    public void onStart() {
        showProgressDialog();
    }

    @Override
    public void onCancel() {
        if(!this.isUnsubscribed()){
            this.unsubscribe();
            Toast.makeText(mContext, "取消", Toast.LENGTH_SHORT).show();
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
