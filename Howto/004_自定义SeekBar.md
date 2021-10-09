# 自定义SeekBar



## 基本

自定义`SeekBar`，可以自定义`SeekBar`的`thumb`，背景颜色等

如下的设置：

```xml
    <androidx.appcompat.widget.AppCompatSeekBar
        android:layout_marginTop="50dp"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:id="@+id/seekbar01"
        android:thumb="@drawable/seekbar_thumb"
        android:progressDrawable="@drawable/seekbar_progress_style"
        android:progress="50"
        />
```

`seekbar_thumb.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<ripple xmlns:android="http://schemas.android.com/apk/res/android"
    android:color="#FF0000">
    <item>
        <shape android:shape="oval">
            <solid android:color="#FF0000"/>
            <size android:width="30dp" android:height="30dp"/>
        </shape>
    </item>
</ripple>
```

`seekbar_progress_style.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<layer-list xmlns:android="http://schemas.android.com/apk/res/android">

    <item android:id="@android:id/background"
        android:drawable="@drawable/seekbar_track">
    </item>

    <item android:id="@android:id/progress">
        <clip android:drawable="@drawable/seekbar_progress"/>
    </item>
</layer-list>
```

`seekbar_track.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<layer-list xmlns:android="http://schemas.android.com/apk/res/android">
    <item>
        <shape>
            <corners android:radius="5dp"/>
            <gradient
                android:angle="270"
                android:centerColor="#0000FF"
                android:endColor="#00FF00"
                android:startColor="#FF0000"
                android:type="linear"/>
        </shape>
    </item>
</layer-list>
```

`seekbar_progress.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<layer-list xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:id="@+id/progressshape">
        <clip>
            <shape android:shape="rectangle">
                <size android:height="5dp"/>
                <corners android:radius="5dp"/>
                <solid android:color="#3db5ea"/>
            </shape>
        </clip>
    </item>
</layer-list>
```

最终的效果如下：

![012](https://github.com/winfredzen/Android-Basic/blob/master/Howto/images/012.png)





















