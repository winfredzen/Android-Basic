package org.example;

import io.reactivex.MaybeEmitter;
import io.reactivex.MaybeOnSubscribe;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class Maybe {
    public static void main(String[] args) {

        io.reactivex.Maybe.create(new MaybeOnSubscribe<String>() {
            @Override
            public void subscribe(MaybeEmitter<String> e) throws Exception {
//                e.onSuccess("test maybe A");
//                e.onSuccess("test maybe B");
                e.onComplete();
                e.onSuccess("test maybe");
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.println("s=" + s);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {

            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                System.out.println("Maybe OnComplete");
            }
        });

    }
}
