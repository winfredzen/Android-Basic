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

class MainActivity05 : AppCompatActivity() {
    private lateinit var nameTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nameTextView = findViewById<TextView>(R.id.textView)
        nameTextView.text = "Jack"

        val submitButton = findViewById<Button>(R.id.submitButton).also {
            it.setOnClickListener {
                Log.d("TAG", "OnClickListener")

                //协程的挂起点通过continuation保存起来
                val continuation = suspend {
                    5
                }.createCoroutine(object : Continuation<Int> {
                    override val context: CoroutineContext = EmptyCoroutineContext

                    override fun resumeWith(result: Result<Int>) {
                        Log.d("MainActivity05", "Coroutine End")
                    }
                })

                continuation.resume(Unit)

            }
        }

    }


}