package com.example.expandablelistviewdemo.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 分组条目
 * create by wangzhen 2021/7/30
 */
public class Chapter {
    private int id;
    private String name;

    //数据库定义相关
    public static final String TABLE_NAME = "tb_chapter";
    public static final String COL_ID = "_id";
    public static final String COL_NAME = "name";

    private List<ChapterItem> children = new ArrayList<>();

    public Chapter() {
    }

    public Chapter(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public void addChild(ChapterItem chapterItem) {
        chapterItem.setPid(id);
        children.add(chapterItem);
    }

    public void addChild(int cid, String cname) {
        ChapterItem chapterItem = new ChapterItem(cid, cname);
        chapterItem.setPid(id);
        children.add(chapterItem);
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

    public List<ChapterItem> getChildren() {
        return children;
    }

    public void setChildren(List<ChapterItem> children) {
        this.children = children;
    }
}
