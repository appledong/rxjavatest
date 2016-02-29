package com.dong.rxjavatest.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.dong.rxjavatest.R;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.observables.GroupedObservable;

/**
 * Created by dongdz on 2016/2/29.
 * GroupBy操作符：将原始Observable分散成指定key的observable
 * 同一个key的数据生成一个item由生成的observable发射给subscribe
 * 具体的使用还有待参考学习？？？？？？？
 * scan 操作符：可以指定函数用于原始observable的计算。
 */
public class GroupByActivity extends AppCompatActivity {

    private List<String> keys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupby);
//        keys = new ArrayList<>();
//        for (int i = 0; i < 5; i++) {
//            keys.add(i + "");
//        }
//        GroupedObservable groupedObservable = getObservable();
//        final List<String> allkeys = (List<String>) groupedObservable.getKey();
//        groupedObservable.groupBy(new Func1<String, String>() {
//            @Override
//            public String call(String s) {
//                for (String s2:allkeys) {
//
//                }
//                return null;
//            }
//        })
        /**
         * 注释：
         *   1.call(Integer item1, Integer item2)：item1是当前的总值，item2是原始observable当前发送的值。
         *   2.响应到subscribe的值第一次是原始observable相应第一个值，其后就是函数处理后的值了
         */
        Observable.just(1, 2, 3, 4, 5, 6, 7, 8)
                .scan(new Func2<Integer, Integer, Integer>() {
                    @Override
                    public Integer call(Integer item1, Integer item2) {
                        Log.e("dongdianzhou1", "GroupByActivity:item1：" + item1 + " item2: " + item2);
                        return item1 + item2;
                    }
                }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.e("dongdianzhou1", "GroupByActivity:最后数是多少：" + integer);
            }
        });
        /**
         * 注释：
         *  和上面的一样。
         *  指定了第一个值，确切的说是添加了一个值，而不是替换了第一个值
         */
        Observable.just(1, 2, 3, 4, 5, 6, 7, 8)
                .scan(7, new Func2<Integer, Integer, Integer>() {
                    @Override
                    public Integer call(Integer item1, Integer item2) {
                        Log.e("dongdianzhou2", "GroupByActivity:item1：" + item1 + " item2: " + item2);
                        return item1 + item2;
                    }
                }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.e("dongdianzhou2", "GroupByActivity:最后数是多少：" + integer);
            }
        });

    }

    private GroupedObservable getObservable() {
//        return Observable.create(new Observable.OnSubscribe<String>() {
//            @Override
//            public void call(Subscriber<? super String> subscriber) {
//
//            }
//        });
        return GroupedObservable.create(keys, new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                for (int i = 0; i < 19; i++) {
                    switch (i % 5) {
                        case 0:
                            subscriber.onNext(i + "0");
                            break;
                        case 1:
                            subscriber.onNext(i + "1");
                            break;
                        case 2:
                            subscriber.onNext(i + "2");
                            break;
                        case 3:
                            subscriber.onNext(i + "3");
                            break;
                        case 4:
                            subscriber.onNext(i + "4");
                            break;

                    }
                }
                subscriber.onCompleted();
            }
        });
    }
}
