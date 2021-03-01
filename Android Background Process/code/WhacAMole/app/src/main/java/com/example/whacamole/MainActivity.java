package com.example.whacamole;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.lang.ref.WeakReference;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private ImageView imageView;
    private TextView textView;
    private int maxWidth;
    private int maxHeight;
    private boolean playing;
    private int count = 0;
    private int maxCount = 10;

    private MyHandler handler = new MyHandler(this);

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (imageView.getVisibility() == View.INVISIBLE) {
                imageView.setVisibility(View.VISIBLE);
            }

            int displayTime = getImageViewDisplayTime();
            Point point = getImageViewDisplayPoint();

            imageView.setX(point.x);
            imageView.setY(point.y);

            handler.postDelayed(runnable, displayTime);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initEvents();
    }

    private void initViews() {
        button = (Button) findViewById(R.id.action_btn);
        imageView = (ImageView) findViewById(R.id.img);
        textView = (TextView) findViewById(R.id.textView);
    }

    private void initEvents() {

        //获取FrameLayout的大小
        FrameLayout layout = (FrameLayout)findViewById(R.id.frameLayout);
        ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                maxWidth  = layout.getMeasuredWidth();
                maxHeight = layout.getMeasuredHeight();

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!playing) {
                    button.setText("游戏中......");
                    handler.postDelayed(runnable, 0);
                } else {
                    //移除runnable
                    handler.removeCallbacks(runnable);
                    button.setText("点击开始");
                }
                playing = !playing;

                if (count == 0) {
                    textView.setText("开始啦");
                }
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                if (count > 0) {
                    if (textView.getVisibility() == View.INVISIBLE) {
                        textView.setVisibility(View.VISIBLE);
                    }
                    String countStr = getResources( ).getString( R.string.countStr);
                    String string = String.format(countStr, count, maxCount);
                    textView.setText(string);

                    //打到地鼠则马上更新
                    handler.removeCallbacks(runnable);
                    handler.postDelayed(runnable, 0);
                }
                if (count >= 10) {
                    //游戏结束
                    playing = false;
                    count = 0;
                    handler.removeCallbacks(runnable);
                    button.setText("点击开始");
                    imageView.setVisibility(View.INVISIBLE);
                    Toast.makeText(MainActivity.this, "地鼠打完了", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    /// 获取图片显示的位置
    private Point getImageViewDisplayPoint() {
        ViewGroup.LayoutParams para = imageView.getLayoutParams();
        int w = para.width;
        int h = para.height;

        int maxX = maxWidth - w;
        int maxY = maxHeight - h;

        int x = (int) (Math.random() * maxX);
        int y = (int) (Math.random() * maxY);

        return new Point(x, y);
    }

    /// 获取图片随机显示时间
    private int getImageViewDisplayTime() {
        int min = 500;
        int max = 1000;
        Random random = new Random();
        int time = random.nextInt(max) % (max - min + 1) + min;
        return time;
    }

    private static class MyHandler extends Handler {

        private WeakReference<MainActivity> weakReference;

        public MyHandler(MainActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

        }
    }

}