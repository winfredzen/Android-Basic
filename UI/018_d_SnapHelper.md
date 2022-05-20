# SnapHelper

[SnapHelper](https://developer.android.com/reference/androidx/recyclerview/widget/SnapHelper)，相当于是RecyclerView的滑动后的对齐工具

如果不使用SnapHelper，下面的RecyclerView滑动后的对齐可能如下所示：

![064](https://github.com/winfredzen/Android-Basic/blob/master/UI/images/064.png)

SnapHelper是一个抽象类

```java
public abstract class SnapHelper extends OnFlingListener
```



## LinearSnapHelper

`LinearSnapHelper`能够在滑动后将最靠近中间线的item移动到中间线的位置

使用方式：

```java
LinearSnapHelper linearSnapHelper = new LinearSnapHelper();
linearSnapHelper.attachToRecyclerView(mRecyclerView);
```

![065](https://github.com/winfredzen/Android-Basic/blob/master/UI/images/065.png)



## PagerSnapHelper

`LinearSnapHelper`一次可以滑动多个item，而`PagerSnapHelper`则像翻页一样，一个item一个item地滑动

```java
PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
pagerSnapHelper.attachToRecyclerView(mRecyclerView);
```

效果和上面类似



## 自定义

SnapHelper有如下的几个抽象方法

```java
public abstract View findSnapView(LayoutManager var1);
```

> 作用是找到当前的snapView（需要对齐的View）并返回，在滑动后，距离指定对齐位置最近的View就是对齐的View，也就是snapView

```java
public abstract int[] calculateDistanceToFinalSnap(@NonNull LayoutManager var1, @NonNull View targetView);
```

> 返回一个二维数组，分别表示在x轴和y轴方向上需要修正的偏移量。targetView就是上面的findSnapView返回的view，即当前离对齐位置最近的View、需要对齐的View。
>
> calculateDistanceToFinalSnap返回的值就是，这个snapView在当前的滑动距离的基础上需要校验的量，以使这个View在滑动后能刚好对齐到指定的位置

```java
public abstract int findTargetSnapPosition(LayoutManager var1, int velocityX, int velocityY);
```

> 根据速度找到snapView的position并返回



如下的一个`StartSnapHelper`，使item都左对齐

```java
public class StartSnapHelper extends LinearSnapHelper {
    private static final String TAG = "StartSnapHelper";

    private OrientationHelper mHorizontalHelper, mVerticalHelper;

    @Nullable
    @Override
    public int[] calculateDistanceToFinalSnap(RecyclerView.LayoutManager layoutManager, View targetView) {
        Log.d(TAG, "calculateDistanceToFinalSnap");
        int[] out = new int[2];
        if (layoutManager.canScrollHorizontally()) {
            out[0] = distanceToStart(targetView, getHorizontalHelper(layoutManager));
        } else {
            out[0] = 0;
        }
        if (layoutManager.canScrollVertically()) {
            out[1] = distanceToStart(targetView, getVerticalHelper(layoutManager));
        } else {
            out[1] = 0;
        }
        return out;
    }

    private int distanceToStart(View targetView, OrientationHelper helper) {
        /**
         * 1.getStartAfterPadding - 获取RecycleView左侧内边距（paddingLeft）
         * 2.getDecoratedStart - 子View左边界点到父View的（0，0）点的水平间距
         */
        Log.d(TAG,"getDecoratedStart："+helper.getDecoratedStart(targetView)+
        "   helper.getStartAfterPadding()"+helper.getStartAfterPadding());
        return helper.getDecoratedStart(targetView) - helper.getStartAfterPadding();
    }

    @Nullable
    @Override
    public View findSnapView(RecyclerView.LayoutManager layoutManager) {
        Log.d(TAG, "findSnapView");
        if (layoutManager instanceof LinearLayoutManager) {

            if (layoutManager.canScrollHorizontally()) {
                return findStartView(layoutManager, getHorizontalHelper(layoutManager));
            } else {
                return findStartView(layoutManager, getVerticalHelper(layoutManager));
            }
        }

        return super.findSnapView(layoutManager);
    }



    private View findStartView(RecyclerView.LayoutManager layoutManager,
                               OrientationHelper helper) {
        if (layoutManager instanceof LinearLayoutManager) {
            int firstChild = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
            //需要判断是否是最后一个Item，如果是最后一个则不让对齐，以免出现最后一个显示不完全。
            boolean isLastItem = ((LinearLayoutManager) layoutManager)
                    .findLastCompletelyVisibleItemPosition()
                    == layoutManager.getItemCount() - 1;

            if (firstChild == RecyclerView.NO_POSITION || isLastItem) {
                return null;
            }

            View child = layoutManager.findViewByPosition(firstChild);

            /**
             * 1.getDecoratedEnd - 子View右边界点到父View的（0，0）点的水平间距
             * 2.getDecoratedMeasurement - view在水平方向上所占位置的大小（包括view的左右外边距）
             *
             *
             */
            if (helper.getDecoratedEnd(child) >= helper.getDecoratedMeasurement(child) / 2
                    && helper.getDecoratedEnd(child) > 0) {
                return child;
            } else {
                if (((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition()
                        == layoutManager.getItemCount() - 1) {
                    return null;
                } else {
                    return layoutManager.findViewByPosition(firstChild + 1);
                }
            }
        }

        return super.findSnapView(layoutManager);
    }


    private OrientationHelper getHorizontalHelper(
            @NonNull RecyclerView.LayoutManager layoutManager) {
        if (mHorizontalHelper == null) {
            mHorizontalHelper = OrientationHelper.createHorizontalHelper(layoutManager);
        }
        return mHorizontalHelper;
    }

    private OrientationHelper getVerticalHelper(RecyclerView.LayoutManager layoutManager) {
        if (mVerticalHelper == null) {
            mVerticalHelper = OrientationHelper.createVerticalHelper(layoutManager);
        }
        return mVerticalHelper;

    }
}
```

这里面用用到的OrientationHelper，参考：

+ [Android源码分析之OrientationHelper详解](https://blog.csdn.net/chenbaige/article/details/80524508)



效果如下：

![066](https://github.com/winfredzen/Android-Basic/blob/master/UI/images/066.png)















