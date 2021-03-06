# Rx系列

主要原理来自[ReactiveX](http://reactivex.io/)，中文翻译可参考[ReactiveX文档中文翻译 - mcxiaoke](https://mcxiaoke.gitbooks.io/rxdocs/content/Intro.html)

> Rx是一个函数库，让开发者可以利用可观察序列和LINQ风格查询操作符来编写异步和基于事件的程序，使用Rx，开发者可以用Observables表示异步数据流，用LINQ操作符查询异步数据流， 用Schedulers参数化异步数据流的并发处理，Rx可以这样定义：Rx = Observables + LINQ + Schedulers。

**Observable**

> In ReactiveX an observer subscribes to an Observable. Then that observer reacts to whatever item or sequence of items the Observable emits. This pattern facilitates concurrent operations because it does not need to block while waiting for the Observable to emit objects, but instead it creates a sentry in the form of an observer that stands ready to react appropriately at whatever future time the Observable does so.
>
> 在ReactiveX中，观察者订阅了一个Observable。 然后，该观察者对可观察对象发出的任何item或item序列做出反应。 这种模式有助于并发操作，因为它在等待Observable发出对象时不需要阻塞，而是以观察者的形式创建了一个哨兵，随时准备在Observable以后的任何时间做出适当的反应。



参考如下的教程：

+ [RxJava2.0——从放弃到入门](https://www.jianshu.com/p/cd3557b1a474)



`Observable`表示被观察者，`Observer`表示观察值，一个简单的例子：

```java
        //被观察者
        Observable observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
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
```

输出结果为：

```java
2021-07-07 09:34:00.210 20135-20135/com.example.rxdemo D/MainActivity_TAG: onSubscribe
2021-07-07 09:34:00.210 20135-20135/com.example.rxdemo D/MainActivity_TAG: onNext:连载1
2021-07-07 09:34:00.210 20135-20135/com.example.rxdemo D/MainActivity_TAG: onNext:连载2
2021-07-07 09:34:00.210 20135-20135/com.example.rxdemo D/MainActivity_TAG: onNext:连载3
2021-07-07 09:34:00.210 20135-20135/com.example.rxdemo D/MainActivity_TAG: onComplete:
```

`observable.subscribe(observer);`表示被观察值被观察值订阅了

> `onNext`方法可以无限调用，Observer（观察者）所有的都能接收到，`onError`和`onComplete`是互斥的，Observer（观察者）只能接收到一个，`OnComplete`可以重复调用，但是Observer（观察者）只会接收一次，而`onError`不可以重复调用，第二次调用就会报异常

> `onSubscribe（Disposable d）`里面的`Disposable`对象要说一下，Disposable英文意思是可随意使用的，这里就相当于读者和连载小说的订阅关系，如果读者不想再订阅该小说了，可以调用 `mDisposable.dispose()`取消订阅，此时连载小说更新的时候就不会再推送给读者了。



## 异步

`Scheduler`，英文名调度器，它是`RxJava`用来控制线程。当我们没有设置的时候，`RxJava`遵循哪个线程产生就在哪个线程消费的原则，也就是说线程不会产生变化，始终在同一个。

`observeOn` - 是事件回调的线程，`observeOn(AndroidSchedulers.mainThread())`中`AndroidSchedulers.mainThread()`表示的是主线程

`subscribeOn`-是事件执行的线程，`subscribeOn(Schedulers.io())`中，`Schedulers.io()`是子线程，也可以使用`Schedulers.newThread()`， 只不过`io`线程可以重用空闲的线程，因此多数情况下 `io()` 比 `newThread()` 更有效率

```java
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
```

测试的控制台输出结果：

```java
2021-07-07 10:36:02.209 9852-9852/com.example.rxdemo E/MainActivity_TAG: onSubscribe
2021-07-07 10:36:02.217 9852-9917/com.example.rxdemo E/MainActivity_TAG: subscribe 线程：RxCachedThreadScheduler-1
2021-07-07 10:36:02.218 9852-9852/com.example.rxdemo E/MainActivity_TAG: onNext:连载1
2021-07-07 10:36:02.218 9852-9852/com.example.rxdemo E/MainActivity_TAG: onNext 线程：main
2021-07-07 10:36:02.218 9852-9852/com.example.rxdemo E/MainActivity_TAG: onNext:连载2
2021-07-07 10:36:02.218 9852-9852/com.example.rxdemo E/MainActivity_TAG: onNext 线程：main
2021-07-07 10:36:02.218 9852-9852/com.example.rxdemo E/MainActivity_TAG: onNext:连载3
2021-07-07 10:36:02.218 9852-9852/com.example.rxdemo E/MainActivity_TAG: onNext 线程：main
2021-07-07 10:36:02.218 9852-9852/com.example.rxdemo E/MainActivity_TAG: onComplete()
2021-07-07 10:36:02.219 9852-9917/com.example.rxdemo E/MainActivity_TAG: subscribe 线程：RxCachedThreadScheduler-1
```

需要注意的是`AndroidSchedulers.mainThread()`需要导入：

```groovy
implementation 'io.reactivex.rxjava3:rxandroid:3.0.0'
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



### 其它文章

+ [RxJava 使用指南 -- 基本概念、数据流创建和线程调度](https://www.heqiangfly.com/2017/10/10/open-source-rxjava-guide-base/)
+ [RxJava 使用指南 -- 操作符介绍](https://www.heqiangfly.com/2017/10/12/open-source-rxjava-guide-operator/)
+ [RxJava 使用指南 -- Flowable 和 Subscriber](https://www.heqiangfly.com/2017/10/14/open-source-rxjava-guide-flowable/)
+ [RxJava 使用指南 -- Single、Completable 和 Maybe 的用法](https://www.heqiangfly.com/2017/10/18/open-source-rxjava-single-completable-maybe/)
+ [RxJava 使用指南 -- PublishSubject 实现事件总线](https://www.heqiangfly.com/2017/10/20/open-source-rxjava-event-bus/)



































































































