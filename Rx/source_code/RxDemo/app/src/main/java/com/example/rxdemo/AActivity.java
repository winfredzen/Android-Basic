package com.example.rxdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.FlowableEmitter;
import io.reactivex.rxjava3.core.FlowableOnSubscribe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity_TAG";
    private Disposable mDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button01).setOnClickListener(this);
        findViewById(R.id.button02).setOnClickListener(this);
        findViewById(R.id.button03).setOnClickListener(this);
        findViewById(R.id.button04).setOnClickListener(this);

        Log.d("AActivity", "onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("AActivity", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("AActivity", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("AActivity", "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("AActivity", "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("AActivity", "onDestroy");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button01:
                onBasicUseClick();
                break;
            case R.id.button02:
                onButtonClick02();
                break;
            case R.id.button03:
                onFlowableClick();
                break;
            case R.id.button04:
                startActivity(new Intent(AActivity.this, BActivity.class));
                break;
            default:
                break;
        }
    }


    public void onFlowableClick() {
        //背压测试
/*        Observable.create(new ObservableOnSubscribe<Integer>() {

            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> emitter) throws Throwable {
                int i = 0;
                while (i < Long.MAX_VALUE) {
                    emitter.onNext(i);
                    i++;
                }
            }
        }).subscribeOn(Schedulers.newThread())
        .observeOn(Schedulers.newThread())
        .subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Throwable {
                Thread.sleep(1000);
                Log.e(TAG,"i = " + integer);
            }
        });*/

        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<Integer> emitter) throws Throwable {
                int i = 0;
                while(i < Long.MAX_VALUE){
                    emitter.onNext(i);
                    i++;
                }
            }
        }, BackpressureStrategy.DROP)
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.newThread())
        .subscribe(new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(Integer integer) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.e(TAG,"i = "+integer);
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

            }
        });

        
    }

    public void onBasicUseClick() {
        //被观察者
        Observable observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                Log.e(TAG, "subscribe 线程：" + Thread.currentThread().getName());
                emitter.onNext("连载1");
                emitter.onNext("连载2");
                emitter.onNext("连载3");
                emitter.onComplete();
            }
        });

        //观察者
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                mDisposable = d;
                Log.d(TAG, "onSubscribe");
            }

            @Override
            public void onNext(String value) {
                if ("2".equals(value)){
                    mDisposable.dispose();
                    return;
                }
                Log.d(TAG, "onNext:"+value);
                Log.e(TAG, "onNext 线程：" + Thread.currentThread().getName());
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError:"+e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete:");
            }
        };

        //建立订阅关系
        observable.subscribe(observer);
    }

    public void onButtonClick02() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Throwable {
                Log.e(TAG, "subscribe 线程：" + Thread.currentThread().getName());
                emitter.onNext("连载1");
                emitter.onNext("连载2");
                emitter.onNext("连载3");
                emitter.onComplete();
                Log.e(TAG, "subscribe 线程：" + Thread.currentThread().getName());
            }
        }).observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.e(TAG,"onSubscribe");
            }

            @Override
            public void onNext(@NonNull String s) {
                Log.e(TAG,"onNext:"+s);
                Log.e(TAG, "onNext 线程：" + Thread.currentThread().getName());
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e(TAG,"onError="+e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.e(TAG,"onComplete()");
            }
        });
    }
}