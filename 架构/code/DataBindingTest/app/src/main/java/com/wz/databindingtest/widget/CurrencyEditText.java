package com.wz.databindingtest.widget;

import android.content.Context;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.wz.databindingtest.R;
import com.wz.databindingtest.databinding.LayoutCurrencyEdittextBinding;
import com.wz.databindingtest.databinding.LayoutMyCustomViewBinding;

/**
 * create by wangzhen 2022/4/2
 */
public class CurrencyEditText extends FrameLayout {
    LayoutCurrencyEdittextBinding binding;
    private EditText editText;
    private double amount;

    public CurrencyEditText(@NonNull Context context) {
        this(context, null);
    }

    public CurrencyEditText(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CurrencyEditText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public CurrencyEditText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.layout_my_custom_view, this, true);
        editText = binding.editTextView;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
    public double getAmount() {
        return this.amount;
    }

    public void addTextChangedListener(TextWatcher textWatcher) {
        binding.editTextView.addTextChangedListener(textWatcher);
    }
}
