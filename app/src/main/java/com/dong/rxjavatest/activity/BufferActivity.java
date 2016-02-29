package com.dong.rxjavatest.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.dong.rxjavatest.R;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

/**
 * Created by dongdz on 2016/2/29.
 * buffer:缓冲器的作用，将原始observable响应的数据缓存并按照buffer提供的条件进行后续的响应，针对每一组api都提供了线程指定
 *???:自定义buffer的发射observable还处理不了？？？需要看别人代码或者再处理
 */
public class BufferActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buffer);
        getObservable()
                /**
                 * 缓冲器，将Observable发射出来的数据缓存起来最大缓存数量为3，最后一个发射的数据数量可能小于3
                 */
                .buffer(3)
                .subscribe(new Action1<List<String>>() {
                    @Override
                    public void call(List<String> strings) {
                        Log.e("dongdianzhou", "当前的数字是：" + strings);
                    }
                });
        /**
         * buffer(count,skip):参考原理图图片
         */
        getObservable().buffer(3, 2).subscribe(new Action1<List<String>>() {
            @Override
            public void call(List<String> strings) {
                Log.e("dongdianzhou2", "(3,2)对应当前的数字是：" + strings);
            }
        });
        getObservable().buffer(3, 3).subscribe(new Action1<List<String>>() {
            @Override
            public void call(List<String> strings) {
                Log.e("dongdianzhou2", "(3,3)对应当前的数字是：" + strings);
            }
        });
        getObservable().buffer(3, 8).subscribe(new Action1<List<String>>() {
            @Override
            public void call(List<String> strings) {
                Log.e("dongdianzhou2", "(3,8)对应当前的数字是：" + strings);
            }
        });
        /**
         * buffer(time,unit):上面是指定的count，当前是指定的时间，每次发出的list都是以
         * 指定时间为单位，即每次发出指定时间内的数据，当前测试发送数据量太少，可以修改为
         * 10000，很明显的显示出来
         */
        getObservable().buffer(2, TimeUnit.MICROSECONDS).subscribe(new Action1<List<String>>() {
            @Override
            public void call(List<String> strings) {
                Log.e("dongdianzhou3", "对应当前的数字是：" + strings);
            }
        });
        /**
         * buffer(time,unit,count):这个是同时指定了时间和count大小，observable在满足任何一个条件的时候
         * 就会发射list。
         */
        getObservable().buffer(2, TimeUnit.MICROSECONDS, 3).subscribe(new Action1<List<String>>() {
            @Override
            public void call(List<String> strings) {
                Log.e("dongdianzhou4", "对应当前的数字是：" + strings);
            }
        });
        /**
         * buffer(counttime,delaytime,count)：指定两个时间，统计时间和延迟时间，counttime如上，delaytime
         * 是发射一个buffer后，延迟多少时间在创建buffer并统计发射新的list数据
         */
        getObservable().buffer(2, 3, TimeUnit.MILLISECONDS).subscribe(new Action1<List<String>>() {
            @Override
            public void call(List<String> strings) {
                Log.e("dongdianzhou5", "对应当前的数字是：" + strings);
            }
        });
//        Observable.create(new Observable.OnSubscribe<Object>() {
//            @Override
//            public void call(Subscriber<? super Object> subscriber) {
//
//            }
//        })
//        getObservable().buffer(Observable.create(new Observable.OnSubscribe<String>() {
//
//            @Override
//            public void call(Subscriber<? super String> subscriber) {
//
//            }
//        }))
//        getObservable().buffer(new Func0<Observable>() {
//            @Override
//            public Observable call() {
//                return null;
//            }
//        })
    }

    private Observable getObservable() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                for (int i = 0; i < 19; i++) {
                    subscriber.onNext(i + "");
                }
                subscriber.onCompleted();
            }
        });
    }
}
