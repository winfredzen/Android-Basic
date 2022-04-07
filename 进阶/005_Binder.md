# Binder

## 1.为什么 Android 要采用 Binder 作为 IPC 机制？

> 参考：
>
> + [为什么 Android 要采用 Binder 作为 IPC 机制？](https://www.zhihu.com/question/39440766)
>
> 1.**管道**
>
> > 在创建时分配一个page大小的内存，缓存区大小比较有限；
> >
> > > [Linux下的进程通信方式： 管道通信详解](https://blog.csdn.net/rl529014/article/details/51464363)
> > >
> > > 只支持单向数据流
> > >
> > > 只能用于具有亲缘关系的进程之间通信，没有名字
> > >
> > > 缓冲区有限，管道只存在于主存中，大小为一个页面
> > >
> > > 所传送的是无格式字节流
>
> 2.**消息队列**：信息复制两次，额外的CPU消耗；不合适频繁或信息量大的通信；
>
> 3.**共享内存**：无须复制，共享缓冲区直接付附加到进程虚拟地址空间，速度快；但进程间的同步问题操作系统无法实现，必须各进程利用同步工具解决；
>
> 4.**套接字**：作为更通用的接口，传输效率低，主要用于不通机器或跨网络的通信；
>
> 5.**信号量**：常作为一种锁机制，防止某进程正在访问共享资源时，其他进程也访问该资源。因此，主要作为进程间以及同一进程内不同线程之间的同步手段。
>
> 6.**信号**: 不适用于信息交换，更适用于进程中断控制，比如非法内存访问，杀死某个进程等；



选择binder大概从3个维度来考虑：

| 性能   | Binder数据拷贝只需要一次，而管道、消息队列、Socket都需要2次，但共享内存方式一次内存拷贝都不需要；从性能角度看，Binder性能仅次于共享内存。首先说说性能上的优势。Socket 作为一款通用接口，其传输效率低，开销大，主要用在跨网络的进程间通信和本机上进程间的低速通信。消息队列和管道采用存储-转发方式，即数据先从发送方缓存区拷贝到内核开辟的缓存区中，然后再从内核缓存区拷贝到接收方缓存区，至少有两次拷贝过程。共享内存虽然无需拷贝，但控制复杂，难以使用。Binder 只需要一次数据拷贝，性能上仅次于共享内存。 |
| ------ | ------------------------------------------------------------ |
| 稳定性 | Binder 基于 C/S 架构，客户端（Client）有什么需求就丢给服务端（Server）去完成，架构清晰、职责明确又相互独立，自然稳定性更好。共享内存虽然无需拷贝，但是控制负责，难以使用。从稳定性的角度讲，Binder 机制是优于内存共享的。 |
| 安全性 | 另一方面就是安全性。Android 作为一个开放性的平台，市场上有各类海量的应用供用户选择安装，因此安全性对于 Android 平台而言极其重要。作为用户当然不希望我们下载的 APP 偷偷读取我的通信录，上传我的隐私数据，后台偷跑流量、消耗手机电量。传统的 IPC 没有任何安全措施，完全依赖上层协议来确保。首先传统的 IPC 接收方无法获得对方可靠的进程用户ID/进程ID（UID/PID），从而无法鉴别对方身份。Android 为每个安装好的 APP 分配了自己的 UID，故而进程的 UID 是鉴别进程身份的重要标志。传统的 IPC 只能由用户在数据包中填入 UID/PID，但这样不可靠，容易被恶意程序利用。可靠的身份标识只有由 IPC 机制在内核中添加。其次传统的 IPC 访问接入点是开放的，只要知道这些接入点的程序都可以和对端建立连接，不管怎样都无法阻止恶意程序通过猜测接收方地址获得连接。同时 Binder 既支持实名 Binder，又支持匿名 Binder，安全性高。 |



## 2.Binder是怎样实现只拷贝一次的？

参考：

+ [为什么要使用Binder？](https://juejin.cn/post/6844903655158579208)
+ [浅析Android Binder机制](https://juejin.cn/post/7003516993999470628)



### Linux中的一些概念

**1.进程隔离**

> **进程隔离**是为保护[操作系统](https://zh.wikipedia.org/wiki/操作系统)中进程互不干扰而设计的一组不同硬件和软件[[1\]](https://zh.wikipedia.org/wiki/进程隔离#cite_note-1)的技术。这个技术是为了避免进程A写入进程B的情况发生。 进程的隔离实现，使用了[虚拟地址空间](https://zh.wikipedia.org/w/index.php?title=虚拟地址空间&action=edit&redlink=1)。进程A的虚拟地址和进程B的虚拟地址不同，这样就防止进程A将数据信息写入进程B。
>
> 进程隔离的安全性通过禁止进程间内存的访问可以方便实现。相比之下，一些不安全的操作系统（例如[DOS](https://zh.wikipedia.org/wiki/DOS)[[2\]](https://zh.wikipedia.org/wiki/进程隔离#cite_note-2)）能够允许任何进程对其他进程的内存进行写操作。



**2.用户空间和内核空间**

参考：

+ [内核空间和用户空间](https://www.cnblogs.com/sparkdev/p/8410350.html)
+ [User space 与 Kernel space](http://www.ruanyifeng.com/blog/2016/12/user_space_vs_kernel_space.html)



> 对 32 位操作系统而言，它的寻址空间（虚拟地址空间，或叫线性地址空间）为 4G（2的32次方）。也就是说一个进程的最大地址空间为 4G。操作系统的核心是内核(kernel)，它独立于普通的应用程序，可以访问受保护的内存空间，也有访问底层硬件设备的所有权限。为了保证内核的安全，现在的操作系统一般都强制用户进程不能直接操作内核。具体的实现方式基本都是由操作系统将虚拟地址空间划分为两部分，一部分为内核空间，另一部分为用户空间。针对 Linux 操作系统而言，最高的 1G 字节(从虚拟地址 0xC0000000 到 0xFFFFFFFF)由内核使用，称为内核空间。而较低的 3G 字节(从虚拟地址 0x00000000 到 0xBFFFFFFF)由各个进程使用，称为用户空间。
> 对上面这段内容我们可以这样理解：
> **每个进程的 4G 地址空间中，最高 1G 都是一样的，即内核空间。只有剩余的 3G 才归进程自己使用。**
> **换句话说就是， 最高 1G 的内核空间是被所有进程共享的！**

>  **为什么需要区分内核空间与用户空间?**
>
> 在 CPU 的所有指令中，有些指令是非常危险的，如果错用，将导致系统崩溃，比如清内存、设置时钟等。如果允许所有的程序都可以使用这些指令，那么系统崩溃的概率将大大增加。
> 所以，CPU 将指令分为特权指令和非特权指令，对于那些危险的指令，只允许操作系统及其相关模块使用，普通应用程序只能使用那些不会造成灾难的指令。比如 Intel 的 CPU 将特权等级分为 4 个级别：Ring0~Ring3。
> 其实 Linux 系统只使用了 Ring0 和 Ring3 两个运行级别(Windows 系统也是一样的)。当进程运行在 Ring3 级别时被称为运行在用户态，而运行在 Ring0 级别时被称为运行在内核态。

> **内核态与用户态**
>
> 好了我们现在需要再解释一下什么是内核态、用户态：
> **当进程运行在内核空间时就处于内核态，而进程运行在用户空间时则处于用户态。**

> **如何从用户空间进入内核空间？**
>
> 其实所有的系统资源管理都是在内核空间中完成的。比如读写磁盘文件，分配回收内存，从网络接口读写数据等等。我们的应用程序是无法直接进行这样的操作的。但是我们可以通过内核提供的接口来完成这样的任务。
> 比如应用程序要读取磁盘上的一个文件，它可以向内核发起一个 "系统调用" 告诉内核："我要读取磁盘上的某某文件"。其实就是通过一个特殊的指令让进程从用户态进入到内核态(到了内核空间)，在内核空间中，CPU 可以执行任何的指令，当然也包括从磁盘上读取数据。具体过程是先把数据读取到内核空间中，然后再把数据拷贝到用户空间并从内核态切换到用户态。此时应用程序已经从系统调用中返回并且拿到了想要的数据，可以开开心心的往下执行了。



如下图所示，`Binder Driver`在`Linux Kernel`中

![031](https://github.com/winfredzen/Android-Basic/blob/master/%E8%BF%9B%E9%98%B6/image/031.png)



**3.物理内存和虚拟内存**

+ [浅析操作系统中的虚拟地址与物理地址](https://www.jb51.net/article/215460.htm)



### 传统通信原理简述

通常的做法是消息发送方将要发送的数据存放在内存缓存区中，通过系统调用进入内核态。然后内核程序在内核空间分配内存，开辟一块内核缓存区，调用 `copy_from_user()` 函数将数据从用户空间的内存缓存区拷贝到内核空间的内核缓存区中。同样的，接收方进程在接收数据时在自己的用户空间开辟一块内存缓存区，然后内核程序调用 `copy_to_user()` 函数将数据从内核缓存区拷贝到接收进程的内存缓存区。这样数据发送方进程和数据接收方进程就完成了一次数据传输，我们称完成了一次进程间通信。

![030](https://github.com/winfredzen/Android-Basic/blob/master/%E8%BF%9B%E9%98%B6/image/030.png)



这种传统IPC机制存在2个问题：

1. 需要进行2次数据拷贝，第1次是从发送方用户空间拷贝到内核缓存区，第2次是从内核缓存区拷贝到接收方用户空间。
2. 接收方进程不知道事先要分配多大的空间来接收数据，可能存在空间上的浪费。





## Binder的底层原理

参考自：

+ [浅析Android Binder机制](https://juejin.cn/post/7003516993999470628)

**Binder IPC 实现步骤**

+ 首先 Binder 驱动在内核空间创建一个数据接收缓存区；

+ 接着在内核空间开辟一块内核缓存区，建立内核缓存区和内核中数据接收缓存区之间的映射关系，以及内核中数据接收缓存区和接收进程用户空间地址的映射关系；

+ 发送方进程通过系统调用 `copy_from_user()` 将数据 copy 到内核中的内核缓存区，由于内核缓存区和接收进程的用户空间存在内存映射，因此也就相当于把数据发送到了接收进程的用户空间，这样便完成了一次进程间的通信。

![032](https://github.com/winfredzen/Android-Basic/blob/master/%E8%BF%9B%E9%98%B6/image/032.png)



## Binder 通信过程

参考：

+ [写给 Android 应用工程师的 Binder 原理剖析](https://zhuanlan.zhihu.com/p/35519585)



> 至此，我们大致能总结出 Binder 通信过程：
>
> 1. 首先，一个进程使用 BINDER*SET*CONTEXT_MGR 命令通过 Binder 驱动将自己注册成为 ServiceManager；
> 2. Server 通过驱动向 ServiceManager 中注册 Binder（Server 中的 Binder 实体），表明可以对外提供服务。驱动为这个 Binder 创建位于内核中的实体节点以及 ServiceManager 对实体的引用，将名字以及新建的引用打包传给 ServiceManager，ServiceManger 将其填入查找表。
> 3. Client 通过名字，在 Binder 驱动的帮助下从 ServiceManager 中获取到对 Binder 实体的引用，通过这个引用就能实现和 Server 进程的通信。
>
> 我们看到整个通信过程都需要 Binder 驱动的接入。下图能更加直观的展现整个通信过程(为了进一步抽象通信过程以及呈现上的方便，下图我们忽略了 Binder 实体及其引用的概念)：
>
> ![074](https://github.com/winfredzen/Android-Basic/blob/master/%E8%BF%9B%E9%98%B6/image/074.jpeg)



## 相关类介绍

参考：

+ [写给 Android 应用工程师的 Binder 原理剖析](https://zhuanlan.zhihu.com/p/35519585)



例如，定义一个aidl, `IMsgManager`

```java
// IMsg.aidl
package com.aidl.service;
import com.aidl.service.IReceiveMsgListener;
import com.aidl.service.Msg;
// Declare any non-default types here with import statements

interface IMsgManager {
   void sendMsg(in Msg msg);
   void registerReceiveListener(IReceiveMsgListener receiveListener);
   void unregisterReceiveListener(IReceiveMsgListener receiveListener);
}

```

生成的类中，有如下的代码

![072](https://github.com/winfredzen/Android-Basic/blob/master/%E8%BF%9B%E9%98%B6/image/072.png)

1.Binder

[Binder](https://developer.android.com/reference/android/os/Binder):Base class for a remotable object - 远程对象的基类

```java
public class Binder implements IBinder
```

2.[IInterface](https://developer.android.com/reference/android/os/IInterface) - 一个接口, Base class for Binder interfaces. When defining a new interface, you must derive it from IInterface.

```java

/**
 * Base class for Binder interfaces.  When defining a new interface,
 * you must derive it from IInterface.
 */
public interface IInterface
{
    /**
     * Retrieve the Binder object associated with this interface.
     * You must use this instead of a plain cast, so that proxy objects
     * can return the correct result.
     */
    public IBinder asBinder();
}

```

3.[IBinder](https://developer.android.com/reference/android/os/IBinder) - Base interface for a remotable object, 远程对象的基本接口

不要直接实现这个接口，而是从 Binder 扩展而来

The key IBinder API is `transact()` matched by `Binder.onTransact()`

The `linkToDeath()` method can be used to register a DeathRecipient with the IBinder, which will be called when its containing process goes away.

4.Stub

编译工具会给我们生成一个名为 Stub 的静态内部类；这个类继承了 Binder, 说明它是一个 Binder 本地对象，它实现了 IInterface 接口，表明它具有 Server 承诺给 Client 的能力；Stub 是一个抽象类，具体的 IInterface 的相关实现需要开发者自己实现。



`asInterface`方法

```java
    /**
     * Cast an IBinder object into an com.aidl.service.IMsgManager interface,
     * generating a proxy if needed.
     */
    public static com.aidl.service.IMsgManager asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof com.aidl.service.IMsgManager))) {
        return ((com.aidl.service.IMsgManager)iin);
      }
      return new com.aidl.service.IMsgManager.Stub.Proxy(obj);
    }
```

> Client 端在创建和服务端的连接，调用 `bindService` 时需要创建一个 `ServiceConnection` 对象作为入参。在 `ServiceConnection` 的回调方法 `onServiceConnected` 中 会通过这个 `asInterface(IBinder binder)` 拿到 `IMsgManager` 对象，这个 `IBinder` 类型的入参 binder 是驱动传给我们的，正如你在代码中看到的一样，方法中会去调用 `binder.queryLocalInterface()` 去查找 `Binder` 本地对象，如果找到了就说明 Client 和 Server 在**同一进程**，那么这个 binder 本身就是 Binder 本地对象，可以直接使用。否则说明是 binder 是个远程对象，也就是 `BinderProxy`。因此需要我们创建一个代理对象 `Proxy`，通过这个代理对象来是实现远程访问。



5.Proxy - 代理对象

和Stub一样，有实现了`com.aidl.service.IMsgManager`接口

![073](https://github.com/winfredzen/Android-Basic/blob/master/%E8%BF%9B%E9%98%B6/image/073.png)

生成的`IMsgManager`源码

```java
/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package com.aidl.service;
// Declare any non-default types here with import statements

public interface IMsgManager extends android.os.IInterface
{
  /** Default implementation for IMsgManager. */
  public static class Default implements com.aidl.service.IMsgManager
  {
    @Override public void sendMsg(com.aidl.service.Msg msg) throws android.os.RemoteException
    {
    }
    @Override public void registerReceiveListener(com.aidl.service.IReceiveMsgListener receiveListener) throws android.os.RemoteException
    {
    }
    @Override public void unregisterReceiveListener(com.aidl.service.IReceiveMsgListener receiveListener) throws android.os.RemoteException
    {
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements com.aidl.service.IMsgManager
  {
    private static final java.lang.String DESCRIPTOR = "com.aidl.service.IMsgManager";
    /** Construct the stub at attach it to the interface. */
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an com.aidl.service.IMsgManager interface,
     * generating a proxy if needed.
     */
    public static com.aidl.service.IMsgManager asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof com.aidl.service.IMsgManager))) {
        return ((com.aidl.service.IMsgManager)iin);
      }
      return new com.aidl.service.IMsgManager.Stub.Proxy(obj);
    }
    @Override public android.os.IBinder asBinder()
    {
      return this;
    }
    @Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
    {
      java.lang.String descriptor = DESCRIPTOR;
      switch (code)
      {
        case INTERFACE_TRANSACTION:
        {
          reply.writeString(descriptor);
          return true;
        }
        case TRANSACTION_sendMsg:
        {
          data.enforceInterface(descriptor);
          com.aidl.service.Msg _arg0;
          if ((0!=data.readInt())) {
            _arg0 = com.aidl.service.Msg.CREATOR.createFromParcel(data);
          }
          else {
            _arg0 = null;
          }
          this.sendMsg(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_registerReceiveListener:
        {
          data.enforceInterface(descriptor);
          com.aidl.service.IReceiveMsgListener _arg0;
          _arg0 = com.aidl.service.IReceiveMsgListener.Stub.asInterface(data.readStrongBinder());
          this.registerReceiveListener(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_unregisterReceiveListener:
        {
          data.enforceInterface(descriptor);
          com.aidl.service.IReceiveMsgListener _arg0;
          _arg0 = com.aidl.service.IReceiveMsgListener.Stub.asInterface(data.readStrongBinder());
          this.unregisterReceiveListener(_arg0);
          reply.writeNoException();
          return true;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
    }
    private static class Proxy implements com.aidl.service.IMsgManager
    {
      private android.os.IBinder mRemote;
      Proxy(android.os.IBinder remote)
      {
        mRemote = remote;
      }
      @Override public android.os.IBinder asBinder()
      {
        return mRemote;
      }
      public java.lang.String getInterfaceDescriptor()
      {
        return DESCRIPTOR;
      }
      @Override public void sendMsg(com.aidl.service.Msg msg) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          if ((msg!=null)) {
            _data.writeInt(1);
            msg.writeToParcel(_data, 0);
          }
          else {
            _data.writeInt(0);
          }
          boolean _status = mRemote.transact(Stub.TRANSACTION_sendMsg, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().sendMsg(msg);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void registerReceiveListener(com.aidl.service.IReceiveMsgListener receiveListener) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongBinder((((receiveListener!=null))?(receiveListener.asBinder()):(null)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_registerReceiveListener, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().registerReceiveListener(receiveListener);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void unregisterReceiveListener(com.aidl.service.IReceiveMsgListener receiveListener) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongBinder((((receiveListener!=null))?(receiveListener.asBinder()):(null)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_unregisterReceiveListener, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().unregisterReceiveListener(receiveListener);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      public static com.aidl.service.IMsgManager sDefaultImpl;
    }
    static final int TRANSACTION_sendMsg = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_registerReceiveListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    static final int TRANSACTION_unregisterReceiveListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
    public static boolean setDefaultImpl(com.aidl.service.IMsgManager impl) {
      // Only one user of this interface can use this function
      // at a time. This is a heuristic to detect if two different
      // users in the same process use this function.
      if (Stub.Proxy.sDefaultImpl != null) {
        throw new IllegalStateException("setDefaultImpl() called twice");
      }
      if (impl != null) {
        Stub.Proxy.sDefaultImpl = impl;
        return true;
      }
      return false;
    }
    public static com.aidl.service.IMsgManager getDefaultImpl() {
      return Stub.Proxy.sDefaultImpl;
    }
  }
  public void sendMsg(com.aidl.service.Msg msg) throws android.os.RemoteException;
  public void registerReceiveListener(com.aidl.service.IReceiveMsgListener receiveListener) throws android.os.RemoteException;
  public void unregisterReceiveListener(com.aidl.service.IReceiveMsgListener receiveListener) throws android.os.RemoteException;
}

```



## 自己的理解

![075](https://github.com/winfredzen/Android-Basic/blob/master/%E8%BF%9B%E9%98%B6/image/075.png)

































