package com.dong.rxjavatest.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.dong.rxjavatest.R;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by dongdz on 2016/2/24.
 * Interval 操作符：在指定的时间间隔逐一发送整形值的observable。
 * Range 操作符：在指定的起始值和数据范围内逐一发送整型值的observable
 * timer 操作符：在指定的事件后单独发送一个0的observable，这个可以做定时器用
 * Repeat 操作符：创建一个重复发送初始Observable的数据的Observable
 * start 操作符：创建一个接受指定的数学函数，将计算后的数据发射的Observable
 * Repeat和Start的细节需要在研究。
 */
public class CalculateObservableOperationActivity extends AppCompatActivity {

    private Subscription intervalSubscriber1;
    private Subscription intervalSubscriber2;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(intervalSubscriber1 != null && !intervalSubscriber1.isUnsubscribed()){
            intervalSubscriber1.unsubscribe();
        }
        if(intervalSubscriber2 != null && !intervalSubscriber2.isUnsubscribed()){
            intervalSubscriber2.unsubscribe();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculateoperation);
        /**
         * Interval 操作符：
         *  在指定的时间间隔逐一发送整形值的observable。
         *  1. 可以指定时间间隔
         *  2. 可以指定初始化的时间间隔和后续的时间间隔
         *  3. 可以指定返回的observable的计算线程Scheduler
         *  缺陷：不能指定整型值的最大值(默认的是长整型long的最大值)
         *  不能指定整形值自增还是自减
         */
        intervalSubscriber1 = Observable.interval(1, TimeUnit.SECONDS).subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                Log.e("dongdianzhou1", "当前发射出来的数字是：" + aLong);
            }
        });
        intervalSubscriber2 = Observable.interval(2, 1, TimeUnit.SECONDS).subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        Log.e("dongdianzhou2", "当前发射出来的数字是：" + aLong);
                    }
        });

        /**
         * range 操作符：可以逐一发送(n,n+m-1)范围内整形值的observable
         * 1. 第二个参数设置为0，不发送任何事件
         * 2. 第二个参数设置为负数，抛出异常
         */
        Observable.range(10, 10).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.e("dongdianzhou3", "当前发射出来的数字是：" + integer);
            }
        });

        /**
         * timer 操作符：在指定的事件后单独发送一个0的observable，这个可以做定时器用
         */
        Observable.timer(1,TimeUnit.SECONDS).subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                Log.e("dongdianzhou4", "当前发射出来的数字是：" + aLong);
            }
        });
    }
}
