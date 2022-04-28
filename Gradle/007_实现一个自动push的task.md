# 实现一个自动push的task

在开发中有个需求，将build生成的apk，自动push到某个目录

我参考网上的一些做法，自定义一个task，然后执行这个task来完成操作



一些知识点

1.在gradle中执行shell脚本

可参考：

+ [Exec](https://docs.gradle.org/current/dsl/org.gradle.api.tasks.Exec.html)

+ [Execute shell script in Gradle](https://stackoverflow.com/questions/25562207/execute-shell-script-in-gradle)

我尝试了下，类似的几种都可以

```groovy
            project.exec {
                executable = android.adbExecutable
                args 'devices'
            }

            exec {
                executable "../shell.sh"

                commandLine 'sh', '../shell.sh', apk
            }
```



2.在执行的shell脚本时传递参数

+ [How to pass argument from gradle exec task to commandline](https://stackoverflow.com/questions/47929483/how-to-pass-argument-from-gradle-exec-task-to-commandline)

> **Windows**
>
> ```sh
> commandLine 'cmd', '/c', 'run.bat' ,arg1
> ```
>
> **Linux / mac**
>
> ```sh
> commandLine './run.sh' , arg1
> ```



3.在task中获取apk文件

参考：

+ [Get resulted apk file path from android gradle plugin](https://stackoverflow.com/questions/24136879/get-resulted-apk-file-path-from-android-gradle-plugin)



```groovy
def apk = null
android.applicationVariants.all { variant ->
    if ( (variant.name).equals("release") ) {
        variant.outputs.each { output ->
            apk = output.outputFile
        }
    }
}
println "Found output file: " + apk
```



其它可参考的文章：

+ [Android开发Gradle高级技巧、自定义Gradle Task、打包完成自动签名、执行Shell脚本](https://blog.csdn.net/a_zhon/article/details/85065047)
+ [最熟悉的陌生人 -- Gradle](https://juejin.cn/post/6844903455014780936#heading-20)



最终的版本，在`assembleDebug`任务后，获取apk的路径，自动执行`emergency.sh`

```groovy
//自动push
task autoPush {
    dependsOn 'assembleDebug'
    doLast {
        println("=========autoPush doLast=========")
        def apk = null
        android.applicationVariants.all { variant ->
            if ( (variant.name).equals("debug") ) {
                variant.outputs.each { output ->
                    apk = output.outputFile
                }
            }
        }
        if (apk != null) {
            println "Found output file: " + apk
            //执行shell脚本
            exec {
                commandLine 'sh', '../emergency.sh', apk
            }
        } else {
            println "Not Found output APK File"
        }

    }
}
```









