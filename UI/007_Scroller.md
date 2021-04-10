# Scroller

在一些滚动的控件中经常看都有关于`Scroller`的使用，记录下学习的过程

[Scroller](https://developer.android.com/reference/android/widget/Scroller)官网对其的介绍：

> This class encapsulates scrolling. You can use scrollers (`Scroller` or `OverScroller`) to collect the data you need to produce a scrolling animation—for example, in response to a fling gesture. Scrollers track scroll offsets for you over time, but they don't automatically apply those positions to your view. It's your responsibility to get and apply new coordinates at a rate that will make the scrolling animation look smooth.
>
> Here is a simple example:
>
> ```java
>  private Scroller mScroller = new Scroller(context);
>  ...
>  public void zoomIn() {
>      // Revert any animation currently in progress
>      mScroller.forceFinished(true);
>      // Start scrolling by providing a starting point and
>      // the distance to travel
>      mScroller.startScroll(0, 0, 100, 0);
>      // Invalidate to request a redraw
>      invalidate();
>  }
> ```
>
> To track the changing positions of the x/y coordinates, use `computeScrollOffset()`. The method returns a boolean to indicate whether the scroller is finished. If it isn't, it means that a fling or programmatic pan operation is still in progress. You can use this method to find the current offsets of the x and y coordinates, for example:
>
> ```java
> if (mScroller.computeScrollOffset()) {
>      // Get current x and y positions
>      int currX = mScroller.getCurrX();
>      int currY = mScroller.getCurrY();
>     ...
>  }
> ```



不错的教程：

+ [Android Scroller完全解析，关于Scroller你所需知道的一切](https://blog.csdn.net/guolin_blog/article/details/48719871)

> `scrollBy()`方法是让`View`相对于当前的位置滚动某段距离，而`scrollTo()`方法则是让`View`相对于初始的位置滚动某段距离。

涉及到的类：

1.ViewConfiguration

[ViewConfiguration](https://developer.android.com/reference/android/view/ViewConfiguration) Contains methods to standard constants used in the UI for timeouts, sizes, and distances.

即定义了UI中所使用到的标准常量，像超时、尺寸、距离

```java
ViewConfiguration configure = ViewConfiguration.get(context); 
```



> `Scroller`的基本用法其实还是比较简单的，主要可以分为以下几个步骤：
>
> 1. 创建Scroller的实例
> 2. 调用`startScroll()`方法来初始化滚动数据并刷新界面
> 3. 重写`computeScroll()`方法，并在其内部完成平滑滚动的逻辑

























































