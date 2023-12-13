# 集合&Lambda&空指针检查



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



## 空指针检查

### 判空辅助工具

最常用的`?.`操作符

```kotlin
if (a != null) {
 a.doSomething()
}
```

`?.`操作符就可以简化成

```kotlin
a?.doSomething()
```



**`?:`操作符**

`?:`操作符。这个操作符的左右两边都接收一个表达式，如果左边表达式的结果不为空就返回左边表达式的结果，否则就返回右边表达式的结果

```kotlin
val c = if (a ! = null) {
 a
} else {
 b
}
```

这段代码的逻辑使用`?:`操作符就可以简化成：

```kotlin
val c = a ?: b
```



**结合起来使用**

如判断字符串的长度

```kotlin
fun getTextLength(text: String?) = text?.length ?: 0
```



**非空断言**

如下的代码：

```kotlin
var content: String? = "hello"
fun main() {
 if (content != null) {
 printUpperCase()
 }
}
fun printUpperCase() {
 val upperCase = content.toUpperCase()
 println(upperCase)
}
```

但是会提示出错：

![032](https://github.com/winfredzen/Android-Basic/blob/master/Kotlin/images/032.png)

> 因为`printUpperCase()`函数并不知道外部已经对`content`变量进行了非空检查，在调用`toUpperCase()`方法时，还认为这里存在空指针风险，从而无法编译通过。

在这种情况下，如果我们想要强行通过编译，可以使用**非空断言**工具，写法是在对象的后面加上`!!`

```kotlin
fun printUpperCase() {
 val upperCase = content!!.toUpperCase()
 println(upperCase)
}
```



### let

let既不是操作符，也不是什么关键字，而是一个函数

如下的代码：

```kotlin
fun doStudy(study: Study?) {
 study?.readBooks()
 study?.doHomework()
}
```

可以结合使用`?.`操作符和`let`函数来对代码进行优化

```kotlin
fun doStudy(study: Study?) {
 study?.let { stu ->
 stu.readBooks()
 stu.doHomework()
 }
}
```

当Lambda表达式的参数列表中只有一个参数时，可以不用声明参数名，直接使用`it`关键字来代替即可

```kotlin
fun doStudy(study: Study?) {
 study?.let {
 it.readBooks()
 it.doHomework()
 }
}
```

`let`函数是可以处理全局变量的判空问题的，而`if`判断语句则无法做到这一点

比如将`doStudy()`函数中的参数变成一个全局变量，使用`let`函数仍然可以正常工作，但使用`if`判断语句则会提示错误

![033](https://github.com/winfredzen/Android-Basic/blob/master/Kotlin/images/033.png)

> 之所以这里会报错，是因为**全局变量的值随时都有可能被其他线程所修改，即使做了判空处理，仍然无法保证if语句中的study变量没有空指针风险**



## 字符串内嵌表达式

```kotlin
"hello, ${obj.name}. nice to meet you!"
```

当表达式中仅有一个变量的时候，还可以将两边的大括号省略

```kotlin
"hello, $name. nice to meet you!"
```



























