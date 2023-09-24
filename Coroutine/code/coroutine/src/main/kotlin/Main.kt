import kotlinx.coroutines.*
import kotlin.concurrent.thread
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

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


//    val name: CoroutineName = CoroutineName("A name")
//    val element: CoroutineContext.Element = name
//    val context: CoroutineContext = element
//
//    val job: Job = Job()
//    val jobElement: CoroutineContext.Element = job
//    val jobContext: CoroutineContext = jobElement


//    val ctx: CoroutineContext = CoroutineName("A name")
//
//    val coroutineName: CoroutineName? = ctx[CoroutineName]
//    // or ctx.get(CoroutineName)
//    println(coroutineName?.name) // A name
//    val job: Job? = ctx[Job] // or ctx.get(Job)
//    println(job) // null

    val empty: CoroutineContext = EmptyCoroutineContext
    println(empty[CoroutineName]) // null
    println(empty[Job]) // null

    val ctxName = empty + CoroutineName("Name1") + empty
    println(ctxName[CoroutineName]) // CoroutineName(Name1)
}

private fun log(msg: Any?) = println("[${Thread.currentThread().name}] $msg")