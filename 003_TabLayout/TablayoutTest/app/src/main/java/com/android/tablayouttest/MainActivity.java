package com.android.tablayouttest;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TableLayout;

public class MainActivity extends AppCompatActivity {

    private TabAdapter mTabAdapter;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mTabLayout = (TabLayout)findViewById(R.id.tabLayout);

        mTabAdapter = new TabAdapter(getSupportFragmentManager(), this);
        mTabAdapter.addFragment(new Tab1Fragment(), "Tab 1");
        mTabAdapter.addFragment(new Tab2Fragment(), "Tab 2");
        mTabAdapter.addFragment(new Tab3Fragment(), "Tab 3");

        mViewPager.setAdapter(mTabAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        highLightCurrentTab(0);

        //监听
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {

                highLightCurrentTab(i);

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }


    private void highLightCurrentTab(int position) {

        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            assert tab != null;
            tab.setCustomView(null);
            tab.setCustomView(mTabAdapter.getTabView(i));
        }

        TabLayout.Tab tab = mTabLayout.getTabAt(position);
        assert tab != null;
        tab.setCustomView(null);
        tab.setCustomView(mTabAdapter.getSelectedTabView(position));

    }
}
