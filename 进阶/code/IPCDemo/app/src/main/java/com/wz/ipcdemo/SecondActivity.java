package com.wz.ipcdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.wz.ipcdemo.manager.UserManager;

public class SecondActivity extends AppCompatActivity {
    private static final String TAG = "SecondActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Log.d(TAG, "onCreate UserManager.sUserId = " + UserManager.sUserId);
    }

    public void onClick(View view) {
        startActivity(new Intent(SecondActivity.this, ThirdActivity.class));
    }
}