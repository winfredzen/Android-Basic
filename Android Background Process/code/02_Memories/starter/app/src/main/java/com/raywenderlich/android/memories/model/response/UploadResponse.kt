package com.raywenderlich.android.memories.model.response

import kotlinx.serialization.Serializable

/*
* 上传响应数据
*
* */
@Serializable
class UploadResponse(val message : String = "", val url : String = "") {


}