# ContentProvider读取联系人

本篇内容是对学习ContentProvier的记录，内容来自第一行代码

`ContentProvider`的用法一般有两种：

+ 一种是使用现有的`ContentProvider`读取和操作相应程序中的数据；
+ 另一种是创建自己的`ContentProvider`，给程序的数据提供外部访问接口。

**基本用法**

对于每一个应用程序来说，如果想要访问`ContentProvider`中共享的数据，就一定要借助`ContentResolver`类，可以通过Context中的`getContentResolver()`方法获取该类的实例。`ContentResolver`中提供了一系列的方法用于对数据进行增删改查操作，其中`insert()`方法用于添加数据，`update()`方法用于更新数据，`delete()`方法用于删除数据，`query()`方法用于查询数据

`ContentResolver`中的增删改查方法都是不接收表名参数的，而是使用一个`Uri`参数代替，这个参数被称为内容URI。内容URI给`ContentProvider`中的数据建立了唯一标识符，它主要由两部分组成：`authority`和`path`。

+ `authority`是用于对不同的应用程序做区分的，一般为了避免冲突，会采用应用包名的方式进行命名。比如某个应用的包名是`com.example.app`，那么该应用对应的`authority`就可以命名为`com.example.app.provider`

+ `path`则是用于对同一应用程序中不同的表做区分的，通常会添加到`authority`的后面。比如某个应用的数据库里存在两张表`table1`和`table2`，这时就可以将`path`分别命名为`/table1`和`/table2`

然后把`authority`和`path`进行组合，内容URI就变成了`com.example.app.provider/table1`和`com.example.app.provider/table2`。

最后还需要在字符串的头部加上协议声明

```text
content://com.example.app.provider/table1
content://com.example.app.provider/table2
```

在得到了内容URI字符串之后，我们还需要将它解析成Uri对象才可以作为参数传入

```kotlin
val uri = Uri.parse("content://com.example.app.provider/table1")
```



## query

可以使用这个Uri对象查询table1表中的数据了

```kotlin
val cursor = contentResolver.query(
 uri,
 projection,
 selection,
 selectionArgs,
 sortOrder)
```

![079](https://github.com/winfredzen/Android-Basic/blob/master/基础知识/images/079.png)

查询完成后返回的仍然是一个`Cursor`对象

```kotlin
while (cursor.moveToNext()) {
 val column1 = cursor.getString(cursor.getColumnIndex("column1"))
 val column2 = cursor.getInt(cursor.getColumnIndex("column2"))
}
cursor.close()
```



## nsert

向table1表中添加一条数据，代码如下所示：

```kotlin
val values = contentValuesOf("column1" to "text", "column2" to 1)
contentResolver.insert(uri, values)
```

将待添加的数据组装到`ContentValues`中，然后调用`ContentResolver`的`insert()`方法，将`Uri`和`ContentValues`作为参数传入即可



## upate

```kotlin
val values = contentValuesOf("column1" to "")
contentResolver.update(uri, values, "column1 = ? and column2 = ?", arrayOf("text", "1"))
```

上述代码使用了`selection`和`selectionArgs`参数来对想要更新的数据进行约束，以防止所有的行都会受影响。



## delete

```kotlin
contentResolver.delete(uri, "column2 = ?", arrayOf("1"))
```



## 读取系统联系人

```kotlin
class MainActivity : AppCompatActivity() {

    private val contactsList = ArrayList<String>()
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, contactsList)
        contactsView.adapter = adapter
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), 1)
        } else {
            readContacts()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    readContacts()
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun readContacts() {
        // 查询联系人数据
        contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null)?.apply {
            while (moveToNext()) {
                // 获取联系人姓名
                val displayName = getString(getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                // 获取联系人手机号
                val number = getString(getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                contactsList.add("$displayName\n$number")
            }
            adapter.notifyDataSetChanged()
            close()
        }
    }

}

```

一些说明：

1.Uri，即为`ContactsContract.CommonDataKinds.Phone.CONTENT_URI`，已做好封装

![080](https://github.com/winfredzen/Android-Basic/blob/master/基础知识/images/080.png)

![080](https://github.com/winfredzen/Android-Basic/blob/master/基础知识/images/080.png)

![082](https://github.com/winfredzen/Android-Basic/blob/master/基础知识/images/082.png)



## 代码

[ContactsTest](https://github.com/winfredzen/Android-Basic/tree/master/%E5%9F%BA%E7%A1%80%E7%9F%A5%E8%AF%86/code/ContactsTest)















