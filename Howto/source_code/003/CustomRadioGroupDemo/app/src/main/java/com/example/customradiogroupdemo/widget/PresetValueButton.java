package com.example.customradiogroupdemo.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.customradiogroupdemo.R;

import java.util.ArrayList;
import java.util.EventListener;

/**
 * create by wangzhen 2021/10/8
 */
public class PresetValueButton extends RelativeLayout implements RadioCheckable {
    // Views
    private TextView mValueTextView, mUnitTextView;

    // Constants
    public static final int DEFAULT_TEXT_COLOR = Color.TRANSPARENT;

    // Attribute Variables
    private String mValue; //值
    private String mUnit; //单位
    private int mValueTextColor;
    private int mUnitTextColor;
    private int mPressedTextColor;

    // Variables
    private Drawable mInitialBackgroundDrawable;
    private OnClickListener mOnClickListener;
    private OnTouchListener mOnTouchListener;
    private boolean mChecked;
    private ArrayList<OnCheckedChangeListener> mOnCheckedChangeListeners = new ArrayList<>();

    public PresetValueButton(Context context) {
        super(context);
        setupView();
    }

    public PresetValueButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        parseAttributes(attrs);
        setupView();
    }

    public PresetValueButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        parseAttributes(attrs);
        setupView();
    }

    public PresetValueButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        parseAttributes(attrs);
        setupView();
    }

    private void parseAttributes(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.PresetValueButton, 0, 0);
        Resources resources = getContext().getResources();
        try {
            mValue = typedArray.getString(R.styleable.PresetValueButton_presetButtonValueText);
            mUnit = typedArray.getString(R.styleable.PresetValueButton_presetButtonUnitText);
            mValueTextColor = typedArray.getColor(R.styleable.PresetValueButton_presetButtonValueTextColor, resources.getColor(R.color.black));
            mPressedTextColor = typedArray.getColor(R.styleable.PresetValueButton_presetButtonPressedTextColor, Color.WHITE);
            mUnitTextColor = typedArray.getColor(R.styleable.PresetValueButton_presetButtonUnitTextColor, resources.getColor(R.color.gray));
        } finally {
            typedArray.recycle();
        }
    }

    private void setupView() {
        inflateView();
        bindView();
        setCustomTouchListener();
    }

    protected void inflateView() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        layoutInflater.inflate(R.layout.custom_preset_button, this, true); //注意参数
        mValueTextView = (TextView) findViewById(R.id.text_view_value);
        mUnitTextView = (TextView) findViewById(R.id.text_view_unit);
        mInitialBackgroundDrawable = getBackground(); //最初始的背景，以后要复原，先保存起来
    }

    protected void bindView() {
        //设置颜色与文字
        if (mValueTextColor != DEFAULT_TEXT_COLOR) {
            mValueTextView.setTextColor(mValueTextColor);
        }
        if (mUnitTextColor != DEFAULT_TEXT_COLOR) {
            mUnitTextView.setTextColor(mUnitTextColor);
        }
        mValueTextView.setText(mValue);
        mUnitTextView.setText(mUnit);
    }

    private void setCustomTouchListener() {
        super.setOnTouchListener(new TouchListener());
    }

    public OnClickListener getOnClickListener() {
        return mOnClickListener;
    }

    @Override
    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public OnTouchListener getOnTouchListener() {
        return mOnTouchListener;
    }

    @Override
    public void setOnTouchListener(OnTouchListener onTouchListener) {
        mOnTouchListener = onTouchListener;
    }

    private void onTouchDown(MotionEvent motionEvent) {
        setChecked(true);
    }

    private void onTouchUp(MotionEvent motionEvent) {
        // Handle user defined click listeners 触发点击事件
        if (mOnClickListener != null) {
            mOnClickListener.onClick(this);
        }
    }

    //================================================================================
    // Public methods
    //================================================================================

    public void setCheckedState() {
        setBackgroundResource(R.drawable.background_shape_preset_button_pressed);
        mValueTextView.setTextColor(mPressedTextColor);
        mUnitTextView.setTextColor(mPressedTextColor);
    }

    public void setNormalState() {
        setBackgroundDrawable(mInitialBackgroundDrawable);
        mValueTextView.setTextColor(mValueTextColor);
        mUnitTextView.setTextColor(mUnitTextColor);
    }

    public String getValue() {
        return mValue;
    }

    public void setValue(String value) {
        mValue = value;
    }

    public String getUnit() {
        return mUnit;
    }

    public void setUnit(String unit) {
        mUnit = unit;
    }

    //================================================================================
    // RadioCheckable
    //================================================================================

    @Override
    public void addOnCheckChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        mOnCheckedChangeListeners.add(onCheckedChangeListener);
    }

    @Override
    public void removeOnCheckChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        mOnCheckedChangeListeners.remove(onCheckedChangeListener);
    }

    @Override
    public void setChecked(boolean checked) {
        if (mChecked != checked) {
            mChecked = checked;
            if (!mOnCheckedChangeListeners.isEmpty()) {
                for (int i = 0; i < mOnCheckedChangeListeners.size(); i++) {
                    mOnCheckedChangeListeners.get(i).onCheckedChanged(this, mChecked);
                }
            }
            if (mChecked) {
                setCheckedState();
            } else {
                setNormalState();
            }
        }
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }

    //================================================================================
    // Inner classes
    //================================================================================

    private final class TouchListener implements OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    onTouchDown(event);
                    break;
                case MotionEvent.ACTION_UP:
                    onTouchUp(event);
                    break;
            }
            if (mOnTouchListener != null) {
                mOnTouchListener.onTouch(v, event);
            }
            return true;
        }
    }

}
























