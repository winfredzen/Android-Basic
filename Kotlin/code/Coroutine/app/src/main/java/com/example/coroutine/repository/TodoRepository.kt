package com.example.coroutine.repository

import com.example.coroutine.api.Todo
import com.example.coroutine.api.userServiceApi

/**
 *
 * create by wangzhen 2021/12/23
 */
class TodoRepository {
    suspend fun getTodo(id: Int): Todo {
        return userServiceApi.retrieveTodoById(id)
    }
}