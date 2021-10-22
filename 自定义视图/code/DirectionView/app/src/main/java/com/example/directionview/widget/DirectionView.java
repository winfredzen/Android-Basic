package com.example.directionview.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.directionview.R;

/**
 * create by wangzhen 2021/10/19
 */
public class DirectionView extends LinearLayout implements View.OnClickListener {
    private Context mContext;

    private float lastX = 0, lastY = 0;

    public DirectionView(Context context) {
        super(context);
        init(context);
    }

    public DirectionView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DirectionView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public DirectionView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        mContext = context;

        LayoutInflater.from(context).inflate(R.layout.direction_view_layout, this);

        //点击事件
        findViewById(R.id.direction_up).setOnClickListener(this);
        findViewById(R.id.direction_down).setOnClickListener(this);
        findViewById(R.id.direction_left).setOnClickListener(this);
        findViewById(R.id.direction_right).setOnClickListener(this);
        findViewById(R.id.direction_ok).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.direction_up: {
                Toast.makeText(mContext, "up clicked", Toast.LENGTH_SHORT).show();
            }
            break;
            case R.id.direction_down: {
                Toast.makeText(mContext, "down clicked", Toast.LENGTH_SHORT).show();
            }
            break;
            case R.id.direction_left: {
                Toast.makeText(mContext, "left clicked", Toast.LENGTH_SHORT).show();
            }
            break;
            case R.id.direction_right: {
                Toast.makeText(mContext, "right clicked", Toast.LENGTH_SHORT).show();
            }
            break;
            case R.id.direction_ok: {
                Toast.makeText(mContext, "ok clicked", Toast.LENGTH_SHORT).show();
            }
            break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = x;
                lastY = y;
                return true;
//                break;
            case MotionEvent.ACTION_MOVE:
                int offsetX = (int) (x - lastX);
                int offsetY = (int) (y - lastY);
                //调用layout方法来重新放置它的位置
                layout(getLeft() + offsetX, getTop() + offsetY, getRight() + offsetX, getBottom() + offsetY);
                break;
        }
        return super.onTouchEvent(event);
    }
}























