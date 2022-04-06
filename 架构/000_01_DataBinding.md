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



## 双向绑定

参考：

+ [双向数据绑定](https://developer.android.com/topic/libraries/data-binding/two-way?hl=zh-cn)

不过在开发中，需要很多自定义的view，如何在自定义view中使用双向绑定呢？

参考：

+ [Two-way Databinding with a custom property in Android](https://medium.com/@douglas.iacovelli/custom-two-way-databinding-made-easy-f8b17a4507d2)



### 相关类和接口

1.`InverseBindingAdapter`

`InverseBindingAdapter` 与用于在设置从视图收集的值时检索视图值的方法相关联。我自己的理解是**get方法**

如TextView的text方法：

```java
    @InverseBindingAdapter(attribute = "android:text", event = "android:textAttrChanged")
    public static String getTextString(TextView view) {
        return view.getText().toString();
    }
```

> 这里的关键点就是`@InverseBindingAdapter`去定义取值操作，注意这里有个`event = “android:textAttrChanged”`，这指的是当这个事件`android:textAttrChanged`发生的时候才去从view取值到viewmodel。那么如何定义`android:textAttrChanged`何时发生呢，这里是个纯粹的观察者，TextView的话是需要定义一个TextWatcher然后在`onTextChanged`的时候回调：`textAttrChanged.onChange()`。

`android:textAttrChanged`的定义，在官方教程中，有如下的说明：

> **注意**：每个双向绑定都会生成“合成事件特性”。该特性与基本特性具有相同的名称，但包含**后缀 `"AttrChanged"`**。合成事件特性允许库创建使用 `@BindingAdapter` 注释的方法，以将事件监听器与相应的 `View` 实例相关联。

```java
    @BindingAdapter(value = {"android:beforeTextChanged", "android:onTextChanged",
            "android:afterTextChanged", "android:textAttrChanged"}, requireAll = false)
    public static void setTextWatcher(TextView view, final BeforeTextChanged before,
            final OnTextChanged on, final AfterTextChanged after,
            final InverseBindingListener textAttrChanged) {
        final TextWatcher newValue;
        if (before == null && after == null && on == null && textAttrChanged == null) {
            newValue = null;
        } else {
            newValue = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    if (before != null) {
                        before.beforeTextChanged(s, start, count, after);
                    }
                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (on != null) {
                        on.onTextChanged(s, start, before, count);
                    }
                    if (textAttrChanged != null) {
                        textAttrChanged.onChange();
                    }
                }
                @Override
                public void afterTextChanged(Editable s) {
                    if (after != null) {
                        after.afterTextChanged(s);
                    }
                }
            };
        }
        final TextWatcher oldValue = ListenerUtil.trackListener(view, newValue, R.id.textWatcher);
        if (oldValue != null) {
            view.removeTextChangedListener(oldValue);
        }
        if (newValue != null) {
            view.addTextChangedListener(newValue);
        }
    }
```

TextView的text的set方法，**注意要判断新值和旧值是否相等**，避免死循环

```java
    @BindingAdapter("android:text")
    public static void setText(TextView view, CharSequence text) {
        final CharSequence oldText = view.getText();
        if (text == oldText || (text == null && oldText.length() == 0)) {
            return;
        }
        if (text instanceof Spanned) {
            if (text.equals(oldText)) {
                return; // No change in the spans, so don't set anything.
            }
        } else if (!haveContentsChanged(text, oldText)) {
            return; // No content changes, so don't set anything.
        }
        view.setText(text);
    }
```



2.`InverseBindingListener`

由所有双向绑定实现的侦听器，以在发生触发更改时得到通知。 例如，当 `android:text` 有双向绑定时，会在布局的绑定类中生成 `InverseBindingListener` 的实现。

```java
 private static class InverseListenerTextView implements InverseBindingListener {
     @Override
     public void onChange() {
         mObj.setTextValue(mTextView.getText());
     }
 }
```



### 参考例子

1.[Android Data Binding-21 | @InverseBindingMethod-TwoWay Binding for Custom View Types | U4Universe](https://www.youtube.com/watch?v=F9hS3Kf4Qz0&list=PLj76U7gxVixSLWg2v5MT6TcssOVL214oH&index=21)

源码例子位于https://github.com/saifi369/TwoWayDataBinding/tree/4-%40InverseBindingMethods

一个自定义Spinner的例子

```kotlin
@BindingMethods(
        BindingMethod(type = MySpinner::class, attribute = "city", method = "setSelectedCity"),
        BindingMethod(type = MySpinner::class, attribute = "cityAttrChanged", method = "setBindingListener")
)

@InverseBindingMethods(
        InverseBindingMethod(type = MySpinner::class, attribute = "city", method = "getSelectedCity")
)

class MySpinner : androidx.appcompat.widget.AppCompatSpinner {

    private lateinit var inverseBindingListener: InverseBindingListener

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {

        onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                inverseBindingListener.onChange()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

    }

    fun setSelectedCity(city: Cities) {
        Log.d(TAG, "setSelectedCity: called")
        setSelection(city.ordinal)
    }

    fun getSelectedCity(): Cities {
        Log.d(TAG, "getSelectedCity: called")
        return Cities.values()[selectedItemPosition]
    }

    fun setBindingListener(listener: InverseBindingListener) {
        Log.d(TAG, "setBindingListener: called")
        this.inverseBindingListener = listener
    }
}
```

在xml中使用：

```xml
<com.example.twowaydatabinding.MySpinner
    android:id="@+id/spinner_city"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_marginStart="50dp"
    city="@={viewModel.user.city}"
    android:entries="@array/cities" />
```



![062](https://github.com/winfredzen/Android-Basic/blob/master/%E6%9E%B6%E6%9E%84/images/062.png)





2.[Advanced DataBinding in Custom Views](https://blog.yudiz.com/advanced-databinding-in-custom-views/)

```kotlin
object CustomEditTextBinding {
    @JvmStatic
    @BindingAdapter(value = ["editTextValueAttrChanged"])
    fun setListener(customField: CustomEditText, listener: InverseBindingListener?) {
        if (listener != null) {
            customField.edt.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    charSequence: CharSequence,
                    i: Int,
                    i1: Int,
                    i2: Int
                ) {}
                override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
                override fun afterTextChanged(editable: Editable) {
                    listener.onChange()
                }
            })
        }
    }
 
    @JvmStatic
    @BindingAdapter("editTextValue")
    fun setRealValue(customField: CustomEditText, value: String) {
        if(customField.edt.text.toString() != value) {
            customField.edt.setText(value)
        }
    }
 
    @JvmStatic
    @InverseBindingAdapter(attribute = "editTextValue")
    fun getRealValue(customField: CustomEditText): String {
        return customField.edt.text.toString()
    }
}
```



### 其它可参考

+ [Android官方架构组件DataBinding-Ex: 双向绑定篇](https://www.jianshu.com/p/e8b6ba90de53)
+ [Android进阶六:Databinding的双向绑定](https://blog.csdn.net/lixpjita39/article/details/78751811)
+ [使用Android BindingAdapter与InverseBindingAdapter实现SeekBar双向（正向/反向）数据绑定](https://blog.csdn.net/zhangphil/article/details/77839555)



















