# Room

参考官方文档：[使用 Room 将数据保存到本地数据库](https://developer.android.com/training/data-storage/room#kts)

对kotlin而言，需要添加的依赖如下：

```groovy
    def room_version = "2.3.0"
    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")

    // To use Kotlin annotation processing tool (kapt)
    kapt("androidx.room:room-compiler:$room_version")
```

还需要添加`'kotlin-kapt'`插件

我在使用的过程中，有遇到如下的问题

1.报`cannot find implementation for XXX. XXX_Impl does not exist`

![044](https://github.com/winfredzen/Android-Basic/blob/master/%E6%9E%B6%E6%9E%84/images/045.png)

解决方式，就是需要添加上面的依赖和插件

2.`Room - Schema export directory is not provided to the annotation processor so we cannot export the schema`

![](https://github.com/winfredzen/Android-Basic/blob/master/%E6%9E%B6%E6%9E%84/images/046.png)

参考：

+  [Room - Schema export directory is not provided to the annotation processor so we cannot export the schema](https://stackoverflow.com/questions/44322178/room-schema-export-directory-is-not-provided-to-the-annotation-processor-so-we)

一种解决方式是：

添加`exportSchema = false`

```kotlin
@Database(entities = [ListCategory::class], version = 1, exportSchema = false)
```

3.操作数据时没有在子线程

![](https://github.com/winfredzen/Android-Basic/blob/master/%E6%9E%B6%E6%9E%84/images/047.png)



总结下Room的基本使用

1.实体类（模型类），使用`@Entity(tableName = "list_categories")`注解

```kotlin
@Entity(tableName = "list_categories")
data class ListCategory(
    @ColumnInfo(name = "category_name") var categoryName: String,
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) var id: Long = 0)
```

2.DAO类，表示访问数据库的方法

```kotlin
@Dao
interface ListCategoryDao {

    @Query("SELECT * FROM list_categories")
    fun getAll(): List<ListCategory>

    @Insert
    fun insertAll(vararg listCategories: ListCategory)

}
```

3.数据库，一般为抽象类

```kotlin
@Database(entities = [ListCategory::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun listCategoryDao(): ListCategoryDao

}
```

获取已创建的数据库实例，例如在`Application`中获取数据库实例

```kotlin
class ListMasterApplication : Application() {

  companion object {
    var database: AppDatabase? = null
  }

  override fun onCreate() {
    super.onCreate()

    ListMasterApplication.database = Room.databaseBuilder(this,
      AppDatabase::class.java,
      "list-master-db").build()

  }

```



数据查询，需要放在子线程，如下的例子：

![048](https://github.com/winfredzen/Android-Basic/blob/master/%E6%9E%B6%E6%9E%84/images/048.png)



上面内容来自：

+ [Data Persistence With Room](https://www.raywenderlich.com/69-data-persistence-with-room)

代码位置：[ListMaster](https://github.com/winfredzen/Android-Basic/tree/master/%E6%9E%B6%E6%9E%84/code/ListMaster)




























