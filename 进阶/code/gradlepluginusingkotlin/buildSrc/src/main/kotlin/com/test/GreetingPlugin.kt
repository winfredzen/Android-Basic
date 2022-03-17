
package com.test

import org.gradle.api.Plugin
import org.gradle.api.Project

class GreetingPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        println("[GreetingPlugin] apply ...")
        project.task("hello") {
            doLast {
                println("[GreetingPlugin] Hello from the GreetingPlugin")
            }
        }
    }
}
