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

class MainActivity01 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val nameTextView = findViewById<TextView>(R.id.textView)
        nameTextView.text = "Jack"

        val submitButton = findViewById<Button>(R.id.submitButton).also {
            it.setOnClickListener {
                Log.d("TAG", "OnClickListener")
                object : AsyncTask<Void, Void, Todo>() {
                    override fun doInBackground(vararg params: Void?): Todo? {
                        return userServiceApi.getTodoById(1).execute().body()
                    }

                    override fun onPostExecute(result: Todo?) {
                        super.onPostExecute(result)
                        nameTextView.text = result?.title
                    }
                }.execute()
            }
        }

    }
}