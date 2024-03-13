import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.PublishSubject

fun main(args: Array<String>) {
    val subscriptions = CompositeDisposable()
    val subject = PublishSubject.create<String>()
    val trigger = PublishSubject.create<String>()
    subscriptions.add(
        subject.takeUntil(trigger)
            .subscribe {
                println(it)
            })
    subject.onNext("1")
    subject.onNext("2")
    trigger.onNext("X")
    subject.onNext("3")
}