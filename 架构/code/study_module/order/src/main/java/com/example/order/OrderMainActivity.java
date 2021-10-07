package com.example.order;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.annotation.ARouter;
import com.example.common.NetManagerUtils;

@ARouter(path = "/order/OrderMainActivity", group = "order")
public class OrderMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_main_activity);

        //在order模块使用common模块中类
        NetManagerUtils.fun();
    }

//    public void toOrderDebugActivity(View view) {
//        startActivity(new Intent(this, OrderDebugActivity.class));
//    }
}