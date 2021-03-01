package com.example.handlerdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.ref.WeakReference;

/**
 * 内存泄漏
 */
public class MemoryLeakActivity extends AppCompatActivity {

    private Button button;
    private TextView textView;

//    private Handler handler = new Handler(Looper.getMainLooper()) {
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            super.handleMessage(msg);
//
//            textView.setText("收到消息");
//
//        }
//    };

    private MyHandler handler = new MyHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_leak);

        button = findViewById(R.id.button);
        textView  = findViewById(R.id.textView);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                handler.sendEmptyMessageDelayed(1, 5000);

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        handler.removeCallbacksAndMessages(null);
    }

    private static class MyHandler extends Handler {

        private WeakReference<MemoryLeakActivity> weakReference;

        public MyHandler(MemoryLeakActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            MemoryLeakActivity activity = weakReference.get();
            activity.textView.setText("xxxx");
        }
    }


}