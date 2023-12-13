open class Person(val name: String, val age: Int) {
    fun eat() {
        println(name + " is eating. He is " + age + " years old.")
    }
}

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

