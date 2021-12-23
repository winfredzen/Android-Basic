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
import kotlin.coroutines.*

class MainActivity06 : AppCompatActivity(), CoroutineScope by MainScope() {
    private lateinit var nameTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nameTextView = findViewById<TextView>(R.id.textView)
        nameTextView.text = "Jack"

        val submitButton = findViewById<Button>(R.id.submitButton).also {
            it.setOnClickListener {
                launch {
                    val todo = userServiceApi.retrieveTodoById(1)
                    nameTextView.text = todo.title
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }

}