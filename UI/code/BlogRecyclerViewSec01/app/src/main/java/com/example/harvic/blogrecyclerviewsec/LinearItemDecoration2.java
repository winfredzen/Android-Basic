package com.example.harvic.blogrecyclerviewsec;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * create by wangzhen 2022/5/18
 */
public class LinearItemDecoration2 extends RecyclerView.ItemDecoration {
    private Paint mPaint;
    private Bitmap mBitmap;

    public LinearItemDecoration2(Context context) {
        mPaint = new Paint();
        mPaint.setColor(Color.GREEN);

        BitmapFactory.Options options = new BitmapFactory.Options();
        //options.inSampleSize = 2;
        mBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon, options);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            c.drawBitmap(mBitmap, 0, child.getTop(), mPaint);
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.top = 1;
        outRect.left = 100;
        outRect.bottom = 1;
    }
}
