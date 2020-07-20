package com.example.imooc_wechat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.imooc_wechat.utils.L;

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

//    private List<TabFragment> mFragments = new ArrayList<>();

    private SparseArray<TabFragment> mFragments = new SparseArray<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        L.d("activity onCreate");
//
//        mFragments.add(TabFragment.newInstance(""));
//        mFragments.add(TabFragment.newInstance(""));
//        mFragments.add(TabFragment.newInstance(""));
//        mFragments.add(TabFragment.newInstance(""));
//        mFragments.get(0);

        initViews();

        mVpMain.setOffscreenPageLimit(mTitles.size());//Set the number of pages that should be retained to either side of the current page in the view hierarchy in an idle state.
        mVpMain.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {

                L.d("Fragement getItem i = " + position);

//                return mFragments.get(position);

                TabFragment fragment = TabFragment.newInstance(mTitles.get(position));

                if (position == 0) {
                    fragment.setOnTitleClickListener(new TabFragment.OnTitleClickListener() {
                        @Override
                        public void onClick(String title) {
                            changeWeChatTab(title);
                        }
                    });
                }

                return fragment;
            }

            @Override
            public int getCount() {
                return mTitles.size();
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                TabFragment fragment = (TabFragment) super.instantiateItem(container, position);
                mFragments.put(position, fragment);
                return fragment;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                mFragments.remove(position);
                super.destroyItem(container, position, object);
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
                TabFragment fragment = mFragments.get(0);
                if (fragment != null) {
                    fragment.changeTitle("微信 changed!");
                }
            }
        });

    }

    public void changeWeChatTab(String title) {
        mBtnWechat.setText(title);
    }



}
