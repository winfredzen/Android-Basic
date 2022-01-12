# 问题集与文章

**UI相关**

+ [Android ImageView 的scaleType 属性图解](https://www.jianshu.com/p/32e335d5b842)
+ [Android状态栏【你真的了解吗？】](https://www.jianshu.com/p/1f2ce8209f24)

在UI线程上运行代码，参考[Running code in main thread from another thread](https://stackoverflow.com/questions/11123621/running-code-in-main-thread-from-another-thread)

> **1. If your background thread has a reference to a `Context` object:**
> 
> ```java
> // Get a handler that can be used to post to the main thread
> Handler mainHandler = new Handler(context.getMainLooper());
> 
> Runnable myRunnable = new Runnable() {
>     @Override 
>     public void run() {....} // This is your code
> };
> mainHandler.post(myRunnable);
> ```
> 
> **2. If your background thread does not have (or need) a `Context` object**
> 
> ```java
> // Get a handler that can be used to post to the main thread
> Handler mainHandler = new Handler(Looper.getMainLooper());
> 
> Runnable myRunnable = new Runnable() {
>     @Override 
>     public void run() {....} // This is your code
> };
> mainHandler.post(myRunnable);
> ```
> 
> 或者
> 
> ```java
> someActivity.runOnUiThread(new Runnable() {
>         @Override
>         public void run() {
>            //Your code to run in GUI thread here
>         }//public void run() {
> });
> ```
> 
> **Kotlin versions**
> 
> ```kotlin
> runOnUiThread {
>     //code that runs in main
> }
> 
> mContext.runOnUiThread {
>     //code that runs in main
> }
> 
> Handler(Looper.getMainLooper()).post {  
>     //code that runs in main
> }
> ```

## View

一些文章：

**requestLayout和invalidate区别**

+ [Android View 深度分析requestLayout、invalidate与postInvalidate](https://blog.csdn.net/a553181867/article/details/51583060)

+ [Usage of forceLayout(), requestLayout() and invalidate()](https://stackoverflow.com/questions/13856180/usage-of-forcelayout-requestlayout-and-invalidate)



![]()


