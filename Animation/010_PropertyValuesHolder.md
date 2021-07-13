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

























