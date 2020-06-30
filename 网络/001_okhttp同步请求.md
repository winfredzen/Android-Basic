# okhttp同步请求

参考：

+ [Calls](https://square.github.io/okhttp/calls/#calls_1)
+ [OkHttp Android Example Tutorial](https://www.journaldev.com/13629/okhttp-android-example-tutorial)

同步请求**Synchronous**，会阻塞线程，直至response响应可读

> **Synchronous:** your thread blocks until the response is readable.

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















