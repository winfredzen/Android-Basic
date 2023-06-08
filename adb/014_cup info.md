# cpu info



## adb shell dumpsys cpuinfo

通过`adb shell dumpsys cpuinfo`

![017](https://github.com/winfredzen/Android-Basic/blob/master/adb/images/017.png)

```
Load: 2.78 / 2.55 / 2.47
```

> 第一行显示的是cpuload （负载平均值）
>
> 这三个数字表示逐渐变长的时间段（平均一分钟，五分钟和十五分钟）的平均值，而较低的数字则更好。数字越大表示问题或机器过载。



> user 和 kernel分别代表用户和系统内核分别占用的cpu比率
>
> user（用户态）: 只能受限的访问内存, 且不允许访问外围设备. 占用CPU的能力被剥夺, CPU资源可以被其他程序获取
>
> kernel（内核态）: CPU可以访问内存所有数据, 包括外围设备, 例如硬盘, 网卡. CPU也可以将自己从一个程序切换到另一个程序



## top

top命令提供了实时的对系统处理器的状态监视.它将显示系统中CPU最“敏感”的任务列表.该命令可以按CPU使用.内存使用和执行时间对任务进行排序.

**查看top帮助**

```shell
adb shell top --help
```

```shell
usage: top [-Hbq] [-k FIELD,] [-o FIELD,] [-s SORT] [-n NUMBER] [-m LINES] [-d SECONDS] [-p PID,] [-u USER,]

Show process activity in real time.

-H      Show threads
-k      Fallback sort FIELDS (default -S,-%CPU,-ETIME,-PID)
-o      Show FIELDS (def PID,USER,PR,NI,VIRT,RES,SHR,S,%CPU,%MEM,TIME+,CMDLINE)
-O      Add FIELDS (replacing PR,NI,VIRT,RES,SHR,S from default)
-s      Sort by field number (1-X, default 9)
-b      Batch mode (no tty)
-d      Delay SECONDS between each cycle (default 3)
-m      Maximum number of tasks to show
-n      Exit after NUMBER iterations
-p      Show these PIDs
-u      Show these USERs
-q      Quiet (no header lines)

```





总体系统信息：

- uptime：系统的运行时间和平均负载。
- tasks：当前运行的进程和线程数目。
- CPU：总体 CPU 使用率和各个核心的使用情况。
- 内存（Memory）：总体内存使用情况、可用内存和缓存。

进程信息：

- PID：进程的标识符。
- USER：运行进程的用户名。
- PR（优先级）：进程的优先级。
- NI（Nice值）：进程的优先级调整值。
- VIRT（虚拟内存）：进程使用的虚拟内存大小。
- RES（常驻内存）：进程实际使用的物理内存大小。
- SHR（共享内存）：进程共享的内存大小。
- %CPU：进程占用 CPU 的使用率。
- %MEM：进程占用内存的使用率。
- TIME+：进程的累计 CPU 时间。

功能和交互操作：

- 按键命令：在 top 运行时可以使用一些按键命令进行操作，如按下 "k" 可以终止一个进程，按下 "h" 可以显示帮助信息等。
- 排序：可以按照 CPU 使用率、内存使用率、进程 ID 等对进程进行排序。
- 刷新频率：可以设置 top 的刷新频率，以便动态查看系统信息。





## CPU架构信息

```shell
 adb shell cat /proc/cpuinfo
```

CPU architecture：7表示arm-v7，8表示arm-v8















