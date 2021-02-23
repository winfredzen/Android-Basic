# EventBus

不使用`EventBus`的方案

**1.使用监听器**

![001](https://github.com/winfredzen/Android-Basic/blob/master/OpenSource/images/001.png)

如下的例子：

`PublisherDialogFragment`相当于`Publisher`

```java
public class PublisherDialogFragment extends DialogFragment {

    private static final String TAG = "PublisherDialogFragment";

    private OnEventListener mListener;

    public interface OnEventListener {
        void onSuccess();
        void onFailure();
    }

    public void setEventListener(OnEventListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Publisher");
        final String[] items = {"Success", "Failure"}; //设置对话框要显示的一个list，一般用于显示几个命令时
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        //succ
                        if (mListener != null) {
                            mListener.onSuccess();
                        }
                        break;
                    case 1:
                        //fail
                        if (mListener != null) {
                            mListener.onFailure();
                        }
                        break;
                    default:
                        break;
                }
            }
        });
        return builder.create();
    }
}
```

在`SubScriber`中监听

```java
final PublisherDialogFragment fragment = new PublisherDialogFragment();
fragment.setEventListener(new PublisherDialogFragment.OnEventListener() {
    @Override
    public void onSuccess() {
        setImageSrc(R.drawable.ic_happy);
    }
    @Override
    public void onFailure() {
        setImageSrc(R.drawable.ic_sad);
    }
});
fragment.show(getSupportFragmentManager(), "publiser");
```



















