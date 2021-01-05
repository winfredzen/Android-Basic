package com.example.objectanimator;

import android.animation.TypeEvaluator;
import android.graphics.Point;

public class FallingBallEvaluator implements TypeEvaluator<Point> {

    private Point point = new Point();

    @Override
    public Point evaluate(float fraction, Point startValue, Point endValue) {
        point.x = (int) (startValue.x + (endValue.x - startValue.x) * fraction);

        if (fraction * 2 <= 1) {
            point.y = (int) (startValue.y + (endValue.y - startValue.y) * fraction);
        } else {
            point.y = endValue.y;
        }
        return point;
    }

}
