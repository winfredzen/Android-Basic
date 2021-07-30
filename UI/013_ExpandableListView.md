# ExpandableListView

`ExpandableListView`是`ListView`的子类。而`expandable` 在英文中的意思是可扩展的，所以`ExpandableListView`就是一个可以扩展的、有层级的`ListView`。

可参考：

+ [ExpandableListView--基本使用介绍](https://www.jianshu.com/p/05df9c17a1d8)

[ExpandableListView](https://developer.android.com/reference/android/widget/ExpandableListView)常用属性：

+ groupIndicator - 分组的指示器
+ childIndicator
+ childDivider - 分割线

常用方法

+ `setAdapter(ExpandableListAdapter adapter)`
+ `setOnGroupClickListener(ExpandableListView.OnGroupClickListener onGroupClickListener)`
+ `setOnChildClickListener(ExpandableListView.OnChildClickListener onChildClickListener)`
+ `setOnGroupCollapseListener(ExpandableListView.OnGroupCollapseListener onGroupCollapseListener)`
+ `setOnGroupExpandListener(ExpandableListView.OnGroupExpandListener onGroupExpandListener)`



`ExpandableListAdapter`是一个接口，继承结构如下：

![017](https://github.com/winfredzen/Android-Basic/blob/master/UI/images/017.png)



`groupIndicator`不设置的话，有默认的显示

![018](https://github.com/winfredzen/Android-Basic/blob/master/UI/images/018.png)

也可以自己设置，展开和折叠的图片

```xml
<?xml version="1.0" encoding="utf-8"?>
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:drawable="@drawable/indicator_expand" android:state_expanded="true" />
    <item android:drawable="@drawable/indicator_collapse" />
</selector>
```

可以通过`android:indicatorLeft`和`android:indicatorRight`控制图片的宽度，但高度则无法控制

```xml
    android:groupIndicator="@drawable/group_indicator"
    android:indicatorLeft="10dp"
    android:indicatorRight="40dp"
```

![019](https://github.com/winfredzen/Android-Basic/blob/master/UI/images/019.png)



可见上面的效果并不理想，所以一般要自己自定义布局，将`android:groupIndicator="@null"`，设置为`@null`

























