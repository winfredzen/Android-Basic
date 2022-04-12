package com.gluems.magiccube

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.gluems.magiccube.views.MagicCubeView

class MainActivity : AppCompatActivity() {

    lateinit var view: MagicCubeView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        view = MagicCubeView(this)
        setContentView(view)
    }
}
