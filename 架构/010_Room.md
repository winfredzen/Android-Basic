# Room

参考官方文档：[使用 Room 将数据保存到本地数据库](https://developer.android.com/training/data-storage/room#kts)

对kotlin而言，需要添加的依赖如下：

```groovy
    def room_version = "2.3.0"
    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")

    // To use Kotlin annotation processing tool (kapt)
    kapt("androidx.room:room-compiler:$room_version")
```

还需要添加`'kotlin-kapt'`插件

我在使用的过程中，有遇到如下的问题

1.报`cannot find implementation for XXX. XXX_Impl does not exist`

![044](https://github.com/winfredzen/Android-Basic/blob/master/%E6%9E%B6%E6%9E%84/images/045.png)

解决方式，就是需要添加上面的依赖和插件

2.`Room - Schema export directory is not provided to the annotation processor so we cannot export the schema`

![](https://github.com/winfredzen/Android-Basic/blob/master/%E6%9E%B6%E6%9E%84/images/046.png)

参考：

+  [Room - Schema export directory is not provided to the annotation processor so we cannot export the schema](https://stackoverflow.com/questions/44322178/room-schema-export-directory-is-not-provided-to-the-annotation-processor-so-we)

一种解决方式是：

添加`exportSchema = false`

```kotlin
@Database(entities = [ListCategory::class], version = 1, exportSchema = false)
```

3.操作数据时没有在子线程

![](https://github.com/winfredzen/Android-Basic/blob/master/%E6%9E%B6%E6%9E%84/images/047.png)



总结下Room的基本使用


















































