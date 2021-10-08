package com.example.customradiogroupdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

public class Demo2 extends AppCompatActivity {
    AppCompatRadioButton rbLeft, rbRight, rbCenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo2);

        rbLeft = findViewById(R.id.rbLeft);
        rbRight = findViewById(R.id.rbRight);
        rbCenter = findViewById(R.id.rbCenter);
    }

    public void onRadioButtonClicked(View view) {
        boolean isChecked = ((AppCompatRadioButton)view).isChecked();
        switch (view.getId()) {
            case R.id.rbLeft:
                if (isChecked) {
                    rbLeft.setTextColor(Color.WHITE);
                    rbRight.setTextColor(Color.RED);
                    rbCenter.setTextColor(Color.RED);
                }
                break;
            case R.id.rbCenter:
                if (isChecked) {
                    rbLeft.setTextColor(Color.RED);
                    rbRight.setTextColor(Color.RED);
                    rbCenter.setTextColor(Color.WHITE);
                }
                break;
            case R.id.rbRight:
                if (isChecked) {
                    rbLeft.setTextColor(Color.RED);
                    rbRight.setTextColor(Color.WHITE);
                    rbCenter.setTextColor(Color.RED);
                }
                break;
            default:
                break;
        }
    }
}