package com.wz.numberpickertest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.NumberPicker;

public class MainActivity extends AppCompatActivity {

    private NumberPicker mNumberPicker;

    private String[] pickerVals;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        mNumberPicker = findViewById(R.id.number_picker);
//
//        mNumberPicker.setMaxValue(4);
//        mNumberPicker.setMinValue(0);

//        pickerVals = new String[] {"dog", "cat", "lizard", "turtle", "axolotl"};
//
//        mNumberPicker.setDisplayedValues(pickerVals);

    }
}