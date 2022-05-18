package com.example.harvic.blogrecyclerviewsec;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import com.example.harvic.blogrecyclerviewsec.RecyclerAdapter.NormalHolder;

import java.util.ArrayList;
import java.util.Collections;

public class MyItemTouchHelperCallBack extends ItemTouchHelper.Callback {
    private ArrayList<String> mDatas;
    private RecyclerView.Adapter<ViewHolder> mAdapter;

    public MyItemTouchHelperCallBack(ArrayList<String> datas, RecyclerView.Adapter<ViewHolder> adapter) {
        mDatas = datas;
        mAdapter = adapter;
    }


    @Override
    public int getMovementFlags(RecyclerView recyclerView, ViewHolder viewHolder) {
        // 如果你不想上下拖动，可以将 dragFlags = 0
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;

        // 如果你不想左右滑动，可以将 swipeFlags = 0
        int swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;

        //最终的动作标识（flags）必须要用makeMovementFlags()方法生成
        int flags = makeMovementFlags(dragFlags, swipeFlags);
        return flags;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, ViewHolder viewHolder, ViewHolder target) {
        int fromPosition = viewHolder.getAdapterPosition();
        int toPosition = target.getAdapterPosition();
        Collections.swap(mDatas, fromPosition, toPosition);
        mAdapter.notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onSwiped(ViewHolder viewHolder, int direction) {
        mDatas.remove(viewHolder.getAdapterPosition());
        mAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
//        Log.d("qijian","onSwiped direction:"+direction);
    }

    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);

        if (viewHolder == null || viewHolder.itemView == null) {
            return;
        }

        //不管是拖拽或是侧滑，背景色都要变化
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            int bgColor = viewHolder.itemView.getContext().getResources().getColor(android.R.color.holo_orange_light);
            viewHolder.itemView.setBackgroundColor(bgColor);
        } else if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            int bgColor = viewHolder.itemView.getContext().getResources().getColor(android.R.color.holo_blue_bright);
            viewHolder.itemView.setBackgroundColor(bgColor);
        }
        Log.d("qijian", "onSelectedChanged actionState:" + actionState);
    }

    @Override
    public void onChildDraw(Canvas c,
                            RecyclerView recyclerView,
                            RecyclerView.ViewHolder viewHolder,
                            float dX,
                            float dY,
                            int actionState,
                            boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        Log.d("qijian", "dx:" + dX + "  dy:" + dY);
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            //滑动时改变Item的透明度
            final float alpha = 1 - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
            viewHolder.itemView.setAlpha(alpha);
            viewHolder.itemView.setScaleX(alpha);
        }
    }

    /**
     * 当item的交互动画结束时触发
     *
     * @param recyclerView
     * @param viewHolder
     */
    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        viewHolder.itemView.setBackgroundColor(viewHolder.itemView.getContext().getResources().getColor(android.R.color.holo_green_dark));
        viewHolder.itemView.setAlpha(1);
        viewHolder.itemView.setScaleX(1);
    }
}
