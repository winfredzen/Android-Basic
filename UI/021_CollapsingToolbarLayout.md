# CollapsingToolbarLayout

[CollapsingToolbarLayout](https://developer.android.com/reference/com/google/android/material/appbar/CollapsingToolbarLayout)官方文档介绍如下：

> CollapsingToolbarLayout is a wrapper for Toolbar which implements a collapsing app bar. It is designed to be used as a direct child of a AppBarLayout
>
> CollapsingToolbarLayout 是 Toolbar 的包装器，它实现了一个折叠应用栏。 它旨在用作 AppBarLayout 的直接子项。
>
> 有如下的功能：
>
> **Collapsing title**
> A title which is larger when the layout is fully visible but collapses and becomes smaller as the layout is scrolled off screen. You can set the title to display via setTitle(CharSequence). The title appearance can be tweaked via the collapsedTextAppearance and expandedTextAppearance attributes.
>
> 当布局完全可见时较大的标题，但当布局滚动到屏幕外时会折叠并变小。 您可以通过 `setTitle(CharSequence)` 设置要显示的标题。 标题外观可以通过`collapsedTextAppearance` 和`expandedTextAppearance` 属性进行调整。
>
> **Content scrim**
> A full-bleed scrim which is show or hidden when the scroll position has hit a certain threshold. You can change this via setContentScrim(Drawable).
>
> 当滚动位置达到某个阈值时显示或隐藏的全 scrim。 您可以通过 `setContentScrim(Drawable)` 更改此设置。
>
> **Status bar scrim** 
>
> A scrim which is shown or hidden behind the status bar when the scroll position has hit a certain threshold. You can change this via setStatusBarScrim(Drawable). This only works on LOLLIPOP devices when we set to fit system windows.
>
> 当滚动位置达到某个阈值时，在状态栏后面显示或隐藏的scrim。 您可以通过 `setStatusBarScrim(Drawable)` 更改此设置。 当我们设置为适合系统窗口时，这仅适用于 LOLLIPOP 设备。
>
> **Parallax scrolling children**
> Child views can opt to be scrolled within this layout in a parallax fashion. See CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PARALLAX and CollapsingToolbarLayout.LayoutParams.setParallaxMultiplier(float).
>
> 子视图视差滚动
> 子视图可以选择以视差方式在此布局中滚动。 请参阅 `CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PARALLAX` 和 `CollapsingToolbarLayout.LayoutParams.setParallaxMultiplier(float)`
>
> **Pinned position children**
> Child views can opt to be pinned in space globally. This is useful when implementing a collapsing as it allows the Toolbar to be fixed in place even though this layout is moving. See CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PIN.
>
> 子视图固定
>
> 子视图可以选择全局固定在空间中。 这在实现折叠时很有用，因为它允许工具栏固定到位，即使此布局正在移动。 请参阅 `CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PIN`。
>
> **Do not manually add views to the Toolbar at run time.** We will add a 'dummy view' to the Toolbar which allows us to work out the available space for the title. This can interfere with any views which you add.
>
> 不要在运行时手动向工具栏添加视图。 我们将向工具栏添加一个“虚拟视图”，以便我们计算标题的可用空间。 这可能会干扰您添加的任何视图。



通过例子说明

1.第一个例子，来源于[Android CollapsingToolbarLayout Example](https://www.journaldev.com/13927/android-collapsingtoolbarlayout-example)

布局如下：

```java
<?xml version="1.0" encoding="utf-8"?>
<androidx.coordinatorlayout.widget.CoordinatorLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:fitsSystemWindows="true"
    tools:context=".ScrollingActivity">

    <com.google.android.material.appbar.AppBarLayout
        android:id="@+id/app_bar"
        android:layout_width="match_parent"
        android:layout_height="@dimen/app_bar_height"
        android:fitsSystemWindows="true"
        android:theme="@style/Theme.ScrollingActivityDemo.AppBarOverlay">

        <com.google.android.material.appbar.CollapsingToolbarLayout
            android:id="@+id/toolbar_layout"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:fitsSystemWindows="true"
            app:contentScrim="?attr/colorPrimary"
            app:layout_scrollFlags="scroll|exitUntilCollapsed">

            <ImageView
                android:id="@+id/expandedImage"
                android:layout_width="match_parent"
                android:layout_height="200dp"
                android:scaleType="centerCrop"
                android:src="@drawable/photo"
                app:layout_collapseMode="parallax"
                app:layout_collapseParallaxMultiplier="0.7" />

            <androidx.appcompat.widget.Toolbar
                android:id="@+id/toolbar"
                android:layout_width="match_parent"
                android:layout_height="?attr/actionBarSize"
                app:layout_collapseMode="pin"
                app:popupTheme="@style/Theme.ScrollingActivityDemo.PopupOverlay" />

        </com.google.android.material.appbar.CollapsingToolbarLayout>
    </com.google.android.material.appbar.AppBarLayout>

    <include layout="@layout/content_scrolling" />

    <com.google.android.material.floatingactionbutton.FloatingActionButton
        android:id="@+id/fab"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_margin="@dimen/fab_margin"
        app:layout_anchor="@id/app_bar"
        app:layout_anchorGravity="bottom|end"
        app:srcCompat="@android:drawable/ic_dialog_info" />

</androidx.coordinatorlayout.widget.CoordinatorLayout>
```

`content_scrolling`是一个`NestedScrollView`

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.core.widget.NestedScrollView xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:layout_behavior="@string/appbar_scrolling_view_behavior"
    tools:context=".ScrollingActivity"
    tools:showIn="@layout/activity_scrolling">

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_margin="@dimen/text_margin"
        android:text="@string/large_text" />

</androidx.core.widget.NestedScrollView>
```

这个例子，设置了AppBarLayout的`addOnOffsetChangedListener`，判断了折叠和展开的状态，在完全折叠时，显示ToolBar上的info按钮

```java
        AppBarLayout mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                Log.d(TAG, "onOffsetChanged verticalOffset = " + verticalOffset);
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                    Log.d(TAG, "appBarLayout.getTotalScrollRange() = " + scrollRange);
                }
                if (scrollRange + verticalOffset == 0) {
                    isShow = true;
                    showOption(R.id.action_info);
                } else if (isShow) {
                    isShow = false;
                    hideOption(R.id.action_info);
                }
            }
        });
```

通过log，可发现`appBarLayout.getTotalScrollRange()`相当于是获取的是appBarLayout可滚动的总范围

当向上滚动时，其值为负数

```java
2021-12-22 10:06:09.630 15323-15323/com.example.scrollingactivitydemo D/ScrollingActivity: onOffsetChanged verticalOffset = -258
2021-12-22 10:06:09.640 15323-15323/com.example.scrollingactivitydemo D/ScrollingActivity: onOffsetChanged verticalOffset = -258
```

下滑到顶部时，其值为0：

```java
2021-12-22 10:06:39.083 15323-15323/com.example.scrollingactivitydemo D/ScrollingActivity: onOffsetChanged verticalOffset = -44
2021-12-22 10:06:39.084 15323-15323/com.example.scrollingactivitydemo D/ScrollingActivity: onOffsetChanged verticalOffset = 0
```

效果如下：

![047](https://github.com/winfredzen/Android-Basic/blob/master/UI/images/047.gif)



































