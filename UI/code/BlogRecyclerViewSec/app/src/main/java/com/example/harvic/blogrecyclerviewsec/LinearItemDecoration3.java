package com.example.harvic.blogrecyclerviewsec;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * create by wangzhen 2022/5/18
 */
public class LinearItemDecoration3 extends RecyclerView.ItemDecoration {
    private Paint mPaint;
    private Bitmap mBitmap;

    public LinearItemDecoration3(Context context) {
        mPaint = new Paint();
        mPaint.setColor(Color.GREEN);

        BitmapFactory.Options options = new BitmapFactory.Options();
        mBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.xunzhang, options);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);

    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            int index = parent.getChildAdapterPosition(child);
            int left = layoutManager.getLeftDecorationWidth(child);
            if (index % 5 == 0) {
                //在交界处绘制
                c.drawBitmap(mBitmap, left - mBitmap.getWidth() / 2, child.getTop(), mPaint);
            }
        }

        //绘制蒙层
        View temp = parent.getChildAt(0);
        LinearGradient linearGradient = new LinearGradient(parent.getWidth() / 2, 0,
                parent.getWidth() / 2, temp.getHeight() * 3,
                0xff0000ff, 0x000000ff, Shader.TileMode.CLAMP);
        mPaint.setShader(linearGradient);
        c.drawRect(0, 0, parent.getWidth(), temp.getHeight() * 3, mPaint);

    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.top = 1;
        outRect.left = 200;
        outRect.bottom = 1;
    }
}
