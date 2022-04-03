package com.wz.databindingtest.adapter;

import android.text.Editable;
import android.text.TextWatcher;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;

import com.wz.databindingtest.widget.CurrencyEditText;

/**
 * create by wangzhen 2022/4/2
 */
public class CurrencyBinder {
    @BindingAdapter(value = "realValueAttrChanged")
    public static void setListener(CurrencyEditText editText, final InverseBindingListener listener) {
        if (listener != null) {
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    listener.onChange();
                }
            });
        }
    }

    @BindingAdapter("realValue")
    public static void setRealValue(CurrencyEditText view, double value) {
        if (!isSameValue()) {
            view.setText(String.valueOf(value * 10));
        }
    }

    @InverseBindingAdapter(attribute = "realValue")
    public static double getRealValue(CurrencyEditText editText) {
        return editText.getRawValue() / 100;
    }
}
