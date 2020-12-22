## Fragment深入

**从 fragment 中启动 activity** 

调用`Fragment.startActivity(Intent)`方法，由它在后台再调用对应的`Activity`方法 

```java
public class CrimeActivity extends SingleFragmentActivity {
    
    public static final String EXTRA_CRIME_ID = "com.bignerdranch.android.criminalintent.crime_id";
    
   //定义启动方法
    public static Intent newIntent(Context packageContext, UUID crimeId) { 
        Intent intent = new Intent(packageContext, CrimeActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        return intent;
    } 
    ...
}

//启动activity
private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    ...
    @Override
    public void onClick(View view) {
        Intent intent = CrimeActivity.newIntent(getActivity(), mCrime.getId());
        startActivity(intent);
    }
}
```

然后可以在fragment的`onCreate()`方法中获取intent中的数据

```java
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);
      
        UUID crimeId = (UUID)getActivity().getIntent().getSerializableExtra(CrimeActivity.EXTRA_CRIME_ID);
      mCrime = CrimeLab.get(getActivity()).getCrime(crimeId)
    }
```

但这样做有一个小缺点就是，fragment和CrimeActivity绑定了，**不再是可复用的构建单元** 

一个比较好的做法是，将`crime ID`存储在属于`CrimeFragment`的某个地方，而不是保存在`CrimeActivity`的私有空间里 



## fragment argument 

每个fragment实例都可附带一个`Bundle`对象。该bundle包含键-值对，我们可以像附加extra 到`Activity`的intent中那样使用它们。一个键-值对即一个argument 

使用`Fragment.setArguments(Bundle)` 方法

Android开发人员采取的习惯做法是:添加名为`newInstance()`的静态方法给`Fragment`类。使用该方法，完成fragment实例及`Bundle`对象的创建，然后将argument放入bundle中，最后再附加给fragment。

```java
public class CrimeFragment extends Fragment {
    private static final String ARG_CRIME_ID = "crime_id";
  
    //构造方法
    public static CrimeFragment newInstance(UUID crimeId) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);

        return fragment;

    }
}
```



### 获取 argument 

调用Fragment类的`getArguments()`方法，再调用`Bundle`限定类型的get方法，如`getSerializable(...)`方法 

```java
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UUID uuid = (UUID) getArguments().getSerializable(ARG_CRIME_ID);

        mCrime = CrimeLab.get(getActivity()).getCrime(uuid);

    }
```



### 通过 fragment 获取返回结果 

从已启动的activity获取返回结果，调用的是Activity的 `startActivityForResult(...)` 方法，重写`Activity.onActivityResult(...)`方法  

对于fragment，调用`Fragment.startActivityForResult(...)`方法，重写`Fragment.onActivityResult(...)` 方法

![028](https://github.com/winfredzen/Android-Basic/raw/master/images/028.png)

除将返回结果从托管activity传递给fragment的额外实现代码之外，`Fragment.startActivityForResult(Intent,int)`方法类似于Activity的同名方法。 

从fragment中返回结果的处理稍有不同。fragment能够从activity中接收返回结果，但其自身无法持有返回结果。只有activity拥有返回结果。因此，尽管Fragment有自己的`startActivityForResult(...)`方法和`onActivityResult(...)`方法，但没有`setResult(...)`方法。 

相反，应让托管activity返回结果值，具体代码如下

![029](https://github.com/winfredzen/Android-Basic/raw/master/images/029.png)









