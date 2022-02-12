# WindowManager

主要是对`WindowManager`添加浮窗有点兴趣，想看下实现的过程

参考：[10.7 WindowManager(窗口管理服务)](https://www.runoob.com/w3cnote/android-tutorial-windowmanager.html)教程，这篇文章比价旧了，在Android11的系统上运行例子有点小问题

如果设置`WindowManager.LayoutParams`的type为`TYPE_SYSTEM_ALERT`，则会有如下的提示

![050](https://github.com/winfredzen/Android-Basic/blob/master/UI/images/050.png)

网上查了下，说是`apiLevel >= 19`，不要使用`WindowManager.LayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT` 

而是要使用`LayoutParams.TYPE_TOAST or TYPE_APPLICATION_PANEL`，但也解决不了问题

后来参考如下的文章：

+ [Add view outside Activity through WindowManager](https://levelup.gitconnected.com/add-view-outside-activity-through-windowmanager-1a70590bad40)



1.需要`SYSTEM_ALERT_WINDOW`权限

```xml
<uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW"/>
```

2.在SDK 23以上的版本中，需要申请悬浮窗权限（显示在其他应用的上层）

```java
    //请求悬浮窗权限
    @TargetApi(Build.VERSION_CODES.M)
    private void getOverlayPermission() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, 0);
    }
```

可通过`Settings.canDrawOverlays(this)`检查权限是否已授予

















