package org.example

fun main() {


    val list = listOf("Apple", "Banana", "Orange", "Pear", "Grape")
    val result = with(StringBuilder()) {
        append("Start eating fruits.\n")
        for (fruit in list) {
            append(fruit).append("\n")
        }
        append("Ate all fruits.")
        toString()
    }
    println(result)

//    val list = listOf("Apple", "Banana", "Orange", "Pear", "Grape")
//    val builder = StringBuilder()
//    builder.append("Start eating fruits.\n")
//    for (fruit in list) {
//        builder.append(fruit).append("\n")
//    }
//    builder.append("Ate all fruits.")
//    val result = builder.toString()
//    println(result)


}