# MVVM

说实话，虽然对着书上的代码敲了一遍后，我对Android的MVVM还是不怎么清晰的

先从官方文档看起吧，官方文档：[数据绑定库](https://developer.android.com/topic/libraries/data-binding)

步骤如下：

**1.编译环境**

将应用配置为使用数据绑定，在`app/build.gradle`文件中添加 `dataBinding` 元素

```groovy
android {
    ......
    dataBinding {
        enabled = true
    }
}

```

**2.布局和绑定表达式**

数据绑定布局文件略有不同，根元素`layout`，后跟 `data` 元素和 `view` 根元素

以布局文件书中的`list_item_sound.xml`为例：

```xml
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <data>
        <variable
            name="viewModel"
            type="com.example.beatbox.SoundViewModel" />
    </data>

    <Button
        android:layout_width="match_parent"
        android:layout_height="120dp"
        android:text="@{viewModel.title}"
        tools:text="Sound name">

    </Button>

</layout>
```

**3.绑定数据**

系统会为每个布局文件生成一个绑定类。默认情况下，类名称基于布局文件的名称，它会转换为 `Pascal` 大小写形式并在末尾添加 `Binding` 后缀

 `activity_main.xml`，生成的对应类为 `ActivityMainBinding`















