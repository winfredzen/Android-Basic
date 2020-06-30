# okhttp同步请求

参考：

+ [Calls](https://square.github.io/okhttp/calls/#calls_1)
+ [OkHttp Android Example Tutorial](https://www.journaldev.com/13629/okhttp-android-example-tutorial)

同步请求**Synchronous**，会阻塞线程，直至response响应可读

> **Synchronous:** your thread blocks until the response is readable.

同步的请求需要一个`AsyncTask`来包裹它

> **Synchronous** calls require an AsyncTask wrapper around it. That means it doesn’t support cancelling a request. Also, AsyncTasks generally leak the Activity’s context, which is not preferred.
>
> 这表示它不支持取消一个request。另外，AsyncTask会泄漏Activity的context，所以不建议这么做

注意网络请求需要权限

```xml
<uses-permission android:name="android.permission.INTERNET"/>
```

如下的例子，获取百度网页数据

```java
public class MainActivity extends AppCompatActivity {

    private TextView mTextView;

    OkHttpClient mOkHttpClient = new OkHttpClient();

    public String url = "https://www.baidu.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.textView);

        OkHttpHandler okHttpHandler = new OkHttpHandler();
        okHttpHandler.execute(url);

    }


    public class OkHttpHandler extends AsyncTask<String, Void, String> {

        OkHttpClient client = new OkHttpClient();

        @Override
        protected String doInBackground(String... strings) {

            Request request = new Request.Builder().url(strings[0]).build();

            try {
                Response response = client.newCall(request).execute();
                return response.body().string();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            mTextView.setText(s);
        }
    }

}
```

![001](https://github.com/winfredzen/Android-Basic/blob/master/网络/images/001.png)













