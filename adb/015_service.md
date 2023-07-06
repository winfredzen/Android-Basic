# service

```shell
adb shell service list
```

列出的是所有系统的服务



列出应用的服务

```shell
// List all services
adb shell dumpsys activity services

// List all services containing "myservice" in its name
adb shell dumpsys activity services myservice
```

