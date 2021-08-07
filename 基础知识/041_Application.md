# Application

Application是维护应用全局状态的基类。Android系统会在启动应用进程时创建一个Application对象

![067](https://github.com/winfredzen/Android-Basic/blob/master/%E5%9F%BA%E7%A1%80%E7%9F%A5%E8%AF%86/images/067.png)



`getApplication()` vs `getApplicationContext()`

参考：[getApplication() vs. getApplicationContext()](https://stackoverflow.com/questions/5018545/getapplication-vs-getapplicationcontext)

> Although in current Android Activity and Service implementations, `getApplication()` and `getApplicationContext()` return the same object, there is no guarantee that this will always be the case (for example, in a specific vendor implementation).
>
> So if you want the Application class you registered in the Manifest, you should **never** call `getApplicationContext()` and cast it to your application, because it may not be the application instance (which you obviously experienced with the test framework).
>
> Why does `getApplicationContext()` exist in the first place ?
>
> `getApplication()` is only available in the Activity class and the Service class, whereas `getApplicationContext()` is declared in the Context class.
>
> That actually means one thing : when writing code in a broadcast receiver, which is not a context but is given a context in its onReceive method, you can only call `getApplicationContext()`. Which also means that you are not guaranteed to have access to your application in a BroadcastReceiver.
>
> When looking at the Android code, you see that when attached, an activity receives a base context and an application, and those are different parameters. `getApplicationContext()` delegates it's call to `baseContext.getApplicationContext()`.

参考：[Android中的getApplication()、getApplicationContext的区别与用法](https://blog.csdn.net/u014665856/article/details/72354406)

> 在获取Application时，如果是在Context的情况下可以就可以直接通过(MyApplication)getApplication()来获取。还有一种做法是在没有Context的情况下，可以通过仿照单例的做法来实现获取：
>
> ```java
> public class MyApplication extends Application {
>     private static MyApplication instance;
> }
> @Override
> public void onCreate() {
>     super.onCreate();
>     instance = this;
> }
>  // 获取Application
>     public static Context getMyApplication() {
>         return instance;
> }
> ```

























































