package com.example.greendaodemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.greendaodemo.manager.GreenDaoManager;
import com.example.greendaodemo.model.GoodsModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private GoodsAdapter mGoodsAdapter;
    private RecyclerView mRv;
    private GreenDaoManager mGreenDaoManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        mGreenDaoManager = new GreenDaoManager(this);
        initView();
    }

    private void initView() {
        mRv = (RecyclerView) findViewById(R.id.rv);
        mGoodsAdapter = new GoodsAdapter(this);
        mRv.setLayoutManager(new LinearLayoutManager(this));
        mRv.setAdapter(mGoodsAdapter);
    }

    /**
     * 进货按钮的点击事件
     * @param v
     */
    public void onAddGoodsClick (View v) {
        mGreenDaoManager.insertGoods();
    }

    /**
     * 查询全部商品
     * @param v
     */
    public void onQueryAllClick (View v) {
        List<GoodsModel> dataSource = mGreenDaoManager.queryGoods();
        notifyAdapter(dataSource);
    }

    /**
     * 筛选-水果
     * @param v
     */
    public void onQueryFruitsClick (View v) {
        List<GoodsModel> dataSource = mGreenDaoManager.queryFruits();
        notifyAdapter(dataSource);
    }

    /**
     * 筛选-零食
     * @param v
     */
    public void onQuerySnacksClick (View v) {
        List<GoodsModel> dataSource = mGreenDaoManager.querySnacks();
        notifyAdapter(dataSource);
    }

    /**
     * 改变展示数据
     */
    private void notifyAdapter (List<GoodsModel> dataSource) {
        mGoodsAdapter.setDataSource(dataSource);
    }
}