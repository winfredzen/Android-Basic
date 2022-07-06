# 自定义ContentProvider实现

本例子来源自第一行代码

1.创建ContentProvider， New→Other→ContentProvider

2.ContentProvider要在`AndroidManifest.xml`中注册

在`application`标签下

```xml
        <provider
                android:name=".DatabaseProvider"
                android:authorities="com.example.databasetest.provider"
                android:enabled="true"
                android:exported="true">
        </provider>
```

> `android:name`属性指定了DatabaseProvider的类名
>
> `android:authorities`属性指定了DatabaseProvider的authority
>
> 而enabled和exported属性则是根据我们刚才勾选的状态自动生成的，这里表示允许DatabaseProvider被其他应用程序访问。

[在DatabaseTest]()的`DatabaseProvider`

