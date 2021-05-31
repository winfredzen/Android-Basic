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
>
> > `onTouch`能够得到执行需要两个前提条件，第一`mOnTouchListener`的值不能为空，第二当前点击的控件必须是`enable`的



> 结合上面Button的例子，和`dispatchTouchEvent`源码，可以发现
>
> `onTouch`事件里返回了`false`，就一定会进入到`onTouchEvent`方法中
>
> 如果在`onTouch`方法中通过返回`true`将事件消费掉（并且满足其他2个条件），`onTouchEvent`将不会再执行。
>
> 1. `onTouch`方法返回false，则`dispatchTouchEvent`调用`onTouchEvent`方法，在`onTouchEvent`方法中调用了`onClick`方法（`performClick()`方法里回调被点击控件的onClick方法）



























