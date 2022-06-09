# 蓝牙

[蓝牙](https://zh.wikipedia.org/wiki/%E8%97%8D%E7%89%99)维基百科的介绍：

> - 还提出了“低功耗蓝牙”、“传统蓝牙”和“高速蓝牙”三种模式。
> - 其中：高速蓝牙主攻数据交换与传输；传统蓝牙则以信息沟通、设备连接为重点；低功耗蓝牙顾名思义，以不需占用太多带宽的设备连接为主。前身其实是NOKIA开发的Wibree技术，本是作为一项专为移动设备开发的极低功耗的移动无线通信技术，在被SIG接纳并规范化之后重命名为Bluetooth Low Energy（后简称低功耗蓝牙）。这三种协议规范还能够互相组合搭配、从而实现更广泛的应用模式，此外，Bluetooth 4.0还把蓝牙的传输距离提升到100米以上（低功耗模式条件下）。



Android官方文档：

+ [蓝牙概览](https://developer.android.com/guide/topics/connectivity/bluetooth?hl=zh-cn)



## 权限

参考：

+ [Bluetooth permissions](https://developer.android.com/guide/topics/connectivity/bluetooth/permissions)
+ [Android 12 中的新蓝牙权限](https://developer.android.google.cn/about/versions/12/features/bluetooth-permissions?hl=zh-cn)

> `BLUETOOTH_SCAN`、`BLUETOOTH_ADVERTISE` 和 `BLUETOOTH_CONNECT` 权限是[运行时权限](https://developer.android.google.cn/guide/topics/permissions/overview?hl=zh-cn#runtime)。





## 发现设备

[startDiscovery()](https://developer.android.com/reference/android/bluetooth/BluetoothAdapter#startDiscovery())方法的一些说明：

> Start the remote device discovery process.
>
> 启动远程设备发现过程
>
> The discovery process usually involves an inquiry scan of about 12 seconds, followed by a page scan of each new device to retrieve its Bluetooth name.
>
> 发现过程通常涉及大约 12 秒的查询扫描，然后对每个新设备进行页面扫描以检索其蓝牙名称
>
> This is an asynchronous call, it will return immediately. Register for `ACTION_DISCOVERY_STARTED` and `ACTION_DISCOVERY_FINISHED` intents to determine exactly when the discovery starts and completes. Register for `BluetoothDevice.ACTION_FOUND` to be notified as remote Bluetooth devices are found.
>
> 这是一个异步调用，它会立即返回。注册 `ACTION_DISCOVERY_STARTED` 和 `ACTION_DISCOVERY_FINISHED`  intent以确定发现何时开始和完成。注册 `BluetoothDevice.ACTION_FOUND` 以在找到远程蓝牙设备时得到通知。
>
> Device discovery is a heavyweight procedure. New connections to remote Bluetooth devices should not be attempted while discovery is in progress, and existing connections will experience limited bandwidth and high latency. Use `cancelDiscovery()` to cancel an ongoing discovery. Discovery is not managed by the Activity, but is run as a system service, so an application should always call `BluetoothAdapter#cancelDiscovery()` even if it did not directly request a discovery, just to be sure.
>
> 设备发现是一个重量级的过程。在发现过程中，不应尝试与远程蓝牙设备建立新连接，现有连接将遇到带宽有限和高延迟的问题。使用 `cancelDiscovery()` 取消正在进行的发现。 Discovery 不由 Activity 管理，而是作为系统服务运行，因此应用程序应始终调用 `BluetoothAdapter#cancelDiscovery()`，即使它没有直接请求发现，只是为了确定
>
> Device discovery will only find remote devices that are currently *discoverable* (inquiry scan enabled). Many Bluetooth devices are not discoverable by default, and need to be entered into a special mode.
>
> 设备发现只会发现当前可发现的远程设备（启用查询扫描）。许多蓝牙设备默认是不可发现的，需要进入特殊模式。

> If Bluetooth state is not `STATE_ON`, this API will return false. After turning on Bluetooth, wait for `ACTION_STATE_CHANGED` with `STATE_ON` to get the updated value.
>
> 如果蓝牙状态不是 `STATE_ON`，此 API 将返回 false。打开蓝牙后，等待 `ACTION_STATE_CHANGED` 和 `STATE_ON` 获取更新的值。
>
> If a device is currently bonding, this request will be queued and executed once that device has finished bonding. If a request is already queued, this request will be ignored.
>
> 如果设备当前正在绑定，则该请求将在该设备完成绑定后排队并执行。如果一个请求已经在队列中，这个请求将被忽略。
>
> For apps targeting `Build.VERSION_CODES#R` or lower, this requires the `Manifest.permission#BLUETOOTH_ADMIN` permission which can be gained with a simple `<uses-permission>` manifest tag.
>
> 对于面向 `Build.VERSION_CODES#R` 或更低版本的应用，这需要 `Manifest.permission#BLUETOOTH_ADMIN` 权限，该权限可以通过简单的 <uses-permission> 清单标记获得。
>
> For apps targeting `Build.VERSION_CODES#S` or or higher, this requires the `Manifest.permission#BLUETOOTH_SCAN` permission which can be gained with `Activity.requestPermissions(String[], int)`.
>
> 对于面向 `Build.VERSION_CODES#S` 或更高版本的应用，这需要 `Manifest.permission#BLUETOOTH_SCAN` 权限，该权限可以通过 `Activity.requestPermissions(String[], int)` 获得。
>
> In addition, this requires either the `Manifest.permission#ACCESS_FINE_LOCATION` permission or a strong assertion that you will never derive the physical location of the device. You can make this assertion by declaring 
>
> `usesPermissionFlags="neverForLocation"` on the relevant `<uses-permission>` manifest tag, but it may restrict the types of Bluetooth devices you can interact with.
>
> 此外，这需要 `Manifest.permission#ACCESS_FINE_LOCATION` 权限或您永远不会获得设备的物理位置的强断言。您可以通过在相关的 `<uses-permission>` 清单标签上声明
>
> Requires `Manifest.permission.BLUETOOTH_SCAN`
>
> 需要 `Manifest.permission.BLUETOOTH_SCAN`



**出现的问题**

1.`bluetoothAdapter.startDiscovery()`一直返回`false`，而且没有获扫描到蓝牙信息

> 需要定位权限