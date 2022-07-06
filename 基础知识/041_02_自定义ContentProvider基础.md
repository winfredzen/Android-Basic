# 自定义ContentProvider基础

参考：

+ [创建内容提供程序](https://developer.android.com/guide/topics/providers/content-provider-creating#ContentProvider)

主要是实现ContentProvider的方法



## URI

一个标准的内容URI写法是：

```shell
content://com.example.app.provider/table1
```

表示调用方期望访问的是com.example.app这个应用的table1表中的数据。

们还可以在这个内容URI的后面加上一个id，例如：

```
content://com.example.app.provider/table1/1
```

表示调用方期望访问的是com.example.app这个应用的table1表中id为1的数据。

内容URI的格式主要就只有以上两种

+ 以路径结尾表示期望访问该表中所有的数据
+ 以id结尾表示期望访问该表中拥有相应id的数据。



**通配符**

+ `*`表示匹配任意长度的任意字符

+ `#`表示匹配任意长度的数字



一个能够匹配任意表的内容URI格式就可以写成：

```
content://com.example.app.provider/*
```

一个能够匹配table1表中任意一行数据的内容URI格式就可以写成：

```
content://com.example.app.provider/table1/#
```



`UriMatcher`这个类就可以轻松地实现匹配内容URI的功能。`UriMatcher`中提供了一个`addURI()`方法，这个方法接收3个参数，可以分别把`authority`、`path`和一个自定义代码传进去。这样，当调用`UriMatcher`的match()方法时，就可以将一个`Uri`对象传入，返回值是某个能够匹配这个Uri对象所对应的自定义代码

> `addURI()` 方法会将授权和路径映射为整型值。`match()` 方法会返回 URI 的整型值



**MIME**

URI所对应的MIME字符串主要由3部分组成，Android对这3个部分做了如下格式规定。

+ 必须以vnd开头。

+ 如果内容URI以路径结尾，则后接`android.cursor.dir/`；如果内容URI以id结尾，则后接android.cursor.item/。

+ 最后接上`vnd.<authority>.<path>`

所以，对于`content://com.example.app.provider/table1`这个内容URI，它所对应的MIME类型就可以写成：

```
vnd.android.cursor.dir/vnd.com.example.app.provider.table1
```

对于`content://com.example.app.provider/table1/1`这个内容URI，它所对应的MIME类型就可以写成：

```
vnd.android.cursor.item/vnd.com.example.app.provider.table1
```





例子`MyProvider`：

```kotlin
class MyProvider : ContentProvider() {

    private val table1Dir = 0

    private val table1Item = 1

    private val table2Dir = 2

    private val table2Item = 3

    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

    init {
        uriMatcher.addURI("com.example.app.provider", "table1", table1Dir)
        uriMatcher.addURI("com.example.app.provider ", "table1/#", table1Item)
        uriMatcher.addURI("com.example.app.provider ", "table2", table2Dir)
        uriMatcher.addURI("com.example.app.provider ", "table2/#", table2Item)
    }

    override fun onCreate(): Boolean {
        return false
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        when (uriMatcher.match(uri)) {
            table1Dir -> {
                // 查询table1表中的所有数据
            }
            table1Item -> {
                // 查询table1表中的单条数据
            }
            table2Dir -> {
                // 查询table2表中的所有数据
            }
            table2Item -> {
                // 查询table2表中的单条数据
            }
        }
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        return 0
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }

    override fun getType(uri: Uri) = when (uriMatcher.match(uri)) {
        table1Dir -> "vnd.android.cursor.dir/vnd.com.example.app.provider.table1"
        table1Item -> "vnd.android.cursor.item/vnd.com.example.app.provider.table1"
        table2Dir -> "vnd.android.cursor.dir/vnd.com.example.app.provider.table2"
        table2Item -> "vnd.android.cursor.item/vnd.com.example.app.provider.table2"
        else -> null
    }

}
```

