package com.example.handlerdemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class TimerActivity2 extends AppCompatActivity {

    private static final String TAG = "TimerActivity2";
    private TextView title, timer, txt;
    private ImageView btn;
    private boolean flag = false;

    private Handler handler = new Handler();

    private int i;

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            int min = i / 60;
            int second = i % 60;

//            String time = (min < 10 ? "0" + min : min) + ":" + (second < 10 ? "0" + second : second);

            String time = String.format("%02d:%02d", min, second);

            timer.setText(time);

            i++;

            handler.postDelayed(runnable, 1000);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        title = findViewById(R.id.title);
        timer = findViewById(R.id.timer);
        txt = findViewById(R.id.txt);
        btn = findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (flag == false) {
                    flag = true;
                    title.setText("工作中...");
                    btn.setImageResource(R.mipmap.stop);

                    new Thread() {
                        @Override
                        public void run() {
                            super.run();

                            i = 1;

                            handler.postDelayed(runnable, 1000);

                        }
                    }.start();

                } else {

                    flag = false;
                    title.setText("计时器");
                    btn.setImageResource(R.mipmap.start);

                    //移除递归回调
                    handler.removeCallbacks(runnable);

                }


            }
        });


        /*
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new Thread(){
                    @Override
                    public void run() {
                        super.run();

                        Log.d(TAG, "Outter Thread is : " + Thread.currentThread().getName());

//                        handler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                btn.setImageResource(R.mipmap.stop);
//
//                                Log.d(TAG, "Inner Run Thread is : " + Thread.currentThread().getName());
//                            }
//                        });

//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                btn.setImageResource(R.mipmap.stop);
//                            }
//                        }, 3000);

//                        handler.postAtTime(new Runnable() {
//                            @Override
//                            public void run() {
//                                btn.setImageResource(R.mipmap.stop);
//                            }
//                        }, SystemClock.uptimeMillis() + 3000);

                    }
                }.start();



            }
        });

         */

    }
}