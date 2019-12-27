# FileProvider

参考：

+ [FileProvider](https://developer.android.com/reference/android/support/v4/content/FileProvider)
+ [我想把 FileProvider 聊的更透彻一些](https://juejin.im/post/5974ca356fb9a06bba4746bc)
+ [Android 7.0 行为变更 通过FileProvider在应用间共享文件吧](https://blog.csdn.net/lmj623565791/article/details/72859156)

在调用系统摄像头拍照并获取拍摄的照片时，有遇到使用FileProvider的问题，开始很蒙啊，与iOS很不一样啊

FileProvider是ContentProvider的特殊子类，通过为文件创建`content://`Uri，而不是`file:///` Uri，来安全的分享文件

content URI允许你使用临时访问权限来授予读写访问权限。当创建包含content URI的`Intent`时，为了将content URI发送到客户端app，还可以调用`Intent.setFlags()`来添加权限。

> These permissions are available to the client app for as long as the stack for a receiving Activity is active. For an Intent going to a Service, the permissions are available as long as the Service is running.

>In comparison, to control access to a `file:///` `Uri` you have to modify the file system permissions of the underlying file. The permissions you provide become available to *any* app, and remain in effect until you change them. This level of access is fundamentally insecure.
>
>The increased level of file access security offered by a content URI makes FileProvider a key part of Android's security infrastructure.



## 如何使用 FileProvider

### 定义一个FileProvider

```xml
    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/AppTheme">
	
				.....
		
        <provider
            android:authorities="com.bignerdranch.android.criminalintent.fileprovider"
            android:name="android.support.v4.content.FileProvider"
            android:exported="false"
            android:grantUriPermissions="true">

            <meta-data android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/files"
                />

        </provider>
        
    </application>
```

`android:name`为`android.support.v4.content.FileProvider`

`android:authorities`由自己控制

`android:exported`设置为`false`，表示这个`FileProvider`不需要公开

 [android:grantUriPermissions](https://developer.android.com/guide/topics/manifest/provider-element.html#gprmsn) 设置为`ture`，表示允许授予对文件的临时访问权限

里面的`meta-data`后面再说



## 指定可分享的文件路径

`FileProvider`只能为你先指定的目录中的文件生成content URI。要指定目录，需要使用`<paths>`元素

在`app/res`目录下，选择`New → Android resource file`，资源类型选择`XML`，输入文件名

![040](https://github.com/winfredzen/Android-Basic/blob/master/images/040.png)

`files.xml`文件内容如下：

```xml
<paths>

    <files-path name="crime_photos" path="."/>

</paths>
```

+ **files-path**：表示 `content.getFileDir()` 获取到的目录

其它的：

+ **root-path**：表示根目录，『/』。

+ **files-path**：表示 `content.getFileDir()` 获取到的目录。

+ **cache-path**：表示 `content.getCacheDir()` 获取到的目录

+ **external-path**：表示`Environment.getExternalStorageDirectory()` 指向的目录。

+ **external-files-path**：表示 `ContextCompat.getExternalFilesDirs()` 获取到的目录。

+ **external-cache-path**：表示 `ContextCompat.getExternalCacheDirs()` 获取到的目录。

它们的子元素：

+ name - URI路径段，为了安全性，其此值的子目录名称包含在path属性中
+ path - 共享的子目录，是实际的子目录名称， 请注意，该值引用的是子目录，而不是单个文件或多个文件。 您不能通过文件名共享单个文件，也不能使用通配符指定文件的子集。

之后，就需要在`FileProvider`中链接这个文件，使用 `<meta-data>`，其name属性被设置为`android.support.FILE_PROVIDER_PATHS`，`android:resource`设置你创建的`.xml`文件



### 生成文件的Content URI

要使用Content URI与另一个应用共享文件，你的应用必须生成Content URI。

要生成content URI，你需要为文件创建一个`File`，并将`File`传递到`getUriForFile()`。你可以将`getUriForFile()`返回的content URI传递到`Intent`中。 接收content URI的客户端app可以通过调用`ContentResolver.openFileDescriptor`以获得`ParcelFileDescriptor`来打开文件并访问其内容。

```java
public static Uri getUriForFile(@NonNull Context context, @NonNull String authority,
            @NonNull File file)
```



### 授予临时的读写权限

要授予对从`getUriForFile()`返回的content URI的访问权限，可以有如下的2中方式：

1.调用[Context.grantUriPermission(package, Uri, mode_flags)](https://developer.android.com/reference/android/content/Context.html#grantUriPermission(java.lang.String, android.net.Uri, int))方法

+ toPackage ：表示授予权限的 App 的包名。
+ uri：授予权限的 `content://` 的 Uri。
+ modeFlags：前面提到的读写权限，为`FLAG_GRANT_READ_URI_PERMISSION`, `FLAG_GRANT_WRITE_URI_PERMISSION`，或者同时包含2个

2.配合 `Intent.addFlags()` 授权

通过调用[Intent](https://developer.android.com/reference/android/content/Intent.html)的`setData()`方法，将URI传递到Intent中

接下来调用`Intent.setFlags()`方法，使用`FLAG_GRANT_READ_URI_PERMISSION`、`FLAG_GRANT_WRITE_URI_PERMISSION`或者2者

最后，将Intent发送到另一个应用程序。 通常，你可以通过调用`setResult()`来执行此操作















