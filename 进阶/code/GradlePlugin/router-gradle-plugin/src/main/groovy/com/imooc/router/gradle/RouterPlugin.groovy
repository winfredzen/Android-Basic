package com.imooc.router.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

class RouterPlugin implements Plugin<Project> {

    //实现apply方法，注入插件的逻辑
    @Override
    void apply(Project project) {
        println("I am from RouterPlugin, apply from ${project.name}")

        //注册Extension
        project.getExtensions().create("router", RouterExtension)

        //获取用户设置的RouterExtension
        project.afterEvaluate {
            //这里是中括号，不是小括号
            RouterExtension extension = project["router"]
            println("用户设置的wiki路径为 ${extension.wikiDir}")
        }
    }
}