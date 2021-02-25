package com.example.imooc_okhttp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.imooc_okhttp.net.INetCallBack;
import com.example.imooc_okhttp.utils.OkHttpUtils;

public class MainActivity extends AppCompatActivity {

    private Button mBtnGet;
    private TextView mTvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initEvents();

    }

    private void initViews() {
        mBtnGet = (Button) findViewById(R.id.btn_get);
        mTvContent = (TextView) findViewById(R.id.tv_content);
    }

    private void initEvents() {
        mBtnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发情网络请求

//                new Thread() {
//                    @Override
//                    public void run() {
//                        String content = OkHttpUtils.getInstance().doGet("http://www.imooc.com/api/okhttp/getmethod");
//
//                        mUiHandler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                mTvContent.setText(content);
//                            }
//                        });
//                    }
//                }.start();


                OkHttpUtils.getInstance().doGet("http://www.imooc.com/api/okhttp/getmethod", new INetCallBack() {
                    @Override
                    public void onSuccess(String response) {
                        mTvContent.setText(response);
                    }

                    @Override
                    public void onFailed(Throwable ex) {
                        Toast.makeText(MainActivity.this, "网络发生错误", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });
    }
}