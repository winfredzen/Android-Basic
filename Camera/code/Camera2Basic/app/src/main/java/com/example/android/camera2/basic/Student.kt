package com.example.android.camera2.basic

/**
 *
 * create by wangzhen 2022/7/11
 */
class Student(val sno: String, val grade: Int, name: String, age: Int) : Person(name, age) {
    init {
        println("sno is " + sno)
        println("grade is " + grade)
    }

    constructor(name: String, age: Int) : this("", 0, name, age) {
    }
    constructor() : this("", 0) {
    }
}

fun main() {
    val stu = Student("a123", 5)
}

data class Cellphone(val brand: String, val price: Double)

object Singleton {
    fun singletonTest() {
        println("singletonTest is called.")
    }
}