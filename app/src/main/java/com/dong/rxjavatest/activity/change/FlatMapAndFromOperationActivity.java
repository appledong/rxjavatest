package com.dong.rxjavatest.activity.change;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.dong.rxjavatest.R;
import com.dong.rxjavatest.bean.FlatmapFunc2Bean;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * Created by dongdz on 2016/2/23.
 * map 操作符 o 接受一个observable数据转换后返回另一个observable
 * flatmap 操作符 o
 * contactmap 操作符 x
 * switchmap 操作符 x
 * spit 操作符 x
 */
public class FlatMapAndFromOperationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flatandfrom);
//        ((TextView) findViewById(R.id.textview)).setText("RxJava from and flatmap操作符");
        /**
         * 一个rxjava的简单实例的使用：提供通过key获取一组数据并展示出来
         * 使用：observable创建并准备数据和处理subscribe的回调。
         * observable可以在任何获取到的地方提交subscribe处理。
         */
        getSerchUrls("").subscribe(new Subscriber<List<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<String> strings) {
                for (String url : strings) {
                    Log.e("dongdianzhou1", url + "?userid=\"\"");
                }
            }
        });
        /**
         * 利用map操作符对observable和subscribe中间流数据进行了处理
         * 如果单纯的处理onnext()事件，没有初始化subscribe，单纯的提交一个action即可。
         */
        getSerchUrls("").map(new Func1<List<String>, List<String>>() {
            @Override
            public List<String> call(List<String> strings) {
                List<String> list = new ArrayList<String>();
                for (String string : strings) {
                    string += "?userid=\"\"";
                    list.add(string);
                }
                return list;
            }
        }).subscribe(new Action1<List<String>>() {
            @Override
            public void call(List<String> strings) {
                for (String url : strings) {
                    Log.e("dongdianzhou2", url);
                }
            }
        });
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
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("测试map");
                subscriber.onCompleted();
            }
        }).map(new Func1<String, String>() {
            @Override
            public String call(String s) {
                return "3调用到这一步了" + s;
            }
        }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.e("dongdianzhou4", "打印出来的s：" + s);
            }
        });
        /**
         * observable提供的数据类型往往与subscribe需求的数据类型不一致
         * flatmap操作符：flatmap通过输入一个类型observable，输出另外一个数据类型observable形式解决上面的需求。
         */
        getSerchUrls("").map(new Func1<List<String>, List<String>>() {
            @Override
            public List<String> call(List<String> strings) {
                List<String> list = new ArrayList<String>();
                for (String string : strings) {
                    string += "?userid=\"\"";
                    list.add(string);
                }
                return list;
            }
        }).flatMap(new Func1<List<String>, Observable<String>>() {
            @Override
            public Observable<String> call(List<String> strings) {
                return Observable.from(strings);
            }
        }).subscribe(new Action1<String>() {
            @Override
            public void call(String url) {
                Log.e("dongdianzhou4", url);
            }
        });
        /**
         * 通过flatmap操作符的组合使用可以将串行的逻辑串起来简单明了的显示逻辑，比如以前的连续串行的调用三个接口
         * api在接口里实现，修改代码的时候很难找到逻辑口，但是使用rxjava处理以后逻辑很明了。
         * 下面的例子是在上面需求的基础上又添加了打印标题而不是url连接的需求。
         * ???串联的逻辑可不可以是异步的，逻辑是线程里面处理
         */
        getSerchUrls("").flatMap(new Func1<List<String>, Observable<String>>() {
            @Override
            public Observable<String> call(List<String> strings) {
                return Observable.from(strings);
            }
        }).flatMap(new Func1<String, Observable<String>>() {
            @Override
            public Observable<String> call(String s) {
                return gettitle(s);
            }
        }).subscribe(new Action1<String>() {
            @Override
            public void call(String url) {
                Log.e("dongdianzhou5", url);
            }
        });

        /**
         * flatMap(Func1,Func2))：flatmap的一个重载api，利用这一个api可以将observable转换前
         * 输入的数据和observable第一次转换后的数据类型复合（可以合在一起，也可以保留成为新的类型）
         * 将复合后的数据给subscribe或者下一个需要转换的observable。
         * flatMap(Func1,Func2,int i)):int参数？？？？？？？？？？？
         */
        getSerchUrls("").flatMap(new Func1<List<String>, Observable<String>>() {
            @Override
            public Observable<String> call(List<String> strings) {
                return Observable.from(strings);
            }
        }, new Func2<List<String>, String, FlatmapFunc2Bean>() {
            @Override
            public FlatmapFunc2Bean call(List<String> strings, String s) {
                FlatmapFunc2Bean flatmapFunc2Bean = new FlatmapFunc2Bean();
                flatmapFunc2Bean.currnt = s;
                flatmapFunc2Bean.list = strings;
                return flatmapFunc2Bean;
            }
        }).subscribe(new Action1<FlatmapFunc2Bean>() {
            @Override
            public void call(FlatmapFunc2Bean flatmapFunc2Bean) {
                if(flatmapFunc2Bean != null){
                    Observable.from(flatmapFunc2Bean.list).subscribe(new Action1<String>() {
                        @Override
                        public void call(String s) {
                            Log.e("dongdianzhou7",s);
                        }
                    });
                    Log.e("dongdianzhou7", "curent value：" + flatmapFunc2Bean.currnt);
                }
            }
        });

        getSerchUrls("").concatMap(new Func1<List<String>, Observable<String>>() {
            @Override
            public Observable<String> call(List<String> strings) {
                return Observable.from(strings);
            }
        }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.e("dongdianzhou8",s);
            }
        });

    }

    /**
     * 根据给的url换取title
     *
     * @param url
     * @return
     */
    private Observable<String> gettitle(final String url) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("百度" + url.substring(url.length() - 1, url.length()));
                subscriber.onCompleted();
            }
        });
    }

    /**
     * 根据给的key搜索获取到一批相应的urls列表
     *
     * @param key
     * @return
     */
    private Observable<List<String>> getSerchUrls(String key) {
//        Observable.defer(new Func0<Observable<String>>() {
//            @Override
//            public Observable<String> call() {
//                return null;
//            }
//        }).subscribe(new Action1<String>() {
//            @Override
//            public void call(String s) {
//
//            }
//        });
        return Observable.create(new Observable.OnSubscribe<List<String>>() {
            @Override
            public void call(Subscriber<? super List<String>> subscriber) {
                List<String> list = new ArrayList<String>();
                for (int i = 0; i < 10; i++) {
                    list.add("http://www.baidu.com" + i);
                }
                subscriber.onNext(list);
                subscriber.onCompleted();
            }
        });
    }
}
