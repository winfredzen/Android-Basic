package com.example.coroutine.kotlin

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

class MainActivity08 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityMain07Binding>(this, R.layout.activity_main_07)

        binding.submitButton.also {
            it.setOnClickListener {
//                runBlocking {
//                    delay(1000L)
//                    println("Hello inner!")
//                    println("thread = " + "${Thread.currentThread().name}")
//                }
//                println("Hello out!")

//                main()
                main02()
                println("after main()")
            }
        }


    }

    fun main() = runBlocking { // this: CoroutineScope
        launch { // launch a new coroutine and continue
            delay(1000L) // non-blocking delay for 1 second (default time unit is ms)
            println("World inner!") // print after delay
        }
        println("Hello out!") // main coroutine continues while a previous one is delayed
    }

    fun main02() = runBlocking {
        doWorld()
    }

    suspend fun doWorld() = coroutineScope {  // this: CoroutineScope
        launch {
            delay(1000L)
            println("World!")
        }
        println("Hello")
    }

}