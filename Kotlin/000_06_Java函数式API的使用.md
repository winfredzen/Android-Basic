# Java函数式API的使用



Kotlin中调用Java方法时也可以使用函数式API，只不过这是有一定条件限制的。

具体来讲，如果我们在Kotlin代码中调用了一个Java方法，并且该方法接收一个Java单抽象方法接口参数，就可以使用函数式API。Java单抽象方法接口指的是接口中**只有一个待实现方法**，如果接口中有多个待实现方法，则无法使用函数式API。

如`Runnable`接口：

```kotlin
public interface Runnable {
 void run();
}
```

kotlin调用线程的写法：

```kotlin
Thread(object : Runnable {
 override fun run() {
 println("Thread is running")
 }
}).start()
```

> 由于Kotlin完全舍弃了`new`关键字，因此创建匿名类实例的时候就不能再使用`new`了，而是改用了`object`关键字

因为`Runnable`类中只有一个待实现方法，可以不显式地重写`run()`方法，

```kotlin
Thread(Runnable {
 println("Thread is running")
}).start()
```

如果一个Java方法的参数列表中有且仅有一个Java单抽象方法接口参数，还可以将接口名进行省略

```kotlin
Thread({
 println("Thread is running")
}).start()
```

当Lambda表达式是方法的最后一个参数时，可以将Lambda表达式移到方法括号的外面。同时，如果Lambda表达式还是方法的唯一一个参数，还可以将方法的括号省略

```kotlin
Thread {
 println("Thread is running")
}.start()
```



如Android的点击事件接口`OnClickListener`

```java
public interface OnClickListener {
 void onClick(View v);
}
```

使用Java代码去注册这个按钮的点击事件，需要这么写

```java
button.setOnClickListener(new View.OnClickListener() {
 @Override
 public void onClick(View v) {
 }
});
```

而用Kotlin代码实现同样的功能，就可以使用函数式API的写法来对代码进行简化，结果如下：

```java
button.setOnClickListener {
}
```

