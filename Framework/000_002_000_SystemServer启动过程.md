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



