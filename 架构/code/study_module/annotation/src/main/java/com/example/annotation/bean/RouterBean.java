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

    public TypeEnum getTypeEnum() {
        return typeEnum;
    }

    public RouterBean() {
    }

    public RouterBean(TypeEnum typeEnum, Element element, Class<?> myClass, String path, String group) {
        this.typeEnum = typeEnum;
        this.element = element;
        this.myClass = myClass;
        this.path = path;
        this.group = group;
    }

    public RouterBean(TypeEnum typeEnum, Class<?> myClass, String path, String group) {
        this.typeEnum = typeEnum;
        this.myClass = myClass;
        this.path = path;
        this.group = group;
    }

    public static RouterBean create(TypeEnum typeEnum, Class<?> myClass, String path, String group) {
        return new RouterBean(typeEnum, myClass, path, group);
    }

    public void setTypeEnum(TypeEnum typeEnum) {
        this.typeEnum = typeEnum;
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public Class<?> getMyClass() {
        return myClass;
    }

    public void setMyClass(Class<?> myClass) {
        this.myClass = myClass;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    @Override
    public String toString() {
        return "RouterBean{" +
                "path='" + path + '\'' +
                ", group='" + group + '\'' +
                '}';
    }
}
