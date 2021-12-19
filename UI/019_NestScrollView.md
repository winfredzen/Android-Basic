# NestScrollView

1.NestScrollView可以解决滑动冲突

在本人的博客文章[滑动冲突处理](https://github.com/winfredzen/Android-Basic/blob/master/%E8%87%AA%E5%AE%9A%E4%B9%89%E8%A7%86%E5%9B%BE/015_%E6%BB%91%E5%8A%A8%E5%86%B2%E7%AA%81%E5%A4%84%E7%90%86.md)，使用的是内部冲突法和外部冲突法来解决滑动冲突的问题

但使用`NestScrollView`貌似可以直接解决滑动冲突的问题

参考[Android NestedScrollView Tutorial With Example](https://tutorialwing.com/android-nestedscrollview-tutorial-example/)中的例子：

```xml
<?xml version="1.0" encoding="utf-8"?>
<ScrollView
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginBottom="20dp"
        android:gravity="center"
        android:orientation="vertical">

        <androidx.core.widget.NestedScrollView
            android:layout_width="match_parent"
            android:layout_height="100dp"
            android:layout_margin="20dp"
            android:background="@android:color/white"
            android:padding="10dp">

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="vertical">

                <TextView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="@string/nested_scroll_text"/>

            </LinearLayout>

        </androidx.core.widget.NestedScrollView>

        <ImageView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginBottom="20dp"
            android:contentDescription="@string/no_image"
            android:src="@drawable/guava"/>

        <ImageView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginBottom="20dp"
            android:contentDescription="@string/no_image"
            android:src="@drawable/jackfruit"/>

        <ImageView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginBottom="20dp"
            android:contentDescription="@string/no_image"
            android:src="@drawable/mix_fruit"/>

        <ImageView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginBottom="20dp"
            android:contentDescription="@string/no_image"
            android:src="@drawable/pomegranate"/>

        <ImageView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginBottom="20dp"
            android:contentDescription="@string/no_image"
            android:src="@drawable/strawberry"/>

        <ImageView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginBottom="20dp"
            android:contentDescription="@string/no_image"
            android:src="@drawable/zespri_kiwi"/>

    </LinearLayout>

</ScrollView>

```

![039](https://github.com/winfredzen/Android-Basic/blob/master/UI/images/039.gif)



































