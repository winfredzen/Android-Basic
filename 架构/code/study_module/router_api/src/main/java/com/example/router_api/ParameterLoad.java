package com.example.router_api;

/**
 * create by wangzhen 2021/10/7
 *  参数传递
 */
public interface ParameterLoad {
    /**
     * 目标对象.属性名 = getIntent().属性类型 -》 完成赋值操作
     * @param targetParameter 目标对象，如OrderActivity
     */
    void getParameter(Object targetParameter);
}
