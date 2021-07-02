package com.ximsfei.skindemo.tab;

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.Menu;

import com.ximsfei.skindemo.BaseActivity;
import com.ximsfei.skindemo.R;
import com.ximsfei.skindemo.tab.fragment.FirstFragment;
import com.ximsfei.skindemo.tab.fragment.LastFragment;
import com.ximsfei.skindemo.tab.fragment.SFragment;
import com.ximsfei.skindemo.tab.fragment.TFragment;
import com.ximsfei.skindemo.tab.fragment.TabFragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import skin.support.annotation.Skinable;
import skin.support.widget.SkinCompatSupportable;

/**
 * Created by ximsfei on 2017/1/9.
 * 基础控件
 */

@Skinable
public class MainActivity extends BaseActivity implements SkinCompatSupportable {
    private TabFragmentPagerAdapter mTabFragmentPagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();
        configFragments();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_options, menu);
        return true;
    }

    private void configFragments() {
        List<Fragment> list = new ArrayList<>();
        list.add(new FirstFragment());//系统组件
        list.add(new SFragment());//自定义view
        list.add(new TFragment());//LIST
        list.add(new LastFragment());//第三方库控件
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(new TabFragmentPagerAdapter(getSupportFragmentManager(), list));
        List<String> listTitle = new ArrayList<>();
        listTitle.add("系统组件");
        listTitle.add("自定义View");
        listTitle.add("List");
        listTitle.add("第三方库控件");
        mTabFragmentPagerAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager(), list, listTitle);
        viewPager.setAdapter(mTabFragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void applySkin() {

    }
}
