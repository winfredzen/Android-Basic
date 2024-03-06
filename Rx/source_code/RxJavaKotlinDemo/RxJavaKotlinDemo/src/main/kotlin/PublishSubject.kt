import io.reactivex.rxjava3.subjects.PublishSubject

fun main(args: Array<String>) {
    exampleOf("PublishSubject") {
        val publishSubject = PublishSubject.create<Int>()
        publishSubject.onNext(0)
        val subscriptionOne = publishSubject.subscribe { int ->
            println(int)
        }
        publishSubject.onNext(1)
    }
}