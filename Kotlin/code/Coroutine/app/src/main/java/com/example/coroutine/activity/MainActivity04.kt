package com.example.coroutine.activity

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.coroutine.R
import com.example.coroutine.api.Todo
import com.example.coroutine.api.userServiceApi
import kotlinx.coroutines.*

class MainActivity04 : AppCompatActivity() {
    private lateinit var nameTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nameTextView = findViewById<TextView>(R.id.textView)
        nameTextView.text = "Jack"

        val submitButton = findViewById<Button>(R.id.submitButton).also {
            it.setOnClickListener {
                Log.d("TAG", "OnClickListener")
                GlobalScope.launch(Dispatchers.Main) {
                    //挂起
                    delay(10000)
                    Log.d("MainActivity04", "delay " + "${Thread.currentThread().name}")
                }

                //阻塞
                Thread.sleep(10000)
                Log.d("MainActivity04", "sleep " + "${Thread.currentThread().name}")
            }
        }

    }

    private suspend fun getTodo() {
        val todo = get()
        show(todo)
    }

    private suspend fun get() = withContext(Dispatchers.IO) {
        userServiceApi.retrieveTodoById(1)
    }

    private fun show(todo: Todo) {
        nameTextView.text = todo.title
    }
}