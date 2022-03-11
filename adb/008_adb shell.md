# adb shell

## dumpsys

`dumpsys` 是一种在 Android 设备上运行的工具，可提供有关系统服务的信息



## 服务

1.获取所有正在运行的服务

```shell
adb shell dumpsys activity services
```

但是这样列出的服务太多了，可使用`adb shell dumpsys activity services <your-package-name>`来过滤



```shell
adb shell service list
```

列出的是系统的服务



2.启动服务

```shell
adb shell am startservice -n com.xxx.xxx/com.xxx.xxx.xxxService
```

