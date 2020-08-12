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



### 定义工作请求

#### 工作约束

您可以向工作添加 `Constraints`，以指明工作何时可以运行。

例如，您可以指定工作应仅在设备空闲且接通电源时运行。

下面的代码展示了如何将这些约束添加到 [`OneTimeWorkRequest`](https://developer.android.google.cn/reference/androidx/work/OneTimeWorkRequest?hl=de)。有关所支持约束的完整列表，请参阅 [`Constraints.Builder` 参考文档](https://developer.android.google.cn/reference/androidx/work/Constraints.Builder?hl=de)。

```kotlin

    // Create a Constraints object that defines when the task should run
    val constraints = Constraints.Builder()
            .setRequiresDeviceIdle(true)
            .setRequiresCharging(true)
            .build()

    // ...then create a OneTimeWorkRequest that uses those constraints
    val compressionWork = OneTimeWorkRequestBuilder<CompressWorker>()
            .setConstraints(constraints)
            .build()

    
```

如果指定了多个约束，您的任务将仅在满足所有约束时才会运行。

如果在任务运行期间某个约束不再得到满足，则 WorkManager 将停止工作器。当约束继续得到满足时，系统将重新尝试执行该任务。



#### 初始延迟

如果您的工作没有约束，或者工作加入队列时所有约束均已得到满足，则系统可能会选择立即运行任务。如果您不希望任务立即运行，则可以将工作指定为在经过一段最短初始延迟时间后再启动。

下面的示例展示了如何将任务设置为在加入队列后至少经过 10 分钟再运行。

```kotlin

    val uploadWorkRequest = OneTimeWorkRequestBuilder<UploadWorker>()
            .setInitialDelay(10, TimeUnit.MINUTES)
            .build()

    
```



#### 重试和退避政策

如果您需要让 WorkManager 重新尝试执行您的任务，可以从工作器返回 [`Result.retry()`](https://developer.android.google.cn/reference/androidx/work/ListenableWorker.Result?hl=de#retry())。

然后，系统会根据默认的退避延迟时间和政策重新调度您的工作。退避延迟时间指定了重试工作前的最短等待时间。[退避政策](https://developer.android.google.cn/reference/androidx/work/BackoffPolicy?hl=de)定义了在后续重试过程中，退避延迟时间随时间以怎样的方式增长；默认情况下按 [`EXPONENTIAL`](https://developer.android.google.cn/reference/androidx/work/BackoffPolicy?hl=de) 延长。

以下是自定义退避延迟时间和政策的示例。

```kotlin

    val uploadWorkRequest = OneTimeWorkRequestBuilder<UploadWorker>()
            .setBackoffCriteria(
                    BackoffPolicy.LINEAR,
                    OneTimeWorkRequest.MIN_BACKOFF_MILLIS,
                    TimeUnit.MILLISECONDS)
            .build()

    
```



#### 定义任务的输入/输出

您的任务可能需要数据以输入参数的形式传入，或者将数据返回为结果。例如，某个任务负责处理图像上传，它要求以要上传的图像的 URI 为输入，并且可能要求用已上传图像的网址作为输出。

输入和输出值以键值对的形式存储在 [`Data`](https://developer.android.google.cn/reference/androidx/work/Data?hl=de) 对象中。下面的代码展示了如何在 `WorkRequest` 中设置输入数据。

```kotlin

    // workDataOf (part of KTX) converts a list of pairs to a [Data] object.
    val imageData = workDataOf(Constants.KEY_IMAGE_URI to imageUriString)

    val uploadWorkRequest = OneTimeWorkRequestBuilder<UploadWorker>()
            .setInputData(imageData)
            .build()

    
```

`Worker` 类可通过调用 Worker.getInputData() 访问输入参数。

类似地，`Data` 类可用于输出返回值。如需返回 `Data` 对象，请将它添加到 `Result.success()` 或 `Result.failure()` 时的 `Result` 中，如下所示。

```kotlin
    class UploadWorker(appContext: Context, workerParams: WorkerParameters)
        : Worker(appContext, workerParams) {

        override fun doWork(): Result {

                // Get the input
                val imageUriInput = getInputData().getString(Constants.KEY_IMAGE_URI)
                // TODO: validate inputs.
                // Do the work
                val response = uploadFile(imageUriInput)

                // Create the output of the work
                val outputData = workDataOf(Constants.KEY_IMAGE_URL to response.imageUrl)

                // Return the output
                return Result.success(outputData)

        }
    }

    
```

>  **注意**：按照设计，`Data` 对象应该很小，值可以是字符串、基元类型或数组变体。如果需要将更多数据传入和传出工作器，应该将数据放在其他位置，例如 Room 数据库。Data 对象的大小上限为 10KB。



#### 标记工作

您可以通过为任意 [`WorkRequest`](https://developer.android.google.cn/reference/androidx/work/WorkRequest?hl=de) 对象分配标记字符串，按逻辑对任务进行分组。这样您就可以对使用特定标记的所有任务执行操作。

例如，[`WorkManager.cancelAllWorkByTag(String)`](https://developer.android.google.cn/reference/androidx/work/WorkManager?hl=de#cancelAllWorkByTag(java.lang.String)) 会取消使用特定标记的所有任务，而 [`WorkManager.getWorkInfosByTagLiveData(String)`](https://developer.android.google.cn/reference/androidx/work/WorkManager?hl=de#getWorkInfosByTagLiveData(java.lang.String)) 会返回 [`LiveData`](https://developer.android.google.cn/reference/androidx/lifecycle/LiveData?hl=de) 和具有该标记的所有任务的状态列表。

以下代码展示了如何使用 [`WorkRequest.Builder.addTag(String)`](https://developer.android.google.cn/reference/androidx/work/WorkRequest.Builder?hl=de#addTag(java.lang.String)) 向任务添加“cleanup”标记：

```kotlin
    val cacheCleanupTask =
            OneTimeWorkRequestBuilder<CacheCleanupWorker>()
        .setConstraints(constraints)
        .addTag("cleanup")
        .build()
    
```



### 工作状态和观察工作

#### 工作状态

在工作的整个生命周期内，它会经历多个不同的 [`State`](https://developer.android.google.cn/reference/androidx/work/WorkInfo.State?hl=de)。[在本文后面的部分中](https://developer.android.google.cn/topic/libraries/architecture/workmanager/how-to/states-and-observation?hl=de#observing)，我们会探讨如何观察这些变化。不过，您首先应对逐一了解各个状态：

- 如果有尚未完成的[前提性工作](https://developer.android.google.cn/topic/libraries/architecture/workmanager/how-to/chain-work?hl=de)，工作处于 [`BLOCKED`](https://developer.android.google.cn/reference/androidx/work/WorkInfo.State?hl=de#BLOCKED) `State`。
- 如果工作能够在满足 [`Constraints`](https://developer.android.google.cn/reference/androidx/work/Constraints?hl=de) 和时机条件后立即运行，将被视为处于 [`ENQUEUED`](https://developer.android.google.cn/reference/androidx/work/WorkInfo.State?hl=de#ENQUEUED)。
- 当工作器在活跃执行时，其处于 [`RUNNING`](https://developer.android.google.cn/reference/androidx/work/WorkInfo.State?hl=de#RUNNING) `State`。
- 返回 [`Result.success()`](https://developer.android.google.cn/reference/androidx/work/ListenableWorker.Result?hl=de#success()) 的工作器会被视为 [`SUCCEEDED`](https://developer.android.google.cn/reference/androidx/work/WorkInfo.State?hl=de#SUCCEEDED)。这是一种终止 `State`；只有 [`OneTimeWorkRequest`](https://developer.android.google.cn/reference/androidx/work/OneTimeWorkRequest?hl=de) 可以进入这种 `State`。
- 相反，返回 [`Result.failure()`](https://developer.android.google.cn/reference/androidx/work/ListenableWorker.Result?hl=de#failure()) 的工作器会被视为 [`FAILED`](https://developer.android.google.cn/reference/androidx/work/WorkInfo.State?hl=de#FAILED)。这也是一种终止 `State`；只有 [`OneTimeWorkRequest`](https://developer.android.google.cn/reference/androidx/work/OneTimeWorkRequest?hl=de) 可以进入这种 `State`。所有依赖工作也会被标记为 `FAILED`，并且不会运行。
- 当您明确[取消](https://developer.android.google.cn/topic/libraries/architecture/workmanager/how-to/cancel-stop-work?hl=de)尚未终止的 `WorkRequest` 时，它会进入 [`CANCELLED`](https://developer.android.google.cn/reference/androidx/work/WorkInfo.State?hl=de#CANCELLED) `State`。所有依赖工作也会被标记为 `CANCELLED`，并且不会运行。



#### 观察您的工作

将工作加入队列后，您可以通过 WorkManager 检查其状态。相关信息在 [`WorkInfo`](https://developer.android.google.cn/reference/androidx/work/WorkInfo?hl=de) 对象中提供，包括工作的 `id`、标签、当前 [`State`](https://developer.android.google.cn/reference/androidx/work/WorkInfo.State?hl=de) 和任何输出数据。

您通过以下三种方式之一来获取 `WorkInfo`：

- 对于特定的 [`WorkRequest`](https://developer.android.google.cn/reference/androidx/work/WorkRequest?hl=de)，您可以利用 [`WorkManager.getWorkInfoById(UUID)`](https://developer.android.google.cn/reference/androidx/work/WorkManager?hl=de#getWorkInfoById(java.util.UUID)) 或 [`WorkManager.getWorkInfoByIdLiveData(UUID)`](https://developer.android.google.cn/reference/androidx/work/WorkManager?hl=de#getWorkInfoByIdLiveData(java.util.UUID)) 来通过 `WorkRequest` `id` 检索其 `WorkInfo`。
- 对于指定的[标记](https://developer.android.google.cn/topic/libraries/architecture/workmanager/how-to/define-work?hl=de#tag)，您可以利用 [`WorkManager.getWorkInfosByTag(String)`](https://developer.android.google.cn/reference/androidx/work/WorkManager?hl=de#getWorkInfosByTag(java.lang.String)) 或 [`WorkManager.getWorkInfosByTagLiveData(String)`](https://developer.android.google.cn/reference/androidx/work/WorkManager?hl=de#getWorkInfosByTagLiveData(java.lang.String)) 检索所有匹配的 `WorkRequest` 的 `WorkInfo` 对象。
- 对于[唯一工作名称](https://developer.android.google.cn/topic/libraries/architecture/workmanager/how-to/unique-work?hl=de)，您可以利用 [`WorkManager.getWorkInfosForUniqueWork(String)`](https://developer.android.google.cn/reference/androidx/work/WorkManager?hl=de#getWorkInfosForUniqueWork(java.lang.String)) 或 [`WorkManager.getWorkInfosForUniqueWorkLiveData(String)`](https://developer.android.google.cn/reference/androidx/work/WorkManager?hl=de#getWorkInfosForUniqueWorkLiveData(java.lang.String)) 检索所有匹配的 `WorkRequest` 的 `WorkInfo` 对象。

利用每个方法的 [`LiveData`](https://developer.android.google.cn/topic/libraries/architecture/livedata?hl=de) 变量，您可以通过注册监听器来观察 `WorkInfo` 的变化。例如，如果您想要在某项工作成功完成后向用户显示消息，您可以进行如下设置：



```kotlin
    WorkManager.getInstance(myContext).getWorkInfoByIdLiveData(uploadWorkRequest.id)
            .observe(lifecycleOwner, Observer { workInfo ->
                if (workInfo != null && workInfo.state == WorkInfo.State.SUCCEEDED) {
                    displayMessage("Work finished!")
                }
            })
    
```

























