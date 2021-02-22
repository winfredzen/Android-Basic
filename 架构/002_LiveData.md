## LiveData

官网说明：

+ [LiveData 概览](https://developer.android.com/topic/libraries/architecture/livedata?hl=zh-cn)

> [`LiveData`](https://developer.android.com/reference/androidx/lifecycle/LiveData?hl=zh-cn) 是一种可观察的数据存储器类。与常规的可观察类不同，LiveData 具有生命周期感知能力，意指它遵循其他应用组件（如 Activity、Fragment 或 Service）的生命周期。这种感知能力可确保 LiveData 仅更新处于活跃生命周期状态的应用组件观察者。

> 如果观察者（由 [`Observer`](https://developer.android.com/reference/androidx/lifecycle/Observer?hl=zh-cn) 类表示）的生命周期处于 [`STARTED`](https://developer.android.com/reference/androidx/lifecycle/Lifecycle.State?hl=zh-cn#STARTED) 或 [`RESUMED`](https://developer.android.com/reference/androidx/lifecycle/Lifecycle.State?hl=zh-cn#RESUMED) 状态，则 LiveData 会认为该观察者处于活跃状态。LiveData 只会将更新通知给活跃的观察者。为观察 [`LiveData`](https://developer.android.com/reference/androidx/lifecycle/LiveData?hl=zh-cn) 对象而注册的非活跃观察者不会收到更改通知。

> LiveData 遵循观察者模式。当底层数据发生变化时，LiveData 会通知 [`Observer`](https://developer.android.com/reference/androidx/lifecycle/Observer?hl=zh-cn) 对象。您可以整合代码以在这些 `Observer` 对象中更新界面。这样一来，您无需在每次应用数据发生变化时更新界面，因为观察者会替您完成更新。

**使用 LiveData 对象**

> + 创建 `LiveData` 的实例以存储某种类型的数据。这通常在 [`ViewModel`](https://developer.android.com/reference/androidx/lifecycle/ViewModel?hl=zh-cn) 类中完成。
> + 创建可定义 [`onChanged()`](https://developer.android.com/reference/androidx/lifecycle/Observer?hl=zh-cn#onChanged(T)) 方法的 [`Observer`](https://developer.android.com/reference/androidx/lifecycle/Observer?hl=zh-cn) 对象，该方法可以控制当 `LiveData` 对象存储的数据更改时会发生什么。通常情况下，您可以在界面控制器（如 Activity 或 Fragment）中创建 `Observer` 对象。
> + 使用 [`observe()`](https://developer.android.com/reference/androidx/lifecycle/LiveData?hl=zh-cn#observe(android.arch.lifecycle.LifecycleOwner, android.arch.lifecycle.Observer)) 方法将 `Observer` 对象附加到 `LiveData` 对象。`observe()` 方法会采用 [`LifecycleOwner`](https://developer.android.com/reference/androidx/lifecycle/LifecycleOwner?hl=zh-cn) 对象。这样会使 `Observer` 对象订阅 `LiveData` 对象，以使其收到有关更改的通知。通常情况下，您可以在界面控制器（如 Activity 或 Fragment）中附加 `Observer` 对象。



如[Incorporate Lifecycle-Aware Components](https://developer.android.com/codelabs/android-lifecycles#3)中的计时器效果：

`LiveDataTimerViewModel`：

```java
public class LiveDataTimerViewModel extends ViewModel {

    private static final int ONE_SECOND = 1000;

    private MutableLiveData<Long> mElapsedTime = new MutableLiveData<>();

    private long mInitialTime;
    private final Timer timer;

    public LiveDataTimerViewModel() {
        mInitialTime = SystemClock.elapsedRealtime();
        timer = new Timer();

        // Update the elapsed time every second.
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                final long newValue = (SystemClock.elapsedRealtime() - mInitialTime) / 1000;
                // setValue() cannot be called from a background thread so post to main thread.
                mElapsedTime.postValue(newValue);
            }
        }, ONE_SECOND, ONE_SECOND);

    }

    public LiveData<Long> getElapsedTime() {
        return mElapsedTime;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        timer.cancel();
    }
}
```

`ChronoActivity3`:

```java
public class ChronoActivity3 extends AppCompatActivity {

    private LiveDataTimerViewModel mLiveDataTimerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.chrono_activity_3);

        mLiveDataTimerViewModel = new ViewModelProvider(this).get(LiveDataTimerViewModel.class);

        subscribe();
    }

    private void subscribe() {
        final Observer<Long> elapsedTimeObserver = new Observer<Long>() {
            @Override
            public void onChanged(@Nullable final Long aLong) {
                String newText = ChronoActivity3.this.getResources().getString(
                        R.string.seconds, aLong);
                ((TextView) findViewById(R.id.timer_textview)).setText(newText);
                Log.d("ChronoActivity3", "Updating timer");
            }
        };

        mLiveDataTimerViewModel.getElapsedTime().observe(this, elapsedTimeObserver);
    }
}
```

> **注意**：您必须调用 [`setValue(T)`](https://developer.android.com/reference/androidx/lifecycle/MutableLiveData?hl=zh-cn#setValue(T)) 方法以从主线程更新 `LiveData` 对象。如果在工作器线程中执行代码，您可以改用 [`postValue(T)`](https://developer.android.com/reference/androidx/lifecycle/MutableLiveData?hl=zh-cn#postValue(T)) 方法来更新 `LiveData` 对象。

![007](https://github.com/winfredzen/Android-Basic/blob/master/%E6%9E%B6%E6%9E%84/images/007.png)



