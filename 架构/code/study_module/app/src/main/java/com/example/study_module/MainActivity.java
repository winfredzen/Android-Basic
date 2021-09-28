package com.example.study_module;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.annotation.ARouter;
import com.example.order.OrderMainActivity;
import com.example.personal.PersonalMainActivity;

@ARouter(path = "/app/MainActivity")
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void toOrderModule(View view) {
        //传统方式，组件间的相互调用
        startActivity(new Intent(MainActivity.this, OrderMainActivity.class));
    }

    public void toPersonalModule(View view) {
        startActivity(new Intent(MainActivity.this, PersonalMainActivity.class));
    }
}