package com.raywenderlich.android.movieapp

/**
 *
 * create by wangzhen 2021/12/28
 */
class Event<out T>(private val content: T) {
    private var hasBeenHandled = false

    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled)
            null
        else {
            hasBeenHandled = true
            content
        }
    }
}