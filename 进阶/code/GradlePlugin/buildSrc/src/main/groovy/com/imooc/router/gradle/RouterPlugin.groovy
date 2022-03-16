package com.imooc.router.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

class RouterPlugin implements Plugin<Project> {

    //实现apply方法，注入插件的逻辑
    @Override
    void apply(Project project) {
        println("I am from RouterPlugin, apply from ${project.name}")
    }
}