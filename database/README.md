# DataBase

## 数据库升级降级

数据库升级降级的原理：

+ [Android SQLite数据库版本升级原理解析](https://www.cnblogs.com/liqw/p/4264925.html)
+ [Android SQLite (二.数据库创建，升级及降级)](https://www.jianshu.com/p/65923fa3e3dc)



### 自动升级降级

在使用OrmLite数据的开源封装类[wobuaihuangjun](https://github.com/wobuaihuangjun)/**[DatabaseManager](https://github.com/wobuaihuangjun/DatabaseManager)**中，有自动升级降级的例子

大致原理是，在SQLite中有一个`SQLITE_MASTER`表，参考[SQLite 中的 sqlite_master 表](https://hanks.pub/2016/05/05/sqlite-master/)

```sql
CREATE TABLE sqlite_master (
  type text,
  name text,
  tbl_name text,
  rootpage integer,
  sql text
);
```

> `sqlite_master` 表，表中保存数据库表的关键信息。
>
> `type` 字段的值是 `table`，`name` 字段是表的名字，`sql` 字段是表的创建语句。
> 所以想要查看数据库中所有表的信息可以执行以下语句：
>
> ```sql
> SELECT name FROM sqlite_master 
> WHERE type = 'table' 
> ORDER BY name;
> ```

