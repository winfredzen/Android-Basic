import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun main(args: Array<String>) {
    GlobalScope.launch {
        delay(1000L) //非阻塞的等待 1 秒钟（默认时间单位是毫秒）
        log("World!")
    }
    log("Hello, ")
    Thread.sleep(2000L)
}

private fun log(msg: Any?) = println("[${Thread.currentThread().name}] $msg")