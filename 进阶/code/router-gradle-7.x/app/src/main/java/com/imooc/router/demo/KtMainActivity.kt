package com.imooc.router.demo

import android.app.Activity
import com.imooc.router.annotations.Destination

@Destination(
        url = "router://page-kotlin",
        description = "登录页面"
)
class KtMainActivity : Activity() {
}