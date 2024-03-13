import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.PublishSubject

fun main(args: Array<String>) {
    exampleOf("skipWhile") {
        val subscriptions = CompositeDisposable()
        subscriptions.add(
            Observable.just(2, 2, 3, 4)
                .skipWhile { number ->
                    number % 2 == 0
                }.subscribe {
                    println(it)
                })
    }
}