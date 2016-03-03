package com.dong.rxjavatest.activity.thread;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.dong.rxjavatest.R;
import com.dong.rxjavatest.bean.Cat;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
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

    private int count = 60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduleoperation);
        final TextView textview_scheduler = (TextView) findViewById(R.id.textview_scheduler);
        /***
         * 特别注意：
         * observeOn()和SuscribOn()两个决定的逻辑和其api名字没有任何鸟关系
         * observeOn()决定了subscribe的逻辑线程
         * subscribeOn决定了observable的逻辑线程，两个反了个
         */
        Observable observable1 = Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {
                Log.e("dongdianzhou1", "SchedulerActivity:" + "当前的observable线程是：" + Thread.currentThread());
                int count = 0;
                for (int i = 0; i < 10000; i++) {
//                    Log.e("dongdianzhou","当前的i是：" + i);
                    count++;
                }
                return Observable.just(count + "");
            }
        }).observeOn(AndroidSchedulers.mainThread())//subscribe逻辑执行的线程
                .subscribeOn(Schedulers.computation());//observable逻辑执行的线程
        Log.e("dongdianzhou1", "SchedulerActivity:" + "当前的UI线程是：" + Thread.currentThread());
        observable1.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.e("dongdianzhou1", "SchedulerActivity:" + "当前的subscribe线程是：" + Thread.currentThread() + " S:" + s);
                textview_scheduler.setText("数据处理后的结果是：" + s);
            }
        });
        /**
         * rxjava提供了worker的概念，可以利用此特性很方便的把自己的逻辑放到不同的线程里去
         */
        final TextView textview_worker = (TextView) findViewById(R.id.textview_worker);
        final Scheduler.Worker worker = AndroidSchedulers.mainThread().createWorker();
        worker.schedulePeriodically(new Action0() {
            @Override
            public void call() {
                if (!worker.isUnsubscribed()) {
                    if (isFinishing()) {
                        worker.unsubscribe();
                    } else {
                        textview_worker.setText(count + "s");
                        count--;
                        if (count < 0) {
                            count = 60;
                        }
                    }
                }
            }
        }, 0, 1, TimeUnit.SECONDS);
        /**
         * 结合文章写的一组伪代码优化逻辑代码
         * 文章连接：http://www.devtf.cn/?p=323
         */
//        Observable.create(new Observable.OnSubscribe<List<Cat>>() {
//            @Override
//            public void call(Subscriber<? super List<Cat>> subscriber) {
////                getAllCatsFromNet();
//            }
//        }).subscribeOn(Schedulers.io())
//                .map(new Func1<List<Cat>, Cat>() {
//                    @Override
//                    public Cat call(List<Cat> cats) {
//                        Cat cat = null;
////                        cat = findMoreNiceCat(cats);
//                        return cat;
//                    }
//                }).subscribeOn(Schedulers.computation())
//                .observeOn(Schedulers.io())
//                .subscribe(new Action1<Cat>() {
//                    @Override
//                    public void call(Cat cat) {
////                        saveToLocal(cat);
//                    }
//                }, new Action1<Throwable>() {
//                    @Override
//                    public void call(Throwable throwable) {
//                        //？？？？？
//                    }
//                });
    }
}
