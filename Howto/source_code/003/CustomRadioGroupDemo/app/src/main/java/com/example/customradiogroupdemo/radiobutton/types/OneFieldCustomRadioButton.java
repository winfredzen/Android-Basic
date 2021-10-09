package com.example.customradiogroupdemo.radiobutton.types;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.example.customradiogroupdemo.R;
import com.example.customradiogroupdemo.radiobutton.BaseCustomRadioButton;


public final class OneFieldCustomRadioButton extends BaseCustomRadioButton {

    private TextView titleTextView;

    private String title;

    public OneFieldCustomRadioButton(Context context) {
        super(context, R.layout.custom_button_one_field, R.styleable.CustomRadioButtonOneField);
    }

    public OneFieldCustomRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs, R.layout.custom_button_one_field,
                R.styleable.CustomRadioButtonOneField);
    }

    public OneFieldCustomRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr, R.layout.custom_button_one_field,
                R.styleable.CustomRadioButtonOneField);
    }

    @Override
    protected void bindViews() {
        titleTextView = findViewById(R.id.radio_button_one_field_title);
    }

    @Override
    protected void initAttributes() {
        initTitle(R.styleable.CustomRadioButtonOneField_title_rb_one_field);
    }

    private void initTitle(int index) {
        if (typedArrayHasValue(index)) {
            title = a.getString(index);
        }
    }

    private boolean typedArrayHasValue(int index) {
        return a.hasValue(index);
    }

    @Override
    protected void populateViews() {
        titleTextView.setText(title);
    }
}

