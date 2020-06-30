# okhttp同步请求

参考：

+ [Calls](https://square.github.io/okhttp/calls/#calls_1)
+ [OkHttp Android Example Tutorial](https://www.journaldev.com/13629/okhttp-android-example-tutorial)
+ [Recipes](https://square.github.io/okhttp/recipes/)

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



> The `string()` method on response body is convenient and efficient for small documents. But if the response body is large (greater than 1 MiB), avoid `string()` because it will load the entire document into memory. In that case, prefer to process the body as a stream.
>
>  `string()` 方法对小的doucument是方便并且高效的。但是，如果响应的body比较大（超过1MiB），就要避免使用 `string()` ，因为它将会加载整个document到内存。在这种情况下，推荐使用stream来处理



如下输出响应的header：

```java
            try {
                Response response = client.newCall(request).execute();

                //输出headers

                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }
                Headers responseHeaders = response.headers();
                for (int i = 0; i < responseHeaders.size(); i++) {
                    System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }

                return response.body().string();

            } catch (IOException e) {
                e.printStackTrace();
            }
```



输出为：

```xml
2020-06-30 15:19:48.894 10236-10325/com.example.okhttpdemo I/System.out: Cache-Control: private, no-cache, no-store, proxy-revalidate, no-transform
2020-06-30 15:19:48.894 10236-10325/com.example.okhttpdemo I/System.out: Connection: keep-alive
2020-06-30 15:19:48.894 10236-10325/com.example.okhttpdemo I/System.out: Content-Type: text/html
2020-06-30 15:19:48.894 10236-10325/com.example.okhttpdemo I/System.out: Date: Tue, 30 Jun 2020 07:19:49 GMT
2020-06-30 15:19:48.894 10236-10325/com.example.okhttpdemo I/System.out: Last-Modified: Mon, 23 Jan 2017 13:23:55 GMT
2020-06-30 15:19:48.894 10236-10325/com.example.okhttpdemo I/System.out: Pragma: no-cache
2020-06-30 15:19:48.894 10236-10325/com.example.okhttpdemo I/System.out: Server: bfe/1.0.8.18
2020-06-30 15:19:48.894 10236-10325/com.example.okhttpdemo I/System.out: Set-Cookie: BDORZ=27315; max-age=86400; domain=.baidu.com; path=/
2020-06-30 15:19:48.894 10236-10325/com.example.okhttpdemo I/System.out: Transfer-Encoding: chunked
```

