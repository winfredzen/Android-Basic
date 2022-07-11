package com.example.android.camera2.basic

import kotlin.math.max

/**
 *
 * create by wangzhen 2022/7/11
 */
fun main() {
    val a = 37
    val b = 40
    val value = largerNumber(a, b)
    println("larger number is " + value)
    for (i in 10 downTo 1) {
        println(i)
    }
}

fun getScore(name: String) = when {
    name == "Tom" -> 86
    name == "Jim" -> 77
    name == "Jack" -> 95
    name == "Lily" -> 100
    else -> 0
}

fun checkNumber(num: Number) {
    when (num) {
        is Int -> println("number is Int")
        is Double -> println("number is Double")
        else -> println("number not support")
    }
}

//fun getScore(name: String) = when (name) {
//    "Tom" -> 86
//    "Jim" -> 77
//    "Jack" -> 95
//    "Lily" -> 100
//    else -> 0
//}

//fun getScore(name: String) = if (name == "Tom") {
//    86
//} else if (name == "Jim") {
//    77
//} else if (name == "Jack") {
//    95
//} else if (name == "Lily") {
//    100
//} else {
//    0
//}

fun largerNumber(num1: Int, num2: Int) = if (num1 > num2) num1 else num2


//fun largerNumber(num1: Int, num2: Int) = max(num1, num2)