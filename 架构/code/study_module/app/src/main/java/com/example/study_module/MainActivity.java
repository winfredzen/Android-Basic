package com.example.study_module;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.annotation.ARouter;
import com.example.order.OrderMainActivity;
import com.example.personal.PersonalMainActivity;
import com.example.router_api.RouterManager;

@ARouter(path = "/app/MainActivity", group = "app")
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(BuildConfig.isRelease){
            Log.i(TAG, "onCreate: 集成化环境");
        }else{
            Log.i(TAG, "onCreate: 组件化环境");
        }
    }

    public void toOrderModule(View view) {
        //传统方式，组件间的相互调用
        //startActivity(new Intent(MainActivity.this, OrderMainActivity.class));

        //路由方式
        RouterManager.getInstance()
                .build("/order/OrderMainActivity")
                .withString("name", "张三")
                .withInt("age", 20)
                .navigation(this);
    }

    public void toPersonalModule(View view) {
        startActivity(new Intent(MainActivity.this, PersonalMainActivity.class));
    }
}