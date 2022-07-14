# Camera2 API使用

此内容来源自：

+ [Camera2 API](https://www.nigeapptuts.com/android-camera2-api-background-handler/)教程
+ 教程代码[recyclerview_image_gallery](https://github.com/mobapptuts/recyclerview_image_gallery) - 注意code在不同的分支和tag上

自己的代码

+ [Camera2API](https://github.com/winfredzen/Android-Basic/tree/master/Camera/code/Camera2API)



基本的步骤：

1.使用`TextureView`来显示预览画面

设置监听器 `setSurfaceTextureListener(SurfaceTextureListener listener)`，在监听器的回调方法`onSurfaceTextureAvailable`

中，获取到`width`和`height`，然后设置camera和打开camera

![010](https://github.com/winfredzen/Android-Basic/blob/master/Camera/images/010.png)

2.设置camera

结合`TextureView`的`width`和`height`，以及camera的输出size，计算出合适的预览尺寸`mPreviewSize`

3.打开相机

```java
cameraManager.openCamera(mCameraId, mCameraDeviceStateCallback, null);
```

在`mCameraDeviceStateCallback`的回调方法中，创建预览session

![011](https://github.com/winfredzen/Android-Basic/blob/master/Camera/images/011.png)

4.session && request

在这一步就开始相机画面的预览了，相关的类：

+ `CaptureRequest`
+ `CaptureRequest.Builder`
+ `CameraCaptureSession`
+ `CameraCaptureSession.CaptureCallback`

将第2步获取的`mPreviewSize`大小设置为`default size of the image buffers`

![012](https://github.com/winfredzen/Android-Basic/blob/master/Camera/images/012.png)





