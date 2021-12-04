# Camera2实现拍照录像

按照教程[Camera2 API Video](https://www.nigeapptuts.com/category/camera2-api-video/)来实现

按照教程的步骤，在本人Android 10 or 11的系统上，遇到一些兼容的问题，如下的问题：

1.保存的录制视频的目录中，获取`getExternalStoragePublicDirectory`已废弃，要修改下，如修改为`getExternalFilesDir`

2.录制视频`MediaRecorder`，提示如下的错误：

![001](https://github.com/winfredzen/Android-Basic/blob/master/Camera/images/001.png)

在[android-Camera2Video](https://github.com/googlearchive/android-Camera2Video)中有如下的描述：

![002](https://github.com/winfredzen/Android-Basic/blob/master/Camera/images/002.png)

我理解是录制的视频的分辨率太高了，不能超过`1080p`

所以要修改下

