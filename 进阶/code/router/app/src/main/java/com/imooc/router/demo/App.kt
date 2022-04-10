package com.imooc.router.demo

import android.app.Application
import com.imooc.gradle.router.runtime.Router

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        Router.init()
    }
}