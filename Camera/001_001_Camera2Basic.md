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































































