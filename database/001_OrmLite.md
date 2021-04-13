# OrmLite

`OrmLite`的官网介绍[OrmLite - Lightweight Object Relational Mapping (ORM) Java Package](https://ormlite.com/)

对一些主要的内容进行摘要

1.使用注解对需要持久化的class标注

```java
@DatabaseTable(tableName = "accounts")
public class Account {
    @DatabaseField(id = true)
    private String name;
    
    @DatabaseField(canBeNull = false)
    private String password;
    ...
    Account() {
    	// all persisted classes must define a no-arg constructor with at least package visibility
      //需提供一个无参数的构造方法
    }
    ...    
}
```



## 在Android中使用OrmLite

参考：

+ [4. Using With Android](https://ormlite.com/javadoc/ormlite-core/doc-files/ormlite.html#Use-With-Android)

需要下载`ormlite-core.jar`和`ormlite-android.jar`，而不是`ormlite-jdbc.jar`版本

1.继承`OrmLiteSqliteOpenHelper`，创建你自己的database helper 类。该类可以在app安装时，创建和更新database，并且可以提供被其它类使用的DAO类。

必须实现两个方法

+ `onCreate(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource)`
+ `onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion)`

`onCreate`在App被第一次安装时创建database。`onUpgrade`处理数据库的升级

`DatabaseHelper`的例子参考[example projects online](http://ormlite.com/android/examples/)

2.The helper can be kept open across all activities in your app with the same SQLite database connection reused by all threads. If you open multiple connections to the same database, stale data and unexpected results may occur. We recommend using the `OpenHelperManager` to monitor the usage of the helper – it will create it on the first access, track each time a part of your code is using it, and then it will close the last time the helper is released.

> 同一个SQLite database connect可以被所有的线程使用，helper可以在所有的Activity中保持open。
>
> 如果open多个connection到同一个database，会有意想不到的结果
>
> 推荐使用`OpenHelperManager`来监控helper的使用



3.定义好database helper后，就可以在Activity中使用了。一个简单的方式是继承`OrmLiteBaseActivity`，使用`OpenHelperManager`，该类提供了`getHelper()`方法来获取database helper，然后它自动的在 `onCreate()`创建helpe，在`onDestroy()`方法中销毁

4.如果不继承`OrmLiteBaseActivity`。可通过`OpenHelperManager.getHelper(Context context, Class openHelperClass)`方法来获取helper，在使用完成后，通过`OpenHelperManager.release()`来释放

```java
 	
private DatabaseHelper databaseHelper = null;

@Override
protected void onDestroy() {
    super.onDestroy();
    if (databaseHelper != null) {
        OpenHelperManager.releaseHelper();
        databaseHelper = null;
    }
}

private DBHelper getHelper() {
    if (databaseHelper == null) {
        databaseHelper =
            OpenHelperManager.getHelper(this, DatabaseHelper.class);
    }
    return databaseHelper;
}
```

























