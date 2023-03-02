# 根Activity启动过程

参考：

+ 《Android进阶解密》



`《Android进阶解密》`一书中的内容，在最新的Android 系统版本中有变化

参考：

+ [从Launcher启动一个APP流程 Android 12](https://blog.csdn.net/followYouself/article/details/125176288)



![059](https://github.com/winfredzen/Android-Basic/blob/master/Framework/images/059.png)





------



## Launcher请求AMS过程

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









