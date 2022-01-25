package com.example.kotlincoroutinedemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    private lateinit var btn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn = findViewById(R.id.button)
        btn.setOnClickListener{
            start()
        }
    }

    private fun start() {
//        runBlocking {
//            Log.d("runBlocking", "启动一个协程")
//        }
//
//        GlobalScope.launch {
//            Log.d("launch", "启动一个协程")
//        }
//
//        GlobalScope.launch {
//            Log.d("async", "启动一个协程")
//        }


        runBlocking<Unit> {
            val startTime = System.currentTimeMillis()
            val job = launch (Dispatchers.Default) {
                println("current thread = " + Thread.currentThread().name)
                var nextPrintTime = startTime
                var i = 0
                while (i < 5) {
                    // print a message twice a second
                    if (System.currentTimeMillis() >= nextPrintTime) {
                        println("Hello ${i++}")
                        nextPrintTime += 500L
                    }
                }
            }
            delay(1000L)
            println("Cancel!")
            job.cancel()
            println("Done!")
        }

    }
}