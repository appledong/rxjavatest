package com.dong.rxjavatest.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.dong.rxjavatest.R;

import rx.Observable;
import rx.Subscriber;

/***
 * rxjava(响应式编程模式)
 * 响应式编程：rxjava 和接口回调都是响应式编程模式，接口回调是observable和observe之间依赖于接口，observable通过接口调用observe的实现。
 * rxjava是observable和observe通过subscribe串联起来，原理是一样的，rxjava强大地方是提供了很多的操作符和操作逻辑实现。
 */
public class CommonActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);
        /**
         * 1.subscribe api可以直接接受一个订阅者（subscribe）
         * 2.subscribe有三个重载action的api，单个next action，next，error action
         * next，error，compelete actin
         */
        myObservable.subscribe(subscriber);
        myObservable.subscribe(subscriber2);
        myObservable.subscribe(subscriber3);
    }

    /**
     * 1. 对于多个被观察者来说，观察者的执行顺序是串行的，执行完第一个逻辑（开始，下一步，结束）后在执行另一个逻辑
     * 2. 结束无论成功还是失败都执行一次，即使在观察者里面代码包含两个逻辑，也是优先执行一个（先执行的哪一个）。
     * 3. 对于被观察者来说，如果没有观察者（订阅者），是不会执行call（）的。不会发出任何事件
     */
    Observable<String> myObservable = Observable.create(new Observable.OnSubscribe<String>() {
        @Override
        public void call(Subscriber<? super String> subscriber) {
            int i = 0;
            while (i <= 5) {
                if (i == 0) {
                    subscriber.onStart();
                } else if (i <= 3) {
                    subscriber.onNext(i + "");
                } else if (i == 4) {
                    subscriber.onError(new Throwable("只是用来测试"));
                } else if (i == 5) {
                    subscriber.onCompleted();
                }
                i++;
            }
        }
    });

    Subscriber<String> subscriber3 = new Subscriber<String>() {

        @Override
        public void onStart() {
            super.onStart();
            Log.e("dongdianzhou", "3调用开始了");
        }

        @Override
        public void onCompleted() {
            Log.e("dongdianzhou", "3调用结束了");
        }

        @Override
        public void onError(Throwable e) {
            Log.e("dongdianzhou", "3调用出错了：" + e.getMessage());
        }

        @Override
        public void onNext(String s) {
            Log.e("dongdianzhou", s);
        }
    };

    Subscriber<String> subscriber2 = new Subscriber<String>() {

        @Override
        public void onStart() {
            super.onStart();
            Log.e("dongdianzhou", "2调用开始了");
        }

        @Override
        public void onCompleted() {
            Log.e("dongdianzhou", "2调用结束了");
        }

        @Override
        public void onError(Throwable e) {
            Log.e("dongdianzhou", "2调用出错了：" + e.getMessage());
        }

        @Override
        public void onNext(String s) {
            Log.e("dongdianzhou", "2调用到这一步了：" + s);
        }
    };

    Subscriber<String> subscriber = new Subscriber<String>() {
        @Override
        public void onStart() {
            super.onStart();
            Log.e("dongdianzhou", "调用开始了");
        }

        @Override
        public void onCompleted() {
            Log.e("dongdianzhou", "调用结束了");
        }

        @Override
        public void onError(Throwable e) {
            Log.e("dongdianzhou", "调用出错了：" + e.getMessage());
        }

        @Override
        public void onNext(String s) {
            Log.e("dongdianzhou", "调用到这一步了：" + s);
        }
    };
}
