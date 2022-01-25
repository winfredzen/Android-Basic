# 协程

## 协程+Retrofit+ViewModel+LiveData+DataBinding

为什么使用ViewModel？

ViewModel会保存界面上的状态在前面的例子上，如果在获取网络数据后，旋转屏幕后，会发现界面显示就不一样了

![019](https://github.com/winfredzen/Android-Basic/blob/master/Kotlin/images/019.png)

![020](https://github.com/winfredzen/Android-Basic/blob/master/Kotlin/images/020.png)

先启动dataBinding的支持

```groovy
    dataBinding {
        enabled = true
    }
```

## Layout

```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools">

    <data>
        <variable
            name="viewModel"
            type="com.example.coroutine.viewmodel.MainViewModel" />
    </data>

    <androidx.constraintlayout.widget.ConstraintLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:context=".activity.MainActivity">

        <Button
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:id="@+id/submitButton"
            android:text="提交"
            app:layout_constraintTop_toTopOf="@+id/guideline"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintLeft_toLeftOf="parent"
            app:layout_constraintRight_toRightOf="parent" />

        <TextView
            android:id="@+id/textView"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="@{viewModel.todoLiveData.title}"
            android:textSize="18sp"
            app:layout_constraintBottom_toTopOf="@+id/guideline"
            app:layout_constraintLeft_toLeftOf="parent"
            app:layout_constraintRight_toRightOf="parent"
            app:layout_constraintTop_toTopOf="parent" />

        <androidx.constraintlayout.widget.Guideline
            android:id="@+id/guideline"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:orientation="horizontal"
            app:layout_constraintGuide_percent="0.5" />

    </androidx.constraintlayout.widget.ConstraintLayout>
</layout>
```

注意viewModel与TextView的text进行了绑定`android:text="@{viewModel.todoLiveData.title}"`

## Repository

```kotlin
class TodoRepository {
    suspend fun getTodo(id: Int): Todo {
        return userServiceApi.retrieveTodoById(id)
    }
}
```

调用网路请求

## ViewModel

```kotlin
class MainViewModel : ViewModel() {

    val todoLiveData = MutableLiveData<Todo>()

    private val todoRepository = TodoRepository()

    fun getTodo(id: Int) {
        viewModelScope.launch {
            todoLiveData.value = todoRepository.getTodo(id)
        }
    }

}
```

## Activity

```kotlin
class MainActivity07 : AppCompatActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityMain07Binding>(this, R.layout.activity_main_07)
        binding.viewModel = mainViewModel
        binding.lifecycleOwner = this

        binding.submitButton.also {
            it.setOnClickListener {
                mainViewModel.getTodo(1)
            }
        }

    }
}
```

这里使用的是`MainViewModel by viewModels()`获取的viewModel

与原理使用`ViewModelProvider`获取viewModel的方式有很多的区别

`viewModels()`可能会报错，参考:

+ [how to get viewModel by viewModels? (fragment-ktx)](https://stackoverflow.com/questions/56748334/how-to-get-viewmodel-by-viewmodels-fragment-ktx)

可能需要添加：

```groovy
    kotlinOptions {
        jvmTarget = "1.8"
    }
```
