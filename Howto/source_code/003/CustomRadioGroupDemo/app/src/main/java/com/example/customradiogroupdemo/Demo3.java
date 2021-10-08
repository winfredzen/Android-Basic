package com.example.customradiogroupdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.os.Bundle;
import android.view.View;

import com.example.customradiogroupdemo.widget.PresetRadioGroup;
import com.example.customradiogroupdemo.widget.PresetValueButton;

public class Demo3 extends AppCompatActivity {

    PresetRadioGroup mSetDurationPresetRadioGroup;
    AppCompatEditText mSetDurationEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo3);

        mSetDurationPresetRadioGroup = (PresetRadioGroup) findViewById(R.id.preset_time_radio_group);
        mSetDurationEditText = (AppCompatEditText) findViewById(R.id.edit_text_set_duration);
        mSetDurationPresetRadioGroup.setOnCheckedChangeListener(new PresetRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(View radioGroup, View radioButton, boolean isChecked, int checkedId) {
                mSetDurationEditText.setText(((PresetValueButton) radioButton).getValue());
                mSetDurationEditText.setSelection(mSetDurationEditText.getText().length());
            }
        });
    }
}