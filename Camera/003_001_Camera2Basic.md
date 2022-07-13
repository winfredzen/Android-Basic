# Camera2Basic

本文是学习[Camera2Basic](https://github.com/android/camera-samples/tree/main/Camera2Basic)的记录



## CameraManager

[CameraManager](https://developer.android.com/reference/android/hardware/camera2/CameraManager)

> A system service manager for detecting, characterizing, and connecting to CameraDevices.
>
> 用于检测、表征和连接到 CameraDevices 的系统服务管理器

### 1.获取`CameraManager`

```kotlin
val cameraManager =
        requireContext().getSystemService(Context.CAMERA_SERVICE) as CameraManager
```

### 2.`getCameraIdList`

当前连接的相机设备列表

例如我的小米手机，返回的是`0`和`1`，虽然手机的后置摄像头由多个摄像头组成，但应该是作为一个整体



### 3.`getCameraCharacteristics`

根据 cameraId 获取对应相机设备的特征

```kotlin
val characteristics = cameraManager.getCameraCharacteristics(id)
```



#### a.获取摄像头的方向

```kotlin
val cameraLensFacing = characteristics.get(CameraCharacteristics.LENS_FACING)
```

可能的方向有：

> - [`CameraMetadata.LENS_FACING_FRONT`](https://developer.android.com/reference/android/hardware/camera2/CameraMetadata#LENS_FACING_FRONT)
> - [`CameraMetadata.LENS_FACING_BACK`](https://developer.android.com/reference/android/hardware/camera2/CameraMetadata#LENS_FACING_BACK)
> - [`CameraMetadata.LENS_FACING_EXTERNAL`](https://developer.android.com/reference/android/hardware/camera2/CameraMetadata#LENS_FACING_EXTERNAL)



#### b.获取当前Camera设备支持的功能列表

```kotlin
val cameraCapabilities = characteristics.get(CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES)
```

可参考：

+ [8.Android Camera2 API AVAILABLE_CAPABILITIES详解](https://deepinout.com/android-camera-official-documentation/android-camera2-api/android-camera2-api-available_capabilities-details.html)



例如：

+ `REQUEST_AVAILABLE_CAPABILITIES_BACKWARD_COMPATIBLE` - 每个相机设备都支持的最小功能集

+ `SCALER_STREAM_CONFIGURATION_MAP` - 相机设备支持的可用流的配置，包括最小帧间隔、不同格式、大小组合的失帧时长





## SCALER_STREAM_CONFIGURATION_MAP

[SCALER_STREAM_CONFIGURATION_MAP](https://developer.android.com/reference/android/hardware/camera2/CameraCharacteristics#SCALER_STREAM_CONFIGURATION_MAP)的说明如下：

```java
public static final Key<StreamConfigurationMap> SCALER_STREAM_CONFIGURATION_MAP
```

> 此相机设备支持的可用流配置； 还包括每个格式/大小组合的最小帧持续时间和停顿持续时间。
>
> 所有相机设备都将支持 JPEG 格式的传感器最大分辨率（由 android.sensor.info.activeArraySize 定义）。
>
> 对于给定的用例，实际支持的最大分辨率可能低于此处列出的分辨率，具体取决于图像数据的目标 Surface。 例如，对于录制视频，所选视频编码器的最大尺寸限制（例如 1080p）可能小于相机（例如，最大分辨率为 3264x2448）可以提供的尺寸限制。

可通过此key，获取输出设备的分辨率

其返回值为`StreamConfigurationMap`，如下的方法返回输出的size：

```java
public Size[] getOutputSizes (int format)
public Size[] getOutputSizes (Class<T> klass)
```

如：

```kotlin
        val size = characteristics.get(
                CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)!!
                .getOutputSizes(args.pixelFormat).maxByOrNull { it.height * it.width }!!
```

在我小米手机上，竖屏，`args.pixelFormat`为JPEG时，返回的size值为`4000x3000`













## 其它

代码：

+ [Camera2Basic](https://github.com/winfredzen/Android-Basic/tree/master/Camera/code/Camera2Basic)



可参考：

+ [Android Camera2 之 CameraCharacteristics 详解](https://blog.csdn.net/afei__/article/details/85960343)

 



























































