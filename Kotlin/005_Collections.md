# Collections

参考：

+ [Kotlin 集合概述](https://www.kotlincn.net/docs/reference/collections-overview.html)

主要包含

+ Arrays
+ Lists
+ Maps

## Arrays

使用`arrayOf()`来创建数组

```kotlin
    val evenNumbers = arrayOf(2,  4, 6, 6)
    //显式指定类型
    val evenNumbers2: Array<Int> = arrayOf(2,  4, 6, 6)

    val intNumbers = intArrayOf(2, 4, 6, 8)
```

~~array创建后，不可add或者remove，会提示出错~~

![004](https://github.com/winfredzen/Android-Basic/blob/master/Kotlin/images/004.png)

> Error:(88, 5) Kotlin: Val cannot be reassigned

这里的原因是使用了val，如果使用var就可以添加了



`sliceArray()`返回array的某个部分

```kotlin
fun <T> Array<T>.sliceArray(
    indices: Collection<Int>
): Array<T>
```

```kotlin
    val players = arrayOf("A", "B", "C")
    val upcomingPlayers = players.sliceArray(1..2)
    for (player in upcomingPlayers) {
        println(player)
        //B
        //C
    }
```



## List

[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html) 以指定的顺序存储元素，并提供使用索引访问元素的方法。

`listOf()`创建不可变list

```kotlin
    var names = listOf("Anna", "Bob", "Craig", "Donna")
    println(names) //[Anna, Bob, Craig, Donna]
```

`mutableListOf()`创建可变list

```kotlin
    var mutNames = mutableListOf<String>("Anna", "Bob", "Craig", "Donna")
    mutNames.add("Sam")
```



## Map

[`Map`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-map/index.html) 不是 `Collection` 接口的继承者；但是它也是 Kotlin 的一种集合类型。

```kotlin
fun main() {
    val numbersMap = mapOf("key1" to 1, "key2" to 2, "key3" to 3, "key4" to 1)

    println("All keys: ${numbersMap.keys}")
    println("All values: ${numbersMap.values}")
    if ("key2" in numbersMap) println("Value by key \"key2\": ${numbersMap["key2"]}")    
    if (1 in numbersMap.values) println("The value 1 is in the map")
    if (numbersMap.containsValue(1)) println("The value 1 is in the map") // 同上
}
```

```kotlin
All keys: [key1, key2, key3, key4]
All values: [1, 2, 3, 1]
Value by key "key2": 2
The value 1 is in the map
The value 1 is in the map
```

[`MutableMap`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/index.html) 是一个具有写操作的 `Map` 接口，可以使用该接口添加一个新的键值对或更新给定键的值

```kotlin
fun main() {
    val numbersMap = mutableMapOf("one" to 1, "two" to 2)
    numbersMap.put("three", 3)
    numbersMap["one"] = 11

    println(numbersMap)
}
```

```kotlin
{one=11, two=2, three=3}
```

可对map进行遍历

```kotlin
    for ((key, value) in numbersMap) {
        println("$key - $value" )
    }
```

```kotlin
one - 11
two - 2
three - 3
```



## 可变与不可变

如下的例子

```kotlin
    var names = ArrayList<String>()
    names.add("Sam")
    names.add("Fred")

    fun printNames(names: ArrayList<String>) {
        println(names)
        names.removeAt(0)
    }

    printNames(names) //[Sam, Fred]
    println(names) //[Fred]
```

`names`在函数`printNames`被修改了，如果不想被修改，可以将函数中`ArrayList`替换为`List`

```kotlin
    fun printNames(names: List<String>) {
        println(names)
    }
```

