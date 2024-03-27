package org.example

class Computer(val model: String) {
    inner class HardDisk(val sizeInGb: Int) {
        fun getInfo() = "Installed on ${this@Computer} with $sizeInGb GB"
    }
}

