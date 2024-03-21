# BluetoothAdapter

[BluetoothAdapter](https://developer.android.com/reference/android/bluetooth/BluetoothAdapter)按其文档描述：

> 代表本地设备蓝牙适配器。 `BluetoothAdapter` 允许您执行基本的蓝牙任务，例如启动设备发现、查询绑定（配对）设备的列表、使用已知 MAC 地址实例化 `BluetoothDevice`、创建 `BluetoothServerSocket` 来侦听来自其他设备的连接请求以及启动蓝牙服务。 扫描`Bluetooth LE` 设备。
>
> 要获取代表本地蓝牙适配器的`BluetoothAdapter`，请调用`BluetoothManager` 上的`BluetoothManager.getAdapter()` 函数。 在 `JELLY_BEAN_MR1` 及以下版本上，您将需要使用静态 `getDefaultAdapter()` 方法。
>
> 从根本上来说，这是所有蓝牙操作的起点。 一旦您拥有本地适配器，您就可以使用 `getBondedDevices()` 获取代表所有配对设备的一组 `BluetoothDevice` 对象； 使用 startDiscovery() 启动设备发现； 或使用`listenUsingRfcommWithServiceRecord(java.lang.String, java.util.UUID)`创建一个`BluetoothServerSocket`来侦听传入的RFComm连接请求； 使用`listenUsingL2capChannel()`监听传入的L2CAP面向连接的通道(CoC)连接请求； 或使用 `startLeScan(android.bluetooth.BluetoothAdapter.LeScanCallback)` 开始扫描`Bluetooth LE`  设备。
>
> 这个类是线程安全的。

在[设置蓝牙](https://developer.android.com/develop/connectivity/bluetooth/setup?hl=zh-cn)有如下的说明：

1.获取 `BluetoothAdapter`

```java
BluetoothManager bluetoothManager = getSystemService(BluetoothManager.class);
BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
if (bluetoothAdapter == null) {
  // Device doesn't support Bluetooth
}
```

2.启用蓝牙

调用 [`isEnabled()`](https://developer.android.com/reference/android/bluetooth/BluetoothAdapter?hl=zh-cn#isEnabled()) 以检查当前是否已启用蓝牙。如果此方法返回 false，则表示蓝牙处于停用状态

```java
if (!bluetoothAdapter.isEnabled()) {
  Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
  startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
}
```

> （可选）您的应用还可以监听 [`ACTION_STATE_CHANGED`](https://developer.android.com/reference/android/bluetooth/BluetoothAdapter?hl=zh-cn#ACTION_STATE_CHANGED) 广播 intent，每当蓝牙状态发生变化时，系统都会广播此 intent。此广播包含 extra 字段 [`EXTRA_STATE`](https://developer.android.com/reference/android/bluetooth/BluetoothAdapter?hl=zh-cn#EXTRA_STATE) 和 [`EXTRA_PREVIOUS_STATE`](https://developer.android.com/reference/android/bluetooth/BluetoothAdapter?hl=zh-cn#EXTRA_PREVIOUS_STATE)，分别包含新旧蓝牙状态。这些额外字段的可能值包括 [`STATE_TURNING_ON`](https://developer.android.com/reference/android/bluetooth/BluetoothAdapter?hl=zh-cn#STATE_TURNING_ON)、[`STATE_ON`](https://developer.android.com/reference/android/bluetooth/BluetoothAdapter?hl=zh-cn#STATE_ON)、[`STATE_TURNING_OFF`](https://developer.android.com/reference/android/bluetooth/BluetoothAdapter?hl=zh-cn#STATE_TURNING_OFF) 和 [`STATE_OFF`](https://developer.android.com/reference/android/bluetooth/BluetoothAdapter?hl=zh-cn#STATE_OFF)。如果您的应用需要检测对蓝牙状态做出的运行时更改，则监听此广播可能会很有用。























