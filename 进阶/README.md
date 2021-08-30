# Android事件分发机制

## View事件分发

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
>  if (mOnTouchListener != null && (mViewFlags & ENABLED_MASK) == ENABLED &&
>          mOnTouchListener.onTouch(this, event)) {
>      return true;
>  }
>  return onTouchEvent(event);
> }
> ```
>
> + `mOnTouchListener` - 在`setOnTouchListener`方法里赋值的，相当于注册了touch事件
> + `(mViewFlags & ENABLED_MASK) == ENABLED` - 控件是否是`enable`，按钮默认是`enable`
> + `mOnTouchListener.onTouch(this, event)`  - 即`onTouch`方法里返回`true`
>
> > `onTouch`能够得到执行需要两个前提条件，第一`mOnTouchListener`的值不能为空，第二当前点击的控件必须是`enable`的
>
> 首先在`dispatchTouchEvent`中最先执行的就是`onTouch`方法，因此`onTouch`肯定是要优先于`onClick`执行的，也是印证了刚刚的打印结果。而如果在`onTouch`方法里返回了`true`，就会让`dispatchTouchEvent`方法直接返回`true`，不会再继续往下执行。而打印结果也证实了如果`onTouch`返回true，`onClick`就不会再执行了



> 结合上面Button的例子，和`dispatchTouchEvent`源码，可以发现
>
> `onTouch`事件里返回了`false`，就一定会进入到`onTouchEvent`方法中
>
> 如果在`onTouch`方法中通过返回`true`将事件消费掉（并且满足其他2个条件），`onTouchEvent`将不会再执行。
>
> 1. `onTouch`方法返回false，则`dispatchTouchEvent`调用`onTouchEvent`方法，在`onTouchEvent`方法中调用了`onClick`方法（`performClick()`方法里回调被点击控件的onClick方法）



**1. onTouch和onTouchEvent有什么区别，又该如何使用？**

> 从源码中可以看出，这两个方法都是在`View`的`dispatchTouchEvent`中调用的，`onTouch`优先于`onTouchEvent`执行。如果在`onTouch`方法中通过返回`true`将事件消费掉，`onTouchEvent`将不会再执行。
>
>  
>
> 另外需要注意的是，`onTouch`能够得到执行需要两个前提条件，第一`mOnTouchListener`的值不能为空，第二当前点击的控件必须是`enable`的。因此如果你有一个控件是非`enable`的，那么给它注册`onTouch`事件将永远得不到执行。对于这一类控件，如果我们想要监听它的`touch`事件，就必须通过在该控件中重写`onTouchEvent`方法来实现。
>







## ViewGroup事件分发

这部分内容来自[Android事件分发机制完全解析，带你从源码的角度彻底理解(下)](https://blog.csdn.net/guolin_blog/article/details/9153747)

`ViewGroup`同样继承自`View`

如下的例子，自定义一个`MyLayout`，继承自`LinearLayout`

```java
public class MyLayout extends LinearLayout {

    public MyLayout(Context context) {
        super(context);
    }

    public MyLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
}
```

在`MyLayout`布局中添加2个按钮，并创建如下的事件：

```java
myLayout.setOnTouchListener(new View.OnTouchListener() {
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.d("TAG", "myLayout on touch");
        return false;
    }
});
button1.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Log.d("TAG", "You clicked button1");
    }
});
button2.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Log.d("TAG", "You clicked button2");
    }
});
```

点击button1，输出：

```java
2021-05-31 11:34:07.715 16721-16721/com.example.eventdemo D/TAG: You clicked button1
```

点击button2，输出：

```java
2021-05-31 11:34:09.517 16721-16721/com.example.eventdemo D/TAG: You clicked button2
```

点击空白区域：

```java
2021-05-31 11:34:11.583 16721-16721/com.example.eventdemo D/TAG: myLayout on touch
```



> 可见，点击按钮时，`myLayout`的`onTouch`方法并不会执行，可以理解为按钮的`onClick`方法将事件消费掉了



`ViewGroup`中有一个`onInterceptTouchEvent`方法，可以拦截事件。

```java
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.isFromSource(InputDevice.SOURCE_MOUSE)
                && ev.getAction() == MotionEvent.ACTION_DOWN
                && ev.isButtonPressed(MotionEvent.BUTTON_PRIMARY)
                && isOnScrollbarThumb(ev.getX(), ev.getY())) {
            return true;
        }
        return false;
    }
```

如果将`onInterceptTouchEvent`方法返回`true`

```java
public class MyLayout extends LinearLayout {
 
	public MyLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return true;
	}
	
}
```

然后分别`Button1`、`Button2`和空白区域

```java
2021-05-31 11:41:19.440 17787-17787/com.example.eventdemo D/TAG: myLayout on touch
2021-05-31 11:41:20.958 17787-17787/com.example.eventdemo D/TAG: myLayout on touch
2021-05-31 11:41:21.995 17787-17787/com.example.eventdemo D/TAG: myLayout on touch
```

> 可发现，都是调用`myLayout`的onTouch方法



**事件传递的步骤**

> Android中touch事件的传递，绝对是先传递到`ViewGroup`，再传递到`View`的。记得在[Android事件分发机制完全解析，带你从源码的角度彻底理解(上)](http://blog.csdn.net/sinyu890807/article/details/9097463) 中我有说明过，只要你触摸了任何控件，就一定会调用该控件的`dispatchTouchEvent`方法。这个说法没错，只不过还不完整而已。实际情况是，当你点击了某个控件，首先会去调用该控件所在布局的`dispatchTouchEvent`方法，然后在布局的`dispatchTouchEvent`方法中找到被点击的相应控件，再去调用该控件的`dispatchTouchEvent`方法。如果我们点击了`MyLayout`中的按钮，会先去调用`MyLayout`的`dispatchTouchEvent`方法，可是你会发现`MyLayout`中并没有这个方法。那就再到它的父类`LinearLayout`中找一找，发现也没有这个方法。那只好继续再找`LinearLayout`的父类`ViewGroup`，你终于在ViewGroup中看到了这个方法，按钮的`dispatchTouchEvent`方法就是在这里调用的。修改后的示意图如下所示：
>
> ![001](https://github.com/winfredzen/Android-Basic/blob/master/%E8%BF%9B%E9%98%B6/image/001.png)



`ViewGroup`中的`dispatchTouchEvent`方法中，会调用`onInterceptTouchEvent()`方法

![012](https://github.com/winfredzen/Android-Basic/blob/master/%E8%BF%9B%E9%98%B6/image/012.png)





**ViewGroup事件分发过程的流程图**

![002](https://github.com/winfredzen/Android-Basic/blob/master/%E8%BF%9B%E9%98%B6/image/002.png)



> 1. Android事件分发是先传递到`ViewGroup`，再由`ViewGroup`传递到`View`的。
>
> 2. 在`ViewGroup`中可以通过`onInterceptTouchEvent`方法对事件传递进行拦截，`onInterceptTouchEvent`方法返回`true`代表不允许事件继续向子`View`传递，返回`false`代表不对事件进行拦截，默认返回`false`。
>
> 3. 子`View`中如果将传递的事件消费掉，`ViewGroup`中将无法接收到任何事件。























