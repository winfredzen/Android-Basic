# GestureDetector

官方文档的介绍[检测常用手势](https://developer.android.com/training/gestures/detector?hl=zh-cn)

> **检测手势**
>
> Android 提供了用于检测常用手势的 `GestureDetector` 类。该类支持的一些手势包括 `onDown()`、`onLongPress()`、`onFling()` 等。您可以将 `GestureDetector` 与上述 `onTouchEvent()` 方法结合使用。
>
> ### 检测所有受支持的手势
>
> 实例化 `GestureDetectorCompat` 对象时，此对象需要的参数之一是实现 `GestureDetector.OnGestureListener` 接口的类。发生特定轻触事件时，`GestureDetector.OnGestureListener` 会通知用户。如需让 `GestureDetector` 对象能够接收事件，您可以替换视图或 Activity 的 `onTouchEvent()` 方法，并将所有观察到的事件传递给检测器实例。
>
> 在以下代码段中，如果单个 `on*<TouchEvent>*` 方法的返回值为 `true`，就表示您已处理轻触事件。返回值`false`会将事件向下传递至数据视图堆栈，直到触摸操作成功处理完毕。
>
> 请运行以下代码段，以大致了解在您与触摸屏互动时会如何触发各种操作，以及每个轻触事件的 `MotionEvent` 内容是什么。您会发现，即使是很简单的互动也会产生大量数据。
>
> ```java
>     public class MainActivity extends Activity implements
>             GestureDetector.OnGestureListener,
>             GestureDetector.OnDoubleTapListener{
> 
>         private static final String DEBUG_TAG = "Gestures";
>         private GestureDetectorCompat mDetector;
> 
>         // Called when the activity is first created.
>         @Override
>         public void onCreate(Bundle savedInstanceState) {
>             super.onCreate(savedInstanceState);
>             setContentView(R.layout.activity_main);
>             // Instantiate the gesture detector with the
>             // application context and an implementation of
>             // GestureDetector.OnGestureListener
>             mDetector = new GestureDetectorCompat(this,this);
>             // Set the gesture detector as the double tap
>             // listener.
>             mDetector.setOnDoubleTapListener(this);
>         }
> 
>         @Override
>         public boolean onTouchEvent(MotionEvent event){
>             if (this.mDetector.onTouchEvent(event)) {
>                 return true;
>             }
>             return super.onTouchEvent(event);
>         }
> 
>         @Override
>         public boolean onDown(MotionEvent event) {
>             Log.d(DEBUG_TAG,"onDown: " + event.toString());
>             return true;
>         }
> 
>         @Override
>         public boolean onFling(MotionEvent event1, MotionEvent event2,
>                 float velocityX, float velocityY) {
>             Log.d(DEBUG_TAG, "onFling: " + event1.toString() + event2.toString());
>             return true;
>         }
> 
>         @Override
>         public void onLongPress(MotionEvent event) {
>             Log.d(DEBUG_TAG, "onLongPress: " + event.toString());
>         }
> 
>         @Override
>         public boolean onScroll(MotionEvent event1, MotionEvent event2, float distanceX,
>                 float distanceY) {
>             Log.d(DEBUG_TAG, "onScroll: " + event1.toString() + event2.toString());
>             return true;
>         }
> 
>         @Override
>         public void onShowPress(MotionEvent event) {
>             Log.d(DEBUG_TAG, "onShowPress: " + event.toString());
>         }
> 
>         @Override
>         public boolean onSingleTapUp(MotionEvent event) {
>             Log.d(DEBUG_TAG, "onSingleTapUp: " + event.toString());
>             return true;
>         }
> 
>         @Override
>         public boolean onDoubleTap(MotionEvent event) {
>             Log.d(DEBUG_TAG, "onDoubleTap: " + event.toString());
>             return true;
>         }
> 
>         @Override
>         public boolean onDoubleTapEvent(MotionEvent event) {
>             Log.d(DEBUG_TAG, "onDoubleTapEvent: " + event.toString());
>             return true;
>         }
> 
>         @Override
>         public boolean onSingleTapConfirmed(MotionEvent event) {
>             Log.d(DEBUG_TAG, "onSingleTapConfirmed: " + event.toString());
>             return true;
>         }
>     }
>     
> ```
>
> ### 检测一部分受支持的手势
>
> 如果您只想处理几个手势，可以扩展 `GestureDetector.SimpleOnGestureListener`，而无需实现 `GestureDetector.OnGestureListener` 接口。
>
> `GestureDetector.SimpleOnGestureListener` 通过针对所有 `on*<TouchEvent>*` 方法返回 `false`，提供对所有这些方法的实现。因此，您可以仅替换您关注的方法。例如，以下代码段会创建一个扩展 `GestureDetector.SimpleOnGestureListener` 的类并替换 `onFling()` 和 `onDown()`。
>
> **无论您是否使用 `GestureDetector.OnGestureListener`，最佳做法都是实现返回 `true` 的 `onDown()` 方法。这是因为所有手势都以 `onDown()` 消息开头。如果您从 `onDown()` 返回 `false`（与 `GestureDetector.SimpleOnGestureListener` 的默认做法一样），系统会认为您想要忽略其余手势，并且永远不会调用 `GestureDetector.OnGestureListener` 的其他方法。这可能会导致您的应用出现意外问题。只有当您确实想要忽略整个手势时，才应该从 `onDown()` 返回 `false`。**
>
> ```java
>     public class MainActivity extends Activity {
> 
>         private GestureDetectorCompat mDetector;
> 
>         @Override
>         public void onCreate(Bundle savedInstanceState) {
>             super.onCreate(savedInstanceState);
>             setContentView(R.layout.activity_main);
>             mDetector = new GestureDetectorCompat(this, new MyGestureListener());
>         }
> 
>         @Override
>         public boolean onTouchEvent(MotionEvent event){
>             this.mDetector.onTouchEvent(event);
>             return super.onTouchEvent(event);
>         }
> 
>         class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
>             private static final String DEBUG_TAG = "Gestures";
> 
>             @Override
>             public boolean onDown(MotionEvent event) {
>                 Log.d(DEBUG_TAG,"onDown: " + event.toString());
>                 return true;
>             }
> 
>             @Override
>             public boolean onFling(MotionEvent event1, MotionEvent event2,
>                     float velocityX, float velocityY) {
>                 Log.d(DEBUG_TAG, "onFling: " + event1.toString() + event2.toString());
>                 return true;
>             }
>         }
>     }
> 
> ```
>
> 

