package com.example.customradiogroupdemo.radiobutton.types;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.customradiogroupdemo.R;
import com.example.customradiogroupdemo.radiobutton.BaseCustomRadioButton;

;

public class ThreeFieldsCustomRadioButton extends BaseCustomRadioButton {

    private ImageView iconImageView;
    private TextView titleTextView;
    private TextView descriptionTextView;

    private int image;
    private String title;
    private String description;

    public ThreeFieldsCustomRadioButton(Context context) {
        super(context, R.layout.custom_button_three_fields,
                R.styleable.CustomRadioButtonThreeFields);
    }

    public ThreeFieldsCustomRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs, R.layout.custom_button_three_fields,
                R.styleable.CustomRadioButtonThreeFields);
    }

    public ThreeFieldsCustomRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr, R.layout.custom_button_three_fields,
                R.styleable.CustomRadioButtonThreeFields);
    }

    @Override
    protected void bindViews() {
        iconImageView = findViewById(R.id.radio_button_three_fields_image);
        titleTextView = findViewById(R.id.radio_button_three_fields_title);
        descriptionTextView = findViewById(R.id.radio_button_three_fields_description);
    }

    @Override
    protected void initAttributes() {
        initImage(R.styleable.CustomRadioButtonThreeFields_image_rb_three_fields);
        initTitle(R.styleable.CustomRadioButtonThreeFields_title_rb_three_fields);
        initDescription(R.styleable.CustomRadioButtonThreeFields_description_rb_three_fields);
    }

    private void initImage(int index) {
        if (a.hasValue(R.styleable.CustomRadioButtonThreeFields_image_rb_three_fields)) {
            image = a.getResourceId(index, -1);
        }
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
        if (image != -1) {
            iconImageView.setBackgroundResource(image);
        }

        titleTextView.setText(title);
        descriptionTextView.setText(description);
    }
}