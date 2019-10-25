# ToolBar、ActionBar

可参考[添加应用栏](https://developer.android.com/training/appbar)中的描述：

>从 Android 3.0（API 级别 11）开始，所有使用默认主题背景的 Activity 均使用 `ActionBar` 作为应用栏。不过，经过不同 Android 版本的演化，应用栏功能已逐渐添加到原生 `ActionBar` 中。因此，原生 `ActionBar` 的行为会有所不同，具体取决于设备使用的是哪个版本的 Android 系统。相比之下，最新功能已添加到支持库版本的 `Toolbar` 中，并且这些功能可以在任何能够使用该支持库的设备上使用。

> 因此，您应使用支持库的 `Toolbar` 类来实现 Activity 的应用栏。使用支持库的工具栏有助于确保您的应用在最大范围的设备上保持一致的行为

**在 XML 文件中定义菜单** 

菜单定义文件位于`res/menu`目录下

在res目录上，右键，选择`New → Android resource file`菜单项，在弹出的窗 口界面，选择`Menu`资源类型 

![033](https://github.com/winfredzen/Android-Basic/blob/master/images/033.png)

```xml
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto">

    <item android:id="@+id/new_crime"
        android:icon="@drawable/ic_menu_add"
        android:title="@string/new_crime"
        app:showAsAction="ifRoom|withText" />

    <item android:id="@+id/show_subtitle"
        android:title="@string/show_subtitle"
        app:showAsAction="ifRoom"/>

</menu>
```

> `showAsAction`属性用于指定菜单项是显示在工具栏上，还是隐藏于溢出菜单(overflow menu)。该属性当前设置为`ifRoom`和`withText`的组合值。因此，只要空间足够，菜单项图标及 其文字描述都会显示在工具栏上。如果空间仅够显示菜单项图标，文字描述就不会显示。如果空间大小不够显示任何项，菜单项就会隐藏到溢出菜单中。 

> 属性`showAsAction`还有另外两个可选值:`always`和`never`。不推荐使用`always`，应尽量使用`ifRoom`属性值，让操作系统决定如何显示菜单项。对于那些很少用到的菜单项，`never`属性值是个不错的选择。总之，为了避免用户界面混乱，工具栏上只应放置常用菜单项 



注意是`app:showAsAction`，why？

>注意，不同于常见的android命名空间声明，`fragment_crime_list.xml`文件使用`xmlns`标签定 义了全新的app命名空间。指定`showAsAction`属性时，就用了这个新定义的命名空间。 

> 出于兼容性考虑，AppCompat库需要使用app命名空间。操作栏API随Android 3.0引入。为了支持各种旧系统版本设备，早期创建的AppCompat库捆绑了兼容版操作栏。这样一来，不管新旧，所有设备都能用上操作栏。在运行Android 2.3或更早版本系统的设备上，菜单及其相应的XML 文件确实是存在的，但是`android:showAsAction`属性是随着操作栏的发布才添加的。 

> `AppCompat`库不希望使用原生`showAsAction`属性，因此，它提供了定制版`showAsAction`属性(`app:showAsAction`) 



**系统图标** 

系统图标(system icon)是Android操作 系统内置的图标 

`@android:drawable/ic_menu_add`就引用了系统图标 

使用`Android Studio`内置的`Android Asset Studio`工具，为工具栏创建或定制图片

在项目工具窗口中，右键单击`drawable`目录，选择`New → Image Asset`菜单项

+ 在Icon Type一栏选Action Bar and Tab Icons 
+ 在Name一栏输入名称
+ Asset Type处选Clip Art 

然后点击`Clip Art`按钮挑选剪贴画图片 

![034](https://github.com/winfredzen/Android-Basic/blob/master/images/034.png)



**创建菜单**

+ `onCreateOptionsMenu(Menu)` 创建菜单
+ `onOptionsItemSelected(MenuItem item)` 菜单点击

```java
    //菜单
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.fragment_crime_list, menu);

        MenuItem subtileItem = menu.findItem(R.id.show_subtitle);
        if (mSubtitleVisible) {
            subtileItem.setTitle(R.string.hide_subtitle);
        } else {
            subtileItem.setTitle(R.string.show_subtitle);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.new_crime:

                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                Intent intent = CrimePagerActivity.newIntent(getContext(), crime.getId());
                startActivity(intent);
                return true;
            case R.id.show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;

                //表示要重新创建menu
                getActivity().invalidateOptionsMenu();;

                updateSubtitle();
                return true;

                default:
                    return super.onOptionsItemSelected(item);
        }

    }
```

使用`MenuInflater.inflate(int, Menu)`方法并传入菜单文件的资源 ID，将布局文件中定义的菜单项目填充到Menu实例中

在Fragment中如何处理？

>`Fragment.onCreateOptionsMenu(Menu, MenuInflater)`方法是由`FragmentManager`负责调用的。因此，当activity接收到操作系统的`onCreateOptionsMenu(...)`方法回调请求时，我们 必须明确告`FragmentManager`其管理的fragment应接收`onCreateOptionsMenu(...)`方法的调用指令。要通知`FragmentManager`，需调用以下方法 
>
>```java
>public void setHasOptionsMenu(boolean hasMenu)
>```

如下的`CrimeListFragment`，在`onCreate`方法中进行了设置

```java
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //有menu
        setHasOptionsMenu(true);

        if (savedInstanceState != null) {
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }
    }
```

长按menu item，会显示提示文字

![035](https://github.com/winfredzen/Android-Basic/blob/master/images/035.png)



**层级式导航** 

+ 后退键导航又称为*临时性导航*， 只能返回到上一次浏览过的用户界面 
+ 层级式导航(`hierarchical navigation`，有时又称为`ancestral navigation`)可在应用内逐级向上导航 

在`AndroidManifest.xml`中添加`parentActivityName`属性

```xml
        <activity android:name=".CrimePagerActivity"
            android:parentActivityName=".CrimeListActivity">

        </activity>
```

![036](https://github.com/winfredzen/Android-Basic/blob/master/images/036.png)

**工作原理**

用户点击向上按钮自`CrimePagerActivity`界面向上导航时，如下的intent会被创建 

```java
Intent intent = new Intent(this, CrimeListActivity.class); intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
startActivity(intent);
finish();
```

`FLAG_ACTIVITY_CLEAR_TOP`指示Android在回退栈中寻找指定的activity实例。如果实例存在，则弹出栈内所有其他activity，让启动的目标activity出现在栈顶(显示在屏幕上) 

![037](https://github.com/winfredzen/Android-Basic/blob/master/images/037.png)









