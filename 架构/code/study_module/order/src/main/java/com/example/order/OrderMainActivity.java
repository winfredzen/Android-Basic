package com.example.order;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.annotation.ARouter;
import com.example.annotation.Parameter;
import com.example.common.NetManagerUtils;
import com.example.router_api.ParameterManager;

@ARouter(path = "/order/OrderMainActivity", group = "order")
public class OrderMainActivity extends AppCompatActivity {

    private static final String TAG = "OrderMainActivity";

    @Parameter
    String name;

    @Parameter
    int age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_main_activity);

        //加载参数
        ParameterManager.getInstance().loadParameter(this);

        //在order模块使用common模块中类
        NetManagerUtils.fun();

        //输出参数
        Log.d(TAG, "name = " + name + ", age = " + age);

    }

//    public void toOrderDebugActivity(View view) {
//        startActivity(new Intent(this, OrderDebugActivity.class));
//    }
}