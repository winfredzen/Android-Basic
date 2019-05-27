# TextView

在《Android权威编程指南》中有用到`tools:text`属性

```xml
    <TextView
        android:id="@+id/answer_text_view"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:padding="24dp"
        tools:text="Answer"/>
```

>它的`tools`和`tools:text`属性的命名空间比较特别。该命名空间可以覆盖某个组件的任何属性。这样，就可以在Android Studio**预览**中看到效果。
>TextView有text属性，可以为它提供初始值，因而在应用运行前就知道它大概的样子。而在应用运行时，Answer文字不会显示出来

