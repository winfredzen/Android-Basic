import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy

fun main(args: Array<String>) {
    exampleOf("toList") {
        val subscriptions = CompositeDisposable()
        val items = Observable.just("A", "B", "C")
        subscriptions.add(
            items
                .toList()
                .subscribeBy {
                    println(it)
                }
        )
    }
}

