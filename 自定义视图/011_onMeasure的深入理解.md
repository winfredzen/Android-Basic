# onMeasure的深入理解

在自定义View的过程中，发现自己对`onMeasure`的理解还是比较浅薄，所以就深入学习下

参考文章：

+ [一文理解 onMeasure -- 从 MeasureSpec 说起](https://juejin.cn/post/6962438735426224136#heading-2)



先看下`View`中`onMeasure`是怎么调用的?

![079](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/079.png)

> 通过该方法的注释，可以知道`onMeasure`是被`measure(int, int)`方法调用的
>
> 在override该方法时，必须要调用`setMeasuredDimension(int ,int)`方法，来存储该view的measured width 和 height。
>
> 如果重写该方法，子类需要确保measured width 和 height，至少是view的最小的width和height（通过`getSuggestedMinimumHeight()` 和 `getSuggestedMinimumWidth()`方法）

> `MeasureSpec`的概念
>
> ![083](https://github.com/winfredzen/Android-Basic/blob/master/自定义视图/images/083.png)
>
> `MeasureSpec` 的结构对于开发者来说应该很熟悉了，是一个 32 位 `Int` 整型。高 2 位是 `MODE`，低 30 位是 `SIZE`。
>
> `MODE` 有 3 种：
>
> - `EXACTLY`
>
>   表示当前 View 的尺寸为确切的值，这个值就是后 30 位 `SIZE` 的值。
>
> - `AT_MOST`
>
>   表示当前 View 的尺寸最大不能超过 `SIZE` 的值。
>
> - `UNSPECIFIED`
>
>   表示当前 View 的尺寸不受父 View 的限制，想要多大就可以多大。这种情况下，`SIZE` 的值意义不大。一般来说，可滑动的父布局对子 View 施加的约束就是 `UNSPECIFIED` ，比如 ScrollView 和 RecyclerView。在滑动时，实际上是让子 View 在它们的内部滚动，这意味着它们的子 View 的尺寸要大于父 View，所以父 View 不应该对子 View 施加尺寸的约束。



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

可从上面的代码中发现，子view的`childWidthMeasureSpec`和`childHeightMeasureSpec`，都是通过`getChildMeasureSpec`方法获取到的

```java
public static int getChildMeasureSpec(int spec, int padding, int childDimension)
```

+ `spec` ，这个参数指的是父 View 的 `measureSpec`，也就是调用子 View 的 `measure` 方法的那个父 View。当然，你可能会问，父 View 的 `measureSpec` 是从哪里来的？答案是父 View 的 父 View，这是一个链条。

+ `padding`，这个参数是父 View 的 padding 和子 View  的 margin 等值的和，在这里不是重点。

+ `childDimension`，这个指的就是我们 View 的宽度或者高度。也就是我们在 xml 设置的 `android:layout_height` 或者 `android:layout_widht`。`childDimension` 可能是一个具体的值，比如 `24dp`（当然，在 `measure` 过程中已经转为了对应的 `px` ），也可以是 `MATCH_PARENT` 或者 `WRAP_CONTENT`，这两者对应的值分别为 `-1` 和 -2，属于标记值。



 `getChildMeasureSpec` 方法，该方法的代码如下：

```java
public static int getChildMeasureSpec(int spec, int padding, int childDimension) {
        int specMode = MeasureSpec.getMode(spec);
        int specSize = MeasureSpec.getSize(spec);

        int size = Math.max(0, specSize - padding);

        int resultSize = 0;
        int resultMode = 0;

        switch (specMode) {
        // Parent has imposed an exact size on us
        case MeasureSpec.EXACTLY:
            if (childDimension >= 0) {
                resultSize = childDimension;
                resultMode = MeasureSpec.EXACTLY;
            } else if (childDimension == LayoutParams.MATCH_PARENT) {
                // Child wants to be our size. So be it.
                resultSize = size;
                resultMode = MeasureSpec.EXACTLY;
            } else if (childDimension == LayoutParams.WRAP_CONTENT) {
                // Child wants to determine its own size. It can't be
                // bigger than us.
                resultSize = size;
                resultMode = MeasureSpec.AT_MOST;
            }
            break;

        // Parent has imposed a maximum size on us
        case MeasureSpec.AT_MOST:
            if (childDimension >= 0) {
                // Child wants a specific size... so be it
                resultSize = childDimension;
                resultMode = MeasureSpec.EXACTLY;
            } else if (childDimension == LayoutParams.MATCH_PARENT) {
                // Child wants to be our size, but our size is not fixed.
                // Constrain child to not be bigger than us.
                resultSize = size;
                resultMode = MeasureSpec.AT_MOST;
            } else if (childDimension == LayoutParams.WRAP_CONTENT) {
                // Child wants to determine its own size. It can't be
                // bigger than us.
                resultSize = size;
                resultMode = MeasureSpec.AT_MOST;
            }
            break;

        // Parent asked to see how big we want to be
        case MeasureSpec.UNSPECIFIED:
            if (childDimension >= 0) {
                // Child wants a specific size... let him have it
                resultSize = childDimension;
                resultMode = MeasureSpec.EXACTLY;
            } else if (childDimension == LayoutParams.MATCH_PARENT) {
                // Child wants to be our size... find out how big it should
                // be
                resultSize = View.sUseZeroUnspecifiedMeasureSpec ? 0 : size;
                resultMode = MeasureSpec.UNSPECIFIED;
            } else if (childDimension == LayoutParams.WRAP_CONTENT) {
                // Child wants to determine its own size.... find out how
                // big it should be
                resultSize = View.sUseZeroUnspecifiedMeasureSpec ? 0 : size;
                resultMode = MeasureSpec.UNSPECIFIED;
            }
            break;
        }
        //noinspection ResourceType
        return MeasureSpec.makeMeasureSpec(resultSize, resultMode);
    }

```

> **为什么子 View 的 `MeasureSpec` 需要由父 View 共同确定？**
>
> 很大程度上是因为 `MATCH_PARENT` 和 `WRAP_CONTENT` 的存在，因为这两个 dimension 需要借助父 View 才能确定。`MATCH_PARENT` 需要知道父 View 有多大才能匹配到父 View 的大小；而 `WRAP_CONTENT` 虽然表示子 View 的尺寸由自己决定，但是这个大小不能超过父 View 的大小。
>
> 如果所有的 View 的 dimension 只能设置为固定的数值，那么其实子 View 的 `MeasureSpec` 就和父 View 无关了。正如上面代码中，当 `childDimension >= 0` 时，子 View 的 `MeasureSpec` 始终由 `childDimension` 和 `MeasureSpec.EXACTLY` 组成。
>
> **`AT_MOST` 和 `WRAP_CONTENT` 的关系**
>
> 网上有很多文章说，当一个 View 的尺寸设置为 `WRAP_CONTENT` 时，它的 `MeasureSpec.MODE` 就是 `AT_MOST`。这并不准确。首先，当父 View 的 MODE 是 `UNSPECIFIED` 时，子 View 设置为 `WRAP_CONTENT` 或 `MATCH_PARENT`，那么子 View 的 MODE 也都是 `UNSPECIIED` 而不是 `AT_MOST`。其次，当父 View 是 `AT_MOST` 的时候，子 View 的 `childDimension` 即使是 `MATCH_PARENT`, 子 View 的 MODE 也是`AT_MOST`。所以 `AT_MOST` 与 `WARP_CONTENT` 并不是一一对应的关系。
>
> 看起来有点乱，但是只要始终抓住关键点，**即 `AT_MOST` 意味着这个 View 的尺寸有上限，最大不能超过 `MeasureSpec.SIZE` 的值**。那具体的值是多少呢？这就要看在 `onMeasure` 中是如何设置 View 的尺寸了。对于一般的视图控件的 `onMeasure` 逻辑，当它的 `MeasureSpec.MODE` 是 `AT_MOST` 的时候，意味着它的大小就是包裹内容的大小，但是最大不能超过 `MeasureSpec.SIZE`，类似于给 View 同时设置 `WRAP_CONENT` 和 `maxHeight`/`maxWidth`。































