# GreenDao

[greenDAO](https://github.com/greenrobot/greenDAO)

> greenDAO’s essence is to offer an object oriented interface to data stored in the relational database SQLite. Just define for data model, and greenDAO will create Java data objects (entities) and DAOs ([data access objects](http://en.wikipedia.org/wiki/Data_access_object)). This will save you a lot of boring code that just moves data back and forth.
>
> ![009](https://github.com/winfredzen/Android-Basic/blob/master/Database%26Cache/images/009.png)
>

在关系型数据库和对象之间做一个映射，这样在操作数据库时，就不用跟复杂的SQL语句打交道了，像操作对象一样就可以了

**核心概念**

+ **Entities** - Persistable objects. Usually, entities are objects representing a database row using standard Java properties (like a POJO or a JavaBean).

+ **[DAOs](http://greenrobot.org/files/greendao/javadoc/current/org/greenrobot/greendao/AbstractDao.html)** - Data access objects (DAOs) persists and queries for entities. For each entity, greenDAO generates a DAO. It has more persistence methods than DaoSession, for example: count, loadAll, and insertInTx.

  数据访问对象（某表的操作）

+ **[DaoSession](http://greenrobot.org/files/greendao/javadoc/current/org/greenrobot/greendao/AbstractDaoSession.html)** - Manages all available DAO objects for a specific schema, which you can acquire using one of the getter methods. DaoSession provides also some generic persistence methods like insert, load, update, refresh and delete for entities. Lastly, a DaoSession objects also keeps track of an identity scope. For more details, have a look at the [session documentation](http://greenrobot.org/greendao/documentation/sessions/).

  由连接生成的会话

+ **[DaoMaster](http://greenrobot.org/files/greendao/javadoc/current/org/greenrobot/greendao/AbstractDaoMaster.html)** - The entry point for using greenDAO. DaoMaster holds the database object (SQLiteDatabase) and manages DAO classes (not objects) for a specific schema. It has static methods to create the tables or drop them. Its inner classes OpenHelper and DevOpenHelper are SQLiteOpenHelper implementations that create the schema in the SQLite database.

  数据库连接对象



![010](https://github.com/winfredzen/Android-Basic/blob/master/Database%26Cache/images/010.png)



**Entities**相关内容可参考：

+ [Modelling entities](https://greenrobot.org/greendao/documentation/modelling-entities/)

`@Entity`注解实体

如：

```java
@Entity
public class User {
    @Id(autoincrement = true)
    private Long id;
 
    @Property(nameInDb = "USERNAME")
    private String name;
 
    @NotNull
    private int repos;
 
    @Transient
    private int tempUsageCount;
 
    ...
}
```

> **主键**
>
> The **@Id** annotation selects a **long**/ **Long** property as the entity ID. In database terms, it’s the primary key.
>
> Currently, entities must have a **long** or **Long** property as their primary key. This is recommended practice for [Android](http://developer.android.com/reference/android/widget/CursorAdapter.html) and [SQLite](http://www.sqlite.org/autoinc.html). 实体需有一个long或者Long属性，最为主键



创建实体类后，添加`@Entity`注解，选择`Build -> Make project`会自动生成`DaoMaster` 、 `DaoSession`  、`XXXXDao`相关代码，如下：

![011](https://github.com/winfredzen/Android-Basic/blob/master/Database%26Cache/images/011.png)



通过`XXXDao`进行数据的CRUD操作

**1.添加数据**

添加数据，可通过如下的方法：

```java
public void insertInTx(Iterable<T> entities)
public long insert(T entity)
......  
```



**2.查询数据**

可使用`QueryBuilder`，可参考：

+ [Queries](https://greenrobot.org/greendao/documentation/queries/)

如：

```java
QueryBuilder<User> qb = userDao.queryBuilder();
qb.where(Properties.FirstName.eq("Joe"),
qb.or(Properties.YearOfBirth.gt(1970),
qb.and(Properties.YearOfBirth.eq(1970), Properties.MonthOfBirth.ge(10))));
List<User> youngJoes = qb.list();
```

表达的意思是：

> First name **is** "Joe" **AND** (year of birth **is** greater than 1970 **OR** (year of birth **is** 1970 **AND** month of birth **is** equal **to** **or** greater than 10))



**3.更新数据**

```java
public void update(T entity)
```



**4.删除数据**

```java
public void delete(T entity)
```



**数据库加密操作**

数据库加密可参考：

+ [Database Encryption](https://greenrobot.org/greendao/documentation/database-encryption/)



使用[sqlcipher](https://www.zetetic.net/sqlcipher/)，android集成可参考：

+ [SQLCipher for Android Application Integration](https://www.zetetic.net/sqlcipher/sqlcipher-for-android/)

```groovy
implementation 'net.zetetic:android-database-sqlcipher:4.4.3@aar'
```



然后通过`.getEncryptedWritableDb(<password>)`获取数据库对象，如下：

```java
DevOpenHelper helper = new DevOpenHelper(this, "notes-db-encrypted.db");
Database db = helper.getEncryptedWritableDb("<your-secret-password>");
daoSession = new DaoMaster(db).newSession();
```



## 例子

+ [GreenDaoDemo](https://github.com/winfredzen/Android-Basic/tree/master/Database%26Cache/code/GreenDaoDemo)







































