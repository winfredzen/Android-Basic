package com.example.order;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.common.NetManagerUtils;

public class OrderMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_main_activity);

        //在order模块使用common模块中类
        NetManagerUtils.fun();
    }
}