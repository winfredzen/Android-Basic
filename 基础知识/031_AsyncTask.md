# AsyncTask

`AsyncTask`异步任务类，比Handler更轻量级，更适合简单的异步操作。内部封装了Handler，在使用AsyncTask类进行刷新控件的刷新操作时，不用再额外创建声明Handler，可以直接使用`AsyncTask`内部封装好的几个方法



使用后台线程最简便的方式是使用`AsyncTask`工具类，`AsyncTask`是一个抽象类，例如如下的例子：

```java
private class FetchItemsTask extends AsyncTask<Void, Void, List<GalleryItem>>
```

在继承的时候，可以为`AsyncTask`指定3个泛型参数：

+ Params - 执行`AsyncTask`需要传入的参数，传入到`execute(...)`方法

  ```java
          AsyncTask<String,Void,Void> task = new AsyncTask<String,Void,Void>() {
              public Void doInBackground(String... params) {
                  for (String parameter : params) {
                      Log.i(TAG, "Received parameter: " + parameter);
                  }
                  return null;
              }
          };
  ```

+ Progress - 可指定发送进度更新需要的类型

  ```java
          AsyncTask<Void,Integer,Void> haveABaby = new AsyncTask<Void,Integer,Void>() {
              public Void doInBackground(Void... params) {
                  while (!babyIsBorn()) {
                      Integer weeksPassed = getNumberOfWeeksPassed();
                      publishProgress(weeksPassed);
                      patientlyWaitForBaby();
                  }
                  public void onProgressUpdate (Integer...params){
                      int progress = params[0];
                      gestationProgressBar.setProgress(progress);
                  }
                  /* call when you want to execute the AsyncTask */
                  haveABaby.execute();
              }
          };
  ```

  > 进度更新通常发生在后台进程执行中途。问题是，在后台进程中无法完成必要的UI更新。因此AsyncTask提供了`publishProgress(...)`和`onProgressUpdate(...)`方法。
  >
  > 其工作方式是这样的 : 在后台线程中，从`doInBackground(...)` 方法中调用`publishProgress(...)`方法。这样`onProgressUpdate(...)`方法便能够在UI线程上调用。因 此，在`onProgressUpdate(...)`方法中执行UI更新就可行了，但必须在`doInBackground(...)` 方法中使用`publishProgress(...)`方法对它们进行管控。

+ Result - 返回值类型



如下面的一个例子：

```java
    private class FetchItemsTask extends AsyncTask<Void, Void, List<GalleryItem>> {

        @Override
        protected List<GalleryItem> doInBackground(Void... voids) {
            return new FlickrFetchr().fetchItems();//获取数据
        }


        @Override
        protected void onPostExecute(List<GalleryItem> galleryItems) {
            mItems = galleryItems;
            setAdapter();//设置UI
        }

    }
```

+ `onPreExecute()` - 在后台任务执行之前调用
+ `doInBackground(Params... params)` - 在子线程中运行，应该在这里去处理所有耗时任务
+ `onPostExecute(Result result)` - 当后台任务执行完毕并通过`return`语句进行返回时，这个方法就很快被调用
+ `onProgressUpdate(Progress... values)` - 当在后台任务中调用了`publishProgress(Progress... values)`方法后，`onProgressUpdate(Progress... values)`方法就会很快被调用
+ `onCancelled() ` - 取消



另外启动任务需要调用`execute()`方法

```java
new FetchItemsTask().execute();
```





## 清理AsyncTask

`AsyncTask.cancel(boolean)`方法有两种工作模式:粗暴的和温和的。如果调用`cancel (false)`方法，它只是简单地设置`isCancelled()`的状态为true。随后，AsyncTask会检查 `isCancelled()`状态，然后选择提前结束运行。

然而，如果调用`cancel(true)`方法，它会立即终止`doInBackground(...)`方法当前所在的线程。`AsyncTask.cancel(true)`方法停止`AsyncTask`的方式简单粗暴，如果可能，应尽量避免。



## AsyncTask的替代方案

> 在使用AsyncTask加载数据时，如果遇到设备配置变化，比如设备旋转，你得负责管理它生命周期，同时还要保存好数据，不让其因旋转丢失。虽然调用Fragment的setRetainInstance (true)方法来保存数据可以解决问题，但它不是万能的。很多时候，你还得介入，编写特殊场景应对代码，让应用无懈可击。这些特殊场景有:用户在AsyncTask运行时按后退键，以及启动 AsyncTask的fragment因内存紧张而被销毁。
>
> 使用Loader是另一种可行的解决方案。它可以代劳很多(并非全部)棘手的事情。Loader用来从某些数据源加载数据(对象)。数据源可以是磁盘、数据库、ContentProvider、网络， 甚至是另一进程。
>
> `AsyncTaskLoader`是个抽象Loader。它可以使用AsyncTask把数据加载工作转移到其他线程上。我们创建的loader类几乎都是`AsyncTaskLoader`的子类。`AsyncTaskLoader`能在不阻塞主线程的前提下获取到数据，并把结果发送给目标对象。
>
> 相比AsyncTask，为什么要推荐使用loader呢?最重要的原因是，遇到类似设备旋转这样的场景时，`LoaderManager`会帮我们妥善管理loader及其加载的数据。而且，`LoaderManager`还负责启动和停止loader，以及管理loader的生命周期。怎么样?理由充足吧!
>
> 设备配置改变后，如果初始化一个已经加载完数据的loader，它能立即提交数据，而不是再次尝试获取数据。无论fragment是否得到保留，它都会这样做。这下放心多了，从此再也不用考 虑因保留fragment而产生的生命周期问题了。



## AsyncTask与线程

AsyncTask的工作方式主要应用于那些短暂且较少重复的任务

如果创建了大量的AsyncTask，或者长时间在运行AsyncTask，那么很可能就是错用了它

> 有一个技术层面的理由更让人信服:在Android 3.2系统版本中，AsyncTask的内部实现有了 重大变化。自Android 3.2版本起，AsyncTask不再为每一个AsyncTask实例单独创建线程。相反， 它使用一个Executor在单一的后台线程上运行所有AsyncTask后台任务。这意味着每个AsyncTask都需要排队顺序执行。显然，长时间运行的AsyncTask会阻塞其他AsyncTask。
>
> 使用一个线程池executor虽然可安全地并发运行多个AsyncTask，但不推荐这么做。如果真 的考虑这么做，最好自己处理线程相关的工作，必要时就使用Handler与主线程通信