# 自定义LayoutManager

自定义LayoutManager可以实现一些特殊的效果

下面的例子，类似于实现一个`LinearLayoutManager`

需要实现的一些方法：

1.`generateDefaultLayoutParams()`

如`LinearLayoutManager`中的`generateDefaultLayoutParams()`

![062](https://github.com/winfredzen/Android-Basic/blob/master/UI/images/062.png)

`-2`其实就是`WRAP_CONTENT`

```java
public static final int WRAP_CONTENT = -2;
```



2.`onLayoutChildren()`

所有item的布局都是在`onLayoutChildren()`函数中处理的



3.滑动效果

`canScrollVertically()`表示是否具有垂直滑动的功能

`canScrollHorizontally()`表示是否具有水平滑动的功能

```java
int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state)
```

dy表示的是手指在屏幕上每次滑动的位移：

+ 当手指由下往上滑动时，dy > 0，即往上滑动，需要加载更多的item，所有的item需要向上移动
+ 当手指由上往下滑动时，dy < 0，即往下滑动，需要显示以前显示的item，所有的item需要向下移动



`CustomLayoutManager`代码如下：

```java
public class CustomLayoutManager extends LayoutManager {
    private static final String TAG = "CustomLayoutManager";

    private int mSumDy = 0;
    private int mTotalHeight = 0;

    @Override
    public LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT,
                RecyclerView.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        //定义竖直方向的偏移量
        int offsetY = 0;
        for (int i = 0; i < getItemCount(); i++) {
            View view = recycler.getViewForPosition(i);
            addView(view);
            //测量这个View，得到宽度 和 高度
            measureChildWithMargins(view, 0, 0);
            int width = getDecoratedMeasuredWidth(view);
            int height = getDecoratedMeasuredHeight(view);
            //将每个item拜访在对应的位置
            layoutDecorated(view, 0, offsetY, width, offsetY + height);
            offsetY += height;
        }

        //如果所有子View的高度和没有填满RecyclerView的高度，
        // 则将高度设置为RecyclerView的高度
        mTotalHeight = Math.max(offsetY, getVerticalSpace());
    }

    private int getVerticalSpace() {
        return getHeight() - getPaddingBottom() - getPaddingTop();
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        Log.d(TAG, "scrollVerticallyBy dy = " + dy);
        int travel = dy;
        //如果滑动到最顶部
        if (mSumDy + dy < 0) {
            travel = -mSumDy;
        } else if (mSumDy + dy > mTotalHeight - getVerticalSpace()) {
            //如果滑动到最底部
            travel = mTotalHeight - getVerticalSpace() - mSumDy;
        }

        mSumDy += travel;
        // 平移容器内的item
        offsetChildrenVertical(-travel);
        return dy;
    }
}

```



效果如下：

![063](https://github.com/winfredzen/Android-Basic/blob/master/UI/images/063.png)









