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

    val list = listOf("Apple", "Banana", "Orange", "Pear", "Grape", "Watermelon")
    val anyResult = list.any { it.length <= 5 }
    val allResult = list.all { it.length <= 5 }
    println("anyResult is " + anyResult + ", allResult is " + allResult)
    //anyResult is true, allResult is false

//    val list = listOf("Apple", "Banana", "Orange", "Pear", "Grape", "Watermelon")
//    val newList = list.map { it.uppercase() }
//    for (fruit in newList) {
//        println(fruit)
//    }

//    val list = listOf("Apple", "Banana", "Orange", "Pear", "Grape", "Watermelon")
////    val lambda = { fruit: String -> fruit.length }
//    val maxLengthFruit = list.maxByOrNull{ it.length }

//    val list = listOf("Apple", "Banana", "Orange", "Pear", "Grape", "Watermelon")
//    var maxLengthFruit = ""
//    for (fruit in list) {
//        if (fruit.length > maxLengthFruit.length) {
//            maxLengthFruit = fruit
//        }
//    }
//    println("max length fruit is " + maxLengthFruit)

//    val map = mapOf("Apple" to 1, "Banana" to 2, "Orange" to 3, "Pear" to 4, "Grape" to 5)
//    for ((fruit, number) in map) {
//        println("fruit is " + fruit + ", number is " + number)
//    }

//    val map = HashMap<String, Int>()
//    map["Apple"] = 1
//    map["Banana"] = 2
//    map["Orange"] = 3
//    map["Pear"] = 4
//    map["Grape"] = 5
//
//    println(map)

//    val map = HashMap<String, Int>()
//    map.put("Apple", 1)
//    map.put("Banana", 2)
//    map.put("Orange", 3)
//    map.put("Pear", 4)
//    map.put("Grape", 5)


//    val set = setOf("Apple", "Banana", "Orange", "Pear", "Grape")
//    for (fruit in set) {
//        println(fruit)
//    }


//    val list = mutableListOf("Apple", "Banana", "Orange", "Pear", "Grape")
//    list.add("Watermelon")
//    for (fruit in list) {
//        println(fruit)
//    }



//    val list = listOf("Apple", "Banana", "Orange", "Pear", "Grape")
//    println(list)
//
//    for (fruit in list) {
//        println(fruit)
//    }



//    val stu = Student("a123", 5)
//
//    val list = ArrayList<String>()
//    list.add("Apple")
//    list.add("Banana")
//    list.add("Orange")
//    list.add("Pear")
//    list.add("Grape")
}

data class Cellphone(val brand: String, val price: Double)

object Singleton {
    fun singletonTest() {
        println("singletonTest is called.")
    }
}

















