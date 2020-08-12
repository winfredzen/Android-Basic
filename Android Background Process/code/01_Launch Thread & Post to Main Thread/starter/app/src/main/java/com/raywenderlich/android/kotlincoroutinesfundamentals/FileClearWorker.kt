package com.raywenderlich.android.kotlincoroutinesfundamentals

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class FileClearWorker(context: Context, workerParameters: WorkerParameters) : Worker(context, workerParameters) {

    override fun doWork(): Result {
        val root = applicationContext.externalMediaDirs.first()

        return try {

            //删除文件
            root.listFiles()?.forEach { child ->
                if (child.isDirectory) {
                    child.deleteRecursively()
                } else {
                    child.delete()
                }
            }
            Result.success()

        } catch (error: Throwable) {
            error.printStackTrace()
            Result.failure()
        }
    }

}