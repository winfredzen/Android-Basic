package com.example.imooc_wechat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<String> mTitles = new ArrayList<>(Arrays.asList("微信", "通讯录", "发现", "我"));

    private ViewPager mVpMain;

    private Button mBtnWechat;
    private Button mBtnFriend;
    private Button mBtnFind;
    private Button mBtnMine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        mVpMain.setOffscreenPageLimit(mTitles.size());//Set the number of pages that should be retained to either side of the current page in the view hierarchy in an idle state.
        mVpMain.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return TabFragment.newInstance(mTitles.get(position));
            }

            @Override
            public int getCount() {
                return mTitles.size();
            }
        });

    }

    private void initViews() {

        mVpMain = findViewById(R.id.vp_main);
        mBtnWechat = findViewById(R.id.btn_wechat);
        mBtnFriend = findViewById(R.id.btn_friend);
        mBtnFind= findViewById(R.id.btn_find);
        mBtnMine = findViewById(R.id.btn_mine);

        mBtnWechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }



}
