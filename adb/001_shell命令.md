## shell命令

参考：

+ [发出 shell 命令](https://developer.android.com/studio/command-line/adb#shellcommands)



在调试的过程中，看到如下的命令：

```shell
adb devices
adb shell setenforce 0
adb remount
adb uninstall com.xxx.xxxapp
adb install -t -r xxxpath
adb shell am start -n xxx/.ui.activity.MainActivity
```

有些疑问，学习记录如下



您可以使用 `shell` 命令通过 adb 发出设备命令，也可以启动交互式 shell。如需发出单个命令，请使用 `shell` 命令，如下所示：

```shell
adb [-d |-e | -s serial_number] shell shell_command
```

要在设备上启动交互式 shell，请使用 `shell` 命令，如下所示：

```shell
adb [-d | -e | -s serial_number] shell
```

要退出交互式 shell，请按 Ctrl + D 键或输入 `exit`。



## 调用 Activity 管理器 (`am`)

在 adb shell 中，您可以使用 Activity 管理器 (`am`) 工具发出命令以执行各种系统操作，如启动 Activity、强行停止进程、广播 intent、修改设备屏幕属性，等等。在 shell 中，相应的语法为：

```shell
am command
```

您也可以直接从 adb 发出 Activity 管理器命令，无需进入远程 shell。例如：

```shell
adb shell am start -a android.intent.action.VIEW
```





## SELinux

> SELinux (Security-Enhanced Linux) 主要由美国国家安全局开发，是一种强制访问控制的实现。它的作法是以最小权限原则为基础，在Linux核心中使用Linux安全模块。它并非一个 Linux 发行版，而是一组可以套用在类 Unix 操作系统的修改。

具体的可参考：

+ [折腾无果？先试试禁用 SELinux](https://sspai.com/post/32197)
+ [Android 中的安全增强型 Linux](https://source.android.com/security/selinux?hl=zh-cn)

> 作为 Android [安全模型](https://source.android.com/security?hl=zh-cn)的一部分，Android 使用安全增强型 Linux (SELinux) 对所有进程强制执行强制访问控制 (MAC)，甚至包括以 Root/超级用户权限运行的进程（Linux 功能）。很多公司和组织都为 Android 的 [SELinux 实现](https://android.googlesource.com/platform/external/selinux/)做出了贡献。借助 SELinux，Android 可以更好地保护和限制系统服务、控制对应用数据和系统日志的访问、降低恶意软件的影响，并保护用户免遭移动设备上的代码可能存在的缺陷的影响。
>
> SELinux 按照默认拒绝的原则运行：任何未经明确允许的行为都会被拒绝。SELinux 可按两种全局模式运行：
>
> - 宽容模式：权限拒绝事件会被记录下来，但不会被强制执行。
> - 强制模式：权限拒绝事件会被记录下来**并**强制执行。
>
> Android 中包含 SELinux（处于强制模式）和默认适用于整个 AOSP 的相应安全政策。在强制模式下，非法操作会被阻止，并且尝试进行的所有违规行为都会被内核记录到 `dmesg` 和 `logcat`。开发时，您应该先利用这些错误信息对软件和 SELinux 政策进行优化，再对它们进行强制执行。如需了解详情，请参阅[实现 SELinux](https://source.android.com/security/selinux/implement?hl=zh-cn)。
>
> 此外，SELinux 还支持基于域的宽容模式。在这种模式下，可将特定域（进程）设为宽容模式，同时使系统的其余部分处于全局强制模式。简单来说，域是安全政策中用于标识一个进程或一组进程的标签，安全政策会以相同的方式处理所有具有相同域标签的进程。借助基于域的宽容模式，可逐步将 SELinux 应用于系统中越来越多的部分，还可以为新服务制定政策（同时确保系统的其余部分处于强制模式）。





**adb remount**

> 'adb remount' 将 '/system' 部分置于可写入的模式，默认情况下 '/system' 部分是只读模式的。这个命令只适用于已被 root 的设备。
>
> 在将文件 push 到 '/system' 文件夹之前，必须先输入命令 'adb remount'。
>
> 'adb remount' 的作用相当于 'adb shell mount -o rw,remount,rw /system'。























