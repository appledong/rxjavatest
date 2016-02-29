package com.dong.rxjavatest.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.dong.rxjavatest.R;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by dongdz on 2016/2/25.
 * Scheduler的用法：
 * 1. 提供一系列的线程模式，供observable和subscribe调用在不同线程里面使用
 * 2. 提供了Worker类，可以方便的让用户在worker的call方法里面执行自己的逻辑
 * 3. worker提供了schedule(someAction, 500, TimeUnit.MILLISECONDS);可以起到定时器，延时器的作用。
 * ????: 具体的操作符结合着线程执行的时候问题暂不理解，需要了解rxjava的各个操作符的原理之后才能够解释
 */
public class SchedulerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduleoperation);
        final TextView textview_scheduler = (TextView) findViewById(R.id.textview_scheduler);
        Observable observable1 = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
//                Log.e("dongdianzhou1", "SchedulerActivity:" + "开始执行observablecall命令了" + System.currentTimeMillis());
                int count = 0;
                for (int i = 0; i < 10000; i++) {
//                    Log.e("dongdianzhou","当前的i是：" + i);
                    count++;
                }
                subscriber.onNext(count + "");
                subscriber.onCompleted();
            }
        })
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread());
//        Log.e("dongdianzhou1", "SchedulerActivity:" + "开始提交一个subscribe了" + System.currentTimeMillis());
        observable1.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.e("dongdianzhou1", "SchedulerActivity:" + "开始执行Subscribecall命令了" + System.currentTimeMillis());
                textview_scheduler.setText("数据处理后的结果是：" + s);
            }
        });
    }
}
