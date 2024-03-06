# Observable的hide方法

在项目中有时会遇到这样的写法`selectedPhotosSubject.hide()`：

```kotlin
    // 内部使用
    private val selectedPhotosSubject = PublishSubject.create<Photo>()

    // 向外暴露的Observable
    val selectedPhotos: Observable<Photo>
        get() = selectedPhotosSubject.hide()
```

这里的`hide`有什么作用呢？

参考：

+ [Reactive java method hide()](https://stackoverflow.com/questions/46868012/reactive-java-method-hide)

> Lets say, you use a PublishSubject internally and you want to pass an Observable to the outside world. This would be a good idea, because of information hiding. The caller from outside would not be able to invoke #onNext() on an Observable. So, you could just use Observable as the return value of the method and just return the PublishSubject. That would be possible, but the caller would be able to cast it and would be able to invoke #onNext() from outside.
>
> Observable#hide create a new Observable from PublishSubject, so no casting would be possible.

大致意思是：在内部使用`PublishSubject`，在外部使用`Observable`。这样的话在外部就不能在`Observable`调用`onNext()`方法了。
