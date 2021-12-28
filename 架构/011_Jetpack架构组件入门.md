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

它是一个特殊的LiveData，可以从持有多个数据源



然后可以在Fragment、Activity中获取ViewModel，并观察LiveData

![051](https://github.com/winfredzen/Android-Basic/blob/master/%E6%9E%B6%E6%9E%84/images/051.png)

![052](https://github.com/winfredzen/Android-Basic/blob/master/%E6%9E%B6%E6%9E%84/images/052.png)






















