# Android Background Process

内容来自：

+ [Android Background Processing](https://www.raywenderlich.com/10376966-android-background-processing)



## Launch Threads & Post To The Main Thread

例子是在主线程中加载并显示一张图片

```kotlin
      val imageUrl = URL("https://upload-images.jianshu.io/upload_images/5809200-a99419bb94924e6d.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240")
      val connection = imageUrl.openConnection() as HttpURLConnection
      connection.doInput = true
      connection.connect()

      val inputStream = connection.inputStream
      val bitmap = BitmapFactory.decodeStream(inputStream)
      image.setImageBitmap(bitmap)
```

会提示如下的错误

![001](https://github.com/winfredzen/Android-Basic/blob/master/Android%20Background%20Process/images/001.png)



修改成在子线程中下载图片，在UI线程上显示图片，如下：

```kotlin
    Thread(Runnable {
      val imageUrl = URL("https://upload-images.jianshu.io/upload_images/5809200-a99419bb94924e6d.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240")
      val connection = imageUrl.openConnection() as HttpURLConnection
      connection.doInput = true
      connection.connect()

      val inputStream = connection.inputStream
      val bitmap = BitmapFactory.decodeStream(inputStream)

      runOnUiThread {
        image.setImageBitmap(bitmap)
      }

    }).start()
```

