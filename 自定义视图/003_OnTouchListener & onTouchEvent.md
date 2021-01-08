# OnTouchListener & onTouchEvent

内容来自：

+ [Android Touch事件传递机制(二) -- OnTouchListener & OnClickListener & OnLongClickListener](https://daemon369.github.io/android/2014/08/25/android-OnTouchListener-OnClickListener-OnLongClickListener)

首先我们来看看`View`的`OnTouchListener`与`onTouchEvent`方法的区别与联系，如下布局：

```xml
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <com.daemon.demo.touchdemo.widget.MyView
        android:id="@+id/my_view"
        android:layout_width="100dp"
        android:layout_height="100dp"
        android:background="#00ff00"/>

</RelativeLayout>
```

设置`OnTouchListener`：

```java
MyView view = (MyView) findViewById(R.id.my_view);

view.setOnTouchListener(new View.OnTouchListener() {
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        boolean ret = false;
        Log.e("Test5Activity", "onTouch:" + ret + " action:" + TouchUtil.actionToString(event.getAction()));
        return ret;
    }
});
```

运行demo，在`MyView`上按下触屏并滑动后松开，查看log：

```java
E/Test5Activity﹕ onTouch:false action:ACTION_DOWN
E/MyView﹕ onTouchEvent:false action:ACTION_DOWN
```

**可以看到，设置了`OnTouchListener`后，`MyView`收到down事件后首先回调`OnTouchListener`的`onTouch`方法，然后才会将down事件分发给`MyView`的`onTouchEvent`方法处理；由于`onTouchEvent`方法返回`false`，后续事件将不再分发给MyView。**

`onTouch`方法默认返回false。那么我们返回true再运行一次试试：

```java
E/Test5Activity﹕ onTouch:true action:ACTION_DOWN
E/Test5Activity﹕ onTouch:true action:ACTION_MOVE
E/Test5Activity﹕ onTouch:true action:ACTION_MOVE

......

E/Test5Activity﹕ onTouch:true action:ACTION_MOVE
E/Test5Activity﹕ onTouch:true action:ACTION_UP
```

可以看到，当`onTouch`方法返回`true`时，`MyView`的`touch`事件将不再分发给其`onTouchEvent`方法。我们看下`OnTouchListener`和`onTouch`的解释：

```java
/**
 * Interface definition for a callback to be invoked when a touch event is
 * dispatched to this view. The callback will be invoked before the touch
 * event is given to the view.
 */
public interface OnTouchListener {
    /**
     * Called when a touch event is dispatched to a view. This allows listeners to
     * get a chance to respond before the target view.
     *
     * @param v The view the touch event has been dispatched to.
     * @param event The MotionEvent object containing full information about
     *        the event.
     * @return True if the listener has consumed the event, false otherwise.
     */
    boolean onTouch(View v, MotionEvent event);
}
```

设置`OnTouchListener`回调之后，touch事件在分发给view之后，我们将有机会在目标view之前对这个touch事件进行响应；

**`onTouch`方法返回`true`表示这个touch事件已被消费掉，这就意味着touch事件不会再继续分发给目标view。**



## OnTouchListener & OnClickListener

对`MyView`设置`OnTouchListener` 和 `OnClickListener`，`onTouch`方法返回`false`：

```java
MyView view = (MyView) findViewById(R.id.my_view);

view.setOnTouchListener(new View.OnTouchListener() {
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        boolean ret;
        ret = false;
//        ret = true;
        Log.e("Test5Activity", "onTouch:" + ret + " action:" + TouchUtil.actionToString(event.getAction()));
        return ret;
    }
});

view.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Log.e("Test5Activity", "onClick");
    }
});
```

在按钮上按下触屏、移动、放开触屏，查看log：

```java
E/Test5Activity﹕ onTouch:false action:ACTION_DOWN
E/MyView﹕ onTouchEvent:true action:ACTION_DOWN
E/Test5Activity﹕ onTouch:false action:ACTION_MOVE
E/MyView﹕ onTouchEvent:true action:ACTION_MOVE
E/Test5Activity﹕ onTouch:false action:ACTION_MOVE
E/MyView﹕ onTouchEvent:true action:ACTION_MOVE

......

E/Test5Activity﹕ onTouch:false action:ACTION_MOVE
E/MyView﹕ onTouchEvent:true action:ACTION_MOVE
E/Test5Activity﹕ onTouch:false action:ACTION_UP
E/MyView﹕ onTouchEvent:true action:ACTION_UP
E/Test5Activity﹕ onClick
```

可以看到，`onTouch`方法被调用了多次，分别对应了按下(`ACTION_DOWN`)、移动(`ACTION_MOVE`)、松开(`ACTION_UP`)等事件；`onClick`事件发生在`ACTION_UP`之后，只回调一次。

`onTouch`方法返回`true`再看下log：

```java
08-20 01:25:18.056: E/TouchTest(658): onTouch -> ACTION_DOWN
08-20 01:25:18.065: E/TouchTest(658): onTouch -> ACTION_MOVE
08-20 01:25:18.100: E/TouchTest(658): onTouch -> ACTION_MOVE
08-20 01:25:18.446: E/TouchTest(658): onTouch -> ACTION_MOVE
08-20 01:25:18.508: E/TouchTest(658): onTouch -> ACTION_MOVE
08-20 01:25:18.656: E/TouchTest(658): onTouch -> ACTION_UP
```

可以看到，**`onClick`方法不再被调用**。这是因为在touch事件分发给`onClick`方法之前，被`onTouch`方法拦截并消费掉，因此不再分发给`onClick`方法。



## OnLongClickListener

再来看看`OnLongClickListener`与`OnTouchListener` 和 OnClickListener的调用时序关系。

先看下`OnLongClickListener`的注释：

```java
/**
 * Interface definition for a callback to be invoked when a view has been clicked and held.
 */
public interface OnLongClickListener {
    /**
     * Called when a view has been clicked and held.
     *
     * @param v The view that was clicked and held.
     *
     * @return true if the callback consumed the long click, false otherwise.
     */
    boolean onLongClick(View v);
}
```

`OnLongClickListener`接口定义了长按触屏的回调，当我们在一个view上点击并保持一段时间后，就会唤起这个回调。`onLongClick`返回`true`表示已经消费了这次长按，否则返回`false`。

给`MyView`设置`OnLongClickListener`，`onLongClick`方法默认返回`false`：

```java
view.setOnLongClickListener(new View.OnLongClickListener() {
    @Override
    public boolean onLongClick(View v) {
        boolean ret;
        ret = false;
//        ret = true;
        Log.e("Test5Activity", "onLongClick:" + ret);
        return ret;
    }
});
```

在`MyView`上按下触屏并滑动，一段时间后松开触屏：

```java
E/Test5Activity﹕ onTouch:false action:ACTION_DOWN
E/Test5Activity﹕ onTouch:false action:ACTION_MOVE
E/Test5Activity﹕ onTouch:false action:ACTION_MOVE

......

E/Test5Activity﹕ onTouch:false action:ACTION_MOVE
E/Test5Activity﹕ onLongClick:false
E/Test5Activity﹕ onTouch:false action:ACTION_MOVE

......

E/Test5Activity﹕ onTouch:false action:ACTION_MOVE
E/Test5Activity﹕ onTouch:false action:ACTION_UP
E/Test5Activity﹕ onClick
```

可以看出，`onClick`在触屏抬起后触发；`onLongClick`方法在触屏抬起前触发，并需要手指按下超过一定时间；滑动手指不影响点击与长按事件的触发。

`onLongClick`方法返回`true`再看下：

```java
E/Test5Activity﹕ onTouch:false action:ACTION_DOWN
E/Test5Activity﹕ onTouch:false action:ACTION_MOVE
E/Test5Activity﹕ onTouch:false action:ACTION_MOVE

......

E/Test5Activity﹕ onTouch:false action:ACTION_MOVE
E/Test5Activity﹕ onLongClick:true
E/Test5Activity﹕ onTouch:false action:ACTION_MOVE
E/Test5Activity﹕ onTouch:false action:ACTION_MOVE

......

E/Test5Activity﹕ onTouch:false action:ACTION_MOVE
E/Test5Activity﹕ onTouch:false action:ACTION_UP
```

可以看到，`onLongClick`方法返回`true`后，`onClick`方法就不再触发了，这是因为`onLongClick`消费了长按事件。



## 总结

1. 设置`OnTouchListener:onTouch`方法返回`false`时，`onTouch`方法及`View`的`onTouchEvent`方法依次被调用；`onTouch`方法返回`true`时，只调用`onTouch`方法，`onTouchEvent`方法不再被调用
2. 设置`OnTouchListener`后`onTouch`方法返回`false`，不影响`OnClickListener`及`OnLongClickListener`的触发；`onTouch`方法返回`true`时，`OnClickListener`及`OnLongClickListener`不再触发
3. `OnClickListener`的触发条件是手指从触屏抬起；`OnLongClickListener`的触发条件是按下触屏且停留一段事件
4. `onLongClick`方法返回`false`不影响`OnClickListener`的触发；`onLongClick`方法返回true，`OnClickListener`不再触发





























































































