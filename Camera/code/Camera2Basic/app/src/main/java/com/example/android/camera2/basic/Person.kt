package com.example.android.camera2.basic

/**
 *
 * create by wangzhen 2022/7/11
 */
open class Person(val name: String, val age: Int) {
    fun eat() {
        println(name + " is eating. He is " + age + " years old.")
    }
}

var content: String? = "hello"
fun main() {
    if (content != null) {
        printUpperCase()
    }
}
fun printUpperCase() {
    val upperCase = content.toUpperCase()
    println(upperCase)
}
