package org.example


class Money(val value: Int) {

    // 和Money对象相加
    operator fun plus(money: Money): Money {
        val sum = value + money.value
        return Money(sum)
    }

    // 和一个数相加
    operator fun plus(newValue: Int): Money {
        val sum = value + newValue
        return Money(sum)
    }
}