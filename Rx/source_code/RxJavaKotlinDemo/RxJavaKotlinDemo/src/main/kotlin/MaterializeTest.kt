import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject

fun main(args: Array<String>) {
    exampleOf("materialize/dematerialize") {
        val subscriptions = CompositeDisposable()
        val ryan = Student(BehaviorSubject.createDefault(80))
        val charlotte = Student(BehaviorSubject.createDefault(90))
        val student = BehaviorSubject.createDefault(ryan)

        val studentScore = student
            .switchMap { it.score.materialize() }
        subscriptions.add(studentScore
            .subscribe {
//                println(it)
            })

        studentScore
            .filter {
                if (it.error != null) {
                    println(it.error)
                    false
                } else {
                    true
                }
            }
            .dematerialize { it }
            .subscribe {
                println(it)
            }
            .addTo(subscriptions)

        ryan.score.onNext(85)
        ryan.score.onError(RuntimeException("Error!"))
        ryan.score.onNext(200)
        student.onNext(charlotte)

    }
}