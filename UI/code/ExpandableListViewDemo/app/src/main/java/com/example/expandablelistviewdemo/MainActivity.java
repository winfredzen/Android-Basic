package com.example.expandablelistviewdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;

import com.example.expandablelistviewdemo.adatper.ChapterAdapter;
import com.example.expandablelistviewdemo.bean.Chapter;
import com.example.expandablelistviewdemo.bean.ChapterLab;
import com.example.expandablelistviewdemo.biz.ChapterBiz;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity-TAG";
    private ExpandableListView mExpandableListView;
    private BaseExpandableListAdapter mAdapter;
    private List<Chapter> mDatas = new ArrayList<>();
    private ChapterBiz mChapterBiz = new ChapterBiz();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initEvents();
        loadDatas(true);
    }

    private void initView() {
        mExpandableListView = findViewById(R.id.expandable_list_view);
//        mDatas.clear();;
//        mDatas.addAll(ChapterLab.generateDatas());
        mAdapter = new ChapterAdapter(this, mDatas);
        mExpandableListView.setAdapter(mAdapter);
    }

    private void loadDatas(boolean useCache) {
        mChapterBiz.loadDatas(this, new ChapterBiz.Callback() {
            @Override
            public void onSuccess(List<Chapter> chapterList) {
                mDatas.clear();;
                mDatas.addAll(chapterList);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(Exception exception) {

            }
        }, useCache);
    }

    private void initEvents() {
        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Log.d(TAG, "onChildClick groupPosition = " + groupPosition + " , childPosition = " + childPosition);
                return false;
            }
        });

        mExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                Log.d(TAG, "onGroupClick groupPosition = " + groupPosition);
                return false;
            }
        });

        mExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                Log.d(TAG, "onGroupExpand groupPosition = " + groupPosition);
            }
        });

        mExpandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                Log.d(TAG, "onGroupCollapse groupPosition = " + groupPosition);
            }
        });
    }
}