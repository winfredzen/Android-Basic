# systrace

参考：

+ [在命令行上捕获系统跟踪记录](https://developer.android.com/topic/performance/tracing/command-line)
+ [手把手教你使用Systrace（一）](https://zhuanlan.zhihu.com/p/27331842)



`systrace`在最新的Android Studio中已被移除

> ```css
> This package used to contain systrace, but that has been obsoleted in favor of Studio Profiler, gpuinspector.dev, or Perfetto.
> systrace已移除。请改用 Studio 性能分析器/gpuinspector.dev/Perfetto。
> ```



> `systrace` 命令会调用 [Systrace 工具](https://developer.android.com/topic/performance/tracing)，您可以借助该工具收集和检查设备上在系统一级运行的所有进程的时间信息
>
> `systrace` 命令在 Android SDK 工具软件包中提供，并且可以在 `android-sdk/platform-tools/systrace/` 中找到

`systrace`会生成html报告

使用方式：

```shell
python systrace.py [options] [categories]
```



| 命令和选项                                | 说明                                                         |
| :---------------------------------------- | :----------------------------------------------------------- |
| `-o file`                                 | 将 HTML 跟踪报告写入指定的 file。如果您未指定此选项，`systrace` 会将报告保存到 `systrace.py` 所在的目录中，并将其命名为 `trace.html`。 |
| `-t N | --time=N`                         | 跟踪设备活动 N 秒。如果您未指定此选项，`systrace` 会提示您在命令行中按 Enter 键结束跟踪。 |
| `-b N | --buf-size=N`                     | 使用 N 千字节的跟踪缓冲区大小。使用此选项，您可以限制跟踪期间收集到的数据的总大小。 |
| `-k functions|--ktrace=functions`         | 跟踪逗号分隔列表中指定的特定内核函数的活动。                 |
| `-a app-name|--app=app-name`              | 启用对应用的跟踪，指定为包含[进程名称](https://developer.android.com/guide/topics/manifest/application-element#proc)的逗号分隔列表。 这些应用必须包含 `Trace` 类中的跟踪插桩调用。您应在分析应用时指定此选项。很多库（例如 `RecyclerView`）都包括跟踪插桩调用，这些调用可在您启用应用级跟踪时提供有用的信息。如需了解详情，请参阅[定义自定义事件](https://developer.android.com/topic/performance/tracing/custom-events)。如需跟踪搭载 Android 9（API 级别 28）或更高版本的设备上的所有应用，请传递用添加引号的通配符字符 `"*"`。 |
| `--from-file=file-path`                   | 根据文件（例如包含原始跟踪数据的 TXT 文件）创建交互式 HTML 报告，而不是运行实时跟踪。 |
| `-e device-serial|--serial=device-serial` | 在已连接的特定设备（由对应的[设备序列号](https://developer.android.com/studio/command-line/adb#devicestatus)标识）上进行跟踪。 |
| `categories`                              | 包含您指定的系统进程的跟踪信息，如 `gfx` 表示用于渲染图形的系统进程。您可以使用 `-l` 命令运行 `systrace`，以查看已连接设备可用的服务列表。 |



## 使用

在代码开始位置和结束位置，添加如下的代码：

```java
TraceCompat.beginSection("AppOnCreate");、

TraceCompat.endSection();
```

然后安装程序

然后使用python脚本：

![014](https://github.com/winfredzen/Android-Basic/blob/master/%E4%BC%98%E5%8C%96/images/014.png)



![015](https://github.com/winfredzen/Android-Basic/blob/master/%E4%BC%98%E5%8C%96/images/015.png)



