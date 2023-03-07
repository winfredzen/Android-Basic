# 根Activity启动过程

参考：

+ 《Android进阶解密》



`《Android进阶解密》`一书中的内容，在最新的Android 系统版本中有变化

参考：

+ [从Launcher启动一个APP流程 Android 12](https://blog.csdn.net/followYouself/article/details/125176288)



![059](https://github.com/winfredzen/Android-Basic/blob/master/Framework/images/059.png)





------

分析大概有三个部分：

1. Launcher请求AMS过程
2. AMS到`ApplicationThread`的调用过程
3. `ActivityThread`启动`Activity`



## 1.Launcher请求AMS过程

点击应用程序的快捷图标，通过Launcher请求AMS启动应用程序

原书中的时序图如下：

![057](https://github.com/winfredzen/Android-Basic/blob/master/Framework/images/057.png)

但是现在最新的Android系统版本中，流程是有变化的

我自己总结下，如下：

![058](https://github.com/winfredzen/Android-Basic/blob/master/Framework/images/058.png)

其中就新引入了**`ActivityTaskManagerService`**



**ActivityTaskManagerService**

> System service for managing activities and their containers (task, displays,... ).

> `ActivityTaskManagerService`(以下简称ATMS)是Android 10新增加的系统服务类，承担了`ActivityManagerService`(AMS)的部分工作(activities、task, stacks, displays相关)，比如将Activity的启动相关的调用转移到了ATMS中

```java
public class ActivityTaskManagerService extends IActivityTaskManager.Stub
```



**Activity**

`frameworks/base/core/java/android/app/Activity.java`的`startActivityForResult`方法实现：

```java
    public void startActivityForResult(@RequiresPermission Intent intent, int requestCode,
            @Nullable Bundle options) {
        if (mParent == null) {
            options = transferSpringboardActivityOptions(options);
            Instrumentation.ActivityResult ar =
                mInstrumentation.execStartActivity(
                    this, mMainThread.getApplicationThread(), mToken, this,
                    intent, requestCode, options);
            if (ar != null) {
                mMainThread.sendActivityResult(
                    mToken, mEmbeddedID, requestCode, ar.getResultCode(),
                    ar.getResultData());
            }
          ...
    }
```

+ `mMainThread` - 类型为`ActivityThread`
+ `mMainThread.getApplicationThread()` 类型为 `ApplicationThread`



`ApplicationThread`是`ActivityThread`的内部类

```java
private class ApplicationThread extends IApplicationThread.Stub
```



**Instrumentation**

`execStartActivity`方法：

```java
    public ActivityResult execStartActivity(
            Context who, IBinder contextThread, IBinder token, Activity target,
            Intent intent, int requestCode, Bundle options) {
        ...
        try {
            intent.migrateExtraStreamToClipData(who);
            intent.prepareToLeaveProcess(who);
          	//
            int result = ActivityTaskManager.getService().startActivity(whoThread,
                    who.getOpPackageName(), who.getAttributionTag(), intent,
                    intent.resolveTypeIfNeeded(who.getContentResolver()), token,
                    target != null ? target.mEmbeddedID : null, requestCode, 0, null, options);
            notifyStartActivityResult(result, options);
            checkStartActivityResult(result, intent);
        } catch (RemoteException e) {
            throw new RuntimeException("Failure from system", e);
        }
        return null;
    }
```

`ActivityTaskManager.getService()`获取的应该是`IActivityTaskManager`

```java
public class ActivityTaskManagerService extends IActivityTaskManager.Stub 
```



## 2.AMS到ApplicationThread的调用过程

![060](https://github.com/winfredzen/Android-Basic/blob/master/Framework/images/060.png)



### ApplicationThread

 `ApplicationThread`是`ActivityThread`的内部类，也是一个`Binder`对象

> AMS通过`ApplicationThread`来与应用程序进程进行Binder通信。换句话说，`ApplicationThread`是AMS所在进程（`SystemServer`进程）和应用程序进程的通信桥梁

![061](https://github.com/winfredzen/Android-Basic/blob/master/Framework/images/061.png)



## 3.ActivityThread启动Activity的过程

![062](https://github.com/winfredzen/Android-Basic/blob/master/Framework/images/062.png)



> Android 应用进程的核心是 **ActivityThread **类，App 的真正入口。每启动一个 App 进程，都会创建 `ActivityThread` 与之对应的实例，会调用 `main ()` 开始运行，开启消息循环队列，这就是传说中的 UI 线程或者叫**主线程**。



## 总结



### 根Activity启动过程中涉及的进程

4个进程

+ Zygote进程
+ Launcher进程
+ AMS所在进程（SystemServer进程）
+ 应用程序进程



![063](https://github.com/winfredzen/Android-Basic/blob/master/Framework/images/063.png)

> 步骤2是`Socket`通信
>
> 步骤1和步骤4采用的是`Binder`通信

1.`Launcher`进程向AMS请求创建根`Activity`，AMS会判断根`Activity`所需的应用进程是否存在并启动，如果不存在就会请求`Zygote`进程创建应用程序进程

2.应用程序进程启动后，AMS会请求创建应用程序进程并启动根Activity

时序图如下：

![064](https://github.com/winfredzen/Android-Basic/blob/master/Framework/images/064.png)



### ActivityThread vs ApplicationThread

参考：

+ [深入理解 Activity 启动流程和 AMS 框架（一）](https://my.oschina.net/u/3735058/blog/4468490)
+ [深入理解 Activity 启动流程和 AMS 框架（二）](https://my.oschina.net/u/3735058/blog/4468488)
+ [android线程管理五（ActivityThread与ApplicationThread）](https://blog.csdn.net/lu1024188315/article/details/75722420)
+ [Android启动过程剖析-深入浅出](https://juejin.cn/post/6844903630462517255#heading-6)



















