package com.example.customradiogroupdemo.radiobutton;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import androidx.constraintlayout.widget.ConstraintLayout;

public abstract class BaseCustomRadioButton extends ConstraintLayout {

    protected AttributeSet attrs;
    protected TypedArray a;
    protected int[] styleable;

    public BaseCustomRadioButton(Context context, int layoutResId, int[] styleable) {
        super(context);

        initLayoutResId(layoutResId);
        initStyleable(styleable);
        initView();
    }

    public BaseCustomRadioButton(Context context,
                                 AttributeSet attrs,
                                 int layoutResId,
                                 int[] styleable) {
        super(context, attrs);

        initLayoutResId(layoutResId);
        initStyleable(styleable);
        initAttrs(attrs);
        initView();
    }

    public BaseCustomRadioButton(Context context,
                                 AttributeSet attrs,
                                 int defStyleAttr,
                                 int layoutResId,
                                 int[] styleable) {

        super(context, attrs, defStyleAttr);

        initLayoutResId(layoutResId);
        initStyleable(styleable);
        initAttrs(attrs);
        initView();
    }

    private void initLayoutResId(int layoutResId) {
        LayoutInflater.from(getContext()).inflate(layoutResId, this, true);
    }

    private void initAttrs(AttributeSet attrs) {
        this.attrs = attrs;
    }

    private void initStyleable(int[] styleable) {
        this.styleable = styleable;
    }

    private void initView() {
        initTypedArray();
        bindViews();
        initAttributes();
        populateViews();
    }

    private void initTypedArray() {
        a = getContext()
                .getTheme().obtainStyledAttributes(attrs, styleable, 0, 0);
    }

    protected abstract void bindViews();

    protected abstract void initAttributes();

    protected abstract void populateViews();
}
