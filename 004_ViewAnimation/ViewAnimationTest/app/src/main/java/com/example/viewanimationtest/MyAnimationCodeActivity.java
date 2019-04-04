package com.example.viewanimationtest;

import android.app.Activity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

/**
 * demonstrates defining animation in code
 * <p/>
 * created using Android Studio (Beta) 0.8.2
 * www.101apps.co.za
 */
public class MyAnimationCodeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation_code);

        //rotate animation
        RotateAnimation rotateAnimation
                = new RotateAnimation(0.0f, 1080.0f,    //3 rotation s of 360 degrees
                Animation.RELATIVE_TO_PARENT, 0.5f,     //x coordinate for pivot type - 50% from left edge of parent
                Animation.RELATIVE_TO_PARENT, 0.5f);    //y coordinate for pivot type - 50% from top edge of parent

        //scale animation
        ScaleAnimation scaleAnimation
                = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,        //from x to x, from y to y
                Animation.RELATIVE_TO_PARENT, 0.5f,                 //x coordinate for pivot point - 50% from left edge of parent
                Animation.RELATIVE_TO_PARENT, 0.5f);                //y coordinate for pivot point - 50% from top edge of parent

        //alpha animation
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);

        //animation set
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(rotateAnimation);
        animationSet.addAnimation(alphaAnimation);
        animationSet.setDuration(2000);

        //set the interpolator
        animationSet.setInterpolator(new OvershootInterpolator());

        ImageView imageViewBadge = (ImageView) findViewById(R.id.imageView);
        imageViewBadge.startAnimation(animationSet);
    }
}
