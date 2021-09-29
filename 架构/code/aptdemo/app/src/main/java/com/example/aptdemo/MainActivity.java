package com.example.aptdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.annotation.DIActivity;
import com.example.annotation.DIView;

@DIActivity()
public class MainActivity extends AppCompatActivity {

    @DIView(value = R.id.text_view)
    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DIMainActivity.bindView(this);
        mTextView.setText("Hello, JavaPoet!");
    }
}