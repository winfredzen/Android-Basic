# 自定义Switch

参考自：

+ [How to make Custom Switch in Android?](https://dev.to/akshayranagujjar/how-to-make-custom-switch-in-android-5d1d)
+ [Custom Switch (Like IOS) In Android Tutorial](https://androidtutorialmagic.wordpress.com/android-material-design-tutorial/custom-switch-like-ios-in-android-tutorial/)



## 基本

通过设置`android:track`和`android:thumb`来自定义Swith

如：

`custom_track.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:state_checked="false">
        <shape android:shape="rectangle">
            <solid android:color="@android:color/white"/>
            <corners android:radius="100sp"/>
            <stroke android:color="#8e8e8e"
                android:width="1dp"/>
        </shape>
    </item>
    <item android:state_checked="true">
        <shape android:shape="rectangle">
            <solid android:color="@color/teal_700"/>
            <corners android:radius="100sp"/>
        </shape>
    </item>
</selector>
```

`custom_thumb.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<selector xmlns:android="http://schemas.android.com/apk/res/android">
<!--    <item android:state_checked="false"-->
<!--        android:drawable="@drawable/notification_icon_off"/>-->

<!--    <item android:state_checked="true"-->
<!--        android:drawable="@drawable/notification_icon_on"/>-->

    <item android:state_checked="false"
        android:drawable="@drawable/custom_thumb_off"/>

    <item android:state_checked="true"
        android:drawable="@drawable/custom_thumb_on"/>
</selector>


custom_thumb_off.xml
<?xml version="1.0" encoding="utf-8"?>
<layer-list xmlns:android="http://schemas.android.com/apk/res/android">
    <item >
        <shape android:shape="oval">
            <solid android:color="@color/teal_700"/>
            <size android:height="48dp" android:width="48dp"/>
            <stroke android:color="@color/black" android:width="1dp"/>
        </shape>
    </item>

<!--    <item android:drawable="@drawable/notification_icon_off"-->
<!--        android:bottom="12dp"-->
<!--        android:top="12dp"-->
<!--        android:left="12dp"-->
<!--        android:right="12dp"-->
<!--        />-->
</layer-list>

custom_thumb_on.xml
<?xml version="1.0" encoding="utf-8"?>
<layer-list xmlns:android="http://schemas.android.com/apk/res/android">
    <item >
        <shape android:shape="oval">
            <solid android:color="@color/white"/>
            <size android:height="48dp" android:width="48dp"/>
            <stroke android:color="@color/black" android:width="1dp"/>
        </shape>
    </item>

<!--    <item android:drawable="@drawable/notification_icon_on"-->
<!--        android:bottom="12dp"-->
<!--        android:top="12dp"-->
<!--        android:left="12dp"-->
<!--        android:right="12dp"-->
<!--        />-->
</layer-list>
```

![028](https://github.com/winfredzen/Android-Basic/blob/master/Howto/images/028.png)

