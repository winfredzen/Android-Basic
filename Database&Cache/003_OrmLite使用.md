# OrmLite使用

在[mvnrepository](https://mvnrepository.com/)搜索ormlite，导入

```groovy
    implementation group: 'com.j256.ormlite', name: 'ormlite-android', version: '5.3'
    implementation group: 'com.j256.ormlite', name: 'ormlite-core', version: '5.3
```

例子来源于：

+ [ORMLite With Android, Store Data Locally](https://medium.com/my-android-acadamy/ormlite-with-android-store-data-locally-ce44704deadf)

步骤：

1.bean，使用注解，标明表名和字段名

2.继承`OrmLiteSqliteOpenHelper`，创建自己的helper

3.获取DAO，进行CRUD的操作

例如本文中的`private Dao<UserItemDB, Long> userItemDBS;`

> Once you have annotated your classes and defined your `ConnectionSource` you will need to create the Data Access Object (DAO) class(es), each of which will handle all database operations for a single persisted class. Each DAO has two generic parameters: the class we are persisting with the DAO, and the class of the ID-column that will be used to identify a specific database row. If your class does not have an ID field, you can put `Object` or `Void` as the 2nd argument. For example, in the above `Account` class, the "name" field is the ID column (id = true) so the ID class is `String`.
>
> + 第一个参数 - 表示我们需要持久化的类
> + 第二个参数 - 表示ID列的类，表示一个数据的一行



**查询**

查询使用QueryBuilder，参考[3. Custom Statement Builder](https://ormlite.com/javadoc/ormlite-core/doc-files/ormlite.html#QueryBuilder-Basics)



最后的效果：

![001](https://github.com/winfredzen/Android-Basic/blob/master/Database%26Cache/images/001.png)

![002](https://github.com/winfredzen/Android-Basic/blob/master/Database%26Cache/images/002.png)



## 其它

+ [Android OrmLite数据库框架详解](https://www.jianshu.com/p/fd8594342334)
+ [Android ORMLite 框架的入门用法](https://blog.csdn.net/lmj623565791/article/details/39121377)











