package com.example.customradiogroupdemo.widget;

import android.view.View;
import android.widget.Checkable;

/**
 * create by wangzhen 2021/10/8
 */
public interface RadioCheckable extends Checkable {
    void addOnCheckChangeListener(OnCheckedChangeListener onCheckedChangeListener);
    void removeOnCheckChangeListener(OnCheckedChangeListener onCheckedChangeListener);

    public static interface OnCheckedChangeListener {
        void onCheckedChanged(View buttonView, boolean isChecked);
    }
}
