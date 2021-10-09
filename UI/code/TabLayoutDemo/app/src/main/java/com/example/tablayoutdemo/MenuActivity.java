package com.example.tablayoutdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void defaultUse(View view){
        startActivity(new Intent(this, MainActivity.class));
    }

    public void fieldvalue(View view){
        startActivity(new Intent(this, SetStyleActivity.class));
    }
}