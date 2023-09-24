import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

fun main(args: Array<String>) {
    GlobalScope.launch {
        delay(1000L) //非阻塞的等待 1 秒钟（默认时间单位是毫秒）
        log("World!")
    }

//    thread {
//        delay(1000L) //Error Suspend function 'delay' should be called only from a coroutine or another suspend function
//        log("World!")
//    }

    log("Hello, ")
    Thread.sleep(2000L)
}

private fun log(msg: Any?) = println("[${Thread.currentThread().name}] $msg")