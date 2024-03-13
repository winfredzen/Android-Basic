import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable

fun main(args: Array<String>) {
    exampleOf("distinctUntilChangedPredicate") {
        val subscriptions = CompositeDisposable()
        subscriptions.add(
            Observable.just(
                "ABC", "BCD", "CDE", "FGH", "IJK", "JKL", "LMN")
                .distinctUntilChanged { first, second ->
                    // Return true if any character in the second string is also in the first string.
                    second.any { it in first }
                }
                .subscribe {
                    println(it)
                }
        )
    }
}