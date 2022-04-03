package com.wz.databindingtest.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.wz.databindingtest.R;
import com.wz.databindingtest.databinding.LayoutMyCustomViewBinding;

/**
 * create by wangzhen 2022/4/1
 */
public class MyCustomView extends FrameLayout {
    private TextView titleTextView;
    private Switch mySwitch;
    private MyOnCheckChangeListener mOnCheckChangeListener;
    private boolean check;

    public MyCustomView(@NonNull Context context) {
        this(context, null);
    }

    public MyCustomView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyCustomView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public MyCustomView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        LayoutMyCustomViewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.layout_my_custom_view, this, true);
        titleTextView = binding.titleTextView;
        mySwitch = binding.mySwitch;


    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        Log.d("TAG", "setCheck check = " + check);
        this.check = check;
        mySwitch.setChecked(check);
    }

    public void setOnCheckChangeListener(MyOnCheckChangeListener onCheckChangeListener) {
        Log.d("TAG", "setOnCheckChangeListener");
        mOnCheckChangeListener = onCheckChangeListener;
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mOnCheckChangeListener != null) {
//                    mOnCheckChangeListener.onCheckedChanged();
                      mOnCheckChangeListener.onCheckedChanged(MyCustomView.this, isChecked);

                }
            }
        });
    }

    public interface MyOnCheckChangeListener {
        void onCheckedChanged(MyCustomView view, boolean isChecked);
//        void onCheckedChanged();
    }
}
