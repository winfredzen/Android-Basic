# ObjectAnimator

`ValueAnimator`有一个缺点，就是只能对动画中的数值进行计算。如果想对哪个控件执行操作，就需要对`ValueAnimator`的动画过程进行监听

`ObjectAnimator`继承自`ValueAnimator`

如下的例子，改变透明度动画：

```java
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ObjectAnimator animator = ObjectAnimator.ofFloat(tv, "alpha", 1, 0, 1);
                animator.setDuration(5000);
                animator.start();

            }
        });
```

效果为：

![027](https://github.com/winfredzen/Android-Basic/blob/master/Animation/images/027.gif)

> 上面的构造方法
>
> ```java
> public static ObjectAnimator ofFloat(Object target, String propertyName, float... values)
> ```
>
> + target - 指定动画要操作哪个控件
> + propertyName - 动画要操作哪个属性
> + values - 指这个属性值如何变化

再如下的旋转动画：

```java
ObjectAnimator animator = ObjectAnimator.ofFloat(tv, "rotation", 0, 180, 0);
animator.setDuration(2000);
animator.start();
```

效果为：

![028](https://github.com/winfredzen/Android-Basic/blob/master/Animation/images/028.gif)



> 上面的`rotation`参数，`TextView`、`View`都没有这个属性，它是通过`set`函数来改变的



这些`set`方法都是从`View`中继承过来的

```java
setRotation(float rotation)
setRotationX(float rotationX)
setRotationY(float rotationY)
  
setAlpha(@FloatRange(from=0.0, to=1.0) float alpha)
  
setTranslationX(float translationX) 
setTranslationY(float translationY)
  
setScaleX(float scaleX)
setScaleY(float scaleY)
```



另外还有一个构造方法：

```java
public static ObjectAnimator ofObject(Object target, String propertyName,
            TypeEvaluator evaluator, Object... values)
```

> 这里可以指定一个`evaluator`





















