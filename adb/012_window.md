# window

通过`adb shell dumpsys window -h`，查看支持的参数

![015](https://github.com/winfredzen/Android-Basic/blob/master/adb/images/015.png)



**获取app启动包名和启动名**

现在设备上打开对应的应用

```shell
adb shell dumpsys window windows | grep com.xxx.xxx
```

![016](https://github.com/winfredzen/Android-Basic/blob/master/adb/images/016.png)



**查看屏幕大尺寸**

```shell
adb shell dumpsys window displays
```

可通过过滤`displayId`来对应



**window的层级**

```shell
adb shell dumpsys window animator 
```

可以查看当前屏幕上的window层级



