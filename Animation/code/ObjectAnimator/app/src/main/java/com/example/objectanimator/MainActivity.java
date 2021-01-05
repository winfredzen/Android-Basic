package com.example.objectanimator;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ImageView ball_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ball_img = (ImageView) findViewById(R.id.ball_img);

        findViewById(R.id.start_anim).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ObjectAnimator animator = ObjectAnimator.ofObject(ball_img, "fallingPos", new FallingBallEvaluator(),
                        new Point(0, 0), new Point(500, 500));
                animator.setDuration(2000);
                animator.start();

            }
        });
    }
}