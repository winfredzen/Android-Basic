# ViewModel

先看官网的介绍[ViewModel 概览](https://developer.android.google.cn/topic/libraries/architecture/viewmodel)

> [`ViewModel`](https://developer.android.google.cn/reference/androidx/lifecycle/ViewModel) 类旨在以注重生命周期的方式存储和管理界面相关的数据。[`ViewModel`](https://developer.android.google.cn/reference/androidx/lifecycle/ViewModel) 类让数据可在发生屏幕旋转等配置更改后继续留存。

如[Incorporate Lifecycle-Aware Components](https://developer.android.com/codelabs/android-lifecycles#0)中的第一个例子：

屏幕旋转之后，原来的计时器的数值会被重置：

![004](https://github.com/winfredzen/Android-Basic/blob/master/%E6%9E%B6%E6%9E%84/images/004.png)

![005](https://github.com/winfredzen/Android-Basic/blob/master/%E6%9E%B6%E6%9E%84/images/005.png)

所以在`Activity` or `Fragment`中使用`ViewModel`来持有跨越生命周期的数据

> You can use a [`ViewModel`](https://developer.android.com/reference/androidx/lifecycle/ViewModel.html) to retain data across the entire lifecycle of an activity or a fragment. As the previous step demonstrates, an activity is a poor choice to manage app data. Activities and fragments are short-lived objects which are created and destroyed frequently as a user interacts with an app. A [`ViewModel`](https://developer.android.com/reference/androidx/lifecycle/ViewModel.html) is also better suited to managing tasks related to network communication, as well as data manipulation and persistence.

定义`ChronometerViewModel`

```java
public class ChronometerViewModel extends ViewModel {

    @Nullable
    private Long mStartTime;

    @Nullable
    public Long getStartTime() {
        return mStartTime;
    }

    public void setStartTime(final long startTime) {
        this.mStartTime = startTime;
    }
}
```

使用

```java
        // The ViewModelStore provides a new ViewModel or one previously created.
        ChronometerViewModel chronometerViewModel
                = new ViewModelProvider(this).get(ChronometerViewModel.class);

        // Get the chronometer reference
        Chronometer chronometer = findViewById(R.id.chronometer);

        if (chronometerViewModel.getStartTime() == null) {
            // If the start date is not defined, it's a new ViewModel so set it.
            long startTime = SystemClock.elapsedRealtime();
            chronometerViewModel.setStartTime(startTime);
            chronometer.setBase(startTime);
        } else {
            // Otherwise the ViewModel has been retained, set the chronometer's base to the original
            // starting time.
            chronometer.setBase(chronometerViewModel.getStartTime());
        }

        chronometer.start();
```

> `ViewModelProvider`构造方法`public ViewModelProvider(@NonNull ViewModelStoreOwner owner)`
>
> `ViewModelStoreOwner`是一个接口， `AppCompatActivity` 和`Fragment`实现了这个接口



## ViewModel 的生命周期

[`ViewModel`](https://developer.android.google.cn/reference/androidx/lifecycle/ViewModel) 对象存在的时间范围是获取 [`ViewModel`](https://developer.android.google.cn/reference/androidx/lifecycle/ViewModel) 时传递给 [`ViewModelProvider`](https://developer.android.google.cn/reference/androidx/lifecycle/ViewModelProvider) 的 [`Lifecycle`](https://developer.android.google.cn/reference/androidx/lifecycle/Lifecycle)。[`ViewModel`](https://developer.android.google.cn/reference/androidx/lifecycle/ViewModel) 将一直留在内存中，直到限定其存在时间范围的 [`Lifecycle`](https://developer.android.google.cn/reference/androidx/lifecycle/Lifecycle) 永久消失：对于 Activity，是在 Activity 完成时；而对于 Fragment，是在 Fragment 分离时。

![006](https://github.com/winfredzen/Android-Basic/blob/master/%E6%9E%B6%E6%9E%84/images/006.png)

您通常在系统首次调用 Activity 对象的 `onCreate()` 方法时请求 [`ViewModel`](https://developer.android.google.cn/reference/androidx/lifecycle/ViewModel)。系统可能会在 Activity 的整个生命周期内多次调用 `onCreate()`，如在旋转设备屏幕时。[`ViewModel`](https://developer.android.google.cn/reference/androidx/lifecycle/ViewModel) 存在的时间范围是从您首次请求 [`ViewModel`](https://developer.android.google.cn/reference/androidx/lifecycle/ViewModel) 直到 Activity 完成并销毁。































