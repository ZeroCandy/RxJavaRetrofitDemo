package com.github.zerocandy.rxredemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.zerocandy.rxredemo.bean.Subject;
import com.github.zerocandy.rxredemo.net.MovieHttpMethods;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Subscriber;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();

    Unbinder mUnbinder;

    @BindView(R.id.tv_result)
    TextView mResultTv;
    private Subscriber<List<Subject>> mSubscriber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUnbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    @OnClick({R.id.btn, R.id.btn_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn:
                getMovie();
                break;
            case R.id.btn_cancel:
                cancel();
                break;
        }
    }

    private void getMovie() {
        mResultTv.setText("");

        mSubscriber = new Subscriber<List<Subject>>() {
            @Override
            public void onCompleted() {
                Toast.makeText(MainActivity.this, "Completed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {
                mResultTv.setText(e.getMessage());
            }

            @Override
            public void onNext(List<Subject> subjects) {
                mResultTv.setText(subjects.size() + "");
            }
        };
        MovieHttpMethods.getInstance().getTopMoive(mSubscriber, 250, 10);
    }

    private void cancel() {
        Log.i(TAG,"蹭蹭");
        if(mSubscriber != null && mSubscriber.isUnsubscribed()){
            Log.i(TAG,"进来了");
            // 因为网络请求的操作不能在主线程中，所以取消订阅的操作必须在IO进程中进行
            mSubscriber.unsubscribe();
            mResultTv.setText("你已经狠心的取消了！！！");
        }
    }
}
