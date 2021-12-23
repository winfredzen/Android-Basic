package com.example.coroutine.api

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path


/**
 *
 * create by wangzhen 2021/12/23
 */

data class Todo (val userId: Int, val id: Int, val title: String, val completed: Boolean)

val userServiceApi: UserServiceApi by lazy {
    val interceptor = HttpLoggingInterceptor()
    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY) //需要设置这个Level貌似才生效

    var okHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

    var retrofit = Retrofit.Builder().client(okHttpClient)
        .baseUrl("https://jsonplaceholder.typicode.com/").
        addConverterFactory(MoshiConverterFactory.create())
        .build()
    retrofit.create(UserServiceApi::class.java)
}

interface UserServiceApi {
    @GET("/todos/{id}")
    fun getTodoById(@Path("id") id: Int): Call<Todo>

    @GET("/todos/{id}")
    suspend fun retrieveTodoById(@Path("id") id: Int): Todo

}