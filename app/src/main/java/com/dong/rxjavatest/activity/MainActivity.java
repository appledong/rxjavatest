package com.dong.rxjavatest.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.dong.rxjavatest.R;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;

/***
 * rxjava(响应式编程模式)
 * 响应式编程：rxjava 和接口回调都是响应式编程模式，接口回调是observable和observe之间依赖于接口，observable通过接口调用observe的实现。
 * rxjava是observable和observe通过subscribe串联起来，原理是一样的，rxjava强大地方是提供了很多的操作符和操作逻辑实现。
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((TextView) findViewById(R.id.textview_fromandflatmap)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, FlatMapAndFromOperationActivity.class));
            }
        });
        ((TextView) findViewById(R.id.textview_logic)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LogicObservableoperationActivity.class));
            }
        });
        ((TextView) findViewById(R.id.textview_calculate)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CalculateObservableOperationActivity.class));
            }
        });
        ((TextView) findViewById(R.id.textview_shcedule)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SchedulerActivity.class));
            }
        });
        ((TextView) findViewById(R.id.textview_buffer)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BufferActivity.class));
            }
        });
        ((TextView) findViewById(R.id.textview_groupby)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GroupByActivity.class));
            }
        });
        /**
         * 1.subscribe api可以直接接受一个订阅者（subscribe）
         * 2.subscribe有三个重载action的api，单个next action，next，error action
         * next，error，compelete actin
         */
        myObservable.subscribe(subscriber);
        myObservable.subscribe(subscriber2);
//        myObservable.subscribe(subscriber3);
        /**
         *通过操作符简化observable和subscriber
         * 1. just操作符简化了observable的逻辑，自动生成一个observable。
         * 2. just操作符根据传入的对象响应与其相应的onnext()事件just操作符最少接受一个对象最多接受10个对象
         * 3. 自动生成的observable不会有逻辑处理
         * 4. just操作符生成的observable是一个完整的操作，包含onstart()和oncompelete()事件响应
         */
        Observable.just("测试是哪一个操作", "第二个String参数")
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.e("dongdianzhou", s);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e("dongdianzhou", "出错了");
                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        Log.e("dongdianzhou", "结束了");
                    }
                });
        Observable.just("测试完整的订阅者", "第二个2String参数")
                .subscribe(subscriber2);
        /**
         * 调用just后重新生成了observable，和myObservable已经没有了关系
         * myObservable.just("测试完整的订阅者", "第二个2String参数").subscribe(subscriber2);
         */

        /**
         * map操作符的使用
         * 对于subscribe来说，observable返回的数据不一定就是自己最终需要的数据，
         * 此时修改被观察者不好不一定有一个观察者（在observable里面逻辑控制会导致逻辑很乱），
         * 在观察者里面修改也不好观察者的逻辑越轻量越好（代码逻辑有可能很复杂）。
         * map操作符实现了被观察者和观察者之间修改数据最终给观察者的实现。
         * func1：单个参数的功能接口，两个泛型参数，第一个是observable处理后的结果，第二个泛型是当前map处理后返回的数据类型。
         * observable响应onnext()事件后，响应的数据对象会先经过map的功能函数处理（可以多次处理），将处理后的数据给最终的观察者
         * 缺陷：map操作符在操作的过程中可以修改参数的类型，但是最终数据类型还是由subscribe决定的。
         */
        myObservable.map(new Func1<String, String>() {
            @Override
            public String call(String s) {
                return "3调用到这一步了" + s;
            }
        }).subscribe(subscriber3);
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
