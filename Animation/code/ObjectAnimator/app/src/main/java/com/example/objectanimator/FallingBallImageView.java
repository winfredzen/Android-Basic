package com.example.objectanimator;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.Nullable;

public class FallingBallImageView extends ImageView  {
    public FallingBallImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setFallingPos(Point pos) {
        layout(pos.x, pos.y, pos.x + getWidth(), pos.y + getHeight());
    }
}
