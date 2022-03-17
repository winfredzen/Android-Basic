plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    implementation(gradleApi())
    // 这里可以引用更多其他第三方库
}

gradlePlugin {
    plugins {
        create("MyTestPlugin") {
            id = "com.test.my.plugin"    // 这里替代了 /META-INF/gradle-plugins 中的配置流程（即无需手动配置该目录了），
                                         // 其他工程可以通过id指定的名字来 使用插件
            implementationClass = "com.test.GreetingPlugin" // 指向对应的 插件实现类
        }
    }
}