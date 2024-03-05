package org.example;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.BehaviorSubject;

public class BehaviorSubjectDemo {

    public static void main(String[] args) {

        BehaviorSubject<String> subject = BehaviorSubject.createDefault("DefaultValue");
        subject.onNext("behaviorSubjectBefore");
        subject.subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.println("behaviorSubject: s = " + s);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                System.out.println("behaviorSubject onError");
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                System.out.println("behaviorSubject complete");
            }
        });

        subject.onNext("behaviorSubject1");
        subject.onNext("behaviorSubject2");
        subject.onComplete();
        //subject.onError(new RuntimeException("Error"));
        subject.onNext("behaviorSubject3");

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


}
