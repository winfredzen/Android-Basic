package org.example;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.schedulers.Schedulers;
import org.reactivestreams.Subscription;

import java.util.concurrent.TimeUnit;

public class ShareTest {
    public static void main(String[] args) throws InterruptedException {

        Consumer<Long> subscriber1 = new Consumer<Long>() {
            @Override
            public void accept(Long aLong) {
                System.out.println ("subscriber1 :" + aLong);
            }
        };

        Consumer<Long> subscriber2 = new Consumer<Long>() {
            @Override
            public void accept(Long aLong) {
                System.out.println ("   subscriber2 :" + aLong);
            }
        };

        Consumer<Long> subscriber3 = new Consumer<Long>() {
            @Override
            public void accept(Long aLong) {
                System.out.println ("       subscriber3 :" + aLong);
            }
        };

        ConnectableObservable<Long> connectableObservable = Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(ObservableEmitter<Long> e) throws Exception {
                Observable.interval(10, TimeUnit.MILLISECONDS, Schedulers.computation())
                        .take(Integer.MAX_VALUE)
                        .subscribe(e::onNext);
            }
        }).observeOn(Schedulers.newThread()).publish();
        connectableObservable.connect();

        // refCount
        Observable<Long> observable = connectableObservable.refCount();

        Disposable disposable1 = observable.subscribe(subscriber1);
        Disposable disposable2 = observable.subscribe(subscriber2);
        observable.subscribe(subscriber3);

        Thread.sleep(40);

        disposable1.dispose();
        disposable2.dispose();

        System.out.println ("重新开始数据流");

        disposable1 = observable.subscribe(subscriber1);
        disposable2 = observable.subscribe(subscriber2);

        Thread.sleep(40L);



//        ConnectableObservable<Long> hot = Observable
//                .interval(500, TimeUnit.MILLISECONDS)
//                .publish(); // same as above
//        Disposable subscription = hot.connect(); // connect returns a subscription object, which we store for further use
//
//        hot.subscribe(l -> System.out.println("sub1, " + l));
//        Thread.sleep(1000);
//        hot.subscribe(l -> System.out.println("sub2, " + l));
//        Thread.sleep(1000);
//        subscription.dispose(); // disconnect, or unsubscribe from subscription
//
//        System.out.println("reconnecting");
//        /* reconnect and redo */
//        subscription = hot.connect();
//        hot.subscribe(l -> System.out.println("sub1, " + l));
//        Thread.sleep(1000);
//        hot.subscribe(l -> System.out.println("sub2, " + l));
//        Thread.sleep(1000);
//        subscription.dispose();


//        Observable.interval(500, TimeUnit.MILLISECONDS)
//                .publish(); // publish converts cold to hot
//        ConnectableObservable<Long> hot = Observable
//                .interval(500, TimeUnit.MILLISECONDS)
//                .publish(); // returns ConnectableObservable
//        hot.connect(); // connect to subscribe
//
//        hot.subscribe(l -> System.out.println("sub1, " + l));
//        Thread.sleep(1000);
//        hot.subscribe(l -> System.out.println("sub2, " + l));
//        Thread.sleep(10000);

//        try {
//            // emits a long every 500 milliseconds
//            Observable<Long> cold = Observable.interval(500, TimeUnit.MILLISECONDS);
//            cold.subscribe(l -> System.out.println("sub1, " + l)); // subscriber1
//            Thread.sleep(1000); // interval between the two subscribes
//            cold.subscribe(l -> System.out.println("sub2, " + l)); // subscriber2
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
    }

}
