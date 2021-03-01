package com.example.handlerdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private TextView mTextView;

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

        }
    };

    Handler handler2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = findViewById(R.id.txt);

        new Thread() {
            @Override
            public void run() {
                super.run();
                Looper.prepare();
                handler2 = new Handler() {
                    @Override
                    public void handleMessage(@NonNull Message msg) {
                        super.handleMessage(msg);
                        Log.e(TAG, "whate is " + msg.what);
                    }
                };
                Looper.loop();

            }
        }.start();
    }

    /**
     * 按钮点击事件
     * @param view
     */
    public void myclick(View view) {
        switch (view.getId()) {
            case R.id.btn1: {
                new Thread() {
                    @Override
                    public void run() {
                        super.run();

                        //网络请求
                        String msg = get();
                        Log.d(TAG, msg);
                        //mTextView.setText(msg); 崩溃

                        //发空消息
                        handler.sendEmptyMessage(1);

                        //发送消息
//                        Message message = new Message();
//                        message.obj = msg;
//                        handler.sendMessage(message);

//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                mTextView.setText(msg);
//                            }
//                        });


                    }
                }.start();
            }
                break;

            case R.id.btn2: {
                new Thread() {
                    @Override
                    public void run() {
                        super.run();


                    }
                }.start();
            }
                break;

            case R.id.btn3: {

                handler2.sendEmptyMessage(1000);

                break;
            }

            case R.id.btn4: {

                Intent intent = new Intent(MainActivity.this, TimerActivity2.class);
                startActivity(intent);

                break;
            }
            case R.id.btn5: {

                Intent intent = new Intent(MainActivity.this, NetDemoActivity.class);
                startActivity(intent);

                break;
            }

            default:
                break;
        }
    }

    private String get() {
        try {
            URL url = new URL("http://www.imooc.com/api/teacher?type=3&cid=1");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.setConnectTimeout(6000);

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream in = conn.getInputStream();
                byte[] b = new byte[1024];
                int len = 0;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                while((len = in.read(b))>-1){
                    baos.write(b,0,len);
                }

                String msg = new String(baos.toByteArray());
                return msg;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


}