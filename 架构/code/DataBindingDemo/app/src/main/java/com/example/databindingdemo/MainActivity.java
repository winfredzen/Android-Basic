package com.example.databindingdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.widget.Toast;

import com.example.databindingdemo.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements MainActivityContract.View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        MainActivityPresenter presenter = new MainActivityPresenter(this, getApplicationContext());
        TemperatureData temperatureData = new TemperatureData("Hamburg", "10");
        binding.setTemp(temperatureData);
        binding.setPresenter(presenter);
    }


    @Override
    public void showData(TemperatureData temperatureData) {
        String celsius = temperatureData.getCelsius();
        Toast.makeText(this, celsius, Toast.LENGTH_SHORT).show();
    }
}