package com.example.annotation.bean;

import javax.lang.model.element.Element;

/**
 * create by wangzhen 2021/9/29
 * 代表路径以及class信息
 */
public class RouterBean {
    //类型，目前只做Activity类型的跳转
    public enum TypeEnum {
        ACTIVITY
    }

    private TypeEnum typeEnum;
    //类节点信息
    private Element element;
    //被注解类的class
    private Class<?> myClass;
    //路径
    private String path;
    //分组
    private String group;

}
