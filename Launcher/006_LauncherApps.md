# LauncherApps

[LauncherApps](https://developer.android.com/reference/android/content/pm/LauncherApps)可以用来获取`launchable activities`，我理解可以用来获取安装的APK

另外[LauncherApps.Callback](https://developer.android.com/reference/android/content/pm/LauncherApps.Callback)可以用来监听package的变化

```java
public void registerCallback (LauncherApps.Callback callback)
```



分析下Launcher3中有关App的安装、删除的实现方式，大致结构图如下：

![013](https://github.com/winfredzen/Android-Basic/blob/master/Launcher/images/013.png)



1.在`LauncherAppState`中设置`LauncherApps.Callback`回调为`LauncherModel`

```java
mContext.getSystemService(LauncherApps.class).registerCallback(mModel);
```

2.`LauncherModel`实现`LauncherApps.Callback`接口

3.当`onPackageAdded`调用是，会进入`PackageUpdatedTask`中

```java
    @Override
    public void onPackageAdded(String packageName, UserHandle user) {
        int op = PackageUpdatedTask.OP_ADD;
        enqueueModelUpdateTask(new PackageUpdatedTask(op, user, packageName));
    }
    
    public void enqueueModelUpdateTask(ModelUpdateTask task) {
        task.init(mApp, this, mBgDataModel, mBgAllAppsList, mMainExecutor);
        MODEL_EXECUTOR.execute(task);
    }    
```

4.最终调用的是`PackageUpdatedTask`的`execute`方法



