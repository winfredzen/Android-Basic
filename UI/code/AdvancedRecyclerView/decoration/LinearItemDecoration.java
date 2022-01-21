package com.example.harvic.blogrecyclerviewsec.decoration;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 自定义ItemDecoration
 * create by wangzhen 2022/1/21
 */
public class LinearItemDecoration extends RecyclerView.ItemDecoration {

    private Paint mPaint;

    public LinearItemDecoration() {
        mPaint = new Paint();
        mPaint.setColor(Color.GREEN);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);

        int childCount = parent.getChildCount();
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();

        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            //动态获取outRect的left
            int left = layoutManager.getLeftDecorationWidth(child);
            int cx = left / 2;
            int cy = child.getTop() + child.getHeight() / 2;
            c.drawCircle(cx, cy, 20, mPaint);
        }

    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.left = 200;
        outRect.bottom = 1;
    }

}
