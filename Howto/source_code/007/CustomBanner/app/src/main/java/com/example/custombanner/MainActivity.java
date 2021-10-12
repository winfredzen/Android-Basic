package com.example.custombanner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.custombanner.banner.CycleViewPager;
import com.example.custombanner.banner.Info;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    /**
     * 模拟请求后得到的数据
     */
    List<Info> mList = new ArrayList<>();

    /**
     * 轮播图
     */
    CycleViewPager mCycleViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initView();

    }

    /**
     * 初始化数据
     */
    private void initData() {
        mList.add(new Info("标题1", "https://picsum.photos/id/237/200/300"));
        mList.add(new Info("标题2", "https://picsum.photos/id/0/200/300"));
        mList.add(new Info("标题3", "https://picsum.photos/id/100/200/300"));
        mList.add(new Info("标题4", "https://picsum.photos/id/1000/200/300"));

    }

    /**
     * 初始化View
     */
    private void initView() {
        mCycleViewPager = (CycleViewPager) findViewById(R.id.cycle_view);
        //设置选中和未选中时的图片
        assert mCycleViewPager != null;
        mCycleViewPager.setIndicators(R.mipmap.ad_select, R.mipmap.ad_unselect);
        //设置轮播间隔时间，默认为4000
        mCycleViewPager.setDelay(2000);
        mCycleViewPager.setData(mList, mAdCycleViewListener);
    }

    /**
     * 轮播图点击监听
     */
    private CycleViewPager.ImageCycleViewListener mAdCycleViewListener = new CycleViewPager.ImageCycleViewListener() {

        @Override
        public void onImageClick(Info info, int position, View imageView) {

            if (mCycleViewPager.isCycle()) {
                position = position - 1;
            }
            Toast.makeText(MainActivity.this, info.getTitle() + "选择了--" + position, Toast.LENGTH_LONG).show();
        }
    };

}