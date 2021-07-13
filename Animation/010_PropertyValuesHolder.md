# PropertyValuesHolder

`ObjectAnimator`中还有一个方法

```java
public static ObjectAnimator ofPropertyValuesHolder(Object target,
            PropertyValuesHolder... values)
```

+ target - 需要执行动画的控件
+ values - 可变长参数，可传入多个`PropertyValuesHolder`。`PropertyValuesHolder`实例会针对一个属性执行动画操作。如果传入多个，则会对控件的多个属性同时执行动画操作



`PropertyValuesHolder`类的含义是，它其中保存了动画过程中所需要操作的属性和对应的值。

我们通过`public static ObjectAnimator ofFloat(Object target, String propertyName, float... values)`构造的动画，其内部实现就是将传入的参数封装为`PropertyValuesHolder`实例来保存动画状态的



`PropertyValuesHolder`常用的一些方法：

```java
public static PropertyValuesHolder ofInt(String propertyName, int... values)
public static PropertyValuesHolder ofFloat(String propertyName, float... values)
public static PropertyValuesHolder ofObject(String propertyName, TypeEvaluator evaluator,
            Object... values)
public static PropertyValuesHolder ofKeyframe(String propertyName, Keyframe... values)  
```

+ propertyName - 表示ObjectAnimator需要操作的属性名。即ObjectAnimator需要通过反射查找对应属性的`setProperty()`函数
+ values - 属性对应的参数，是可变长参数，可以指定多个。如果只指定了一个，会通过查找对应属性的`getProperty()`函数来获得初始值



如下的例子：

```java
PropertyValuesHolder rotationHolder = PropertyValuesHolder.ofFloat("rotation", 60f, -60f, 40f, -40f, -20f, 20f, 10f, -10f, 0);
PropertyValuesHolder alphaHolder = PropertyValuesHolder.ofFloat("alpha", 0.1f, 1f, 0.1f, 1f);
ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(mTextView, rotationHolder, alphaHolder);
animator.setDuration(3000);
animator.start();
```

![038](https://github.com/winfredzen/Android-Basic/blob/master/Animation/images/038.gif)



## Keyframe

关键帧表示的是某个物体在哪个时间点应该在哪个位置上

```java
public static Keyframe ofFloat(float fraction, float value) 
```

+ fraction - 表示当前的显示进度，即在插值器中`getInterpolation()`函数的返回值
+ value - 表示动画当前所在的数值位置

```java
public static PropertyValuesHolder ofKeyframe(String propertyName, Keyframe... values)
```

+ propertyName - 动画所要操作的属性名
+ values - Keyframe的列表



如下的电话响铃效果：

```java
                /**
                 * 左右震动效果
                 */
                Keyframe frame0 = Keyframe.ofFloat(0f, 0);
                Keyframe frame1 = Keyframe.ofFloat(0.1f, -20f);
                Keyframe frame2 = Keyframe.ofFloat(0.2f, 20f);
                Keyframe frame3 = Keyframe.ofFloat(0.3f, -20f);
                Keyframe frame4 = Keyframe.ofFloat(0.4f, 20f);
                Keyframe frame5 = Keyframe.ofFloat(0.5f, -20f);
                Keyframe frame6 = Keyframe.ofFloat(0.6f, 20f);
                Keyframe frame7 = Keyframe.ofFloat(0.7f, -20f);
                Keyframe frame8 = Keyframe.ofFloat(0.8f, 20f);
                Keyframe frame9 = Keyframe.ofFloat(0.9f, -20f);
                Keyframe frame10 = Keyframe.ofFloat(1, 0);
                PropertyValuesHolder frameHolder1 = PropertyValuesHolder.ofKeyframe("rotation", frame0, frame1, frame2, frame3, frame4, frame5, frame6, frame7, frame8, frame9, frame10);


                /**
                 * scaleX放大1.1倍
                 */
                Keyframe scaleXframe0 = Keyframe.ofFloat(0f, 1);
                Keyframe scaleXframe1 = Keyframe.ofFloat(0.1f, 1.1f);
                Keyframe scaleXframe9 = Keyframe.ofFloat(0.9f, 1.1f);
                Keyframe scaleXframe10 = Keyframe.ofFloat(1, 1);
                PropertyValuesHolder frameHolder2 = PropertyValuesHolder.ofKeyframe("ScaleX", scaleXframe0, scaleXframe1, scaleXframe9, scaleXframe10);


                /**
                 * scaleY放大1.1倍
                 */
                Keyframe scaleYframe0 = Keyframe.ofFloat(0f, 1);
                Keyframe scaleYframe1 = Keyframe.ofFloat(0.1f, 1.1f);
                Keyframe scaleYframe9 = Keyframe.ofFloat(0.9f, 1.1f);
                Keyframe scaleYframe10 = Keyframe.ofFloat(1, 1);
                PropertyValuesHolder frameHolder3 = PropertyValuesHolder.ofKeyframe("ScaleY", scaleYframe0, scaleYframe1, scaleYframe9, scaleYframe10);

                /**
                 * 构建动画
                 */
                Animator animator = ObjectAnimator.ofPropertyValuesHolder(mImageView, frameHolder1, frameHolder2, frameHolder3);
                animator.setDuration(1000);
                animator.start();
```

![039](https://github.com/winfredzen/Android-Basic/blob/master/Animation/images/039.gif)



从中可以看到，借助`Keyframe`， 不需要使用`AnimationSet`，也可以实现多个动画同时播放。这也是`ObjectAnimator`中唯一一个能实现多动画同时播放的方法























































