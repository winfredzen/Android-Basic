package com.imooc.demo

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.imooc.biz.reading.Test
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try {
            val appInfo = packageManager
                .getApplicationInfo(packageName,PackageManager.GET_META_DATA)
            val channelName = appInfo.metaData.getString("MTA_CHANNEL")
            Log.i("channel_test", "channel = $channelName")
        } catch (e: Exception) {
            // ignore
        }

        findViewById<View>(R.id.btn).setOnClickListener {

            Test().hello(it.context)

        }

        importantMethod()
        importantMethodTwo()
    }

    private fun importantMethod() {
    }

    private fun importantMethodTwo() {
        Log.i("test", "test")
    }
}