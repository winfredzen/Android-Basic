package com.example.customradiogroupdemo.radiobutton.types;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.example.customradiogroupdemo.R;
import com.example.customradiogroupdemo.radiobutton.BaseCustomRadioButton;

public final class TwoFieldsCustomRadioButton extends BaseCustomRadioButton {

    private TextView titleTextView;
    private TextView descriptionTextView;

    private String title;
    private String description;

    public TwoFieldsCustomRadioButton(Context context) {
        super(context, R.layout.custom_button_two_fields, R.styleable.CustomRadioButtonTwoFields);
    }

    public TwoFieldsCustomRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs, R.layout.custom_button_two_fields,
                R.styleable.CustomRadioButtonTwoFields);
    }

    public TwoFieldsCustomRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr, R.layout.custom_button_two_fields,
                R.styleable.CustomRadioButtonTwoFields);
    }

    @Override
    protected void bindViews() {
        titleTextView = findViewById(R.id.radio_button_two_fields_title);
        descriptionTextView = findViewById(R.id.radio_button_two_fields_description);
    }

    @Override
    protected void initAttributes() {
        initTitle(R.styleable.CustomRadioButtonTwoFields_title_rb_two_fields);
        initDescription(R.styleable.CustomRadioButtonTwoFields_description_rb_two_fields);
    }

    private void initTitle(int index) {
        if (typedArrayHasValue(index)) {
            title = a.getString(index);
        }
    }

    private void initDescription(int index) {
        if (typedArrayHasValue(index)) {
            description = a.getString(index);
        }
    }

    private boolean typedArrayHasValue(int index) {
        return a.hasValue(index);
    }

    @Override
    protected void populateViews() {
        titleTextView.setText(title);
        descriptionTextView.setText(description);
    }
}
