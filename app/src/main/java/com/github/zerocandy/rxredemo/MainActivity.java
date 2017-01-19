package com.github.zerocandy.rxredemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.zerocandy.rxredemo.bean.Subject;
import com.github.zerocandy.rxredemo.net.MovieHttpMethods;
import com.github.zerocandy.rxredemo.net.subscriber.ProgressSubscriber;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity implements ProgressSubscriber.OnNextListener<List<Subject>> {
    private static final String TAG = MainActivity.class.getName();

    Unbinder mUnbinder;

    @BindView(R.id.tv_result)
    TextView mResultTv;

    private ProgressSubscriber<List<Subject>> mProgressSubscriber;

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

        mProgressSubscriber = new ProgressSubscriber<>(MainActivity.this, MainActivity.this);
        MovieHttpMethods.getInstance().getTopMoive(mProgressSubscriber, 0, 10);
    }

    @Override
    public void onNext(List<Subject> subjects) {
        mResultTv.setText(subjects.size() + "");
    }

    private void cancel() {
        Toast.makeText(this, "这个取消并没有什么卵用哈哈哈哈", Toast.LENGTH_SHORT).show();
    }


}
