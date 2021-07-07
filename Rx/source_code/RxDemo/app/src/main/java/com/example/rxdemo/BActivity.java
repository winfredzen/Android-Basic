package com.example.rxdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class BActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b);

        Log.d("BActivity", "onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("BActivity", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("BActivity", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("BActivity", "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("BActivity", "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("BActivity", "onDestroy");
    }
}