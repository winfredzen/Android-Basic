# DataBinding

使用一段时间DataBinding后的一点总结

## Codelab

官方Codelab的的例子：[Data Binding in Android](https://developer.android.com/codelabs/android-databinding#0)

代码位于[android-databinding-master](https://github.com/winfredzen/Android-Basic/tree/master/%E6%9E%B6%E6%9E%84/code/android-databinding-master)，UI效果如下：

![055](https://github.com/winfredzen/Android-Basic/blob/master/%E6%9E%B6%E6%9E%84/images/055.png)

1.如果不使用DataBinding，实现界面的效果，需要获取到对应的view，设置文本内容，添加事件等，如`PlainOldActivity`中演示的那样

![056](https://github.com/winfredzen/Android-Basic/blob/master/%E6%9E%B6%E6%9E%84/images/056.png)

2.使用使用DataBinding+ViewModel+LiveData的形式来解决，则代码会更简洁

![057](https://github.com/winfredzen/Android-Basic/blob/master/%E6%9E%B6%E6%9E%84/images/057.png)

![058](https://github.com/winfredzen/Android-Basic/blob/master/%E6%9E%B6%E6%9E%84/images/058.png)

在xml中使用

![059](https://github.com/winfredzen/Android-Basic/blob/master/%E6%9E%B6%E6%9E%84/images/059.png)

![060](https://github.com/winfredzen/Android-Basic/blob/master/%E6%9E%B6%E6%9E%84/images/060.png)

使用到了自定义的Binding Adapter

```kotlin
/**
 * A Binding Adapter that is called whenever the value of the attribute `app:popularityIcon`
 * changes. Receives a popularity level that determines the icon and tint color to use.
 */
@BindingAdapter("app:popularityIcon")
fun popularityIcon(view: ImageView, popularity: Popularity) {

    val color = getAssociatedColor(popularity, view.context)

    ImageViewCompat.setImageTintList(view, ColorStateList.valueOf(color))

    view.setImageDrawable(getDrawablePopularity(popularity, view.context))
}

/**
 * A Binding Adapter that is called whenever the value of the attribute `android:progressTint`
 * changes. Depending on the value it determines the color of the progress bar.
 */
@BindingAdapter("app:progressTint")
fun tintPopularity(view: ProgressBar, popularity: Popularity) {

    val color = getAssociatedColor(popularity, view.context)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        view.progressTintList = ColorStateList.valueOf(color)
    }
}

/**
 *  Sets the value of the progress bar so that 5 likes will fill it up.
 *
 *  Showcases Binding Adapters with multiple attributes. Note that this adapter is called
 *  whenever any of the attribute changes.
 */
@BindingAdapter(value = ["app:progressScaled", "android:max"], requireAll = true)
fun setProgress(progressBar: ProgressBar, likes: Int, max: Int) {
    progressBar.progress = (likes * max / 5).coerceAtMost(max)
}

/**
 * Unused Binding Adapter to replace the Binding Converter that hides a view if the number
 * of likes is zero.
 */
@BindingAdapter("app:hideIfZero")
fun hideIfZero(view: View, number: Int) {
    view.visibility = if (number == 0) View.GONE else View.VISIBLE
}

private fun getAssociatedColor(popularity: Popularity, context: Context): Int {
    return when (popularity) {
        Popularity.NORMAL -> context.theme.obtainStyledAttributes(
            intArrayOf(android.R.attr.colorForeground)
        ).getColor(0, 0x000000)
        Popularity.POPULAR -> ContextCompat.getColor(context, R.color.popular)
        Popularity.STAR -> ContextCompat.getColor(context, R.color.star)
    }
}

private fun getDrawablePopularity(popularity: Popularity, context: Context): Drawable? {
    return when (popularity) {
        Popularity.NORMAL -> {
            ContextCompat.getDrawable(context, R.drawable.ic_person_black_96dp)
        }
        Popularity.POPULAR -> {
            ContextCompat.getDrawable(context, R.drawable.ic_whatshot_black_96dp)
        }
        Popularity.STAR -> {
            ContextCompat.getDrawable(context, R.drawable.ic_whatshot_black_96dp)
        }
    }
}

```



### ViewModel

在看下的上面用到的`ViewModel`

```kotlin
/**
 * A VM for [com.example.android.databinding.basicsample.ui.SolutionActivity].
 */
class SimpleViewModelSolution : ViewModel() {
    private val _name = MutableLiveData("Ada")
    private val _lastName = MutableLiveData("Lovelace")
    private val _likes =  MutableLiveData(0)

    val name: LiveData<String> = _name
    val lastName: LiveData<String> = _lastName
    val likes: LiveData<Int> = _likes

    // popularity is exposed as LiveData using a Transformation instead of a @Bindable property.
    val popularity: LiveData<Popularity> = Transformations.map(_likes) {
        when {
            it > 9 -> Popularity.STAR
            it > 4 -> Popularity.POPULAR
            else -> Popularity.NORMAL
        }
    }

    fun onLike() {
        _likes.value = (_likes.value ?: 0) + 1
    }
}

```

> you can make one `LiveData` depend on another using [Transformations](https://developer.android.com/reference/android/arch/lifecycle/Transformations). This mechanism allows the library to update the UI when the value changes.



## Observable vs LiveData

该使用Observable还是LiveData呢？

参考：

+ [Android Data Binding Library — from Observable Fields to LiveData in two steps](https://medium.com/androiddevelopers/android-data-binding-library-from-observable-fields-to-livedata-in-two-steps-690a384218f2)



[Data Binding Library](https://developer.android.com/topic/libraries/data-binding/)中有许多可观察类：ObservableBoolean` , `ObservableInt`, `ObservableDouble等，还可以使用`ObservableField<T>`

在[Architecture Components](https://developer.android.com/topic/libraries/architecture)中，则引入了[**LiveData**](https://developer.android.com/topic/libraries/architecture/livedata)，也是可观察的

> [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) is lifecycle-aware but this is not a huge advantage with respect to Observable Fields because Data Binding already checks when the view is active. However, **LiveData supports** [**Transformations**](https://developer.android.com/reference/android/arch/lifecycle/Transformations)**, and many Architecture Components, like** [**Room**](https://developer.android.com/topic/libraries/architecture/room) **and** [**WorkManager**](https://developer.android.com/reference/androidx/work/WorkManager)**, support LiveData**.
>
> LiveData 是生命周期感知的，但这对于可观察字段来说并不是一个巨大的优势，因为数据绑定已经检查了视图何时处于活动状态。 但是，LiveData 支持转换，并且许多架构组件，例如 Room 和 WorkManager，都支持 LiveData。

推荐使用LiveData，迁移到LiveData需要如下的操作

1.使用LiveData替换Observable Fields可观察字段

替换前：

```xml
<data>
    <import type="android.databinding.ObservableField"/>
    <variable 
        name="name" 
        type="ObservableField&lt;String>" />
</data>
…
<TextView
    android:text="@{name}"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"/>
```

替换后：

```xml
<data>
        <import type="android.arch.lifecycle.LiveData" />
        <variable
            name="name"
            type="LiveData&lt;String>" />
</data>
…
<TextView
    android:text="@{name}"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"/>
```

另一种方式，在ViewModel中可直接替换

替换前：

```kotlin
class MyViewModel : ViewModel() {
    val name = ObservableField<String>("Ada")
}
```

替换后：

```kotlin
class MyViewModel : ViewModel() {
    private val _name = MutableLiveData<String>().apply { value = "Ada" }

    val name: LiveData<String> = _name // Expose the immutable version of the LiveData
}
```



2.设置LiveData的lifecycle owner

替换前：

```kotlin
val binding = DataBindingUtil.setContentView<TheGeneratedBinding>(
    this,
    R.layout.activity_data_binding
)

binding.name = myLiveData // or myViewModel
```

替换后：

```kotlin
val binding = DataBindingUtil.setContentView<TheGeneratedBinding>(
    this,
    R.layout.activity_data_binding
)

binding.lifecycleOwner = this // Use viewLifecycleOwner for fragments

binding.name = myLiveData // or myViewModel
```

> 如果使用的是**fragment**，推荐使用**fragment.viewLifecycleOwner**（代替**fragment’s lifecycle**）



## 经验教训

官方文档推荐的

- [数据绑定 - 经验教训](https://medium.com/androiddevelopers/data-binding-lessons-learnt-4fd16576b719)

总结了作者在使用DataBinding时的一些经验教训

1.尽量使用标准的binding

很多的时候，都需要格式化字符串，如果使用自定义binding adapter的话

```kotlin
/* Copyright 2018 Google LLC.
   SPDX-License-Identifier: Apache-2.0 */
@BindingAdapter("showTitle")
fun showTitle(view: TextView, show: TiviShow) {
    view.text = buildSpannedString {
        inSpans(...) {
            append(show.title)
        }
        if (show.firstAired != null) {
            append(" ")
            inSpans(...) {
                append("(")
                append(show.firstAired.year)
                append(")")
            }
        }
    }
}
```

```kotlin
<!-- Copyright 2018 Google LLC.
     SPDX-License-Identifier: Apache-2.0 -->
<TextView
    app:showTitle="@{viewState.show}" />
```

有很多副作用，所以可以把这些逻辑放在一个类中

```xml
<!-- Copyright 2018 Google LLC.
     SPDX-License-Identifier: Apache-2.0 -->
<layout>
    <data>
        <variable
            name="textCreator"
            type="app.tivi.ShowDetailsTextCreator" />
        <variable
            name="state"
            type="app.tivi.showdetails.ShowDetailsViewState" />
    </data>

    <TextView
        android:text="@{textCreator.genreString(state.genres)}" />

</layout>
```



2.include中使用databinding的一些问题

a.传递变量，这个官方文档有讲到

b.获取include中的控件，参考：

+ [DataBinding-xml中使用include](https://blog.csdn.net/u010976213/article/details/77746315)

需要设置id等属性

例如，遇到在include中设置margin不生效的问题，测试就需要设置width、height等其他的属性，

c.自定义adapter的问题，参考：

+ [Applying databinding adapter to include tag](https://stackoverflow.com/questions/50722783/applying-databinding-adapter-to-include-tag)

> Apparently, currently you are not able to use `BindingAdapters` with `included` layout elements, but you can pass your variables inside the included layouts (for them to handle).
>
> 即不能使用





























