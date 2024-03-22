import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable

fun main(args: Array<String>) {
    exampleOf("startWidth") {
        val subscriptions = CompositeDisposable()
        val missingNumbers = Observable.just(3, 4, 5)
        val completeSet = missingNumbers.startWithIterable(listOf(1, 2))

        completeSet.subscribe { number ->
            println(number)
        }
    }
}