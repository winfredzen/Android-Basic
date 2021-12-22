# AppBarLayout

[AppBarLayout](https://developer.android.com/reference/com/google/android/material/appbar/AppBarLayout)按照官方文档的介绍，它是一个垂直的[LinearLayout](https://developer.android.com/reference/android/widget/LinearLayout)

其child通过 `AppBarLayout.LayoutParams.setScrollFlags(int)`，或者`app:layout_scrollFlags`设置 scrolling behavior 

AppBarLayout应该放置在[CoordinatorLayout](https://developer.android.com/reference/androidx/coordinatorlayout/widget/CoordinatorLayout)下，作为其直接的child

通过一个个例子来说明，基本的布局如下：

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.coordinatorlayout.widget.CoordinatorLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <com.google.android.material.appbar.AppBarLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content">

        <androidx.appcompat.widget.Toolbar
            android:id="@+id/topAppBar"
            android:layout_width="match_parent"
            android:layout_height="?attr/actionBarSize"
            app:title="Toolbar"
            app:layout_scrollFlags="scroll"
            />

    </com.google.android.material.appbar.AppBarLayout>

    <!-- Note: A RecyclerView can also be used -->
    <androidx.core.widget.NestedScrollView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:layout_behavior="@string/appbar_scrolling_view_behavior"
        >

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:orientation="vertical"
            >

            <!-- Scrollable content -->
            <TextView
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:text="@string/large_text"
                android:textSize="20sp"
                />

        </LinearLayout>
    </androidx.core.widget.NestedScrollView>
</androidx.coordinatorlayout.widget.CoordinatorLayout>
```

> 注意
>
> 1.NestedScrollView中需设置`app:layout_behavior="@string/appbar_scrolling_view_behavior"`



1.将AppBarLayout中的child，即Toolbar，其`layout_scrollFlags`设置为scroll

> 子View添加`layout_scrollFlags`属性 的值`scroll` 时，这个View将会随着可滚动View（如：ScrollView,以下都会用ScrollView 来代替可滚动的View ）一起滚动，就好像子View 是属于ScrollView的一部分一样。

![040](https://github.com/winfredzen/Android-Basic/blob/master/UI/images/040.gif)



注意:ScrollFlags是设置给AppbarLayout的子View的,它可以有很多子View,你给那个子View设置,那个子View就会有效果,如下图,有俩个ToolBar,一个设置ScrollFlags,一个没有设置.

```xml
        <androidx.appcompat.widget.Toolbar
            android:id="@+id/topAppBar"
            android:layout_width="match_parent"
            android:layout_height="?attr/actionBarSize"
            app:title="Toolbar01"
            app:layout_scrollFlags="scroll"
            />

        <androidx.appcompat.widget.Toolbar
            android:id="@+id/topAppBar2"
            android:layout_width="match_parent"
            android:layout_height="?attr/actionBarSize"
            app:title="Toolbar02"
            />
```

![041](https://github.com/winfredzen/Android-Basic/blob/master/UI/images/041.gif)



2.设置为`enterAlways`，需配合`scroll`一起使用

```java
app:layout_scrollFlags="scroll|enterAlways"
```

有`enterAlways`时, 当`ScrollView`向下滑动时，子View将直接向下滑动（不管scrollView是否滑动顶部了）。

前面`scroll`的情况是，在scrollView向下滑动到顶部时，子view才向下滑动

![042](https://github.com/winfredzen/Android-Basic/blob/master/UI/images/042.gif)



3.`enterAlwaysCollapsed`,`enterAlwaysCollapsed` 的目的是进一步细化`enterAlways`的行为的

需要配合设置`android:minHeight`

```xml
<androidx.appcompat.widget.Toolbar
    android:id="@+id/topAppBar"
    android:layout_width="match_parent"
    app:title="Toolbar"
    app:layout_scrollFlags="scroll|enterAlways|enterAlwaysCollapsed"
    android:layout_height="250dp"
    android:minHeight="100dp"
    />
```

> 大家仔细观察一下，`enterAlwaysCollapsed` 其实也只是 enter 行为，也就是内容下滑的时候。当它 exit 的时候，也就是说手指向上滑动，它和 scroll 保持一致。
>
> 关键是 enter 的时候。
>
> 因为有 `enterAlways` 的存在，`Toolbar` 会和 `NestedScrollView` 一起响应滑动，但是又因为 `enterAlwaysCollapsed` 的存在，它的这种行为被限定了。`Toolbar` 先滑动，等到视图可见范围高度为 `collapsed` 指定高度时它会静止，等到 `NestedScrollView` 内容完全显示在 Toolbar 下方时它再一起滑动，它的动作是 3 段式的。

![043](https://github.com/winfredzen/Android-Basic/blob/master/UI/images/043.gif)



4.`exitUntilCollapsed`

当ScrollView 滑出屏幕时（也就时向上滑动时），滑动View先响应滑动事件，滑动至折叠高度，也就是通过minimum height 设置的最小高度后，就固定不动了，再把滑动事件交给 scrollview 继续滑动。

下拉时，先保持minimum height，当ScrollView下滑到顶部时，再展开子view

> 注意：exitUntilCollapsed是独立的，只依赖scroll，当然你可以和enterAlways或者enterAlwaysCollapsed一起用，但是效果还是以exitUntilCollapsed为主，并且效果会有一丝的卡顿和古怪，所以应该没人这么干

```java
<androidx.appcompat.widget.Toolbar
    android:id="@+id/topAppBar"
    android:layout_width="match_parent"
    app:title="Toolbar"
    app:layout_scrollFlags="scroll|exitUntilCollapsed"
    android:layout_height="250dp"
    android:minHeight="100dp"
    />
```

![044](https://github.com/winfredzen/Android-Basic/blob/master/UI/images/044.gif)



5.`snap`

>  snap 代表一种吸附的行为，当一个滑动事件结束后，Toolbar 会向最接近它的边缘自行滚动。那什么是最近的概念呢？比如向上滑动时，如果滑过了一半它就向上滚动，否则滚动回原来的地方。

```java
<androidx.appcompat.widget.Toolbar
    android:id="@+id/topAppBar"
    android:layout_width="match_parent"
    app:title="Toolbar"
    app:layout_scrollFlags="scroll|snap"
    android:layout_height="250dp"
    android:minHeight="100dp"
    />
```

向上滑动时

![045](https://github.com/winfredzen/Android-Basic/blob/master/UI/images/045.gif)

向下滑动时

![045](https://github.com/winfredzen/Android-Basic/blob/master/UI/images/045.gif)













































