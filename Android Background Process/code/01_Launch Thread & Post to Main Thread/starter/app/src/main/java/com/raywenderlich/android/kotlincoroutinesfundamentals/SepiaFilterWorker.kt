package com.raywenderlich.android.kotlincoroutinesfundamentals

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.raywenderlich.android.introapp.ImageUtils
import java.io.FileOutputStream

//棕褐色过滤
class SepiaFilterWorker(context: Context, workerParameters: WorkerParameters) : Worker(context, workerParameters) {

    override fun doWork(): Result {

        val imagePath = inputData.getString("image_path") ?: return Result.failure()

        val bitmap = BitmapFactory.decodeFile(imagePath)

        val newBitmap = ImageUtils.applySepiaFilter(bitmap)

        val outputStream = FileOutputStream(imagePath)
        outputStream.use { output ->

            newBitmap.compress(Bitmap.CompressFormat.PNG, 100, output)

            output.flush()



        }

        val output = workDataOf("image_path" to imagePath)
        return Result.success(output)


    }

}