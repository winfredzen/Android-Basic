import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.PublishSubject

fun main(args: Array<String>) {
    exampleOf("takeWhile") {
        val subscriptions = CompositeDisposable()
        subscriptions.add(
            Observable.fromIterable(
                listOf(1, 2, 7, 3, 4, 5, 6, 7, 8, 9, 10, 1))
                .takeWhile { number ->
                    number < 5
                }.subscribe {
                    println(it)
                })
    }
}