package com.example.handlerdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class TimerActivity extends AppCompatActivity {

    private TextView title, timer, txt;
    private ImageView btn;
    private boolean flag = false;

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            int min = msg.arg1 / 60;
            int second = msg.arg1 % 60;

            String time = (min < 10 ? "0" + min : min) + ":" + (second < 10 ? "0" + second : second);

            timer.setText(time);
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

                            int i = 0;
                            while (flag) {
                                try {
                                    sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                Message message = new Message();
                                message.arg1 = i;
                                handler.sendMessage(message);

                                i++;


                            }

                        }
                    }.start();
                } else {
                    flag = false;
                    title.setText("计时器");
                    btn.setImageResource(R.mipmap.start);
                }

            }
        });

    }
}