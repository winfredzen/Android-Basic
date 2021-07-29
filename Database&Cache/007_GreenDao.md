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





















