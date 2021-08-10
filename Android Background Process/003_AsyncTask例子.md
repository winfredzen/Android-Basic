# AsyncTask例子

**1.一个简单的网络请求例子**

如下的`MainActivity`

```java
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity-TAG";
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = findViewById(R.id.txt);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //实例化AsyncTask
                MyTask myTask = new MyTask();
                //启动
                myTask.execute("https://www.imooc.com/api/teacher?type=3&cid=1");
            }
        });
    }

    class MyTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Log.d(TAG, "onPreExecute");
        }

        //请求服务器
        //https://www.imooc.com/api/teacher?type=3&cid=1
        @Override
        protected String doInBackground(String... strings) {
            Log.d(TAG, "doInBackground");

            String urlParam = strings[0];

            try {
                URL url = new URL(urlParam);
                //获取HttpURLConnection实例
                HttpURLConnection connection = (HttpURLConnection) (url.openConnection());
                //设置和请求相关的属性
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(6000);
                //获取响应码
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    //判断响应码并获取响应数据
                    //获取输入流
                    InputStream is = connection.getInputStream();
                    //循环中读取输入流
                    byte[] b = new byte[1024];
                    int len = 0;
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    while ((len = is.read(b)) > -1) {
                        //将字节数组里面的内容存入缓存流
                        baos.write(b, 0, len);
                    }

                    String msg = new String(baos.toByteArray());
                    Log.d(TAG, "msg  = " + msg);
                    // 返回响应
                    return msg;

                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        //显示
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d(TAG, "onPostExecute");

            if (s != null) {
                mTextView.setText(s);
            }
        }
    }

}
```











































