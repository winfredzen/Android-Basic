package com.wz.databindingtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.wz.databindingtest.databinding.ActivityMainBinding;
import com.wz.databindingtest.viewmodel.MainViewModel;

public class MainActivity extends AppCompatActivity {

    private MainViewModel mMainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.setLifecycleOwner(this);

        mMainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        binding.setViewModel(mMainViewModel);

    }
}