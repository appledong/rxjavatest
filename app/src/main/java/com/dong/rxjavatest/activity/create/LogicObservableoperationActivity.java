package com.dong.rxjavatest.activity.create;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.dong.rxjavatest.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;

/**
 * Created by dongdz on 2016/2/24.
 * create操作符 创建一个observable，call方法中传入了一个subscribe，手动的处理observable的onnext等三个响应
 * defer 操作符 创建一个observable，在有subscribe订阅的时候才会返回一个响应的observable。
 * from 操作符 o 将一组数据拆分并逐一发送observable
 * 使用rxjava实现延迟订阅：http://www.jianshu.com/p/c83996149f5b
 * empty 操作符：生成一个不发射任何事件但是可以正常终止的observable
 * never 操作符：生成一个什么都不处理的observable（结束和错误都不处理）
 * error 操作符：生成一个不发射任何事件但是响应失败的observable
 * 以上三个操作符只是提供辅助逻辑的开发，变态操作符
 * just 操作符：原样发送自己接受的数据生成的observable
 */
public class LogicObservableoperationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logicoperation);
        /**
         * create操作符，手动生成一个observable，手动的处理onnext()和oncompelete()事件的发射
         * create()调用后observable立刻生成observable，在接收到subscribe后执行call方法体发射相应的事件。
         */
        final Observable create = Observable.create(new Observable.OnSubscribe<List<String>>() {
            @Override
            public void call(Subscriber<? super List<String>> subscriber) {
                Log.e("dongdainzhou", "call 逻辑开始执行了");
                List<String> list = new ArrayList<String>();
                for (int i = 0; i < 10; i++) {
                    list.add("http://www.baidu.com" + i);
                }
                subscriber.onNext(list);
                subscriber.onCompleted();
            }
        });
        Log.e("dongdianzhou1", "LogicObservableoperationActivity: create:" + create);
        create.flatMap(new Func1<List<String>, Observable<String>>() {
            @Override
            public Observable<String> call(List<String> strings) {
                return Observable.from(strings);
            }
        }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.e("dongdianzhou1", "LogicObservableoperationActivity: s:" + s);
            }
        });
        /**
         * defer操作符不会立刻生成observable，只有subscribe后才会生成相应的observable
         * 针对不同的subscribe生成不同对象的observable
         * defer操作符会先生成一个observable去处理后续的逻辑，在接受到subscribe后才会生成最初的处理逻辑observable
         */
        Observable defer = Observable.defer(new Func0<Observable<List<String>>>() {
            @Override
            public Observable<List<String>> call() {
                List<String> list = new ArrayList<String>();
                for (int i = 0; i < 10; i++) {
                    list.add("http://www.baidu.com" + i);
                }
                Observable defer2 = Observable.just(list);
                /**
                 * 针对不同的subscribe，此处打印的defer2类别是不同的说明对于每一个subscribe都生成一个对应的observable。
                 */
                Log.e("dongdianzhou2", "LogicObservableoperationActivity:defer2:" + defer2);
                return defer2;
            }
        });
        Log.e("dongdianzhou2", "LogicObservableoperationActivity: defer:" + defer);
        defer.flatMap(new Func1<List<String>, Observable<String>>() {
            @Override
            public Observable<String> call(List<String> strings) {
                return Observable.from(strings);
            }
        }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.e("dongdianzhou2", "LogicObservableoperationActivity: s1:" + s);
            }
        });
        defer.subscribe(new Action1<List<String>>() {
            @Override
            public void call(List<String> strings) {
                for (String s : strings) {
                    Log.e("dongdianzhou2", "LogicObservableoperationActivity: s2:" + s);
                }
            }
        });

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
                        Log.e("dongdianzhou3", s);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e("dongdianzhou3", "出错了");
                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        Log.e("dongdianzhou3", "结束了");
                    }
                });
        /**
         * 调用just后重新生成了observable，和myObservable已经没有了关系
         * myObservable.just("测试完整的订阅者", "第二个2String参数").subscribe(subscriber2);
         */
        Observable.just("测试完整的订阅者", "第二个2String参数")
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.e("dongdianzhou4", "just打印出来的s：" + s);
                    }
                });

        /**
         * from()操作符有五个重载的api，当前接触到的最简单的2种一个是输入一个对象列表输出一个单独的对象
         * 另外屏蔽的一个是输入一个对象数组，输出一个单独的对象。
         * from()针对的对象是list，String[],没有特殊的限制。
         * from(Future):
         * from(Future,timeout,timeunit):
         * from(Future,timeout,timeunit,Scheduler):
         * Future 是Java线程中的一个utils类和rxjava关系不大，了解上面三个需要先了解Java并发编程中的Future
         * 参考文档：http://www.oschina.net/question/54100_83333
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
//                Observable.from(T[],arrays);
                Observable.from(strings).subscribe(new Action1<String>() {
                    @Override
                    public void call(String url) {
                        Log.e("dongdianzhou3", url);
                    }
                });
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
        Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {
                return null;
            }
        }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {

            }
        });
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
