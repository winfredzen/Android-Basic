# DialogFragment

[DialogFragment](https://developer.android.com/reference/android/app/DialogFragment)介绍如下：

>**This class was deprecated in API level 28.**
>Use the [Support Library](https://developer.android.com/tools/extras/support-library.html) `DialogFragment` for consistent behavior across all devices and access to [Lifecycle](https://developer.android.com/topic/libraries/architecture/lifecycle.html).
>
>已经废弃了，使用支持库中的版本

> A fragment that displays a dialog window, floating on top of its activity's window. This fragment contains a Dialog object, which it displays as appropriate based on the fragment's state. Control of the dialog (deciding when to show, hide, dismiss it) should be done through the API here, not with direct calls on the dialog.
>
> 显示dialog窗口的片段，浮动在所有activity窗口的上面。包含一个Dialog对象
>
> Implementations should override this class and implement `Fragment.onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)` to supply the content of the dialog. Alternatively, they can override `onCreateDialog(android.os.Bundle)` to create an entirely custom dialog, such as an AlertDialog, with its own content.
>
> 可重写`Fragment.onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)`来提供dialog的内容，另外，可以重写`onCreateDialog(android.os.Bundle)`来自定义dialog，例如一个`AlertDialog`

参考：

+ [Android 必知必会 - DialogFragment 使用总结](https://likfe.com/2016/10/27/dialog-fragment/)
+ [DialogFragment使用到源码完全解析](https://juejin.im/post/5c270c396fb9a04a09561f16)

显示使用如下的方法：

```java
public void show(FragmentManager manager, String tag)
public void show(FragmentTransaction transaction, String tag)
```

> String参数可唯一识别`FragmentManager`队列中的`DialogFragment`。两个方法都可以:如果传入`FragmentTransaction`参数，你自己负责创建并提交事务;如果传入`FragmentManager` 参数，系统会自动创建并提交事务 

## 使用

1.弹出一个日期选择框

>建议将`AlertDialog`封装在`DialogFragment`(Fragment的子类)实例中使用。当然，不使 用`DialogFragment`也可显示`AlertDialog`视图，但不推荐这样做。使用`FragmentManager`管理对话框，可以更灵活地显示对话框。 

> 另外，如果旋转设备，单独使用的`AlertDialog`会消失，而封装在fragment中的`AlertDialog` 则不会有此问题(旋转后，对话框会被重建恢复) 

注意要使用`android.support.v4.app.DialogFragment`

`dialog_date.xml`内容如下，定义了一个`DatePicker`

```xml
<?xml version="1.0" encoding="utf-8"?>
<DatePicker xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/dialog_date_picker"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:calendarViewShown="false">

</DatePicker>
```

`DatePickerFragment`实现如下：

```java
//对话框
public class DatePickerFragment extends DialogFragment {

    private static final String ARG_DATE = "date";

    public static final String EXTRA_DATE = "com.bignerdranch.android.criminalintent.date";

    private DatePicker mDatePicker;

    //创建实例对象，传入date对象
    public static DatePickerFragment newInstance(Date date) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE, date);

        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //取出传入的对象
        Date date = (Date) getArguments().getSerializable(ARG_DATE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(calendar.MONTH);
        int day = calendar.get(calendar.DAY_OF_MONTH);

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date, null);
        mDatePicker = (DatePicker) view.findViewById(R.id.dialog_date_picker);
        mDatePicker.init(year, month, day, null);


        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.date_picker_title)
                .setView(view)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        int year = mDatePicker.getYear();
                        int month = mDatePicker.getMonth();
                        int day = mDatePicker.getDayOfMonth();
                        Date date = new GregorianCalendar(year, month, day).getTime();

                        sendResult(Activity.RESULT_OK, date);

                    }
                })
                .create();

    }


    //发送结果
    private void sendResult(int resultCode, Date date) {
        if (getTargetFragment() == null) {
            return;
        }
        //选择的date
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE, date);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

}

```

弹出效果如下：

![030](https://github.com/winfredzen/Android-Basic/blob/master/images/030.png)

但现在还设计到数据传递的问题

1.CrimeFragment 将 Date 传递给DatePickerFragment 

2.DatePickerFragment 将选择好Date 传递给CrimeFragment

![031](https://github.com/winfredzen/Android-Basic/blob/master/images/031.png)

**传递数据给 DatePickerFragment** 

通过设置`setArguments`

```java
    //创建实例对象，传入date对象
    public static DatePickerFragment newInstance(Date date) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE, date);

        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;

    }
```

**返回数据给 CrimeFragment** 

1.设置目标fragment 

类似于activity间的关联，可将`CrimeFragment`设置成`DatePickerFragment`的目标fragment。 这样，在`CrimeFragment`和`DatePickerFragment`被销毁并重建后，操作系统会重新关联它们。 调用以下Fragment方法可建立这种关联: 

```java
public void setTargetFragment(Fragment fragment, int requestCode)
```

目标fragment和请求代码由`FragmentManager`负责跟踪管理，我们可调用fragment(设置 目标fragment的fragment)的`getTargetFragment()`方法和`getTargetRequestCode()`方法获 取它们 

在`CrimeFragment.java` 可进行如下的设置：

```java
        
        private static final int REQUEST_DATE = 0;
        
        //mDateButton.setEnabled(false);
        mDateButton.setOnClickListener(new View.OnClickListener() { //日期按钮事件
            @Override
            public void onClick(View v) {

                //展示弹窗
                FragmentManager manager = getFragmentManager();
                //DatePickerFragment fragment = new DatePickerFragment();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE); //设置目标fragment
                dialog.show(manager, DIALOG_DATE);

            }
        });
```

`DatePickerFragment`类调用`CrimeFragment.onActivityResult(int, int, Intent)`方法 

```java
@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //获取日趋数据
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_DATE) {
            //重新设置时间
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            updateDate();
        }
    }
```

>`Activity.onActivityResult(...)`方法是`ActivityManager`在子activity被销毁后调用的父activity方法。处理activity间的数据返回时，`ActivityManager`会自动调用`Activity.onActivity- Result(...)`方法。父activity接收到`Activity.onActivityResult(...)`方法调用命令后，其`FragmentManager`会调用对应fragment的`Fragment.onActivityResult(...)`方法。 

> 处理由同一activity托管的两个fragment间的数据返回时，可借用`Fragment.onActivityResult(...)`方法。因此，直接调用目标fragment的`Fragment.onActivityResult(...)`方法， 就能实现数据的回传 

`DatePickerFragment`是直接调用目标fragment的`onActivityResult`方法，如下：

```java
    //发送结果
    private void sendResult(int resultCode, Date date) {
        if (getTargetFragment() == null) {
            return;
        }
        //选择的date
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE, date);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
```































