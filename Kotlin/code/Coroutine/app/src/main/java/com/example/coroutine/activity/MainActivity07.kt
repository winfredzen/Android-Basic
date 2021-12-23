package com.example.coroutine.activity

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.coroutine.R
import com.example.coroutine.api.Todo
import com.example.coroutine.api.userServiceApi
import com.example.coroutine.databinding.ActivityMain07Binding
import com.example.coroutine.databinding.ActivityMain07BindingImpl
import com.example.coroutine.viewmodel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity07 : AppCompatActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityMain07Binding>(this, R.layout.activity_main_07)
        binding.viewModel = mainViewModel
        binding.lifecycleOwner = this

        binding.submitButton.also {
            it.setOnClickListener {
                mainViewModel.getTodo(1)
            }
        }

    }
}