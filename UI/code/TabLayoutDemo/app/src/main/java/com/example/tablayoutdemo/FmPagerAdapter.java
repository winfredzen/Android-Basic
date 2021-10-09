package com.example.tablayoutdemo;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

/**
 * create by wangzhen 2021/10/9
 */
public class FmPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragmentList;

    public FmPagerAdapter(List<Fragment> fragmentList, FragmentManager fm) {
        super(fm);
        mFragmentList = fragmentList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList != null && !mFragmentList.isEmpty() ? mFragmentList.size() : 0;
    }
}
