package com.example.kotlincoroutinedemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

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
        runBlocking {
            Log.d("runBlocking", "启动一个协程")
        }

        GlobalScope.launch {
            Log.d("launch", "启动一个协程")
        }

        GlobalScope.launch {
            Log.d("async", "启动一个协程")
        }
    }
}