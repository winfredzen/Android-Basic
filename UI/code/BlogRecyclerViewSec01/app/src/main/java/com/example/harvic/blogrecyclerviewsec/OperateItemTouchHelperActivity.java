package com.example.harvic.blogrecyclerviewsec;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.example.harvic.blogrecyclerviewsec.OperateItemRecyclerAdapter.NormalHolder;

import java.util.ArrayList;

public class OperateItemTouchHelperActivity extends AppCompatActivity {
    private ArrayList<String> mDatas = new ArrayList<>();
    private ItemTouchHelper mItemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linear);

        generateDatas();
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.linear_recycler_view);

        //线性布局
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        //添加分割线
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        OperateItemRecyclerAdapter adapter = new OperateItemRecyclerAdapter(this, mDatas);
        mRecyclerView.setAdapter(adapter);

        adapter.setOnItemDragListener(new OnItemDragListener() {
            @Override
            public void onDrag(NormalHolder holder) {
                mItemTouchHelper.startDrag(holder);
//                mItemTouchHelper.startSwipe(holder);
            }
        });

        mItemTouchHelper = new ItemTouchHelper(new OperateItemTouchHelperCallBack(mDatas, adapter));
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    private void generateDatas() {
        for (int i = 0; i < 200; i++) {
            mDatas.add("第 " + i + " 个item");
        }
    }
}
