# DataBinding

官方文档：

+ [数据绑定库](https://developer.android.com/topic/libraries/data-binding)

数据绑定库是一种支持库，借助该库，您可以使用声明性格式（而非程序化地）将布局中的界面组件绑定到应用中的数据源。

## 入门

1.应用模块的`build.gradle`中添加`dataBinding`元素

```groovy

android {

    dataBinding {
        enabled = true
    }
}
```

2.布局以`layout`开头，后面跟`data`元素和`view`根元素

```xml
<?xml version="1.0" encoding="utf-8"?>
    <layout xmlns:android="http://schemas.android.com/apk/res/android">
       <data>
           <variable name="user" type="com.example.User"/>
       </data>
       <LinearLayout
           android:orientation="vertical"
           android:layout_width="match_parent"
           android:layout_height="match_parent">
           <TextView android:layout_width="wrap_content"
               android:layout_height="wrap_content"
               android:text="@{user.firstName}"/>
           <TextView android:layout_width="wrap_content"
               android:layout_height="wrap_content"
               android:text="@{user.lastName}"/>
       </LinearLayout>
    </layout>
```

绑定的数据对象，可以是public的属性，也可以是get方法

```java

    public class User {
      public final String firstName;
      public final String lastName;
      public User(String firstName, String lastName) {
          this.firstName = firstName;
          this.lastName = lastName;
      }
    }
    
    public class User {
      private final String firstName;
      private final String lastName;
      public User(String firstName, String lastName) {
          this.firstName = firstName;
          this.lastName = lastName;
      }
      public String getFirstName() {
          return this.firstName;
      }
      public String getLastName() {
          return this.lastName;
      }
    }

    
```

用于 [`android:text`](https://developer.android.com/reference/android/widget/TextView#attr_android:text) 特性的表达式 `@{user.firstName}` 访问前一个类中的 `firstName` 字段和后一个类中的 `getFirstName()` 方法。或者，如果该方法存在，也将解析为 `firstName()`。



3.生成的绑定类

> 系统会为每个布局文件生成一个绑定类。默认情况下，类名称基于布局文件的名称，它会转换为 Pascal 大小写形式并在末尾添加 Binding 后缀。以上布局文件名为 `activity_main.xml`，因此生成的对应类为 `ActivityMainBinding`

+ `ViewDataBinding` - Base class for generated data binding classes. If possible, the generated binding should be instantiated using one of its generated static bind or inflate methods. If the specific binding is unknown, `bind(View)` or `inflate(LayoutInflater, int, ViewGroup, boolean)` should be used.
+ `DataBindingUtil` - Utility class to create `ViewDataBinding` from layouts

自动生成类的位置：

![003](https://github.com/winfredzen/Android-Basic/blob/master/%E6%9E%B6%E6%9E%84/images/003.png)



4.绑定数据

建议的绑定创建方法是在扩充布局时创建，如以下示例所示：

```java
    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
       User user = new User("Test", "User");
       binding.setUser(user);
    }
```

可以使用 `LayoutInflater` 获取视图，如以下示例所示：

```java
    ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
```

如果您要在 `Fragment`、`ListView` 或 `RecyclerView` 适配器中使用数据绑定项，您可能更愿意使用绑定类或 [`DataBindingUtil`](https://developer.android.com/reference/androidx/databinding/DataBindingUtil?hl=zh-cn) 类的 `inflate()`方法，如以下代码示例所示

```java
ListItemBinding binding = ListItemBinding.inflate(layoutInflater, viewGroup, false);
// or
ListItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.list_item, viewGroup, false);
```



### 表达式语言

```xml
android:text="@{String.valueOf(index + 1)}"
    android:visibility="@{age > 13 ? View.GONE : View.VISIBLE}"
    android:transitionName='@{"image_" + id}'
```

**Null 合并运算符**

```xml
android:text="@{user.displayName ?? user.lastName}"

android:text="@{user.displayName != null ? user.displayName : user.lastName}"
```

**属性引用**

对于字段、getter 和 [`ObservableField`](https://developer.android.com/reference/androidx/databinding/ObservableField) 对象都一样

```xml
android:text="@{user.lastName}"
```

**避免出现 Null 指针异常**

生成的数据绑定代码会自动检查有没有 `null` 值并避免出现 Null 指针异常。例如，在表达式 `@{user.name}` 中，如果 `user` 为 Null，则为 `user.name` 分配默认值 `null`。如果您引用 `user.age`，其中 age 的类型为 `int`，则数据绑定使用默认值 `0`

**视图引用**

表达式可以通过以下语法按 ID 引用布局中的其他视图：

```xml
android:text="@{exampleText.text}"
```

**集合**

为方便起见，可使用 `[]` 运算符访问常见集合，例如数组、列表、稀疏列表和映射。

```xml
<data>
        <import type="android.util.SparseArray"/>
        <import type="java.util.Map"/>
        <import type="java.util.List"/>
        <variable name="list" type="List&lt;String>"/>
        <variable name="sparse" type="SparseArray&lt;String>"/>
        <variable name="map" type="Map&lt;String, String>"/>
        <variable name="index" type="int"/>
        <variable name="key" type="String"/>
    </data>
    …
    android:text="@{list[index]}"
    …
    android:text="@{sparse[index]}"
    …
    android:text="@{map[key]}"
    
```

> **注意**：要使 XML 不含语法错误，您必须转义 `<` 字符。例如：不要写成 `List<String>` 形式，而是必须写成 `List<String>`。

还可以使用 `object.key` 表示法在映射中引用值。例如，以上示例中的 `@{map[key]}` 可替换为 `@{map.key}`。

**字符串字面量**

可以使用单引号括住特性值，这样就可以在表达式中使用双引号

```xml
android:text='@{map["firstName"]}'
```

也可以使用双引号括住特性值。如果这样做，则还应使用反单引号 ``` 将字符串字面量括起来

```xml
android:text="@{map[`firstName`]}"
```

**资源**

可以使用以下语法引用应用资源

```xml
android:padding="@{large? @dimen/largePadding : @dimen/smallPadding}"
```

可以通过提供参数来评估格式字符串和复数形式

```xml
android:text="@{@string/nameFormat(firstName, lastName)}"
    android:text="@{@plurals/banana(bananaCount)}"
```

可以将[属性引用](https://developer.android.com/topic/libraries/data-binding/expressions#property_reference)和[视图引用](https://developer.android.com/topic/libraries/data-binding/expressions#view_references)作为资源参数进行传递：

```xml
android:text="@{@string/example_resource(user.lastName, exampleText.text)}"
```



### 事件处理

**方法引用**

主要优点是**表达式在编译时进行处理**，因此，如果该方法不存在或其签名不正确，则会收到编译时错误。

**方法引用和监听器绑定之间的主要区别在于实际监听器实现是在绑定数据时创建的，而不是在事件触发时创建的。**如果您希望在事件发生时对表达式求值，则应使用[监听器绑定](https://developer.android.com/topic/libraries/data-binding/expressions#listener_bindings)。

如：

```java
    public class MyHandlers {
        public void onClickFriend(View view) { ... }
    }


<?xml version="1.0" encoding="utf-8"?>
    <layout xmlns:android="http://schemas.android.com/apk/res/android">
       <data>
           <variable name="handlers" type="com.example.MyHandlers"/>
           <variable name="user" type="com.example.User"/>
       </data>
       <LinearLayout
           android:orientation="vertical"
           android:layout_width="match_parent"
           android:layout_height="match_parent">
           <TextView android:layout_width="wrap_content"
               android:layout_height="wrap_content"
               android:text="@{user.firstName}"
               android:onClick="@{handlers::onClickFriend}"/>
       </LinearLayout>
    </layout>
```

> **注意**：表达式中的方法签名必须与监听器对象中的方法签名完全一致。



**监听器绑定**

监听器绑定是在事件发生时运行的绑定表达式

在方法引用中，方法的参数必须与事件监听器的参数匹配。在监听器绑定中，只有您的返回值必须与监听器的预期返回值相匹配（预期返回值无效除外）

例如，请参考以下具有 `onSaveClick()` 方法的 presenter 类：

```java
    public class Presenter {
        public void onSaveClick(Task task){}
    }
```

然后，您可以将点击事件绑定到 `onSaveClick()` 方法，如下所示：

```xml
<?xml version="1.0" encoding="utf-8"?>
    <layout xmlns:android="http://schemas.android.com/apk/res/android">
        <data>
            <variable name="task" type="com.android.example.Task" />
            <variable name="presenter" type="com.android.example.Presenter" />
        </data>
        <LinearLayout android:layout_width="match_parent" android:layout_height="match_parent">
            <Button android:layout_width="wrap_content" android:layout_height="wrap_content"
            android:onClick="@{() -> presenter.onSaveClick(task)}" />
        </LinearLayout>
    </layout>
    
```

在上面的示例中，我们尚未定义传递给 `onClick(View)` 的 `view` 参数

监听器绑定提供两个监听器参数选项：您可以忽略方法的所有参数，也可以命名所有参数。如果您想命名参数，则可以在表达式中使用这些参数。例如，上面的表达式可以写成如下形式：

```xml
android:onClick="@{(view) -> presenter.onSaveClick(task)}"
```

如果您想在表达式中使用参数，则采用如下形式：

```java
    public class Presenter {
        public void onSaveClick(View view, Task task){}
    }

    
android:onClick="@{(theView) -> presenter.onSaveClick(theView, task)}"
    
```

您可以在 lambda 表达式中使用多个参数：

```java
    public class Presenter {
        public void onCompletedChanged(Task task, boolean completed){}
    }

    
<CheckBox android:layout_width="wrap_content" android:layout_height="wrap_content"
          android:onCheckedChanged="@{(cb, isChecked) -> presenter.completeChanged(task, isChecked)}" />
    
```

如果您监听的事件返回类型不是 `void` 的值，则您的表达式也必须返回相同类型的值。例如，如果要监听长按事件，表达式应返回一个布尔值。

```java
    public class Presenter {
        public boolean onLongClick(View view, Task task) { }
    }

    
android:onLongClick="@{(theView) -> presenter.onLongClick(theView, task)}"
    
```

如果由于 `null` 对象而无法对表达式求值，则数据绑定将返回该类型的默认值。例如，引用类型返回 `null`，`int` 返回 `0`，`boolean` 返回 `false`，等等。

如果您需要将表达式与谓词（例如，三元运算符）结合使用，则可以使用 `void` 作为符号。

```xml
android:onClick="@{(v) -> v.isVisible() ? doSomething() : void}"
```



### 导入、变量和包含

**导入**

```xml
<data>
        <import type="android.view.View"/>
    </data>
```

类型别名

```xml
<import type="android.view.View"/>
    <import type="com.example.real.estate.View"
            alias="Vista"/>
```

导入其他类

```xml
<data>
        <import type="com.example.User"/>
        <import type="java.util.List"/>
        <variable name="user" type="User"/>
        <variable name="userList" type="List&lt;User>"/>
    </data>
```

还可以使用导入的类型来对表达式的一部分进行类型转换。以下示例将 `connection` 属性强制转换为类型 `User`

```xml
<TextView
       android:text="@{((User)(user.connection)).lastName}"
       android:layout_width="wrap_content"
       android:layout_height="wrap_content"/>
    
```

在表达式中引用静态字段和方法时，也可以使用导入的类型。以下代码会导入 `MyStringUtils` 类，并引用其 `capitalize` 方法：

```xml
<data>
        <import type="com.example.MyStringUtils"/>
        <variable name="user" type="com.example.User"/>
    </data>
    …
    <TextView
       android:text="@{MyStringUtils.capitalize(user.lastName)}"
       android:layout_width="wrap_content"
       android:layout_height="wrap_content"/>
    
```

就像在托管代码中一样，系统会自动导入 `java.lang.*`



**变量**

```xml
<data>
        <import type="android.graphics.drawable.Drawable"/>
        <variable name="user" type="com.example.User"/>
        <variable name="image" type="Drawable"/>
        <variable name="note" type="String"/>
    </data>
```

系统会根据需要生成名为 `context` 的特殊变量，用于绑定表达式。`context` 的值是根视图的 `getContext()` 方法中的 `Context` 对象。`context` 变量会被具有该名称的显式变量声明替换。



**包含**

通过使用应用命名空间和特性中的变量名称，变量可以从包含的布局传递到被包含布局的绑定。以下示例展示了来自 `name.xml` 和 `contact.xml` 布局文件的被包含 `user` 变量：

```xml
<?xml version="1.0" encoding="utf-8"?>
    <layout xmlns:android="http://schemas.android.com/apk/res/android"
            xmlns:bind="http://schemas.android.com/apk/res-auto">
       <data>
           <variable name="user" type="com.example.User"/>
       </data>
       <LinearLayout
           android:orientation="vertical"
           android:layout_width="match_parent"
           android:layout_height="match_parent">
           <include layout="@layout/name"
               bind:user="@{user}"/>
           <include layout="@layout/contact"
               bind:user="@{user}"/>
       </LinearLayout>
    </layout>
```

数据绑定不支持 include 作为 merge 元素的直接子元素。例如，以下布局不受支持：

```xml
<?xml version="1.0" encoding="utf-8"?>
    <layout xmlns:android="http://schemas.android.com/apk/res/android"
            xmlns:bind="http://schemas.android.com/apk/res-auto">
       <data>
           <variable name="user" type="com.example.User"/>
       </data>
       <merge><!-- Doesn't work -->
           <include layout="@layout/name"
               bind:user="@{user}"/>
           <include layout="@layout/contact"
               bind:user="@{user}"/>
       </merge>
    </layout>
```







## 可观察的数据对象

可观察性是指一个对象将其数据变化告知其他对象的能力

通过数据绑定库，你可以让对象、字段或集合变为可观察

任何 plain-old 对象都可用于数据绑定，但修改对象不会自动使界面更新。通过数据绑定，数据对象可在其数据发生更改时通知其他对象，即监听器。可观察类有三种不同类型：[对象](https://developer.android.com/topic/libraries/data-binding/observability?hl=zh-cn#observable_objects)、[字段](https://developer.android.com/topic/libraries/data-binding/observability?hl=zh-cn#observable_fields)和[集合](https://developer.android.com/topic/libraries/data-binding/observability?hl=zh-cn#observable_collections)。

当其中一个可观察数据对象绑定到界面并且该数据对象的属性发生更改时，界面会自动更新。

+ 可观察字段

  ```java
      private static class User {
          public final ObservableField<String> firstName = new ObservableField<>();
          public final ObservableField<String> lastName = new ObservableField<>();
          public final ObservableInt age = new ObservableInt();
      }
  ```

  

+ 可观察对象

  > ```java
  > private static class User extends BaseObservable {
  >   private String firstName;
  >   private String lastName;
  > 
  >   @Bindable
  >   public String getFirstName() {
  >       return this.firstName;
  >   }
  > 
  >   @Bindable
  >   public String getLastName() {
  >       return this.lastName;
  >   }
  > 
  >   public void setFirstName(String firstName) {
  >       this.firstName = firstName;
  >       notifyPropertyChanged(BR.firstName);
  >   }
  > 
  >   public void setLastName(String lastName) {
  >       this.lastName = lastName;
  >       notifyPropertyChanged(BR.lastName);
  >   }
  > }
  > ```
  >
  > > 具体操作过程是向 `getter` 分配 [`Bindable`](https://developer.android.com/reference/android/databinding/Bindable?hl=zh-cn) 注释，然后在 `setter` 中调用 [`notifyPropertyChanged()`](https://developer.android.com/reference/android/databinding/BaseObservable?hl=zh-cn#notifypropertychanged) 方法
  > >
  > > 数据绑定在模块包中生成一个名为 `BR` 的类，该类包含用于数据绑定的资源的 ID。在编译期间，[`Bindable`](https://developer.android.com/reference/android/databinding/Bindable?hl=zh-cn) 注释会在 `BR` 类文件中生成一个条目。如果数据类的基类无法更改，[`Observable`](https://developer.android.com/reference/android/databinding/Observable?hl=zh-cn) 接口可以使用 [`PropertyChangeRegistry`](https://developer.android.com/reference/android/databinding/PropertyChangeRegistry?hl=zh-cn) 对象实现，以便有效地注册和通知监听器。
  > >
  > > > 在某些地方，看到有直接`implements Observable`接口的，参考：
  > > >
  > > > + [4.1. Observable 对象](https://yifei8.gitbooks.io/databinding/content/chapter4.html)
  > > > + [Android中的MVVM DataBinding指南（二）](https://www.jianshu.com/p/cfd258ddc43d)
  > > >
  > > > ```java
  > > > public class BaseObservable implements Observable {
  > > >     private transient PropertyChangeRegistry mCallbacks;
  > > > 
  > > >     public BaseObservable() {
  > > >     }
  > > > 
  > > >     @Override
  > > >     public synchronized void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
  > > >         if (mCallbacks == null) {
  > > >             mCallbacks = new PropertyChangeRegistry();
  > > >         }
  > > >         mCallbacks.add(callback);
  > > >     }
  > > > 
  > > >     @Override
  > > >     public synchronized void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
  > > >         if (mCallbacks != null) {
  > > >             mCallbacks.remove(callback);
  > > >         }
  > > >     }
  > > > 
  > > >     /**
  > > >      * Notifies listeners that all properties of this instance have changed.
  > > >      */
  > > >     public synchronized void notifyChange() {
  > > >         if (mCallbacks != null) {
  > > >             mCallbacks.notifyCallbacks(this, 0, null);
  > > >         }
  > > >     }
  > > > 
  > > >     /**
  > > >      * Notifies listeners that a specific property has changed. The getter for the property
  > > >      * that changes should be marked with {@link Bindable} to generate a field in
  > > >      * <code>BR</code> to be used as <code>fieldId</code>.
  > > >      *
  > > >      * @param fieldId The generated BR id for the Bindable field.
  > > >      */
  > > >     public void notifyPropertyChanged(int fieldId) {
  > > >         if (mCallbacks != null) {
  > > >             mCallbacks.notifyCallbacks(this, fieldId, null);
  > > >         }
  > > >     }
  > > > }
  > > > ```
  > > >
  > > > 



使用 [`set()`](https://developer.android.com/reference/android/databinding/ObservableField?hl=zh-cn#set) 和 [`get()`](https://developer.android.com/reference/android/databinding/ObservableField?hl=zh-cn#get) 访问器方法，来访问字段值

```java
    user.firstName.set("Google");
    int age = user.age.get();
```



## 绑定适配器

**自动选择方法**

> 以 `android:text="@{user.name}"` 表达式为例，库会查找接受 `user.getName()` 所返回类型的 `setText(arg)` 方法。如果 `user.getName()` 的返回类型为 `String`，则库会查找接受 `String` 参数的 `setText()` 方法。如果表达式返回的是 `int`，则库会搜索接受 `int` 参数的 `setText()` 方法。表达式必须返回正确的类型，您可以根据需要强制转换返回值的类型。

Sometimes you have to perform complex data conversions. For this, you can register a custom converter via the static `@BindingAdapter` method. This method can be placed anywhere in your code and can override the default conversion of a field to your data model.

For example, assume that you want to assign a field of your data model to an image view.

```xml
  <ImageView
           android:id="@+id/icon"
           android:layout_width="40dp"
           android:layout_height="fill_parent"
           android:layout_alignParentBottom="true"
           android:layout_alignParentTop="true"
           android:layout_marginRight="6dip"
           android:contentDescription="TODO"
           android:src="@{obj.url}"
           /
```

You can register for this property on `ImageView` with the following method. This method uses [Glide](https://www.vogella.com/tutorials/AndroidHandlingImages/article.html) to download the image.

```java
@BindingAdapter("android:src")
    public static void setImageUrl(ImageView view, String url) {
        Glide.with(view.getContext()).load(url).into(view);
    }
```



## 生成的绑定类

参考自：

+ [生成的绑定类](https://developer.android.com/topic/libraries/data-binding/generated-binding)



**创建绑定对象**

如：

```java
mViewDataBinding = DataBindingUtil.setContentView(this, getLayoutId());
```



**即时绑定**

当可变或可观察对象发生更改时，绑定会按照计划在下一帧之前发生更改。但有时必须立即执行绑定。要强制执行，请使用 [`executePendingBindings()`](https://developer.android.com/reference/androidx/databinding/ViewDataBinding#executePendingBindings()) 方法。



**自定义绑定类名称**

默认情况下，绑定类是根据布局文件的名称生成的，以大写字母开头，移除下划线 ( _ )，将后一个字母大写，最后添加后缀 **Binding**。

通过调整 `data` 元素的 `class` 特性，绑定类可重命名或放置在不同的包中

```xml
<data class="ContactItem">
        …
    </data>
```



## 绑定适配器

参考：

+ [绑定适配器](https://developer.android.com/topic/libraries/data-binding/binding-adapters)



1.自动选择方法，对于名为 `example` 的特性，库自动尝试查找接受兼容类型作为参数的方法 `setExample(arg)`

2.指定自定义方法名称

一些属性具有名称不符的 setter 方法

在以下示例中，`android:tint` 属性与 `setImageTintList(ColorStateList)` 方法相关联，而不与 `setTint()` 方法相关联：

```java
    @BindingMethods({
           @BindingMethod(type = "android.widget.ImageView",
                          attribute = "android:tint",
                          method = "setImageTintList"),
    })
```



3.提供自定义的逻辑

例如，`android:paddingLeft` 特性没有关联的 setter，而是提供了 `setPadding(left, top, right, bottom)` 方法。使用 [`BindingAdapter`](https://developer.android.com/reference/androidx/databinding/BindingAdapter) 注释的静态绑定适配器方法支持自定义特性 setter 的调用方式。

```java
    @BindingAdapter("android:paddingLeft")
    public static void setPaddingLeft(View view, int padding) {
      view.setPadding(padding,
                      view.getPaddingTop(),
                      view.getPaddingRight(),
                      view.getPaddingBottom());
    }
```

还可以使用接收多个属性的适配器

```java
    @BindingAdapter({"imageUrl", "error"})
    public static void loadImage(ImageView view, String url, Drawable error) {
      Picasso.get().load(url).error(error).into(view);
    }
```

可以在布局中使用适配器，如以下示例所示。请注意，`@drawable/venueError` 引用应用中的资源。使用 `@{}` 将资源括起来可使其成为有效的绑定表达式。

```xml
<ImageView app:imageUrl="@{venue.imageUrl}" app:error="@{@drawable/venueError}" />
```

如果 `ImageView` 对象同时使用了 `imageUrl` 和 `error`，并且 `imageUrl` 是字符串，`error` 是 `Drawable`，就会调用适配器。如果您希望在设置了任意属性时调用适配器，则可以将适配器的可选 [`requireAll`](https://developer.android.com/reference/androidx/databinding/BindingAdapter#requireAll()) 标志设置为 `false`，如以下示例所示：

```java
    @BindingAdapter(value={"imageUrl", "placeholder"}, requireAll=false)
    public static void setImageUrl(ImageView imageView, String url, Drawable placeHolder) {
      if (url == null) {
        imageView.setImageDrawable(placeholder);
      } else {
        MyImageLoader.loadInto(imageView, url, placeholder);
      }
    }
```

绑定适配器方法可以选择性在处理程序中使用旧值。同时获取旧值和新值的方法应该先为属性声明所有旧值，然后再声明新值，如以下示例所示：

```java
    @BindingAdapter("android:paddingLeft")
    public static void setPaddingLeft(View view, int oldPadding, int newPadding) {
      if (oldPadding != newPadding) {
          view.setPadding(newPadding,
                          view.getPaddingTop(),
                          view.getPaddingRight(),
                          view.getPaddingBottom());
       }
    }
```

事件处理脚本只能与具有一种抽象方法的接口或抽象类一起使用，如以下示例所示：

```java
    @BindingAdapter("android:onLayoutChange")
    public static void setOnLayoutChangeListener(View view, View.OnLayoutChangeListener oldValue,
           View.OnLayoutChangeListener newValue) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
        if (oldValue != null) {
          view.removeOnLayoutChangeListener(oldValue);
        }
        if (newValue != null) {
          view.addOnLayoutChangeListener(newValue);
        }
      }
    }

    
```

按如下方式在布局中使用此事件处理脚本：

```xml
<View android:onLayoutChange="@{() -> handler.layoutChanged()}"/>
```



#### 自定义转换

在某些情况下，需要在特定类型之间进行自定义转换。例如，视图的 `android:background` 特性需要 `Drawable`，但指定的 `color` 值是整数。以下示例展示了某个属性需要 `Drawable`，但结果提供了一个整数：

```xml
<View
       android:background="@{isError ? @color/red : @color/white}"
       android:layout_width="wrap_content"
       android:layout_height="wrap_content"/>
    
```

每当需要 `Drawable` 且返回整数时，`int` 都应转换为 `ColorDrawable`。您可以使用带有 [`BindingConversion`](https://developer.android.com/reference/androidx/databinding/BindingConversion) 注释的静态方法完成这个转换，如下所示：

```java
    @BindingConversion
    public static ColorDrawable convertColorToDrawable(int color) {
        return new ColorDrawable(color);
    }

    
```

但是，绑定表达式中提供的值类型必须保持一致。您不能在同一个表达式中使用不同的类型，如以下示例所示：

```xml
<View
       android:background="@{isError ? @drawable/error : @color/white}"
       android:layout_width="wrap_content"
       android:layout_height="wrap_content"/>
    
```



## 将布局视图绑定到架构组件

参考：

+ [将布局视图绑定到架构组件](https://developer.android.com/topic/libraries/data-binding/architecture)



1.使用LiveData

> 与实现 [`Observable`](https://developer.android.com/reference/androidx/databinding/Observable) 的对象（例如[可观察字段](https://developer.android.com/topic/libraries/data-binding/observability#observable_fields)）不同，`LiveData` 对象了解订阅数据更改的观察器的生命周期。了解这一点有许多好处，具体说明请参阅[使用 LiveData 的优势](https://developer.android.com/topic/libraries/architecture/livedata#the_advantages_of_using_livedata)



2.使用ViewModel

```java
    class ViewModelActivity extends AppCompatActivity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            // Obtain the ViewModel component.
            UserModel userModel = new ViewModelProvider(this).get(UserModel.class);

            // Inflate view and obtain an instance of the binding class.
            UserBinding binding = DataBindingUtil.setContentView(this, R.layout.user);

            // Assign the component to a property in the binding class.
            binding.viewmodel = userModel;
        }
    }
    
