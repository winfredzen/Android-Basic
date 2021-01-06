# 动画

在Android动画中，有2种类型的动画：

+ View Animation 视图动画
  + Tween Animation 补间动画
  + Frame Animation 逐帧动画
+ Property Animation 属性动画
  + ValueAnimation
  + ObjectAnimation

## 视图动画

视图动画标签

+ alpha
+ scale
+ translate
+ rotate
+ set - 动画集



在res目录下创建`anim`文件夹，创建xml文件，通过`R.anim.xxx`来引用这个文件

如下的`scale`动画，`scaleanim.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<scale xmlns:android="http://schemas.android.com/apk/res/android"
    android:fromXScale="0.0"
    android:toXScale="2.0"
    android:fromYScale="0.0"
    android:toYScale="2.0"
    android:duration="700">
</scale>
```

点击按钮开始动画

```java
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                TextView textView = (TextView) findViewById(R.id.textView);
                Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.scaleanim);//加载动画
                textView.startAnimation(animation);//开始动画
            }
        });
```

动画效果如下：

![001](https://github.com/winfredzen/Android-Basic/blob/master/Animation/images/001.gif)





### scale

+ `android:pivotX` - 缩放起点X轴坐标，可以是数值、百分数、百分数p
  + 数值 - 表示在当前的视图的左上角，即原点处加上某个数值`px`
  + 百分数 - 表示在当前的视图的左上角加上自己宽度的百分数
  + 百分数p -  表示在当前的视图的左上角加上自己父控件的宽度的百分数

+ `android:pivotY` - 同理



#### pivotX pivotY

`pivotX`和`pivotY`用于指定动画的起始点坐标



**1.取数值时**

```xml
<?xml version="1.0" encoding="utf-8"?>
<scale xmlns:android="http://schemas.android.com/apk/res/android"
    android:fromXScale="0.0"
    android:toXScale="2.0"
    android:fromYScale="0.0"
    android:toYScale="2.0"
    android:pivotX="50"
    android:pivotY="50"
    android:duration="700">
</scale>
```

![002](https://github.com/winfredzen/Android-Basic/blob/master/Animation/images/002.gif)



**2.百分数时**

```xml
<?xml version="1.0" encoding="utf-8"?>
<scale xmlns:android="http://schemas.android.com/apk/res/android"
    android:fromXScale="0.0"
    android:toXScale="2.0"
    android:fromYScale="0.0"
    android:toYScale="2.0"
    android:pivotX="50%"
    android:pivotY="50%"
    android:duration="700">
</scale>
```

![003](https://github.com/winfredzen/Android-Basic/blob/master/Animation/images/003.gif)



**3.为百分数p时**

```xml
<?xml version="1.0" encoding="utf-8"?>
<scale xmlns:android="http://schemas.android.com/apk/res/android"
    android:fromXScale="0.0"
    android:toXScale="2.0"
    android:fromYScale="0.0"
    android:toYScale="2.0"
    android:pivotX="50%p"
    android:pivotY="50%p"
    android:duration="700">
</scale>
```

![004](https://github.com/winfredzen/Android-Basic/blob/master/Animation/images/004.gif)



## Animation继承属性

+ `android:duration` - 动画持续时间，单位为毫秒



1.`android:fillAfter`保持动画结束时的状态

```xml
<?xml version="1.0" encoding="utf-8"?>
<scale xmlns:android="http://schemas.android.com/apk/res/android"
    android:fromXScale="0.0"
    android:toXScale="1.4"
    android:fromYScale="0.0"
    android:toYScale="1.4"
    android:pivotX="50%"
    android:pivotY="50%"
    android:duration="700"
    android:fillAfter="true">
</scale>
```

![005](https://github.com/winfredzen/Android-Basic/blob/master/Animation/images/005.gif)



2.`android:fillBefore`还原到初始化状态，不保留动画的任何痕迹

```xml
<?xml version="1.0" encoding="utf-8"?>
<scale xmlns:android="http://schemas.android.com/apk/res/android"
    android:fromXScale="0.0"
    android:toXScale="1.4"
    android:fromYScale="0.0"
    android:toYScale="1.4"
    android:pivotX="50%"
    android:pivotY="50%"
    android:duration="700"
    android:fillBefore="true">
</scale>
```

![006](https://github.com/winfredzen/Android-Basic/blob/master/Animation/images/006.gif)



3.`android:repeatCount`和`android:repeatMode`

+ `android:repeatCount` - 指定动画重复次数，取值为`infinite`时，表示无限循环
+ `android:repeatMode` - 指定重复的类型，必须与repeatCount一起使用才可以看到效果
  + `reverse` - 倒序回放
  + `restart` - 表示重复



```xml
<?xml version="1.0" encoding="utf-8"?>
<scale xmlns:android="http://schemas.android.com/apk/res/android"
    android:fromXScale="0.0"
    android:toXScale="1.4"
    android:fromYScale="0.0"
    android:toYScale="1.4"
    android:pivotX="50%"
    android:pivotY="50%"
    android:duration="700"
    android:fillBefore="true"
    android:repeatCount="1"
    android:repeatMode="restart">
</scale>
```

![007](https://github.com/winfredzen/Android-Basic/blob/master/Animation/images/007.gif)









