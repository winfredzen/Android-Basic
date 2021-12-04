# Camera2实现拍照录像

按照教程[Camera2 API Video](https://www.nigeapptuts.com/category/camera2-api-video/)来实现

按照教程的步骤，在本人Android 10 or 11的系统上，遇到一些兼容的问题，如下的问题：

1.保存的录制视频的目录中，获取`getExternalStoragePublicDirectory`已废弃，要修改下，如修改为`getExternalFilesDir`

2.录制视频`MediaRecorder`，提示如下的错误：