```

```xml
<CheckBox
        android:id="@+id/rememberMeCheckBox"
        android:checked="@{viewmodel.rememberMe}"
        android:onCheckedChanged="@{() -> viewmodel.rememberMeChanged()}" />
```



3.使用Observable ViewModel 

这种情况与使用LiveData类似

使用实现 `Observable` 的 `ViewModel` 组件可让您更好地控制应用中的绑定适配器。例如，这种模式可让您更好地控制数据更改时发出的通知，您还可以指定自定义方法来设置双向数据绑定中的属性值。

如需实现可观察的 `ViewModel` 组件，您必须创建一个从 [`ViewModel`](https://developer.android.com/reference/androidx/lifecycle/ViewModel) 类继承而来并实现 [`Observable`](https://developer.android.com/reference/androidx/databinding/Observable) 接口的类。您可以使用 [`addOnPropertyChangedCallback()`](https://developer.android.com/reference/androidx/databinding/Observable#addOnPropertyChangedCallback(android.databinding.Observable.OnPropertyChangedCallback)) 和 [`removeOnPropertyChangedCallback()`](https://developer.android.com/reference/androidx/databinding/Observable#removeOnPropertyChangedCallback(android.databinding.Observable.OnPropertyChangedCallback)) 方法提供观察器订阅或取消订阅通知时的自定义逻辑。您还可以在 [`notifyPropertyChanged()`](https://developer.android.com/reference/androidx/databinding/BaseObservable#notifyPropertyChanged(int)) 方法中提供属性更改时运行的自定义逻辑。以下代码示例展示了如何实现一个可观察的 `ViewModel`：

```java
    /**
     * A ViewModel that is also an Observable,
     * to be used with the Data Binding Library.
     */
    class ObservableViewModel extends ViewModel implements Observable {
        private PropertyChangeRegistry callbacks = new PropertyChangeRegistry();

        @Override
        protected void addOnPropertyChangedCallback(
                Observable.OnPropertyChangedCallback callback) {
            callbacks.add(callback);
        }

        @Override
        protected void removeOnPropertyChangedCallback(
                Observable.OnPropertyChangedCallback callback) {
            callbacks.remove(callback);
        }

        /**
         * Notifies observers that all properties of this instance have changed.
         */
        void notifyChange() {
            callbacks.notifyCallbacks(this, 0, null);
        }

        /**
         * Notifies observers that a specific property has changed. The getter for the
         * property that changes should be marked with the @Bindable annotation to
         * generate a field in the BR class to be used as the fieldId parameter.
         *
         * @param fieldId The generated BR id for the Bindable field.
         */
        void notifyPropertyChanged(int fieldId) {
            callbacks.notifyCallbacks(this, fieldId, null);
        }
    }
    
```



## 双向数据绑定

参考：

+ [双向数据绑定](https://developer.android.com/topic/libraries/data-binding/two-way)



使用单向数据绑定时，您可以为特性设置值，并设置对该特性的变化作出反应的监听器：

```xml
    <CheckBox
        android:id="@+id/rememberMeCheckBox"
        android:checked="@{viewmodel.rememberMe}"
        android:onCheckedChanged="@{viewmodel.rememberMeChanged}"
    />
    
```

双向数据绑定为此过程提供了一种快捷方式：

```xml
    <CheckBox
        android:id="@+id/rememberMeCheckBox"
        android:checked="@={viewmodel.rememberMe}"
    />
    
```

`@={}` 表示法（其中重要的是包含“=”符号）可接收属性的数据更改并同时监听用户更新。





## 教程

### 1.参考[Using data binding in Android - Tutorial](https://www.vogella.com/tutorials/AndroidDatabinding/article.html)

+ 演示了DataBinding的基本使用
+ RecyclerView中DataBinding的使用
+ 自定义绑定适配器



## 其它

教程

+ [DataBinding，再学不会你砍我](https://juejin.cn/post/6844903917424214029)
+ [Android DataBinding 从入门到进阶](https://juejin.cn/post/6844903609079971854)



































