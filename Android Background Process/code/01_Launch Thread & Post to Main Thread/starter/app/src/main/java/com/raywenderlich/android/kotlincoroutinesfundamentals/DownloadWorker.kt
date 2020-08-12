package com.raywenderlich.android.kotlincoroutinesfundamentals

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

class DownloadWorker(context: Context, workerParameters: WorkerParameters) : Worker(context, workerParameters) {

    override fun doWork(): Result {
        val imageUrl = URL("https://upload-images.jianshu.io/upload_images/5809200-a99419bb94924e6d.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240")

        val conneection = imageUrl.openConnection() as HttpURLConnection
        conneection.doInput = true
        conneection.connect()

        val imagePath = "owl_image_${System.currentTimeMillis()}.jpg"
        val inputStream = conneection.inputStream
        val file = File(applicationContext.externalMediaDirs.first(), imagePath) //getExternalMediaDirs()可存放共享媒体文件

        val outputStream = FileOutputStream(file)
        outputStream.use { output ->

            val buffer = ByteArray(4 * 1024)
            var byteCount = inputStream.read(buffer) //读取数据填充到字节数组buf

            while (byteCount > 0) {
                output.write(buffer,0, byteCount)

                byteCount = inputStream.read(buffer)
            }

            output.flush()//缓冲区的内容写入到文件

        }

        val output = workDataOf("image_path" to file.absolutePath)

        return Result.success(output)

    }

}