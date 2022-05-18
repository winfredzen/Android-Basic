package com.example.harvic.blogrecyclerviewsec;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.harvic.blogrecyclerviewsec.qqdelete.QQDeleteActivity;
import com.example.harvic.blogrecyclerviewsec.snaphelper.SnapHelperActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button linearBtn = (Button)findViewById(R.id.linear_activity_btn);
        Button gridBtn = (Button)findViewById(R.id.grid_activity_btn);
        Button staggerBtn = (Button)findViewById(R.id.stagger_activity_btn);
        Button customBtn = (Button)findViewById(R.id.custom_activity_btn);


        linearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,LinearActivity.class);
                startActivity(intent);
            }
        });

        gridBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,GridActivity.class);
                startActivity(intent);
            }
        });

        staggerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,StaggeredActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.recycler_view_btn).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,CoverFlowActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.diff_item_activity_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,DiffItemActivity.class);
                startActivity(intent);
            }
        });

        customBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,CustomLayoutActivity.class);
                startActivity(intent);
            }
        });


        findViewById(R.id.recycled_activity_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,CustomRecycledLayoutActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.first_remould_recycled_activity_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,RecycledFirstRemouldActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.second_remould_recycled_activity_btn).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,RecycledSecondRemouldActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.linear_item_touch_helper).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,LinearItemTouchHelperActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.grid_item_touch_helper).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,GridItemTouchHelperActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btn_item_touch_helper).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,OperateItemTouchHelperActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btn_qq_delete).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,QQDeleteActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.snap_helper_btn).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SnapHelperActivity.class);
                startActivity(intent);
            }
        });
    }
}
