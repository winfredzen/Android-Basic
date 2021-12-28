# Jetpack架构组件入门

本文的内容是学习[Android Jetpack Architecture Components: Getting Started](https://www.raywenderlich.com/6729-android-jetpack-architecture-components-getting-started)一文的总结

整个调用过程如下，使用到了LiveData、Navigation

viewmodel-> repository -> Dao -> DB





## Room

room的基本使用方式与前面文章中总结的类似

如下的数据库类`PeopleDatabase`

```kotlin
// 1
@Database(entities = [People::class], version = 1)
abstract class PeopleDatabase : RoomDatabase() {

  abstract fun peopleDao(): PeopleDao

  // 2
  companion object {
    private val lock = Any()
    //数据库名称
    private const val DB_NAME = "People.db"
    //单例
    private var INSTANCE: PeopleDatabase? = null

    // 3
    fun getInstance(application: Application): PeopleDatabase {
      synchronized(PeopleDatabase.lock) {
        if (PeopleDatabase.INSTANCE == null) {
          PeopleDatabase.INSTANCE =
              Room.databaseBuilder(application, PeopleDatabase::class.java, PeopleDatabase.DB_NAME)
                  .allowMainThreadQueries()
                  .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                      super.onCreate(db)
                      PeopleDatabase.INSTANCE?.let {
                        PeopleDatabase.prePopulate(it, PeopleInfoProvider.peopleList)
                      }
                    }
                  })
                  .build()
        }
        return PeopleDatabase.INSTANCE!!
      }
    }

    //导入数据到数据库中
    fun prePopulate(database: PeopleDatabase, peopleList: List<People>) {
      for (people in peopleList) {
        AsyncTask.execute { database.peopleDao().insert(people) }
      }
    }
    
  }

```

通过如下的方式获取数据库实例，获取dao

如在`PeopleRepository`中，如下：

```kotlin
class PeopleRepository(application: Application) {

    private val peopleDao: PeopleDao

    init {
        val peopleDatabase = PeopleDatabase.getInstance(application)
        peopleDao = peopleDatabase.peopleDao()
    }

    fun getAllPeople(): LiveData<List<People>> {
        return peopleDao.getAll()
    }

    fun insertPeople(people: People) {
        peopleDao.insert(people)
    }

    fun findPeople(id: Int): LiveData<People> {
        return peopleDao.find(id)
    }


    fun findPeople(name: String): LiveData<List<People>> {
        return peopleDao.findBy(name)
    }

}
```

注意方法中的返回值，有的为`LiveData`

所以我们的DAO中，返回的也是`LiveData`

![049](https://github.com/winfredzen/Android-Basic/blob/master/%E6%9E%B6%E6%9E%84/images/049.png)



## LiveData

> [`LiveData`](https://developer.android.com/reference/androidx/lifecycle/LiveData?hl=zh-cn) 是一种可观察的数据存储器类。与常规的可观察类不同，LiveData 具有生命周期感知能力，意指它遵循其他应用组件（如 Activity、Fragment 或 Service）的生命周期。这种感知能力可确保 LiveData 仅更新处于活跃生命周期状态的应用组件观察者。

> 创建 `LiveData` 的实例以存储某种类型的数据。这通常在 [`ViewModel`](https://developer.android.com/reference/androidx/lifecycle/ViewModel?hl=zh-cn) 类中完成。



## ViewModel

如`PeoplesListViewModel`

![050](https://github.com/winfredzen/Android-Basic/blob/master/%E6%9E%B6%E6%9E%84/images/050.png)



这里用到的是`MediatorLiveData`，其继承自`MutableLiveData`

```kotlin
public class MediatorLiveData<T> extends MutableLiveData<T>
```

它是一个特殊的LiveData，可以持有多个数据源



然后可以在Fragment、Activity中获取ViewModel，并观察LiveData

![051](https://github.com/winfredzen/Android-Basic/blob/master/%E6%9E%B6%E6%9E%84/images/051.png)

![052](https://github.com/winfredzen/Android-Basic/blob/master/%E6%9E%B6%E6%9E%84/images/052.png)





### LiveData Transformations

如下的例子，通过id获取people的详情

```kotlin
class PeopleDetailsViewModel(application: Application) : AndroidViewModel(application) {

    private val peopleRepository = getApplication<IMetApp>().getPeopleRepository()
    private val peopleId = MutableLiveData<Int>()

    // Maps people id to people details
    fun getPeopleDetails(id: Int): LiveData<People> {
        peopleId.value = id
        val peopleDetails = Transformations.switchMap<Int, People>(peopleId) { id ->
            peopleRepository.findPeople(id)
        }
        return peopleDetails
    }

}
```

> So, basically, it’s acting like a converter — it takes input from people `id` as an argument and returns a *People* object by searching into `PeopleRepository`



## Navigation

对Navigation还我还是不怎么理解，只是按文章中的步骤实现了效果

首先，它使用的一种叫做 ***Single-Activity Architecture***，即只有一个Activity，其它的都是Fragment（我是这么理解的）

几个Fragment之间的跳转如下：

![](https://github.com/winfredzen/Android-Basic/blob/master/%E6%9E%B6%E6%9E%84/images/053.png)



1.首先在res下创建navigation目录

![](https://github.com/winfredzen/Android-Basic/blob/master/%E6%9E%B6%E6%9E%84/images/054.png)

2.在navigation目录下创建`navigation_graph.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<navigation xmlns:android="http://schemas.android.com/apk/res/android"
  xmlns:app="http://schemas.android.com/apk/res-auto"
  xmlns:tools="http://schemas.android.com/tools"
  android:id="@+id/navigation_graph"
  app:startDestination="@id/peoplesListFragment">

  <fragment
    android:id="@+id/peoplesListFragment"
    android:name="com.raywenderlich.android.imet.ui.list.PeoplesListFragment"
    android:label="iMet"
    tools:layout="@layout/fragment_peoples_list">
    <action
      android:id="@+id/action_peoplesListFragment_to_addPeopleFragment"
      app:destination="@id/addPeopleFragment" />
    <action
      android:id="@+id/action_peoplesListFragment_to_peopleDetailsFragment"
      app:destination="@id/peopleDetailsFragment" />
  </fragment>
  <fragment
    android:id="@+id/addPeopleFragment"
    android:name="com.raywenderlich.android.imet.ui.add.AddPeopleFragment"
    android:label="Add People"
    tools:layout="@layout/fragment_add_people" />
  <fragment
    android:id="@+id/peopleDetailsFragment"
    android:name="com.raywenderlich.android.imet.ui.details.PeopleDetailsFragment"
    android:label="People Details"
    tools:layout="@layout/fragment_people_details" />
</navigation>
```

3.修改Activity的布局*activity_main.xml*

```xml
<?xml version="1.0" encoding="utf-8"?>
<android.support.constraint.ConstraintLayout
  xmlns:android="http://schemas.android.com/apk/res/android"
  xmlns:app="http://schemas.android.com/apk/res-auto"
  xmlns:tools="http://schemas.android.com/tools"
  android:layout_width="match_parent"
  android:layout_height="match_parent"
  tools:context=".ui.MainActivity">

  <fragment
    android:id="@+id/navigationHostFragment"
    android:name="androidx.navigation.fragment.NavHostFragment"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:defaultNavHost="true"
    app:navGraph="@navigation/navigation_graph" />

</android.support.constraint.ConstraintLayout>
```

> 1.在Activity中添加了一个`NavHostFragment`，作为默认的navigation host（或者说是默认的entry point）
> 
> 2.*NavHostFragment* is a part of the *Navigation Architecture Components* library
> 
> 3.`app:navGraph="@navigation/navigation_graph"`引用上面的navigation文件



3.更新MainActivity，处理*Back* or *Up* navigation

```kotlin
class MainActivity : AppCompatActivity() {

  //1
  private lateinit var navigationController: NavController

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)


    //2
    navigationController = findNavController(R.id.navigationHostFragment)
    NavigationUI.setupActionBarWithNavController(this, navigationController)
  }

  //3
  override fun onSupportNavigateUp() = navigationController.navigateUp()
}
```

> 1.The *NavController* is actually a part of a *NavigationHostFragment* attached to this Activity. This section initializes the `navigationController` instance using `findNavController()`. Then, the *NavigationUI* helper class ties the `navigationController` with the *ActionBar* in this Activity. This is necessary to allow the *ActionBar* to show a *Back* button whenever a child fragment is attached to this Activity.
> 
> 2.重写方法，让`navigationController`处理回退栈（*Fragment Back-Stack*）



4.如果跳转到另一个Fragment

例如，列表页跳转到`AddPeopleFragment`

```kotlin
    // Navigate to add people
    addFab.setOnClickListener {
      view.findNavController().navigate(R.id.action_peoplesListFragment_to_addPeopleFragment)
    }
```

> `R.id.action_peoplesListFragment_to_addPeopleFragment`是`navigation_graph.xml`中定义的



如果要传递参数该怎么办？

```kotlin
  override fun onItemClick(people: People, itemView: View) {
    val peopleBundle = Bundle().apply {
      putInt(getString(R.string.people_id), people.id)
    }
    view?.findNavController()
        ?.navigate(R.id.action_peoplesListFragment_to_peopleDetailsFragment, peopleBundle)
  }
```

如何回退到上一个页面？

```kotlin
Navigation.findNavController(view!!).navigateUp()
```




























