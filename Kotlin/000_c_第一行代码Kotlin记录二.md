# 第一行代码Kotlin记录二



## 集合

### Array

通常创建Array的形式如下：

```kotlin
    val list = ArrayList<String>()
    list.add("Apple")
    list.add("Banana")
    list.add("Orange")
    list.add("Pear")
    list.add("Grape")
```

Kotlin专门提供了一个内置的`listOf()`函数来简化初始化集合的写法

```kotlin
val list = listOf("Apple", "Banana", "Orange", "Pear", "Grape")
```

使用`for-in`循环来遍历

```kotlin
for (fruit in list) {
    println(fruit)
}
```

`listOf()`创建的是不可变集合，创建可变集合使用`mutableListOf()`函数

```kotlin
    val list = mutableListOf("Apple", "Banana", "Orange", "Pear", "Grape")
    list.add("Watermelon")
    for (fruit in list) {
        println(fruit)
    }
```



### Set

Set可使用`setOf()`和`mutableSetOf()`函数

```kotlin
    val set = setOf("Apple", "Banana", "Orange", "Pear", "Grape")
    for (fruit in set) {
        println(fruit)
    }
```



### Map

传统的创建Map的方式是：

```kotlin
    val map = HashMap<String, Int>()
    map.put("Apple", 1)
    map.put("Banana", 2)
    map.put("Orange", 3)
    map.put("Pear", 4)
    map.put("Grape", 5)
```

在Kotlin中并不建议使用`put()`和`get()`方法来对Map进行添加和读取数据操作，而是更加推荐使用一种类似于数组下标的语法结构

```kotlin
    val map = HashMap<String, Int>()
    map["Apple"] = 1
    map["Banana"] = 2
    map["Orange"] = 3
    map["Pear"] = 4
    map["Grape"] = 5
```

也可以使用`mapOf()`和`mutableMapOf()`函数来继续简化Map的用法

```kotlin
val map = mapOf("Apple" to 1, "Banana" to 2, "Orange" to 3, "Pear" to 4, "Grape" to 5)
```

> 这里的键值对组合看上去好像是使用`to`这个关键字来进行关联的，但其实`to`并不是关键字，而是一个`infix`函数

**遍历**

```kotlin
    val map = mapOf("Apple" to 1, "Banana" to 2, "Orange" to 3, "Pear" to 4, "Grape" to 5)
    for ((fruit, number) in map) {
        println("fruit is " + fruit + ", number is " + number)
    }
```



## Lambda

### 集合的函数式**API**

如在集合中找到长度最长的单词，通常我们的做法如下：

```kotlin
    val list = listOf("Apple", "Banana", "Orange", "Pear", "Grape", "Watermelon")
    var maxLengthFruit = ""
    for (fruit in list) {
        if (fruit.length > maxLengthFruit.length) {
            maxLengthFruit = fruit
        }
    }
    println("max length fruit is " + maxLengthFruit)
```

而如果使用函数式的API，则非常的简洁

```kotlin
val list = listOf("Apple", "Banana", "Orange", "Pear", "Grape", "Watermelon")
val maxLengthFruit = list.maxByOrNull { it.length }
println("max length fruit is " + maxLengthFruit)
```

Lambda表达式的语法结构

```kotlin
{参数名1: 参数类型, 参数名2: 参数类型 -> 函数体}
```

起始上面的函数的形式是：

```kotlin
val list = listOf("Apple", "Banana", "Orange", "Pear", "Grape", "Watermelon")
val lambda = { fruit: String -> fruit.length }
val maxLengthFruit = list.maxByOrNull(lambda)
```

**简化**

1.不需要定义lambda变量，直接传入函数

```kotlin
val maxLengthFruit = list.maxByOrNull({ fruit: String -> fruit.length })
```

2.Kotlin规定，当Lambda参数是函数的最后一个参数时，可以将Lambda表达式移到函数括号的外面

```kotlin
val maxLengthFruit = list.maxByOrNull() { fruit: String -> fruit.length }
```

3.如果Lambda参数是函数的唯一一个参数的话，还可以将函数的括号省略

```kotlin
val maxLengthFruit = list.maxByOrNull{ fruit: String -> fruit.length }
```

4.kotlin的类型推导机制，可以不必声明参数类型

```kotlin
val maxLengthFruit = list.maxByOrNull{ fruit -> fruit.length }
```

5.当Lambda表达式的参数列表中只有一个参数时，也不必声明参数名，而是可以使用`it`关键字来代替

```kotlin
val maxLengthFruit = list.maxByOrNull{ it.length }
```



## 集合常用API

### `map`

`map`函数用于将集合中的每个元素都映射成一个另外的值，映射的规则在Lambda表达式中指定，最终生成一个新的集合

```kotlin
    val list = listOf("Apple", "Banana", "Orange", "Pear", "Grape", "Watermelon")
    val newList = list.map { it.uppercase() }
    for (fruit in newList) {
        println(fruit)
    }
```



### filter

`filter`函数用来过滤数据

```kotlin
    val list = listOf("Apple", "Banana", "Orange", "Pear", "Grape", "Watermelon")
    val newList = list.filter { it.length <= 5 }
        .map { it.toUpperCase() }
    for (fruit in newList) {
        println(fruit)
    }
```



### any和all函数

any函数用于判断集合中是否至少存在一个元素满足指定条件

all函数用于判断集合中是否所有元素都满足指定条件

```kotlin
    val list = listOf("Apple", "Banana", "Orange", "Pear", "Grape", "Watermelon")
    val anyResult = list.any { it.length <= 5 }
    val allResult = list.all { it.length <= 5 }
    println("anyResult is " + anyResult + ", allResult is " + allResult)
    //anyResult is true, allResult is false
```



## **Java**函数式**API**的使用

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



































