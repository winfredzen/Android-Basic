# Kotlin使用笔记

1.`val intent = Intent(this, MainActivity::class.java)`

MainActivity::class.java的含义是，参考[getClass()](https://www.kotlincn.net/docs/reference/java-interop.html)

>要取得对象的 Java 类，请在[类引用](https://www.kotlincn.net/docs/reference/reflection.html#类引用)上使用 `java` 扩展属性：
>
>```kotlin
>val fooClass = foo::class.java
>```
>
>上面的代码使用了自 Kotlin 1.1 起支持的[绑定的类引用](https://www.kotlincn.net/docs/reference/reflection.html#绑定的类引用自-11-起)。你也可以使用 `javaClass` 扩展属性：
>
>```kotlin
>val fooClass = foo.javaClass
>```