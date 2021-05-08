# MMKV

[MMKV](https://github.com/Tencent/MMKV)官网的介绍：

> MMKV 是基于 mmap 内存映射的 key-value 组件，底层序列化/反序列化使用 protobuf 实现，性能高，稳定性强。从 2015 年中至今在微信上使用，其性能和稳定性经过了时间的验证。近期也已移植到 Android / macOS / Win32 / POSIX 平台，一并开源。

Android上的使用方式：

> MMKV 的使用非常简单，所有变更立马生效，无需调用 `sync`、`apply`。 在 App 启动时初始化 MMKV，设定 MMKV 的根目录（files/mmkv/），例如在 `Application` 里：
>
> ```java
> public void onCreate() {
>     super.onCreate();
> 
>     String rootDir = MMKV.initialize(this);
>     System.out.println("mmkv root: " + rootDir);
>     //……
> }
> ```
>
> MMKV 提供一个全局的实例，可以直接使用：
>
> ```java
> import com.tencent.mmkv.MMKV;
> //……
> 
> MMKV kv = MMKV.defaultMMKV();
> 
> kv.encode("bool", true);
> boolean bValue = kv.decodeBool("bool");
> 
> kv.encode("int", Integer.MIN_VALUE);
> int iValue = kv.decodeInt("int");
> 
> kv.encode("string", "Hello from mmkv");
> String str = kv.decodeString("string");
> ```



详细的使用方式，参考[wiki页面](https://github.com/Tencent/MMKV/wiki/android_setup_cn):

>  **配置 MMKV 根目录**
>
> 在 App 启动时初始化 MMKV，设定 MMKV 的根目录（files/mmkv/），例如在 `Application` 里：
>
> ```java
> public void onCreate() {
>     super.onCreate();
> 
>     String rootDir = MMKV.initialize(this);
>     System.out.println("mmkv root: " + rootDir);
> }
> ```
>
> **CRUD 操作**
>
> MMKV 提供一个**全局的实例**，可以直接使用：
>
> ```java
> import com.tencent.mmkv.MMKV;
> ...
> MMKV kv = MMKV.defaultMMKV();
> 
> kv.encode("bool", true);
> System.out.println("bool: " + kv.decodeBool("bool"));
> 
> kv.encode("int", Integer.MIN_VALUE);
> System.out.println("int: " + kv.decodeInt("int"));
> 
> kv.encode("long", Long.MAX_VALUE);
> System.out.println("long: " + kv.decodeLong("long"));
> 
> kv.encode("float", -3.14f);
> System.out.println("float: " + kv.decodeFloat("float"));
> 
> kv.encode("double", Double.MIN_VALUE);
> System.out.println("double: " + kv.decodeDouble("double"));
> 
> kv.encode("string", "Hello from mmkv");
> System.out.println("string: " + kv.decodeString("string"));
> 
> byte[] bytes = {'m', 'm', 'k', 'v'};
> kv.encode("bytes", bytes);
> System.out.println("bytes: " + new String(kv.decodeBytes("bytes")));
> ```
>
> **删除 & 查询：**
>
> ```java
> MMKV kv = MMKV.defaultMMKV();
> 
> kv.removeValueForKey("bool");
> System.out.println("bool: " + kv.decodeBool("bool"));
>     
> kv.removeValuesForKeys(new String[]{"int", "long"});
> System.out.println("allKeys: " + Arrays.toString(kv.allKeys()));
> 
> boolean hasBool = kv.containsKey("bool");
> ```
>
> 如果不同业务需要**区别存储**，也可以单独创建自己的实例：
>
> ```java
> MMKV kv = MMKV.mmkvWithID("MyID");
> kv.encode("bool", true);
> ```
>
> 如果业务需要**多进程访问**，那么在初始化的时候加上标志位 `MMKV.MULTI_PROCESS_MODE`：
>
> ```java
> MMKV kv = MMKV.mmkvWithID("InterProcessKV", MMKV.MULTI_PROCESS_MODE);
> kv.encode("bool", true);
> ```
>
> 









官方的demo，下载下来后，尝试后，发现在Android Studio中需要import，如果使用open直接打开工程的话，会提示很多的错误

MMKV保存的路径：`String rootDir = MMKV.getRootDir();`

```xml
/data/user/0/com.tencent.mmkvdemo/files/mmkv
```



































