import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject

fun main(args: Array<String>) {
    exampleOf("switchMap") {
        val ryan = Student(BehaviorSubject.createDefault(80))
        val charlotte = Student(BehaviorSubject.createDefault(90))
        val student = PublishSubject.create<Student>()
        student
            .switchMap { it.score }
            .subscribe { println(it) }
        student.onNext(ryan)
        ryan.score.onNext(85)
        student.onNext(charlotte)
        ryan.score.onNext(95)
        charlotte.score.onNext(100)
    }
}