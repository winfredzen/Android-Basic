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















































































