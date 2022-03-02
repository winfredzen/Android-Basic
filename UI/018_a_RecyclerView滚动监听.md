# RecyclerView滚动监听

RecyclerView的`setOnScrollListener`已经废弃，提示使用`addOnScrollListener(RecyclerView.OnScrollListener)`和`removeOnScrollListener(RecyclerView.OnScrollListener)`方法来代替



```java
public void onScrollStateChanged(RecyclerView recyclerView, int newState)
```

```java
/**
 * The RecyclerView is not currently scrolling.（静止没有滚动）
 */
public static final int SCROLL_STATE_IDLE = 0;

/**
 * The RecyclerView is currently being dragged by outside input such as user touch input.
 *（正在被外部拖拽,一般为用户正在用手指滚动）
 */
public static final int SCROLL_STATE_DRAGGING = 1;

/**
 * The RecyclerView is currently animating to a final position while not under outside control.
 *（自动滚动）
 */
public static final int SCROLL_STATE_SETTLING = 2;
```



```java
public void onScrolled(RecyclerView recyclerView, int dx, int dy) 
```

对一个正常的竖直滚动的列表来说，向上滑动时，dy的值为正值

![053](https://github.com/winfredzen/Android-Basic/blob/master/UI/images/053.png)

向下滑动是，dy为负值

![054](https://github.com/winfredzen/Android-Basic/blob/master/UI/images/054.png)



## 判断滚动到底部

1.使用`linearLayoutManager.findLastCompletelyVisibleItemPosition()`

```java
@Override
public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
    super.onScrolled(recyclerView, dx, dy);
    Log.d(TAG, "dx = " + dx + ", dy = " + dy);
    if(linearLayoutManager.findLastCompletelyVisibleItemPosition() == mDatas.size() -1){
        //bottom of list!
        Log.d(TAG, "Last");
    }
}
```

验证了一下，基本可以

> `LinearLayoutManager#findLastVisibleItemPosition()` is a better alternative to `LinearLayoutManager#findLastCompletelyVisibleItemPosition()`, especially when your items are longer than the window height.
>
> 使用`LinearLayoutManager#findLastVisibleItemPosition()`更好



2.使用 `recyclerView.canScrollVertically(1)`

这种方式，本人没有验证



3.通过item数量来判断

第一个可见item的位置 + 当前可见的item个数 >= item的总个数

![055](https://github.com/winfredzen/Android-Basic/blob/master/UI/images/055.png)

![056](https://github.com/winfredzen/Android-Basic/blob/master/UI/images/056.png)

![057](https://github.com/winfredzen/Android-Basic/blob/master/UI/images/057.png)

大致的实现如下：

![058](https://github.com/winfredzen/Android-Basic/blob/master/UI/images/058.png)



































