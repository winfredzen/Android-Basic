# Android系统启动流程

参考：

+ [Android 显示系统：SurfaceFlinger详解](https://www.cnblogs.com/blogs-of-lxl/p/11272756.html)

借用其一张图，如下：

![029](https://github.com/winfredzen/Android-Basic/blob/master/Framework/images/029.png)



另外可参考：

+ [图解 | Android系统的启动](https://mp.weixin.qq.com/s/xsoc9omtu-gGAFG1pCsCsQ)
+ [图解 | 一图摸清Android应用进程的启动](https://juejin.cn/post/6887431834041483271)



![040](https://github.com/winfredzen/Android-Basic/blob/master/Framework/images/040.png)



**有哪些主要的系统进程？**

查看`init.rc`配置文件，在这个文件中，定义了许多需要启动的系统进程，如

![028](https://github.com/winfredzen/Android-Basic/blob/master/Framework/images/028.png)

还需要注意的是`SystemServer`进程不是init进程创建的，它是Zygote进程创建的



**Zygote是怎么启动的？**

上一节已讲过，主要如下：

1. init进程fork出zygote进程
2. 启动虚拟机，注册jni函数 - 为接下来切换到java做准备
3. 预加载系统资源
4. 启动SystemServer - SystemServer中跑了很多系统服务
5. 进入Socket Loop



**Zygote的工作流程**

在zygote启动的最后一步，zygote进入socket循环，在循环中，它一方面看有没有发过来的socket消息，有消息的时候就回去执行`runOnce`函数

![030](https://github.com/winfredzen/Android-Basic/blob/master/Framework/images/030.png)



**SystemServer是怎么启动的？**

参考：

+ [Android系统启动-SystemServer上篇](http://gityuan.com/2016/02/14/android-system-server/)

>  [Android系统启动-zygote篇](http://gityuan.com/2016/02/13/android-zygote/)中讲到Zygote启动过程中会调用`startSystemServer()`，可知`startSystemServer()`函数是system_server启动流程的起点， 启动流程图如下：
>
> ![031](https://github.com/winfredzen/Android-Basic/blob/master/Framework/images/031.png)
>
> 上图前4步骤（即颜色为紫色的流程）运行在是`Zygote`进程，从第5步（即颜色为蓝色的流程）`ZygoteInit.handleSystemServerProcess`开始是运行在新创建的`system_server`，这是fork机制实现的（fork会返回2次）

`SystemServer`的`main`函数

```java
    /**
     * The main entry point from zygote.
     */
    public static void main(String[] args) {
        new SystemServer().run();
    }
```

`run()`方法主要结构如下：

```java
    private void run() {
        TimingsTraceAndSlog t = new TimingsTraceAndSlog();
        try {
						......
						// 为主线程创建Looper
            Looper.prepareMainLooper();
            Looper.getMainLooper().setSlowLogThresholdMs(
                    SLOW_DISPATCH_THRESHOLD_MS, SLOW_DELIVERY_THRESHOLD_MS);

            SystemServiceRegistry.sEnableServiceNotFoundWtf = true;

            // Initialize native services. 加载共享库
            System.loadLibrary("android_servers");

						......

            // Initialize the system context. 创建系统上下文
            createSystemContext();

        // Start services.
        try {
            t.traceBegin("StartServices");
            startBootstrapServices(t);
            startCoreServices(t);
            startOtherServices(t);
        } catch (Throwable ex) {
            
        } finally {
           
        }

        // Loop forever.
        Looper.loop();
    }
```



## 系统服务

**系统服务是怎么启动？**

1.系统服务是如何发布的？

![032](https://github.com/winfredzen/Android-Basic/blob/master/Framework/images/032.png)

最终是将系统服务注册到了ServcieManager中

2.系统服务跑在什么线程？

3.怎么解决系统服务启动的互相依赖？

+ 分批启动

+ 分阶段启动



**怎么添加一个系统服务？**

![034](https://github.com/winfredzen/Android-Basic/blob/master/Framework/images/034.png)

Client要使用Service的话，需要先拿到service的binder，再通过binder发起系统调用



**如何使用系统服务？**

![035](https://github.com/winfredzen/Android-Basic/blob/master/Framework/images/035.png)

![036](https://github.com/winfredzen/Android-Basic/blob/master/Framework/images/036.png)

`createService`的实现，以power_service为例

![037](https://github.com/winfredzen/Android-Basic/blob/master/Framework/images/037.png)

`ServiceManager`的`getService`的实现：

```java
    /**
     * Returns a reference to a service with the given name.
     *
     * @param name the name of the service to get
     * @return a reference to the service, or <code>null</code> if the service doesn't exist
     * @hide
     */
    @UnsupportedAppUsage
    public static IBinder getService(String name) {
        try {
            IBinder service = sCache.get(name);
            if (service != null) {
                return service;
            } else {
                return Binder.allowBlocking(rawGetService(name));
            }
        } catch (RemoteException e) {
            Log.e(TAG, "error in getService", e);
        }
        return null;
    }

```



**如何注册系统服务？**

```java
    /**
     * Place a new @a service called @a name into the service
     * manager.
     *
     * @param name the name of the new service
     * @param service the service object
     * @param allowIsolated set to true to allow isolated sandboxed processes
     * @param dumpPriority supported dump priority levels as a bitmask
     * to access this service
     * @hide
     */
    @UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.R, trackingBug = 170729553)
    public static void addService(String name, IBinder service, boolean allowIsolated,
            int dumpPriority) {
        try {
            getIServiceManager().addService(name, service, allowIsolated, dumpPriority);
        } catch (RemoteException e) {
            Log.e(TAG, "error in addService", e);
        }
    }
```



**什么时候注册的系统服务？**

在SystemServer启动的时候，注册的系统服务



**独立进程的系统服务**

如`surfaceflinger`

![038](https://github.com/winfredzen/Android-Basic/blob/master/Framework/images/038.png)

































