# Linux

记录一下用到的Linux命令



#### cp

**cp命令** 用来将一个或多个源文件或者目录复制到指定的目的文件或目录

```sh
cp -rf soatest/ /
```

> `-R/r`：递归处理，将指定目录下的所有文件与子目录一并处理；
>
> `-f`：强行复制文件或目录，不论目标文件或目录是否已存在；



#### ps

**ps命令** 用于报告当前系统的进程状态。可以搭配kill指令随时中断、删除不必要的程序

`ps` 与`grep` 常用组合用法，查找特定进程

如：`adb shell ps | grep com.android.car`

```text
system         4049    377 16378972 144372 do_epoll_wait      0 S com.android.car
system         4336    377 15986804 117864 do_epoll_wait      0 S com.android.car.settings
nobody         4367      1 12363860  3440 binder_thread_read  0 S com.android.car.procfsinspector
u10_system     6150    377 15886372 100020 do_epoll_wait      0 S com.android.car
u10_a8         6267    377 15931696 106608 do_epoll_wait      0 S com.android.car.media
```

