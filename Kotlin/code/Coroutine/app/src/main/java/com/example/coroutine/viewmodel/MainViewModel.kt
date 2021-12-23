package com.example.coroutine.viewmodel

import android.view.ViewManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coroutine.api.Todo
import com.example.coroutine.repository.TodoRepository
import kotlinx.coroutines.launch

/**
 *
 * create by wangzhen 2021/12/23
 */
class MainViewModel : ViewModel() {

    val todoLiveData = MutableLiveData<Todo>()

    private val todoRepository = TodoRepository()

    fun getTodo(id: Int) {
        viewModelScope.launch {
            todoLiveData.value = todoRepository.getTodo(id)
        }
    }

}