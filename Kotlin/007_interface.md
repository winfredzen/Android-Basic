# interface

参考：

+ [接口](https://www.kotlincn.net/docs/reference/interfaces.html)

Kotlin 的接口可以既包含抽象方法的声明也包含实现

```kotlin
interface MyInterface {
    fun bar()
    fun foo() {
      // 可选的方法体
    }
}
```

可以在接口中定义属性。在接口中声明的属性要么是抽象的，要么提供访问器的实现

```kotlin
interface MyInterface {
    val prop: Int // 抽象的

    val propertyWithImplementation: String
        get() = "foo"

    fun foo() {
        print(prop)
    }
}

class Child : MyInterface {
    override val prop: Int = 29
}
```

