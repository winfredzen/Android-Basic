package com.example.customradiogroupdemo.radiobutton;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.customradiogroupdemo.R;

public final class CustomRadioGroup extends ConstraintLayout {

    private static OnCustomRadioButtonListener onClickListener;

    public CustomRadioGroup(Context context) {
        super(context);
    }

    public CustomRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomRadioGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public static void setOnClickListener(OnCustomRadioButtonListener onClickListener) {
        CustomRadioGroup.onClickListener = onClickListener;
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (child instanceof BaseCustomRadioButton) {
            child.setOnClickListener(view -> {
                BaseCustomRadioButton selectedButton = (BaseCustomRadioButton) child;

                setAllButtonsToUnselectedState();
                setSelectedButtonToSelectedState(selectedButton);
                initOnClickListener(selectedButton);
            });
        }

        super.addView(child, index, params);
    }

    private void setAllButtonsToUnselectedState() {
        ConstraintLayout container = this;

        for (int i = 0; i < container.getChildCount(); i++) {
            View child = container.getChildAt(i);

            if (child instanceof BaseCustomRadioButton) {
                BaseCustomRadioButton containerView = (BaseCustomRadioButton) child;
                setButtonToUnselectedState(containerView);
            }
        }
    }

    private void setButtonToUnselectedState(BaseCustomRadioButton containerView) {
        float viewWithFilter = 0.5f;

        containerView.setAlpha(viewWithFilter);
        containerView.setBackground(getResources().getDrawable(R.drawable.background_custom_radio_buttons_unselected_state));
    }

    private void setSelectedButtonToSelectedState(BaseCustomRadioButton selectedButton) {
        float viewWithoutFilter = 1f;

        selectedButton.setAlpha(viewWithoutFilter);
        selectedButton.setBackground(getResources().getDrawable(R.drawable.background_custom_radio_buttons_selected_state));
    }

    private void initOnClickListener(View selectedButton) {
        if(onClickListener != null) {
            onClickListener.onClick(selectedButton);
        }
    }
}
