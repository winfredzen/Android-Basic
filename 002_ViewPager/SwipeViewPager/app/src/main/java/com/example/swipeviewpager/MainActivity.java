package com.example.swipeviewpager;

import android.animation.ArgbEvaluator;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ViewPager mViewPager;
    Adapter mAdapter;
    List<Model> models;
    Integer[] colors = null;
    ArgbEvaluator mArgbEvaluator = new ArgbEvaluator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        models = new ArrayList<>();
        models.add(new Model(R.drawable.brochure, "Brochure", "Brochure Brochure Brochure Brochure Brochure Brochure Brochure Brochure Brochure Brochure Brochure Brochure "));
        models.add(new Model(R.drawable.sticker, "Sticker", "Sticker Sticker Sticker Sticker Sticker Sticker Sticker Sticker "));
        models.add(new Model(R.drawable.poster, "Poster", "Poster Poster Poster Poster Poster Poster Poster Poster "));
        models.add(new Model(R.drawable.namecard, "Namecard", "Namecard Namecard Namecard Namecard Namecard Namecard Namecard Namecard Namecard Namecard Namecard Namecard Namecard Namecard Namecard Namecard Namecard Namecard Namecard Namecard Namecard Namecard Namecard Namecard Namecard Namecard Namecard Namecard "));

        mAdapter = new Adapter(models, this);
        mViewPager = findViewById(R.id.viewPager);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setPadding(130,0 , 130, 0);


        Integer[] colors_temp = {
                getResources().getColor(R.color.color1),
                getResources().getColor(R.color.color2),
                getResources().getColor(R.color.color3),
                getResources().getColor(R.color.color4)

        };

        colors = colors_temp;

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float postionOffset, int i1) {

                Log.d("MainActivity", "position = " + position + " postionOffset = " + postionOffset + " i1 = " + i1);

                if (position < (mAdapter.getCount() - 1) && position < (colors.length - 1)) {

                    mViewPager.setBackgroundColor(
                            (Integer) mArgbEvaluator.evaluate(
                                    postionOffset,
                                    colors[position],
                                    colors[position+1]
                            )
                    );

                } else {

                    mViewPager.setBackgroundColor(colors[colors.length - 1]);

                }

            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }

}
