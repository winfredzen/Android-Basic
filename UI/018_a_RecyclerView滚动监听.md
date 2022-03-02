# RecyclerView滚动监听

RecyclerView的`setOnScrollListener`已经废弃，提示使用`addOnScrollListener(RecyclerView.OnScrollListener)`和`removeOnScrollListener(RecyclerView.OnScrollListener)`方法来代替



```java
public void onScrolled(RecyclerView recyclerView, int dx, int dy) 
```

对一个正常的竖直滚动的列表来说，向上滑动时，dy的值为正值

![053](https://github.com/winfredzen/Android-Basic/blob/master/UI/images/053.png)

向下滑动是，dy为负值

![054](https://github.com/winfredzen/Android-Basic/blob/master/UI/images/054.png)

































