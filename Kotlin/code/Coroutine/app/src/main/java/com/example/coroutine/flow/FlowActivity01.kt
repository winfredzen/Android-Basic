package com.example.coroutine.flow

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
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

class FlowActivity01 : AppCompatActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityMain07Binding>(this, R.layout.activity_main_07)
        binding.viewModel = mainViewModel
        binding.lifecycleOwner = this

        binding.submitButton.also {
            it.setOnClickListener {

//                simpleList().forEach { value -> println(value) }

//                simpleSequence().forEach { value -> println(value) }
//
//                runBlocking<Unit> {
//                    simpleList().forEach { value -> println(value) }
//                }


                runBlocking<Unit> {
                    launch {
                        for (k in 1..3) {
                            println("I'm not blocking $k")
                            delay(1500)
                        }
                    }
                    simpleFlow().collect {
                        value -> println(value)
                    }
                }

            }
        }

    }

    fun simpleFlow() = flow<Int> {
        for (i in 1..3) {
            delay(1000)
            emit(i) //发射并产生一个元素
        }
    }

    fun simpleSequence(): Sequence<Int> = sequence {
        for (i in 1..3) {
            Thread.sleep(1000)
            yield(i)
        }
    }
}