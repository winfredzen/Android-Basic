# Collections

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

