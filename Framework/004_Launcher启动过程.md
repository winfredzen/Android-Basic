# Launcher启动过程

参考：

+ [Android Launcher应用进程启动流程 基于Android-12](https://blog.csdn.net/Bye_Moon/article/details/121683051)
+ [Android launcher应用启动过程（基于Android 11）](https://juejin.cn/post/6978743408756162590)



Launcher作用

+ 作为Android系统的启动器，用于启动应用程序
+ 作为Android系统的桌面，用于显示和管理应用程序的快捷图标或者其他桌面组件



`SystemServer`进程在启动的过程中会启动`PackageManagerService`，`PackageManagerService`启动后会将系统中的应用程序安装完成。在此前已经启动的AMS会将Launcher启动起来



大致启动过程如下：

![054](https://github.com/winfredzen/Android-Basic/blob/master/Framework/images/054.png)

