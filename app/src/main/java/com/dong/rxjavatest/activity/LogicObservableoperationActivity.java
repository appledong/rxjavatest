package com.dong.rxjavatest.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.dong.rxjavatest.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;

/**
 * Created by dongdz on 2016/2/24.
 * create操作符
 * defer 操作符
 * 使用rxjava实现延迟订阅：http://www.jianshu.com/p/c83996149f5b
 * empty 操作符：生成一个不发射任何事件但是可以正常终止的observable
 * never 操作符：生成一个什么都不处理的observable（结束和错误都不处理）
 * error 操作符：生成一个不发射任何事件但是响应失败的observable
 * 以上三个操作符只是提供辅助逻辑的开发，变态操作符
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
                Log.e("dongdainzhou","call 逻辑开始执行了");
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
    }
}
