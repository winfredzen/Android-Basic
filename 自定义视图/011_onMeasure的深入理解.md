# onMeasure的深入理解

在自定义View的过程中，发现自己对`onMeasure`的理解还是比较浅薄，所以就深入学习下

先看下`View`中`onMeasure`是怎么调用的

![079](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/079.png)

> 通过该方法的注释，可以知道`onMeasure`是被`measure(int, int)`方法调用的
>
> 在override该方法时，必须要调用`setMeasuredDimension(int ,int)`方法，来存储该view的measured width 和 height。
>
> 如果重写该方法，子类需要确保measured width 和 height，至少是view的最小的width和height（通过`getSuggestedMinimumHeight()` 和 `getSuggestedMinimumWidth()`方法）

> `MeasureSpec`的概念
>
> ![080](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/080.png)
>
> 



`getDefaultSize`方法的实现

![080](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/080.png)



`View`中的`void measure(int widthMeasureSpec, int heightMeasureSpec)`方法调用了`onMeasure(widthMeasureSpec, heightMeasureSpec)`方法



而在`ViewGroup`的

```java
measureChild(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec)
 measureChildWithMargins(View child,
            int parentWidthMeasureSpec, int widthUsed,
            int parentHeightMeasureSpec, int heightUsed)  
```

方法中，调用了`View`的`measure`方法

![081](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/081.png)









































