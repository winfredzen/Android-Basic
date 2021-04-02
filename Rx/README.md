# Rx系列

主要原理来自[ReactiveX](http://reactivex.io/)，中文翻译可参考[ReactiveX文档中文翻译 - mcxiaoke](https://mcxiaoke.gitbooks.io/rxdocs/content/Intro.html)

**Observable**

> In ReactiveX an observer subscribes to an Observable. Then that observer reacts to whatever item or sequence of items the Observable emits. This pattern facilitates concurrent operations because it does not need to block while waiting for the Observable to emit objects, but instead it creates a sentry in the form of an observer that stands ready to react appropriately at whatever future time the Observable does so.
>
> 在ReactiveX中，观察者订阅了一个Observable。 然后，该观察者对可观察对象发出的任何item或item序列做出反应。 这种模式有助于并发操作，因为它在等待Observable发出对象时不需要阻塞，而是以观察者的形式创建了一个哨兵，随时准备在Observable以后的任何时间做出适当的反应。

