# Android系统服务

参考：

+ [图解 | 一图摸清Android系统服务](https://juejin.cn/post/6885640823484973064)



借用一张图:

![039](https://github.com/winfredzen/Android-Basic/blob/master/Framework/images/039.png)





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

> 系统服务可以分成两大类：
>
> 一是有**独立进程**的ServiceManager、SurfaceFlinger等，他们在**init进程启动时就会被fork创建**；
>
> 二是**非独立进程**的AMS、PMS、WMS等，他们在init进程fork出Zygote进程，Zygote进程fork出的**SystemServer进程创建**。



**独立进程的系统服务**

如`surfaceflinger`

![038](https://github.com/winfredzen/Android-Basic/blob/master/Framework/images/038.png)



> 综上，不管是**由init进程启动的独立进程的系统服务**如SurfaceFlinger，还是**由SystemServer进程启动的非独立进程的系统服务**如AMS，都是在**ServiceManager进程**中完成注册和获取的，在跨进程通信上使用了Android的binder机制。





























