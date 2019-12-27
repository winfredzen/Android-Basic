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

















