# LauncherApps

[LauncherApps](https://developer.android.com/reference/android/content/pm/LauncherApps)可以用来获取`launchable activities`，我理解可以用来获取安装的APK

另外[LauncherApps.Callback](https://developer.android.com/reference/android/content/pm/LauncherApps.Callback)可以用来监听package的变化

```java
public void registerCallback (LauncherApps.Callback callback)
```



分析下Launcher3中有关App的安装、删除的实现方式，大致结构图如下：

![013](https://github.com/winfredzen/Android-Basic/blob/master/Launcher/images/013.png)
