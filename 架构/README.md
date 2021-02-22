# DataBinding

官方文档：

+ [数据绑定库](https://developer.android.com/topic/libraries/data-binding)



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

如果您要在 `Fragment`、`ListView` 或 `RecyclerView` 适配器中使用数据绑定项，您可能更愿意使用绑定类或 [`DataBindingUtil`](https://developer.android.com/reference/androidx/databinding/DataBindingUtil?hl=zh-cn) 类的 [`inflate()`](https://developer.android.com/reference/androidx/databinding/DataBindingUtil?hl=zh-cn#inflate(android.view.LayoutInflater, int, android.view.ViewGroup, boolean, android.databinding.DataBindingComponent)) 方法，如以下代码示例所示

```java
ListItemBinding binding = ListItemBinding.inflate(layoutInflater, viewGroup, false);
// or
ListItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.list_item, viewGroup, false);
```



5.可观察的数据对象

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
  >  private static class User extends BaseObservable {
  >      private String firstName;
  >      private String lastName;
  > 
  >      @Bindable
  >      public String getFirstName() {
  >          return this.firstName;
  >      }
  > 
  >      @Bindable
  >      public String getLastName() {
  >          return this.lastName;
  >      }
  > 
  >      public void setFirstName(String firstName) {
  >          this.firstName = firstName;
  >          notifyPropertyChanged(BR.firstName);
  >      }
  > 
  >      public void setLastName(String lastName) {
  >          this.lastName = lastName;
  >          notifyPropertyChanged(BR.lastName);
  >      }
  >  }
  > ```
  >
  > > 具体操作过程是向 `getter` 分配 [`Bindable`](https://developer.android.com/reference/android/databinding/Bindable?hl=zh-cn) 注释，然后在 `setter` 中调用 [`notifyPropertyChanged()`](https://developer.android.com/reference/android/databinding/BaseObservable?hl=zh-cn#notifypropertychanged) 方法
  > >
  > > 数据绑定在模块包中生成一个名为 `BR` 的类，该类包含用于数据绑定的资源的 ID。在编译期间，[`Bindable`](https://developer.android.com/reference/android/databinding/Bindable?hl=zh-cn) 注释会在 `BR` 类文件中生成一个条目。如果数据类的基类无法更改，[`Observable`](https://developer.android.com/reference/android/databinding/Observable?hl=zh-cn) 接口可以使用 [`PropertyChangeRegistry`](https://developer.android.com/reference/android/databinding/PropertyChangeRegistry?hl=zh-cn) 对象实现，以便有效地注册和通知监听器。



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



## 教程

### 1.参考[Using data binding in Android - Tutorial](https://www.vogella.com/tutorials/AndroidDatabinding/article.html)

+ 演示了DataBinding的基本使用
+ RecyclerView中DataBinding的使用
+ 自定义绑定适配器



## 其它

教程

+ [DataBinding，再学不会你砍我](https://juejin.cn/post/6844903917424214029)
+ [Android DataBinding 从入门到进阶](https://juejin.cn/post/6844903609079971854)



































