# 根Activity启动过程

参考：

+ 《Android进阶解密》



## Launcher请求AMS过程

原书中的时序图如下：

![057](https://github.com/winfredzen/Android-Basic/blob/master/Framework/images/057.png)

但是现在最新的Android系统版本中，流程是有变化的

我自己总结下，如下：

![058](https://github.com/winfredzen/Android-Basic/blob/master/Framework/images/058.png)

其中就新引入了**`ActivityTaskManagerService`**



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



![055](https://github.com/winfredzen/Android-Basic/blob/master/Framework/images/055.png)









