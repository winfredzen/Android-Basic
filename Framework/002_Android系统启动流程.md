# Android系统启动流程

参考：

+ [Android 显示系统：SurfaceFlinger详解](https://www.cnblogs.com/blogs-of-lxl/p/11272756.html)

借用其一张图，如下：

![029](https://github.com/winfredzen/Android-Basic/blob/master/Framework/images/029.png)



**有哪些主要的系统进程？**

查看`init.rc`配置文件，在这个文件中，定义了许多需要启动的系统进程，如

![028](https://github.com/winfredzen/Android-Basic/blob/master/Framework/images/028.png)

还需要注意的是`SystemServer`进程不是init进程创建的，它是Zygote进程创建的



**Zygote是怎么启动的？**

上一节已讲过，主要如下：



**SystemServer是怎么启动的？**

