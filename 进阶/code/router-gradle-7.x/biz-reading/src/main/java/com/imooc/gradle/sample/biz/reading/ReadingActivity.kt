package com.imooc.gradle.sample.biz.reading

import android.app.Activity
import com.imooc.router.annotations.Destination

@Destination(
        url = "router://reading",
        description = "阅读页"
)
class ReadingActivity : Activity() {

}