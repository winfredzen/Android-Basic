# Fragment基础

Fragment有关的基础教程，可参考郭霖的相关博客：

+ [Android Fragment完全解析，关于碎片你所需知道的一切](https://guolin.blog.csdn.net/article/details/8881711)
+ [Android手机平板两不误，使用Fragment实现兼容手机和平板的程序](https://blog.csdn.net/guolin_blog/article/details/8744943)
+ [Android Fragment应用实战，使用碎片向ActivityGroup说再见](https://blog.csdn.net/guolin_blog/article/details/13171191)





----

[fragment](https://developer.android.com/reference/android/app/Fragment):

> A Fragment is a piece of an application's user interface or behavior that can be placed in an `Activity`. Interaction with fragments is done through `FragmentManager`, which can be obtained via `Activity#getFragmentManager()` and `Fragment#getFragmentManager()`

托管:activity在其视图层级里提供一处位置，用来放置fragment视图 ,fragment本身没有在屏幕上显示视图的能力。因此，只有将它的视图放置在activity的视图层级结构中，fragment视图才能显示在屏幕上 

![019](https://github.com/winfredzen/Android-Basic/raw/master/images/019.png)

**两类 fragment** 

一个是系统内置的`android.app.Fragment`，一个是`support-v4`库中的`android.support.v4.app.Fragment`，建议使用支持库的

## 托管 UI fragment

为托管UI fragment，activity必须：

+ 在其布局中为fragment的视图安排位置 
+ 管理fragment实例的生命周期 

### Fragment生命周期

fragment生命周期与activity生命周期的一个关键区别就在于，**fragment的生命周期方法由托管activity而不是操作系统调用**。操作系统不关心activity用来管理视图的fragment。fragment的使用是activity内部的事情 

状态：

+ 运行状态 - 当一个碎片是可见的，并且它所关联的活动正处于运行状态时，该碎片也处于运行状态
+ 暂停状态 - 当一个活动进入暂停状态时(由于另一个未占满屏幕的活动被添加到了栈顶)，与它相关联的可见碎片就会进入到暂停状态
+ 停止状态 - 当一个活动进入停止状态，与它相关联的碎片就会进入到停止状态，或者通过调用`FragmentTransition`的`remove()`、 `repleace()`方法将碎片从活动中移除，但如果在事务提交前调用`addToBackStack()`方法，这时的碎片也会进入到停止状态。进入停止状态的碎片对用户来说是完全不可见的，有可能会被系统回收
+ 销毁状态 - 碎片总是依附于活动而存在，因此当活动被销毁时，与它相关联的碎片就会进入到销毁状态。或者通过调用`FragmentTransition`的`remove()`、 `repleace()`方法将碎片从活动中移除，当在事物提交之前并没有调用`addToBackStack()`方法，这时的碎片也会进入到销毁状态

生命周期，来自官网[片段](https://developer.android.com/guide/components/fragments):

![022](https://github.com/winfredzen/Android-Basic/raw/master/images/022.png)

![023](https://github.com/winfredzen/Android-Basic/raw/master/images/023.png)

+ `onCreate()` - 当碎片和活动建立关联的时候调用
+ `onCreateView()` - 为碎片创建视图(加载布局)时调用
+ `onActivityCreated()` - 确保与碎片相关联的活动一定已经创建完毕的时候调用
+ `onDestroyView()` - 当与碎片关联的视图被移除的时候调用
+ `onDetach()` - 当碎片和活动解除关联的时候调用

### 托管的2种方式

activity托管UI fragment有如下两种方式：

+ 在activity布局中添加fragment

  > 这种方式简单但不够灵活。在activity布局中添加 fragment，就等同于将fragment及其视图与activity的视图绑定在一起，并且在activity的生命周期 过程中，无法替换fragment视图 

+ 在activity代码中添加fragment 

  > 这种方式比较复杂，但也是唯一可以动态控制fragment的方式。何时添加fragment以及随 后可以完成何种具体任务由你自己定;也可以移除fragment，用其他fragment代替当前fragment， 然后重新添加已移除的fragment 



### 定义容器视图 

通常使用`FrameLayout` 

![061](https://github.com/winfredzen/Android-Basic/blob/master/基础知识/images/061.png)



## 创建 UI fragment 

创建UI fragment的步骤与创建activity的步骤相同: 

+ 定义用户界面布局文件
+ 创建fragment类并设置其视图为定义的布局; 
+ 编写代码以实例化组件。 

```java
 public class CrimeFragment extends Fragment {
         private Crime mCrime;
         @Override
         public void onCreate(Bundle savedInstanceState) {
           super.onCreate(savedInstanceState); 
           mCrime = new Crime();
         }
} 
```

>首先，`Fragment.onCreate(Bundle)`是公共方法，而`Activity.onCreate(Bundle)`是受保 护方法。`Fragment.onCreate(Bundle)`方法及其他`Fragment`生命周期方法必须是公共方法，因 为托管fragment的activity要调用它们。 

> 其次，类似于activity，fragment同样具有保存及获取状态的bundle。如同使用`Activity. onSaveInstanceState(Bundle)`方法那样，你也可以根据需要覆盖`Fragment.onSaveInstance- State(Bundle)`方法。 

> 另外，fragment的视图并没有在`Fragment.onCreate(Bundle)`方法中生成。虽然我们在该方 法中配置了fragment实例，但创建和配置fragment视图是另一个Fragment生命周期方法完成的 
>
> ```java
> public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
> ```



```java
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_crime, container, false);
        return view;

    }
```

`activity_fragment.xml`的布局如下：

![062](https://github.com/winfredzen/Android-Basic/blob/master/基础知识/images/062.png)

## 向 FragmentManager 添加 UI fragment 

`FragmentManager`类负责管理fragment并将它们的视图添加到activity的视图层级结构中 

![024](https://github.com/winfredzen/Android-Basic/raw/master/images/024.png)

FragmentManager类具体管理:

+ fragment队列
+ fragment事务回退栈 

获取`FragmentManager`

```java
FragmentManager fm = getSupportFragmentManager();
```

**fragment 事务** 

例如，添加一个fragment，这里是定义了一个持有单个`Fragment`的`Activity`的基类`SingleFragmentActivity`：

```java
/**
 * 通用超类
 */
public abstract class SingleFragmentActivity extends AppCompatActivity {

    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentManager fm = getSupportFragmentManager();

        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }

    }
}
```

> `Fragment fragment = fm.findFragmentById(R.id.fragment_container)`
>
> `FragmentManager`使用`FrameLayout`组件的**资源ID**识别`CrimeFragment`，这看上去可能有点怪。但实际上，使用容器视图资源ID识别UI fragment就是`FragmentManager`的一种内部实现机制。 如果要向activity添加多个fragment，通常就需要分别为每个fragment创建具有不同ID的不同容器 （如果container中只有一个fragment可以这样做？）

>`FragmentManager.beginTransaction()` 方 法 创 建 并 返 回 `FragmentTransaction` 实 例 。 
>
>`FragmentTransaction`类支持流接口(fluent interface)的链式方法调用，以此配置Fragment- Transaction再返回它。因此，以上灰底代码可解读为:“创建一个新的fragment事务，执行一个 fragment添加操作，然后提交该事务 

> `FragmentTransaction add(@IdRes int containerViewId, Fragment fragment)`
>
> + 容器视图资源ID
> + 新创建的fragment

为什么要判断`fragment == null`？

> 前面说过，设备旋转或回收内存时，Android系统会销毁`CrimeActivity`，而后重建时，会调用`CrimeActivity.onCreate(Bundle)`方法。activity被销毁时，它的`FragmentManager`会将fragment队列保存下来。这样，activity重建时，新的`FragmentManager`会首先获取保存的队列，然后重建fragment队列，从而恢复到原来的状态 
>
> 当然，如果指定容器视图资源ID的fragment不存在，则fragment变量为空值。这时应该新建 `CrimeFragment`，并启动一个新的fragment事务，将新建fragment添加到队列中 













