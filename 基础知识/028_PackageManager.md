# PackageManager

参考：

+ [Android随笔之——PackageManager详解](https://www.cnblogs.com/travellife/p/3932823.html)

+ [APK安装流程详解2——PackageManager简介](https://www.jianshu.com/p/c56376916d5e)
+ [Android PackageManager 详解](https://www.cnblogs.com/a284628487/archive/2013/06/01/3111913.html)

PackageManager是用于获取Android系统中应用程序的信息，查询Application相关信息(application，activity，receiver，service，provider及相应属性等）、查询已安装应用、增加或删除permission、清除用户数据、缓存，代码段等

## 查询应用信息

使用PackageManager获取所有可启动主activity，可启动的activity都带有包含`MAIN`操作和`LAUNCHER`类型的intent过滤器

```xml
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
```

方法

```java
public abstract List<ResolveInfo> queryIntentActivities (Intent intent, 
                int flags)
```

获取匹配的的activity信息

+ intent 查询条件，Activity所配置的action和category
+ flags：MATCH_DEFAULT_ONLY:Category必须带有CATEGORY_DEFAULT的Activity;GET_INTENT_FILTERS:匹配Intent条件即可;GET_RESOLVED_FILTER:匹配Intent条件即可;(本质上是Activity)

如下，获取activity，并按activity标签首字母排序。使用的是隐式Intent获取目标activity

```java
        Intent startIntent = new Intent(Intent.ACTION_MAIN);
        startIntent.addCategory(Intent.CATEGORY_LAUNCHER); 

				PackageManager pm = getActivity().getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(startIntent, 0);

        Log.i(TAG, "Found " + activities.size() + " activities.");

        //排序
        Collections.sort(activities, new Comparator<ResolveInfo>() {
            @Override
            public int compare(ResolveInfo a, ResolveInfo b) {
                PackageManager pm = getActivity().getPackageManager();
                return String.CASE_INSENSITIVE_ORDER.compare(a.loadLabel(pm).toString(), b.loadLabel(pm).toString());
            }
        });
```

列出手机中的其它应用，获取Activity的名称，如下的绑定数据

```java
        public void bindActivity(ResolveInfo resolveInfo) {
            mResolveInfo = resolveInfo;
            PackageManager pm = getActivity().getPackageManager();
            String appName = mResolveInfo.loadLabel(pm).toString();//名称
            mNameTextView.setText(appName);

            Drawable drawable = mResolveInfo.loadIcon(pm);//图标
            mImageView.setImageDrawable(drawable);
        }
```

![044](https://github.com/winfredzen/Android-Basic/blob/master/%E5%9F%BA%E7%A1%80%E7%9F%A5%E8%AF%86/images/044.png)

## 启动目标Activity

启动activity，需创建启动activity的显示intent，需要从`ResolveInfo`对象中获取activity的包名与类名

```java
        /*点击监听*/
        @Override
        public void onClick(View v) {

            ActivityInfo activityInfo = mResolveInfo.activityInfo;

            Intent intent = new Intent(Intent.ACTION_MAIN)
                    .setClassName(activityInfo.applicationInfo.packageName, activityInfo.name);

            startActivity(intent);

        }
```

例如，此时点击“58同城”该应用，在overview screen会发现，并没用启动的新的应用，后台只有一个当前的应用`NerdLauncher`

![045](https://github.com/winfredzen/Android-Basic/blob/master/%E5%9F%BA%E7%A1%80%E7%9F%A5%E8%AF%86/images/045.png)

这是为什么呢？



### 任务与回退栈

应用运行时，Android使用任务来跟踪用户的状态。通过**Android默认启动器**应用打开的应用都有自己的任务

**任务**是一个activity栈。栈底部的activity通常称为基activity。栈顶的activity用户能看得到。如果按后退键，栈顶activity会弹出栈外。如果用户看到的是基activity，按后退键，系统就会回到主屏幕

默认情况下，新activity都在当前任务中启动。

![046](https://github.com/winfredzen/Android-Basic/blob/master/%E5%9F%BA%E7%A1%80%E7%9F%A5%E8%AF%86/images/046.png)

在当前任务中启动activity的好处是，用户可以在任务内而不是在应用层级间导航返回

![047](https://github.com/winfredzen/Android-Basic/blob/master/%E5%9F%BA%E7%A1%80%E7%9F%A5%E8%AF%86/images/047.png)



### 启动新任务

有时你需要在当前任务中启动activity，而有时又需要在新任务中启动activity

在上面的例子中，从NerdLauncher启动的任何activity都会添加到NerdLauncher任务中

![048](https://github.com/winfredzen/Android-Basic/blob/master/%E5%9F%BA%E7%A1%80%E7%9F%A5%E8%AF%86/images/048.png)

现在是需要NerdLauncher在**新任务**中启动activity

![049](https://github.com/winfredzen/Android-Basic/blob/master/%E5%9F%BA%E7%A1%80%E7%9F%A5%E8%AF%86/images/049.png)

为了在启动新activity时启动新任务，需要为intent添加一个**标志**

```java
        /*点击监听*/
        @Override
        public void onClick(View v) {

            ActivityInfo activityInfo = mResolveInfo.activityInfo;

            Intent intent = new Intent(Intent.ACTION_MAIN)
                    .setClassName(activityInfo.applicationInfo.packageName, activityInfo.name)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);

        }
```

![050](https://github.com/winfredzen/Android-Basic/blob/master/%E5%9F%BA%E7%A1%80%E7%9F%A5%E8%AF%86/images/050.png)



## 使用NerdLauncher应用作为设备主屏幕

添加**HOME**和**DEFAULT**类别定义后，NerdLauncher应用的activity会成为可选的主界面

```xml
<intent-filter>
	<action android:name="android.intent.action.MAIN" /> 
	<category android:name="android.intent.category.LAUNCHER" /> 
	<category android:name="android.intent.category.HOME" /> 
	<category android:name="android.intent.category.DEFAULT" />
</intent-filter>
```



## 进程与任务

对象需要内存和虚拟机的支持才能生存。**进程**是操作系统创建的、供应用对象生存以及应用运行的地方。

进程通常会占用由操作系统管理着的系统资源，如内存、网络端口以及打开的文件等。进程还拥有至少一个(可能多个)执行线程。在Android系统中，每个进程都需要一个**虚拟机**来运行。

尽管存在未知的异常情况，但总的来说，Android世界里的每个应用组件都仅与一个进程相关联。应用伴随着自己的进程一起完成创建，该进程同时也是应用中所有组件的默认进程。

(虽然组件可以指派给不同的进程，但我们推荐使用默认进程。如果确实需要在不同进程中运行应用组件，通常也可以借助多线程来实现。相比多进程的使用，Android多线程的使用更加简单。)

每一个activity实例都仅存在于一个进程之中，同一个任务关联。这也是进程与任务的唯一相似之处。任务只包含activity，这些activity通常来自于不同的应用进程;而进程则包含了应用的全部运行代码和对象。

进程与任务很容易让人混淆，主要原因在于它们不仅在概念上有某种重叠，而且通常会被人以应用名提及。例如，从`NerdLauncher`启动器中启动`CriminalIntent`应用时，操作系统创建了一个`CriminalIntent`进程以及一个以`CrimeListActivity`为基栈activity的新任务。在overview screen中， 可以看到这个任务就被标名为`CriminalIntent`。













