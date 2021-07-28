# Android原生SQLite操作

`SQLiteOpenHelper`可以非常简单的对数据库进行创建和升级，`SQLiteOpenHelper`是一个抽象类，所以需要创建自己的类去继承它，需要重写两个方法

```java
public void onCreate(SQLiteDatabase db)
public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)  
```

> `onCreate`方法在创建数据库时调用，如果数据库已经存在，不会再执行。就算有新添加的表，该方法也不会执行
>
> `onUpgrade`升级数据库时调用

构造方法

```java
public SQLiteOpenHelper(@Nullable Context context, @Nullable String name,
            @Nullable CursorFactory factory, int version)
```

+ name - 数据库名，创建数据库使用的就是这里的名称
+ factory - to use for creating cursor objects, or null for the default
+ version - 版本号， 可用于对数据库的升级操作

构造`SQLiteOpenHelper`的实例后，调用其`getWritableDatabase()`或者`getReadableDatabase()`方法就能够创建数据库和升级数据库

> 当数据库不可写入的时候（如磁盘空间已满，`getReadableDatabase()`方法返回的对象将已只读的方式打开数据库，而`getWritableDatabase()`方法则抛出异常）

> 我自己测试了下，发现`onUpgrade`被调用，也是在调用`getWritableDatabase()`方法之后，才调用



**添加数据**

使用`SQLiteDatabase`的`insert(String table, String nullColumnHack, ContentValues values)`方法

+ table - 表名
+ nullColumnHack - 用于在未指定添加数据的情况下给某些可能为空的列自动复制`NULL`，一般用不到这个功能，直接出入`null`即可
+ values - 列名及其对应的数据



**更新数据**

使用如下的方法：

```java
public int update(String table, ContentValues values, String whereClause, String[] whereArgs)
```

+ whereClause、whereArgs - 表示条件



**查询数据**

如下的`query`方法：

```java
public Cursor query(String table, String[] columns, String selection,
            String[] selectionArgs, String groupBy, String having,
            String orderBy)
```

+ columns - 指定查询的列名
+ selection、selectionArgs - 指定where的约束条件
+ groupBy - 指定需要group by的列
+ having - 对group by后的结果进一步约束
+ orderBy - 指定查询结果的排序方式



**其它方式**

![008](https://github.com/winfredzen/Android-Basic/blob/master/Database%26Cache/images/008.png)



**简单的例子**

`MyDatabaseHelper`

```java
public class MyDatabaseHelper extends SQLiteOpenHelper {

    public static final String CREATE_BOOK = "create table Book ("
            + "id integer primary key autoincrement, "
            + "author text, "
            + "price real, "
            + "pages integer, "
            + "name text)";

    public static final String CREATE_CATEGORY = "create table Category ("
            + "id integer primary key autoincrement, "
            + "category_name text, "
            + "category_code integer)";

    private Context mContext;

    public MyDatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建表
        db.execSQL(CREATE_BOOK);
        db.execSQL(CREATE_CATEGORY);
        Toast.makeText(mContext, "Create succeeded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //数据库升级
        db.execSQL("drop table if exists Book");
        db.execSQL("drop table if exists Category");
        onCreate(db);
    }
}
```

```java
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivityTag";

    public static final String DB_NAME = "BookStore.db";
    //public static final int DB_VERSION = 1;
    public static final int DB_VERSION = 2;//数据库升级

    private MyDatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mDatabaseHelper = new MyDatabaseHelper(this, DB_NAME, null, DB_VERSION);

        Button createDatabase = (Button) findViewById(R.id.create_database);
        createDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //没有BookStore.db则创建
                mDatabaseHelper.getWritableDatabase();
            }
        });


        //添加数据
        Button addData = (Button) findViewById(R.id.add_data);
        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                //组装第一条数据
                values.put("name", "The Da Vinci Code");
                values.put("author", "Dan Brown");
                values.put("pages", 454);
                values.put("price", 16.56);
                db.insert("Book", null, values);//插入第一条数据
                //组装第二条数据
                values.put("name", "The Lost Symbol");
                values.put("author", "Dan Brown");
                values.put("pages", 510);
                values.put("price", 19.95);
                db.insert("Book", null, values);//插入第二条数据

            }
        });

        //更新数据
        Button updateData = (Button) findViewById(R.id.update_data);
        updateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("price", 10.99);
                /**
                 * name = ? 相当与where name = xxx
                 * ? 是一个占位符
                 */
                db.update("Book", values, "name = ?", new String[] {"The Da Vinci Code"});

            }
        });


        //删除数据
        Button deleteData = (Button) findViewById(R.id.delete_data);
        deleteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
                db.delete("Book", "pages > ?" , new String[] {"500"});

            }
        });

        //查询数据
        Button queryButton = (Button) findViewById(R.id.query_data);
        queryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //查询Book表中的所有数据
                SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
                Cursor cursor = db.query("Book", null, null, null, null, null, null);
                if (cursor.moveToFirst()) {//将数据的指针移动到第一行的位置
                    do {
                        //遍历Cursor对象
                        String name = cursor.getString(cursor.getColumnIndex("name"));//getColumnIndex获取某一列在表中对应的索引位置
                        String author = cursor.getString(cursor.getColumnIndex("author"));
                        int pages = cursor.getInt(cursor.getColumnIndex("pages"));
                        double price = cursor.getDouble(cursor.getColumnIndex("price"));

                        Log.d(TAG, "book name = " + name + ", author = " + author + ", pages = " + pages + ", price = " + price);


                    } while (cursor.moveToNext());
                }
                //关闭cursor
                cursor.close();

            }
        });

    }
}
```



































