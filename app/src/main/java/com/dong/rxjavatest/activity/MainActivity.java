package com.dong.rxjavatest.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.dong.rxjavatest.R;
import com.dong.rxjavatest.activity.change.BufferActivity;
import com.dong.rxjavatest.activity.change.FlatMapAndFromOperationActivity;
import com.dong.rxjavatest.activity.change.GroupByActivity;
import com.dong.rxjavatest.activity.create.CalculateObservableOperationActivity;
import com.dong.rxjavatest.activity.create.LogicObservableoperationActivity;
import com.dong.rxjavatest.activity.thread.SchedulerActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.textview_common, R.id.textview_shcedule, R.id.textview_logic
            , R.id.textview_calculate, R.id.textview_fromandflatmap, R.id.textview_buffer,
            R.id.textview_groupby,})
    public void onTextviewClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.textview_common:
                intent.setClass(MainActivity.this,CommonActivity.class);
                break;
            case R.id.textview_shcedule:
                intent.setClass(MainActivity.this,SchedulerActivity.class);
                break;
            case R.id.textview_logic:
                intent.setClass(MainActivity.this,LogicObservableoperationActivity.class);
                break;
            case R.id.textview_calculate:
                intent.setClass(MainActivity.this,CalculateObservableOperationActivity.class);
                break;
            case R.id.textview_fromandflatmap:
                intent.setClass(MainActivity.this,FlatMapAndFromOperationActivity.class);
                break;
            case R.id.textview_buffer:
                intent.setClass(MainActivity.this,BufferActivity.class);
                break;
            case R.id.textview_groupby:
                intent.setClass(MainActivity.this,GroupByActivity.class);
                break;
        }
        startActivity(intent);
    }
}
