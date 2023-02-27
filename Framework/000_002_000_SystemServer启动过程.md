# SystemServer启动过程

参考：

+ 《Android进阶解密》



SystemServer主要用于创建系统服务

**Zygote处理SystemServer进程的时序图**

![048](https://github.com/winfredzen/Android-Basic/blob/master/Framework/images/048.png)



**SystemServer进程总结**

+ 启动Binder线程池，这样就可以与其他进程进行通信
+ 创建SystemServiceManager，其用于对系统的服务进行创建、启动和生命周期管理
+ 启动各种系统服务
  + 引导服务
  + 核心服务
  + 其它服务




例如，启动[PowerManagerService](https://cs.android.com/android/platform/superproject/+/master:frameworks/base/services/core/java/com/android/server/power/PowerManagerService.java;drc=841c92eb880dbbb3b475e886322b2ff5975a811c;l=154)：

1.`SystemServiceManager`调用`startService`

![050](https://github.com/winfredzen/Android-Basic/blob/master/Framework/images/050.png)

2.调用 `public void startService(@NonNull final SystemService service)`方法

![051](https://github.com/winfredzen/Android-Basic/blob/master/Framework/images/051.png)



除了`startService`启动服务外，还可以通过如下的形式来启动服务，如 `PackageManagerService`

![052](https://github.com/winfredzen/Android-Basic/blob/master/Framework/images/052.png)

 `PackageManagerService.main`方法如下：

![053](https://github.com/winfredzen/Android-Basic/blob/master/Framework/images/053.png)

> ServiceManager用于管理系统中的各种service，用于系统C/S架构中的Binder通信机制。Client端要使用某个Service，则先需要到ServiceManager中查询Service的相关消息，然后根据Service的相关信息与Service所在的Server进程建立通信通路，这样Client端就可以使用Service了

















