package org.example;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;

public class SingleTest {
    public static void main(String[] args) {

//        Single.create(new SingleOnSubscribe<String>() {
//            @Override
//            public void subscribe(SingleEmitter<String> e) throws Exception {
//                e.onSuccess("成功了");
//            }
//        }).subscribe(new Consumer<String>() {
//            @Override
//            public void accept(String s) throws Exception {
//                System.out.println(s);
//            }
//        }, new Consumer<Throwable>() {
//            @Override
//            public void accept(Throwable throwable) throws Exception {
//                throwable.printStackTrace();
//            }
//        });

        Single.create(new SingleOnSubscribe<String>() {
            @Override
            public void subscribe(SingleEmitter<String> e) throws Exception {
                e.onSuccess("成功了");
            }
        }).subscribe(new BiConsumer<String, Throwable>() {
            @Override
            public void accept(String s, Throwable throwable) throws Exception {
                System.out.println(s);
            }
        });

    }
}
