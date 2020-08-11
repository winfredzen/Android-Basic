# WorkManager简介

参考:

+ 官方文档[使用 WorkManager 调度任务](https://developer.android.google.cn/topic/libraries/architecture/workmanager?hl=de)



## 官方文档内容

使用 WorkManager API 可以轻松地调度即使在应用退出或设备重启时仍应运行的可延迟异步任务。

最高向后兼容到 API 14

+ 在运行 API 23 及以上级别的设备上使用 JobScheduler
+ 在运行 API 14-22 的设备上结合使用 BroadcastReceiver 和 AlarmManager

WorkManager 旨在用于**可延迟**运行（即不需要立即运行）并且在应用退出或设备重启时必须能够**可靠运行**的任务。例如：

+ 向后端服务发送日志或分析数据
+ 定期将应用数据与服务器同步

要将 WorkManager 库导入到 Android 项目中，请将以下依赖项添加到应用的 `build.gradle` 文件：

```groovy
    dependencies {
      def work_version = "2.3.4"

        // (Java only)
        implementation "androidx.work:work-runtime:$work_version"

        // Kotlin + coroutines
        implementation "androidx.work:work-runtime-ktx:$work_version"

        // optional - RxJava2 support
        implementation "androidx.work:work-rxjava2:$work_version"

        // optional - GCMNetworkManager support
        implementation "androidx.work:work-gcm:$work_version"

        // optional - Test helpers
        androidTestImplementation "androidx.work:work-testing:$work_version"
      }
```



### WorkManager 使用入门

适用步骤

**1.将 WorkManager 添加到您的 Android 项目中**

**2.创建后台任务**

任务是使用 [`Worker`](https://developer.android.google.cn/reference/androidx/work/Worker?hl=de) 类定义的。`doWork()` 方法在 WorkManager 提供的后台线程上同步运行。

要创建后台任务，请扩展 `Worker` 类并替换 `doWork()` 方法。例如，要创建上传图像的 `Worker`，您可以执行以下操作：

```kotlin
    class UploadWorker(appContext: Context, workerParams: WorkerParameters)
        : Worker(appContext, workerParams) {

        override fun doWork(): Result {
            // Do the work here--in this case, upload the images.

            uploadImages()

            // Indicate whether the task finished successfully with the Result
            return Result.success()
        }
    }
    
```

从 `doWork()` 返回的 [`Result`](https://developer.android.google.cn/reference/androidx/work/ListenableWorker.Result?hl=de) 会通知 WorkManager 任务：

- 已成功完成：`Result.success()`
- 已失败：`Result.failure()`
- 需要稍后重试：`Result.retry()`



**3.配置运行任务的方式和时间**

`Worker` 定义工作单元，[`WorkRequest`](https://developer.android.google.cn/reference/androidx/work/WorkRequest?hl=de) 则定义工作的运行方式和时间。任务可以是一次性的，也可以是周期性的。对于一次性 `WorkRequest`，请使用 [`OneTimeWorkRequest`](https://developer.android.google.cn/reference/androidx/work/OneTimeWorkRequest?hl=de)，对于周期性工作，请使用 [`PeriodicWorkRequest`](https://developer.android.google.cn/reference/androidx/work/PeriodicWorkRequest?hl=de)。如需详细了解如何调度周期性工作，请参阅[周期性工作文档](https://developer.android.google.cn/topic/libraries/architecture/workmanager/how-to/recurring-work?hl=de)。

在本例中，为 UploadWorker 构建 `WorkRequest` 最简单的示例为：

```kotlin
    val uploadWorkRequest = OneTimeWorkRequestBuilder<UploadWorker>()
            .build()
    
```

`WorkRequest` 中还可以包含其他信息，例如任务在运行时应遵循的约束、工作输入、延迟，以及重试工作的退避时间政策。关于这些选项，在[定义工作指南](https://developer.android.google.cn/topic/libraries/architecture/workmanager/how-to/define-work?hl=de)中有更详细的说明。



**4.将任务提交给系统**

定义 `WorkRequest` 之后，您现在可以通过 [`WorkManager`](https://developer.android.google.cn/reference/androidx/work/WorkManager?hl=de) 使用 [`enqueue()`](https://developer.android.google.cn/reference/androidx/work/WorkManager?hl=de#enqueue(androidx.work.WorkRequest)) 方法来调度它。

```kotlin
    WorkManager.getInstance(myContext).enqueue(uploadWorkRequest)
    
```

执行 Worker 的确切时间取决于 `WorkRequest` 中使用的约束以及系统优化。WorkManager 经过专门设计，能够在满足这些约束的情况下提供可能的最佳行为。







