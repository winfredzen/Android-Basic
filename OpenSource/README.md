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



**2.使用本地广播**

![002](https://github.com/winfredzen/Android-Basic/blob/master/OpenSource/images/002.png)

注册广播：

```java
    public static final String HANDLE_EVENT_ACTION = "handle_event_action";

    public static final String STATUS_KEY = "status";

    final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //获取action
            final String action = intent.getAction();
            if (HANDLE_EVENT_ACTION.equals(action)) {
                final boolean status = intent.getBooleanExtra(STATUS_KEY, false);
                if (status) {
                    setImageSrc(R.drawable.ic_happy);
                } else {
                    setImageSrc(R.drawable.ic_sad);
                }
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        //注册广播

        final IntentFilter filter = new IntentFilter(HANDLE_EVENT_ACTION);

        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, filter);

    }

    @Override
    protected void onStop() {
        super.onStop();

        //取消注册广播
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
    }
```

发送广播：

```java
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: {
                        //succ
                        //发送广播
                        final Intent intent = new Intent();
                        intent.setAction(MainActivity.HANDLE_EVENT_ACTION);
                        intent.putExtra(MainActivity.STATUS_KEY, true);
                        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                    }
                        break;
                    case 1: {
                        //fail
                        final Intent intent = new Intent();
                        intent.setAction(MainActivity.HANDLE_EVENT_ACTION);
                        intent.putExtra(MainActivity.STATUS_KEY, false);
                        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                    }
                        break;
                    default:
                        break;
                }
```













































