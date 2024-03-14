import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.subscribeBy

fun main(args: Array<String>) {
    exampleOf("map") {
        val subscriptions = CompositeDisposable()
        subscriptions.add(
            Observable.just("M", "C", "V", "I")
                .map {
                    it.romanNumeralIntValue()
                }
                .subscribeBy {
                    println(it)
                })
    }
}