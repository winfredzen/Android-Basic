# BehaviorSubject

Observer会先接收到 `BehaviorSubject`被订阅之前的最后一个数据，再接收订阅之后发射过来的数据。如果`BehaviorSubject`被订阅之前没有发送任何数据，则会发送一个默认数据

如下的例子：

```java
        BehaviorSubject<String> subject = BehaviorSubject.createDefault("DefaultValue");
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
```

输出如下：

```java
behaviorSubject: s = DefaultValue
behaviorSubject: s = behaviorSubject1
behaviorSubject: s = behaviorSubject2
behaviorSubject complete
```

可见**“`BehaviorSubject`被订阅之前没有发送任何数据，则会发送一个默认数据”**（`DefaultValue`）

如果注释掉`subject.onComplete();`，放开`subject.onError(new RuntimeException("Error"));`的注释

```java
        subject.onNext("behaviorSubject1");
        subject.onNext("behaviorSubject2");
        //subject.onComplete();
        subject.onError(new RuntimeException("Error"));
        subject.onNext("behaviorSubject3");
```

此时控制台的输出如下：

```java
behaviorSubject: s = DefaultValue
behaviorSubject: s = behaviorSubject1
behaviorSubject: s = behaviorSubject2
behaviorSubject onError
```



在此修改上面的代码，在`subscribe`之前，再发送一个事件

```java
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
```

控制台输出如下：

```java
behaviorSubject: s = behaviorSubjectBefore
behaviorSubject: s = behaviorSubject1
behaviorSubject: s = behaviorSubject2
behaviorSubject complete
```

此时首次输出就不是默认值了，而是`behaviorSubjectBefore`































