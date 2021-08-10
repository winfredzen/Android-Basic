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

![004](https://github.com/winfredzen/Android-Basic/blob/master/Android%20Background%20Process/images/004.png)



**2.模拟下载**

布局文件`activity_main.xml`

```java
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="#ff00ff"
    tools:context=".MainActivity">

    <TextView
        android:id="@+id/text"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="还没开始哦"
        android:textSize="28sp"
        android:layout_above="@+id/progressBar"
        android:layout_centerHorizontal="true"
        />

    <ProgressBar
        android:id="@+id/progressBar"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        style="?android:attr/progressBarStyleHorizontal"
        android:layout_marginTop="30dp"
        android:layout_centerInParent="true"
        />

    <ImageView
        android:id="@id/start"
        android:layout_width="80dp"
        android:layout_height="80dp"
        android:src="@mipmap/start"
        android:layout_below="@id/progressBar"
        android:layout_marginTop="30dp"
        android:layout_marginLeft="100dp"
        />

    <ImageView
        android:id="@id/stop"
        android:layout_width="80dp"
        android:layout_height="80dp"
        android:src="@mipmap/stop"
        android:layout_below="@id/progressBar"
        android:layout_alignParentRight="true"
        android:layout_marginTop="30dp"
        android:layout_marginRight="100dp"
        />


</RelativeLayout>
```

`MainActivity`

```java
public class MainActivity extends AppCompatActivity {
    private ImageView mStart, mCancel;
    private ProgressBar mProgressBar;
    private TextView mTextView;
    private ProgressTask mProgressTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressBar = findViewById(R.id.progressBar);
        mTextView = findViewById(R.id.text);
        mStart = findViewById(R.id.start);
        mCancel = findViewById(R.id.stop);

        mProgressTask = new ProgressTask();
        mStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //启动任务
                if (mProgressTask.isCancelled()) { //如果取消了，重新创建
                    mProgressTask = new ProgressTask();
                }
                mProgressTask.execute();
            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消任务
                mProgressTask.cancel(true);
            }
        });

    }

    class ProgressTask extends AsyncTask<Void, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mTextView.setText("加载中");
        }

        //处理耗时操作
        @Override
        protected String doInBackground(Void... voids) {
            //模拟耗时操作
            try {
                for (int i = 1; i <= 100; i++) {
                    //更新进度
                    publishProgress(i);

                    Thread.sleep(50);
                }
                return "加载完毕";
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            //更新进度
            mProgressBar.setProgress(values[0]);
            mTextView.setText("加载..." + values[0] + "%");
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s != null) {
                mTextView.setText(s);
            }

            //任务正常结束
            mProgressTask = new ProgressTask();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

            mTextView.setText("已取消");
            mProgressBar.setProgress(0);
        }
    }

}
```

![005](https://github.com/winfredzen/Android-Basic/blob/master/Android%20Background%20Process/images/005.png)



**使用注意问题**

1.Task实例必须在UI线程中创建

2.主线程中执行`execute()`

3.注意防止内存泄漏，可以在Activity or Fragment中的销毁方法中调用cancel方法































