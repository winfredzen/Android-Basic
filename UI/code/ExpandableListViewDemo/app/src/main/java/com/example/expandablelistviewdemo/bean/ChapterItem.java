package com.example.expandablelistviewdemo.bean;

/**
 * 分组子条目
 * create by wangzhen 2021/7/30
 */
public class ChapterItem {
    private int id;
    private String name;
    private int pid;

    //数据库定义相关
    public static final String TABLE_NAME = "tb_chapter_item";
    public static final String COL_ID = "_id";
    public static final String COL_NAME = "name";
    public static final String COL_PID = "pid";

    public ChapterItem() {
    }

    public ChapterItem(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }
}
