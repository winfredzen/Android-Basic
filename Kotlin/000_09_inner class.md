# inner class

Kotlin中使用`inner class`关键字来定义内部类

参考：

+ [Kotlin Nested and Inner Classes](https://www.baeldung.com/kotlin/inner-classes)



如下定义一个`Computer`类：

```kotlin
class Computer(val model: String) {
    inner class HardDisk(val sizeInGb: Int) {
        fun getInfo() = "Installed on ${this@Computer} with $sizeInGb GB"
    }
}
```

`this@Computer`是[Qualified this](https://kotlinlang.org/docs/this-expressions.html)



其转为Java代码如下，可见是一个内部类：

```java
public final class Computer {
    @NotNull
    private final String model;

    public Computer(@NotNull String model) {
        Intrinsics.checkNotNullParameter(model, "model");
        this.model = model;
    }

    @NotNull
    public final String getModel() {
        return this.model;
    }

    @Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0000\b\u0086\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\u0007\u001a\u00020\bR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\t"}, d2={"Lorg/example/Computer$HardDisk;", "", "sizeInGb", "", "(Lorg/example/Computer;I)V", "getSizeInGb", "()I", "getInfo", "", "KotlinTest"})
    public final class HardDisk {
        private final int sizeInGb;

        public HardDisk(int sizeInGb) {
            this.sizeInGb = sizeInGb;
        }

        public final int getSizeInGb() {
            return this.sizeInGb;
        }

        @NotNull
        public final String getInfo() {
            return "Installed on " + Computer.this + " with " + this.sizeInGb + " GB";
        }
    }
}

```



上面中的内部类使用方式如下：

```kotlin
    val hardDisk = Computer("Desktop").HardDisk(100)
    println(hardDisk.getInfo())
```

