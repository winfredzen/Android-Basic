# ConstraintLayout

参考：

+ [ConstraintLayout](https://developer.android.com/reference/android/support/constraint/ConstraintLayout)
+ [Build a Responsive UI with ConstraintLayout](https://developer.android.com/training/constraint-layout)
+ [约束布局ConstraintLayout看这一篇就够了](https://juejin.im/post/5bac92f2f265da0aba70c1bf)
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

>When a view is constrained on both sides horizontally or vertically, either to parent or other views, by default it has 0.5 or 50% constraint bias. In other words, the view stays in the center between the two edges to which it’s constrained.
>
>Constraint bias ranges from 0.0 (0%) to 1.0 (100%). Horizontal constraint bias grows from left to right, while vertical constraint bias grows from top to bottom. Constraint bias is useful for positioning a view dynamically for different screen sizes.

也就是说当一个view在水平或者垂直，2个边都要约束的时候。不管是约束到parent还是其他的view，默认是0.5的constraint bias。也就是说view在2个edge的中间。

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











