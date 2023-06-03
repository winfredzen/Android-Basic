# logcat

在看源码的是否发现log也是有分类的，参考：

+ [Logcat 命令行工具](https://developer.android.com/studio/command-line/logcat?hl=zh-cn)
+ [Android 解读main log和event log日志信息](https://blog.csdn.net/yelangjueqi/article/details/52621903)



查看logcat的帮助

```shell
 adb logcat --help
```



**event日志**

```shell
logcat -b events
```

