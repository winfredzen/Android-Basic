import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.subjects.PublishSubject

fun main(args: Array<String>) {

    exampleOf("Merge") {
        val subscriptions = CompositeDisposable()

        val germanCities = PublishSubject.create<String>()
        val spanishCities = PublishSubject.create<String>()

        germanCities.mergeWith(spanishCities).subscribe {
            println(it)
        }.addTo(subscriptions)

        germanCities.onNext("Frankfurt")
        germanCities.onNext("Berlin")
        spanishCities.onNext("Madrid")
        germanCities.onNext("Münich")
        spanishCities.onNext("Barcelona")
        spanishCities.onNext("Valencia")
    }

//    exampleOf("Merge") {
//        val subscriptions = CompositeDisposable()
//
//        val left = PublishSubject.create<Int>()
//        val right = PublishSubject.create<Int>()
//
//        Observable.merge(left, right).subscribe {
//            println(it)
//        }.addTo(subscriptions)
//
//        left.onNext(0)
//        left.onNext(1)
//        right.onNext(3)
//        left.onNext(4)
//        right.onNext(5)
//        right.onNext(6)
//    }
}