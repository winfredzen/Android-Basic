fun main(args: Array<String>) {
    println("Hello World!")

    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.
    println("Program arguments: ${args.joinToString()}")

    println("getScore ${getScore("wz")}")

    val stu = Student("1", 1, "Jack", 19)

    val student1 = Student()
    val student2 = Student("Jack", 19)
    val student3 = Student("a123", 5, "Jack", 19)

    var list = listOf("Apple", "Banana")
    println("list = ${list}")

    printParams(str = "Hello")
}

fun printParams(num: Int = 100, str: String) {
    println("num is $num, str is $str")
}

fun getScore(name: String) = when (name) {
    "Tom" -> 88
    "Jim" -> 89
    "Lily" -> 90
    else -> 0
}
