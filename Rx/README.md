# Rx系列

主要原理来自[ReactiveX](http://reactivex.io/)，中文翻译可参考[ReactiveX文档中文翻译 - mcxiaoke](https://mcxiaoke.gitbooks.io/rxdocs/content/Intro.html)

**Observable**

> In ReactiveX an observer subscribes to an Observable. Then that observer reacts to whatever item or sequence of items the Observable emits. This pattern facilitates concurrent operations because it does not need to block while waiting for the Observable to emit objects, but instead it creates a sentry in the form of an observer that stands ready to react appropriately at whatever future time the Observable does so.
>
> 在ReactiveX中，观察者订阅了一个Observable。 然后，该观察者对可观察对象发出的任何item或item序列做出反应。 这种模式有助于并发操作，因为它在等待Observable发出对象时不需要阻塞，而是以观察者的形式创建了一个哨兵，随时准备在Observable以后的任何时间做出适当的反应。



参考如下的教程：

+ [RxJava2.0——从放弃到入门](https://www.jianshu.com/p/cd3557b1a474)



`Observable`表示被观察者，`Observer`表示观察值，一个简单的例子：

```java
    public static void main(String[] args) {
        Observable observable = Observable.create(new ObservableOnSubscribe<String>() {

            @Override
            public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Throwable {
                emitter.onNext("One");
                emitter.onNext("Two");
                emitter.onNext("Three");
                emitter.onComplete();
            }
        });

        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                System.out.println("onSubscribe : " + d);
            }

            @Override
            public void onNext(@NonNull String s) {
                System.out.println("onNext : " + s);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                System.out.println("onError : " + e);
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete : ");
            }
        };

        observable.subscribe(observer);
    }
```

输出结果为：

```java
onSubscribe : CreateEmitter{null}
onNext : One
onNext : Two
onNext : Three
onComplete :
```

`observable.subscribe(observer);`表示被观察值被观察值订阅了



## 异步

`Scheduler`，英文名调度器，它是`RxJava`用来控制线程。当我们没有设置的时候，`RxJava`遵循哪个线程产生就在哪个线程消费的原则，也就是说线程不会产生变化，始终在同一个。

`observeOn` - 是事件回调的线程，`observeOn(AndroidSchedulers.mainThread())`中`AndroidSchedulers.mainThread()`表示的是主线程

`subscribeOn`-是事件执行的线程，`subscribeOn(Schedulers.io())`中，`Schedulers.io()`是子线程，也可以使用`Schedulers.newThread()`， 只不过`io`线程可以重用空闲的线程，因此多数情况下 `io()` 比 `newThread()` 更有效率

```java
 Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("连载1");
                emitter.onNext("连载2");
                emitter.onNext("连载3");
                emitter.onComplete();
            }
        })
                .observeOn(AndroidSchedulers.mainThread())//回调在主线程
                .subscribeOn(Schedulers.io())//执行在io线程
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.e(TAG,"onSubscribe");
                    }

                    @Override
                    public void onNext(String value) {
                        Log.e(TAG,"onNext:"+value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG,"onError="+e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG,"onComplete()");
                    }
                })
```



## 进阶

参考：

+ [给 Android 开发者的 RxJava 详解](https://gank.io/post/560e15be2dca930e00da1083)



除了 `Observer` 接口之外，RxJava 还内置了一个实现了 `Observer` 的抽象类：`Subscriber`。 `Subscriber` 对 `Observer` 接口进行了一些扩展，但他们的基本使用方式是完全一样的：

```java
Subscriber<String> subscriber = new Subscriber<String>() {
    @Override
    public void onNext(String s) {
        Log.d(tag, "Item: " + s);
    }

    @Override
    public void onCompleted() {
        Log.d(tag, "Completed!");
    }

    @Override
    public void onError(Throwable e) {
        Log.d(tag, "Error!");
    }
};
```

































































































