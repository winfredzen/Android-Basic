# zygote

参考《Android进阶解密》

> 在Android系统中，DVM（Dalvik虚拟机）和ART、应用程序进程以及运行系统的关键服务SystemServer进程都是由Zygote进程来创建的，我们称之为**孵化器**。它通过**fork**（复制进程）的形式来创建应用程序进程和SystemServer进程，由于Zygote进程在启动时会创建DVM或ART，因此通过fork而创建的应用程序进程和SystemServer进程可以在内部获取一个DVM或者ART的实例副本



**zygote的作用是什么？**

+ 启动SystemServer
+ 孵化应用进程

**启动三段式**

![001](https://github.com/winfredzen/Android-Basic/blob/master/Framework/images/001.png)

### zygote启动流程

#### 1.zygote进程是怎么启动的？

a.`init`进程是linux启动之后，用户空间的第一个进程

b.`init`进程启动之后，首先会去加载一个配置文件`init.rc`

c.配置文件`init.rc`定义了需要启动的系统服务，zygote就是其中之一

![008](https://github.com/winfredzen/Android-Basic/blob/master/Framework/images/008.png)

**启动配置文件**

![009](https://github.com/winfredzen/Android-Basic/blob/master/Framework/images/009.png)

进程启动时通过`fork` 加 `execve`，上面标红的部分是service的名称，标蓝的部分是可执行程序的路径，标黄的部分就是参数

> 参考：
>
> + [linux中execve函数的用法](https://www.cnblogs.com/jxhd1/p/6706701.html)
>
> > 在父进程中fork一个子进程，在子进程中调用exec函数启动新的程序。exec函数一共有六个，其中execve为内核级系统调用，其他（execl，execle，execlp，execv，execvp）都是调用execve的库函数。
> >
> > \#include<unistd.h>
> >
> > **函数定义** `int execve(const char *filename, char *const argv[ ], char *const envp[ ]);`
> >
> > **返回值** 函数执行成功时没有返回值，执行失败时的返回值为-1.
> >
> > **函数说明** `execve()`用来执行参数filename字符串所代表的文件路径，第二个参数是利用数组指针来传递给执行文件，并且需要以空指针(NULL)结束，最后一个参数则为传递给执行文件的新环境变量数组。



**启动进程**

启动进程有2种方式

+ fork + handle
+ fork + execve

![010](https://github.com/winfredzen/Android-Basic/blob/master/Framework/images/010.png)

> `fork`函数，有两种返回，子进程中返回的pid是0，父进程中返回的pid是子进程的pid，所以可以通过判断pid是0，来判断当前是子进程还是父进程
>
> 默认的情况下，创建的子进程是继承了父进程的所有资源的
>
> 如果又通过加载execve系统调用去加载另一个二进制的话，那么继承的父进程资源就会被清掉，完全被二进制程序替换掉



**信号处理-SIGCHLD**

参考：

+ [Linux: 关于 SIGCHLD 的更多细节](https://segmentfault.com/a/1190000015060304)

父进程fork出了子进程，如果子进程挂了，那么父进程就会收到`SIGCHLD`信号，就会去做一些处理

![011](https://github.com/winfredzen/Android-Basic/blob/master/Framework/images/011.png)

比如，zygote进程如果挂了，init进程就会收到`SIGCHLD`信号，就会重启zygote进程



#### 2.zygote进程启动后做了什么？

zygote进程启动之后，执行了execve系统调用，系统调用执行的是二进制的系统调用，C++实现的，里面有一个main函数作为入口，所以所zygote天生就是Native的，做了一些准备工作之后，就会切到Java世界进行运行

1.zygote的Native世界

组要做了3件事情

![012](https://github.com/winfredzen/Android-Basic/blob/master/Framework/images/012.png)

![013](https://github.com/winfredzen/Android-Basic/blob/master/Framework/images/013.png)

问：我们的应用里面好像可以直接JNI调用，并没有先需要创建Java虚拟机？

答：Java虚拟机在zygote进程就已经创建好了，应用进程就是由zygote孵化出来的，继承了它的虚拟机，所以就不用再创建一遍了。需要做的就是，在进程启动的时候，重置一下虚拟机的状态，再重启下虚拟机里面的使用线程



2.zygote的Java世界

+ 预加载资源
+ 启动SystemServer
+ 进入Loop循环

![014](https://github.com/winfredzen/Android-Basic/blob/master/Framework/images/014.png)



**Loop循环里面是怎么处理Socket请求的？**

> `Socket`用于等待`ActivityManagerService`请求`Zygote`来创建新的应用程序进程

zygote在启动之后，会进入socket循环，在循环中，不断的轮询socket，当有新的请求过来时，就会去执行`runOnce`

![015](https://github.com/winfredzen/Android-Basic/blob/master/Framework/images/015.png)

这个函数，做了三件事：

1. 读取参数列表

2. 再根据参数启动子进程

3. 在子进程中开始干活，起始就是执行力Java类的main函数，就是入口函数，java类型就是来源子上面读取的参数列表，参数列表是AMS跨进程发过来的，类名就是`ActivityThread.main()`.

   就是说，应用程序进程启动之后，会马上执行`ActivityThread.main()`函数



**要注意的细节**

1.Zygote的fork要单线程，不管父进程有多少个线程，子进程在创建时它就只有一个线程，也就是所对子进程来说，这些线程就不见了。在父进程创建子进程的时候，就把除主线程之外的子线程都停了，等创建完了子进程之后，再把这些线程重启

2.Zygote的跨进程并没有采用binder，而是采用的本地socket，所以应用程序的binder机制，并不是从zygote继承过来的，而是在应用程序启动之后，自己启动的binder机制



















