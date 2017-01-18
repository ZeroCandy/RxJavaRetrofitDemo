package com.github.zerocandy.rxredemo.net;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;

/**
 * Created by 帅阳 on 2017/1/18.
 */

public class ProgressDialogHandler extends Handler {
    public static final int SHOW_DIALOG = 0x01;
    public static final int DISMISS_DIALOG = 0x02;

    private boolean mCancelable;

    private Context mContext;
    private OnCancelListener mListener;
    private ProgressDialog mProgressDialog;

    public ProgressDialogHandler(Context context, OnCancelListener listener, boolean cancelable) {
        super();
        this.mContext = context;
        this.mListener = listener;
        this.mCancelable = cancelable;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case SHOW_DIALOG:
                showDialog();
                break;
            case DISMISS_DIALOG:
                dismissDialog();
                break;
        }
    }

    private void showDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setCancelable(mCancelable);
            if (mCancelable && mListener != null) {
                mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        mListener.onCancel();
                    }
                });
            }
        }
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    private void dismissDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    public interface OnCancelListener {
        void onCancel();
    }
}
