# Cold vs Hot

Hot Observable的特点：

+ Hot Observable无论有没有观察者进行订阅，事件始终都会发生
+ Hot Observable与订阅者们的关系是一对多关系，可以与多个订阅者共享信息



Cold Observable的特点： 

+ Cold Observable是只有观察者订阅了，才开始执行发射数据流的代码
+ Cold Observable和 Observer 是一对一的关系 。当有多个不同的订阅者时，消息是重新完整发迭的。也就是说，对Cold Observable，有多个Observer的时候，它们各自的事件是独立的



**把 Hot Observable 想象成 个广播电台，所有在此刻收听的昕众都会听到同一首歌**

**Cold Observable 一张音乐 CD ，人们可以独立购买并听取它**



## Cold Observable

Observable 的 `just`、`create`、`range`、 `fromXXX` 等操作符都能生成 Cold Observable

如下的例子：

```java
        try {
            // emits a long every 500 milliseconds
            Observable<Long> cold = Observable.interval(500, TimeUnit.MILLISECONDS);
            cold.subscribe(l -> System.out.println("sub1, " + l)); // subscriber1
            Thread.sleep(1000); // interval between the two subscribes
            cold.subscribe(l -> System.out.println("sub2, " + l)); // subscriber2
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
```

```java
sub1, 0
sub1, 1
sub1, 2
sub2, 0
sub2, 1
sub1, 3
sub2, 2
sub1, 4
sub1, 5
sub2, 3
```

注意，虽然`sub2`晚开始，但它的值是从头开始的



## Hot Observable

将Cold Observable转为Hot Observable

1.使用 `publish` ，生`ConnectableObservable`

2.`ConnectableObservable` 调用 `connect`

如下的例子：

```java
        Observable.interval(500, TimeUnit.MILLISECONDS)
                .publish(); // publish converts cold to hot
        ConnectableObservable<Long> hot = Observable
                .interval(500, TimeUnit.MILLISECONDS)
                .publish(); // returns ConnectableObservable
        hot.connect(); // connect to subscribe

        hot.subscribe(l -> System.out.println("sub1, " + l));
        Thread.sleep(1000);
        hot.subscribe(l -> System.out.println("sub2, " + l));
        Thread.sleep(10000);
```

```java
sub1, 0
sub1, 1
sub1, 2
sub2, 2
sub1, 3
sub2, 3
sub1, 4
sub2, 4
sub1, 5
sub2, 5
```

注意，虽然`sub2`晚开始，但它的值和sub1是同步的



再如下的例子：

```java
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
```

```java
sub1, 0
sub1, 1
sub1, 2
sub2, 2
sub1, 3
sub2, 3
reconnecting
sub1, 0
sub1, 1
sub2, 1
sub1, 2
sub2, 2
sub1, 3
sub2, 3
```

断开连接后，Observable “终止”了，并在添加新订阅时重新启动。



















































