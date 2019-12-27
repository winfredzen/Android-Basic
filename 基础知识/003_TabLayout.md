# TabLayout

TabLayout通常搭配Fragment和ViewPager使用，使用时引入design support library 

```groovy
implementation 'com.android.support:design:28.0.0'
```

[TabLayout](https://developer.android.com/reference/android/support/design/widget/TabLayout)官方文档介绍如下：

选项卡是[TabLayout.Tab](https://developer.android.com/reference/android/support/design/widget/TabLayout.Tab.html)实例，可通过 `newTab()`创建tabs。可通过`setText(int)` 和 `setIcon(int)`方法修改文字和icon，要显示tab，需要通过[addTab(Tab)](https://developer.android.com/reference/android/support/design/widget/TabLayout.html#addTab(android.support.design.widget.TabLayout.Tab))方法将其添加到布局中

```java
 TabLayout tabLayout = ...;
 tabLayout.addTab(tabLayout.newTab().setText("Tab 1"));
 tabLayout.addTab(tabLayout.newTab().setText("Tab 2"));
 tabLayout.addTab(tabLayout.newTab().setText("Tab 3"));
```

设置[setOnTabSelectedListener(OnTabSelectedListener)](https://developer.android.com/reference/android/support/design/widget/TabLayout.html#setOnTabSelectedListener(android.support.design.widget.TabLayout.OnTabSelectedListener))监听器，tab的选中状态改变时获得通知

也可以通过使用[TabItem](https://developer.android.com/reference/android/support/design/widget/TabItem.html)，把item添加大TabLayout中

```swift
 <android.support.design.widget.TabLayout
         android:layout_height="wrap_content"
         android:layout_width="match_parent">

     <android.support.design.widget.TabItem
             android:text="@string/tab_text"/>

     <android.support.design.widget.TabItem
             android:icon="@drawable/ic_android"/>

 </android.support.design.widget.TabLayout>
```

**集成ViewPager**

通过[setupWithViewPager(ViewPager)](https://developer.android.com/reference/android/support/design/widget/TabLayout.html#setupWithViewPager(android.support.v4.view.ViewPager))可将[ViewPager](https://developer.android.com/reference/android/support/v4/view/ViewPager.html)和TabLayout绑定在一起。TabLayout将自动从PagerAdapter的页面标题中填充

所以一般会重写`getPageTitle`方法

```java
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
```

该view也可成为ViewPager's decor的一部分，可被直接添加到ViewPager中，如下：

```xml
 <android.support.v4.view.ViewPager
     android:layout_width="match_parent"
     android:layout_height="match_parent">

     <android.support.design.widget.TabLayout
         android:layout_width="match_parent"
         android:layout_height="wrap_content"
         android:layout_gravity="top" />

 </android.support.v4.view.ViewPager>
```



可参考的文档：

+ [Tablayout使用全解，一篇就够了](https://www.jianshu.com/p/fde38f367019)
+ [Android Material Design Tabs (Tab Layout) with Swipe](https://medium.com/@droidbyme/android-material-design-tabs-tab-layout-with-swipe-884085ae80ff)
+ [Android Material Design working with Tabs](https://www.androidhive.info/2015/09/android-material-design-working-with-tabs/)



常用属性：

+ app:tabIndicatorColor ：指示线的颜色
+ app:tabIndicatorHeight ：指示线的高度
+ app:tabSelectedTextColor ： tab选中时的字体颜色
+ app:tabMode="scrollable" ： 默认是fixed，固定的；scrollable：可滚动的
+ app:tabGravity=”fill” ：表示tabs item會填滿整個TabLayout，center表示居中显示，适用于布局中view比较少的时候



