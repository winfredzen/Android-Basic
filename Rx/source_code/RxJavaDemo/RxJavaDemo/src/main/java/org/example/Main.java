package org.example;

import io.reactivex.Notification;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class Main {
    public static void main(String[] args) {





//        Observable.just("Hello")
//                .doOnNext(new Consumer<String>() {
//                    @Override
//                    public void accept(String s) throws Exception {
//                        System.out.println("doOnNext: " + s);
//                    }
//                }).doAfterNext(new Consumer<String>() {
//                    @Override
//                    public void accept(String s) throws Exception {
//                        System.out.println("doAfterNext: " + s);
//                    }
//                }).doOnComplete(new Action() {
//                    @Override
//                    public void run() throws Exception {
//                        System.out.println("doOnComplete");
//                    }
//                })
//                .doOnEach(new Consumer<Notification<String>>() {
//                    @Override
//                    public void accept(Notification<String> stringNotification) throws Exception {
//                        System.out.println("doOnEach: " + (stringNotification.isOnNext() ? "onNext" : (stringNotification.isOnComplete() ? "onComplete" : "onError")));
//                    }
//                })
//                .subscribe(new Consumer<String>() {
//                    @Override
//                    public void accept(String s) throws Exception {
//                        System.out.println("收到消息: " + s);
//                    }
//                });



//        Observable.just("Hello World").subscribe(new Observer<String>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//                System.out.println("onSubscribe");
//            }
//
//            @Override
//            public void onNext(String s) {
//                System.out.println("onNext s = " + s);
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                System.out.println("onError");
//            }
//
//            @Override
//            public void onComplete() {
//                System.out.println("onComplete");
//            }
//        });


//        Observable.just("Hello word").subscribe(new Consumer<String>() {
//            @Override
//            public void accept(String s) throws Exception {
//
//            }
//        }, new Consumer<Throwable>() {
//            @Override
//            public void accept(Throwable throwable) throws Exception {
//
//            }
//        }, new Action() {
//            @Override
//            public void run() throws Exception {
//
//            }
//        });
    }
}