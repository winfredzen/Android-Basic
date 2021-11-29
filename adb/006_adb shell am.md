# adb shell am

> 在 adb shell 中，您可以使用 Activity 管理器 (`am`) 工具发出命令以执行各种系统操作，如启动 Activity、强行停止进程、广播 intent、修改设备屏幕属性，等等



参考：

+ [调用 Activity 管理器 (`am`)](https://developer.android.com/studio/command-line/adb#am)
+ [Am命令用法](http://gityuan.com/2016/02/27/am-command/)



参见的一些参数说明：

+ -W: 等待app启动完成
+ -R `<COUNT`>: 重复启动Activity COUNT次
+ -S: 启动activity之前，先调用forceStopPackage()方法强制停止app.



**Intent参数**

+ `-a <ACTION>`: 指定Intent action， 实现原理`Intent.setAction()；`
+ `-n <COMPONENT>`: 指定组件名，格式为{包名}/.{主Activity名}，实现原理`Intent.setComponent()`；
+ `-d <DATA_URI>`: 指定Intent data URI



## 使用例子

### 统计启动时间

如下显示的热启动：

```shell
 adb shell am start -W com.tobin.life/.MainActivity
```

![009](https://github.com/winfredzen/Android-Basic/blob/master/adb/images/009.png)

冷启动

```shell
adb shell am start -S -W com.tobin.life/.MainActivity 
```

![010](https://github.com/winfredzen/Android-Basic/blob/master/adb/images/010.png)









