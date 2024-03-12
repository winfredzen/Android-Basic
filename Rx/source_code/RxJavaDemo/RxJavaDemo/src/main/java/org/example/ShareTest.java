package org.example;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observables.ConnectableObservable;
import org.reactivestreams.Subscription;

import java.util.concurrent.TimeUnit;

public class ShareTest {
    public static void main(String[] args) throws InterruptedException {


        ConnectableObservable<Long> hot = Observable
                .interval(500, TimeUnit.MILLISECONDS)
                .publish(); // same as above
        Disposable subscription = hot.connect(); // connect returns a subscription object, which we store for further use

        hot.subscribe(l -> System.out.println("sub1, " + l));
        Thread.sleep(1000);
        hot.subscribe(l -> System.out.println("sub2, " + l));
        Thread.sleep(1000);
        subscription.dispose(); // disconnect, or unsubscribe from subscription

        System.out.println("reconnecting");
        /* reconnect and redo */
        subscription = hot.connect();
        hot.subscribe(l -> System.out.println("sub1, " + l));
        Thread.sleep(1000);
        hot.subscribe(l -> System.out.println("sub2, " + l));
        Thread.sleep(1000);
        subscription.dispose();


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
