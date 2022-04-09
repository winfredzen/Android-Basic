# ConstraintLayout

参考：

+ [ConstraintLayout](https://developer.android.com/reference/android/support/constraint/ConstraintLayout)
+ [Build a Responsive UI with ConstraintLayout](https://developer.android.com/training/constraint-layout)
+ [约束布局ConstraintLayout看这一篇就够了](https://www.jianshu.com/p/17ec9bd6ca8a)
+ [Android新特性介绍，ConstraintLayout完全解析](https://blog.csdn.net/guolin_blog/article/details/53122387)

**约束编辑器** 

![025](https://github.com/winfredzen/Android-Basic/raw/master/images/025.png)

**宽高设置**

![026](https://github.com/winfredzen/Android-Basic/raw/master/images/026.png)

![027](https://github.com/winfredzen/Android-Basic/raw/master/images/027.png)

## 居中和偏移

`ConstraintLayout`中居中的写法是

```kotlin
app:layout_constraintBottom_toBottomOf="parent"
app:layout_constraintLeft_toLeftOf="parent"
app:layout_constraintRight_toRightOf="parent"
app:layout_constraintTop_toTopOf="parent"
```

使用margin来设置偏移，如

```xml
android:layout_marginLeft="100dp"
```

## Constraint Bias

参考：

+ [Constraint Bias](https://www.raywenderlich.com/9193-constraintlayout-tutorial-for-android-getting-started)

> When a view is constrained on both sides horizontally or vertically, either to parent or other views, by default it has 0.5 or 50% constraint bias. In other words, the view stays in the center between the two edges to which it’s constrained.
> 
> Constraint bias ranges from 0.0 (0%) to 1.0 (100%). Horizontal constraint bias grows from left to right, while vertical constraint bias grows from top to bottom. Constraint bias is useful for positioning a view dynamically for different screen sizes.

也就是说当一个view在水平或者垂直，2个边都要约束的时候。不管是约束到parent还是其他的view，默认是0.5的constraint bias。也就是说view在2个edge的中间。

如下：

```xml
    <TextView
        android:id="@+id/textView"
        android:layout_width="100dp"
        android:layout_height="200dp"
        android:background="#00BCD4"
        android:text="TextView"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        android:layout_marginTop="100dp"
        app:layout_constraintHorizontal_bias="0.5"
        />
```

![069](https://github.com/winfredzen/Android-Basic/blob/master/%E5%9F%BA%E7%A1%80%E7%9F%A5%E8%AF%86/images/069.png)

如果`app:layout_constraintHorizontal_bias="0"`

![070](https://github.com/winfredzen/Android-Basic/blob/master/%E5%9F%BA%E7%A1%80%E7%9F%A5%E8%AF%86/images/070.png)

如果`app:layout_constraintHorizontal_bias="1"`

![071](https://github.com/winfredzen/Android-Basic/blob/master/%E5%9F%BA%E7%A1%80%E7%9F%A5%E8%AF%86/images/071.png)

可参考的文档：

+ [【约束布局】ConstraintLayout 偏移 ( Bias ) 计算方式详解 ( 缝隙比例 | 计算公式 | 图解 | 测量图 + 公式 )](https://blog.csdn.net/shulianghan/article/details/97102389)

**Align工具**

![009](https://github.com/winfredzen/Android-Basic/blob/master/基础知识/images/009.png)

如下的例子，TextView在ImageView和parent之间水平居中

```xml
    <ImageView
        android:id="@+id/imageView2"
        android:layout_width="46dp"
        android:layout_height="46dp"
        android:layout_marginStart="16dp"
        android:layout_marginTop="30dp"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:srcCompat="@drawable/track_icon" />

    <TextView
        android:id="@+id/textView2"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Raze Galactic"
        android:textAppearance="@style/TextAppearance.AppCompat.Headline"
        app:layout_constraintBottom_toBottomOf="@+id/imageView2"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.5"
        app:layout_constraintStart_toEndOf="@+id/imageView2"
        app:layout_constraintTop_toTopOf="@+id/imageView2" />
```

![010](https://github.com/winfredzen/Android-Basic/blob/master/基础知识/images/010.png)

## Chain

**生成Chain**

选择多个组件后，右键选择`Chains`

注意`Chain heads`

切换Chain的模式，也是右键，选择`Cycle Chain Mode`，例如：

```xml
 app:layout_constraintHorizontal_chainStyle="packed"
```

而且貌似只作用于`Chain heads`

可参考：

+ [Chains](https://constraintlayout.com/basics/create_chains.html)
+ [ConstraintLayout Tutorial for Android: Complex Layouts](https://www.raywenderlich.com/9475-constraintlayout-tutorial-for-android-complex-layouts)



以[ConstraintLayout Tutorial for Android: Complex Layouts](https://www.raywenderlich.com/9475-constraintlayout-tutorial-for-android-complex-layouts)中的教程为例

下面的三个icon使用的是Chain布局，如下：

![072](https://github.com/winfredzen/Android-Basic/blob/master/基础知识/images/072.png)

xml中的表示如下：

```xml
  <ImageView
      android:id="@+id/spaceStationIcon"
      android:layout_width="30dp"
      android:layout_height="30dp"
      android:layout_marginTop="15dp"
      android:src="@drawable/space_station_icon"
      app:layout_constraintEnd_toStartOf="@+id/flightsIcon"
      app:layout_constraintHorizontal_bias="0.5"
      app:layout_constraintHorizontal_chainStyle="spread"
      app:layout_constraintStart_toStartOf="parent"
      app:layout_constraintTop_toTopOf="parent" />
  
  
  
    <ImageView
      android:id="@+id/flightsIcon"
      android:layout_width="30dp"
      android:layout_height="30dp"
      android:src="@drawable/rocket_icon"
      app:layout_constraintBottom_toBottomOf="@+id/spaceStationIcon"
      app:layout_constraintEnd_toStartOf="@+id/roverIcon"
      app:layout_constraintHorizontal_bias="0.5"
      app:layout_constraintStart_toEndOf="@+id/spaceStationIcon"
      app:layout_constraintTop_toTopOf="@+id/spaceStationIcon" />
  
  
    <ImageView
      android:id="@+id/roverIcon"
      android:layout_width="30dp"
      android:layout_height="30dp"
      android:src="@drawable/rover_icon"
      app:layout_constraintBottom_toBottomOf="@+id/flightsIcon"
      app:layout_constraintEnd_toEndOf="parent"
      app:layout_constraintHorizontal_bias="0.5"
      app:layout_constraintStart_toEndOf="@+id/flightsIcon"
      app:layout_constraintTop_toTopOf="@+id/flightsIcon" />
```



## GuideLine

引导线在实际应用中还是有很大的作用的

还是上面例子中的教程，其创建了2个引导线

```xml
  <android.support.constraint.Guideline
      android:id="@+id/guideline1"
      android:layout_width="wrap_content"
      android:layout_height="wrap_content"
      android:orientation="horizontal"
      app:layout_constraintGuide_begin="200dp" />

  <android.support.constraint.Guideline
      android:id="@+id/guideline2"
      android:layout_width="wrap_content"
      android:layout_height="wrap_content"
      android:orientation="vertical"
      app:layout_constraintGuide_percent="0.05" />
```

![073](https://github.com/winfredzen/Android-Basic/blob/master/基础知识/images/073.png)



主要有如下的三个属性：

- *`layout_constraintGuide_begin`*: positions a guideline with a specific number of dp from the left (for vertical guides) or the top (for horizontal guides) of its parent.
- *`layout_constraintGuide_end`*: positions a guideline a specific number of dp from the right or bottom of its parent.
- *`layout_constraintGuide_percent`*: places a guideline at a percentage of the width or height of its parent.



## 其它

### 1.TextView左侧对齐

参考：

+ [How to align TextView with ConstraintLayout parent to the left](https://medium.com/tiendeo-tech/how-to-align-textview-with-constraintlayout-parent-to-the-left-4c0d25c1c819)



```xml
  <TextView
      android:id="@+id/addressTextView"
      android:layout_width="wrap_content"
      android:layout_height="wrap_content"
      android:layout_marginStart="16dp"
      android:layout_marginTop="8dp"
      android:layout_marginEnd="16dp"
      android:fontFamily="@font/opensans_regular"
      android:textColor="@color/blue"
      android:textSize="14sp"
      app:layout_constrainedWidth="true"
      app:layout_constraintEnd_toEndOf="parent"
      app:layout_constraintHorizontal_bias="0.0"
      app:layout_constraintStart_toStartOf="parent"
      app:layout_constraintTop_toBottomOf="@+id/supermarketTextView"
      tools:text="2100 88th St North Bergen, 07047" />
```












