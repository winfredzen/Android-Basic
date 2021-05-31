# Android事件分发机

这部分内容来自[Android事件分发机制完全解析，带你从源码的角度彻底理解(上)](https://blog.csdn.net/guolin_blog/article/details/9097463)

如下的button注册的事件：

```java
button.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick execute");
    }
});
button.setOnTouchListener(new View.OnTouchListener() {
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.d(TAG, "onTouch execute, action " + event.getAction());
        return false;
    }
});
```

点击按钮时，Log输出的顺序可能如下：

```java
2021-05-31 10:36:23.523 32666-32666/com.example.eventdemo D/MainActivityTag: onTouch execute, action 0
2021-05-31 10:36:23.659 32666-32666/com.example.eventdemo D/MainActivityTag: onTouch execute, action 1
2021-05-31 10:36:23.661 32666-32666/com.example.eventdemo D/MainActivityTag: onClick execute
```

> 可见，先执行`onTouch`，再执行`onClick`



如果将`public boolean onTouch(View v, MotionEvent event)`方法，返回`true`（表示事件被`onTouch`消费掉了）

```java
2021-05-31 10:38:47.932 1178-1178/com.example.eventdemo D/MainActivityTag: onTouch execute, action 0
2021-05-31 10:38:48.029 1178-1178/com.example.eventdemo D/MainActivityTag: onTouch execute, action 1
```

> 此时，`onClick`方法则不再执行





1.对某个控件来说

> 触摸某个控件，调用其`dispatchTouchEvent`方法，如果该类没有这个方法，则调用其父类的`dispatchTouchEvent`方法。可能最终会在`View`类中找到`dispatchTouchEvent`方法
>
> `View`中`dispatchTouchEvent`方法的源码：
>
> ```java
> public boolean dispatchTouchEvent(MotionEvent event) {
>     if (mOnTouchListener != null && (mViewFlags & ENABLED_MASK) == ENABLED &&
>             mOnTouchListener.onTouch(this, event)) {
>         return true;
>     }
>     return onTouchEvent(event);
> }
> ```
>
> + `mOnTouchListener` - 在`setOnTouchListener`方法里赋值的，相当于注册了touch事件
> + `(mViewFlags & ENABLED_MASK) == ENABLED` - 控件是否是`enable`，按钮默认是`enable`
> + `mOnTouchListener.onTouch(this, event)`  - 即`onTouch`方法里返回`true`

