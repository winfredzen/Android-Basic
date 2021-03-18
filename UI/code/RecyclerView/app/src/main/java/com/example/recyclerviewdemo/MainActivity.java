package com.example.recyclerviewdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private MyRecyclerViewAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recycler_view);

        //线性布局
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        //设置反向
//        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        //设置反向展示
//        linearLayoutManager.setReverseLayout(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new MyRecyclerViewAdapter(this, mRecyclerView);
        adapter.setOnItemClickListener(new MyRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int postion) {

                Toast.makeText(MainActivity.this, "第" + postion + "条数据被点击", Toast.LENGTH_LONG).show();

            }
        });
        mRecyclerView.setAdapter(adapter);
    }

    // 添加数据
    public void onAddDataClick(View view) {
        List<String> data = new ArrayList<>();

        for(int i = 0; i < 20; i++) {
            String s = "第" + i + "条数据";
            data.add(s);
        }
        adapter.setDataSource(data);
    }

    // 切换布局
    public void onChangeLayout(View view) {
        //从线性布局切换为网格布局
        if (mRecyclerView.getLayoutManager().getClass() == LinearLayoutManager.class) {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
            mRecyclerView.setLayoutManager(gridLayoutManager);
        } else if (mRecyclerView.getLayoutManager().getClass() == GridLayoutManager.class) {
            // 瀑布流布局
            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        } else {
            // 线性布局
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(linearLayoutManager);
        }
    }

    // 添加数据
    public void onAddDataItemClick(View view) {
        adapter.addData(1);
    }

    //移除数据
    public void onRemoveDataItemClick(View view) {
        adapter.removeData(1);
    }
}



























