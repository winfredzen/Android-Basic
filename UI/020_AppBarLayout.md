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





























