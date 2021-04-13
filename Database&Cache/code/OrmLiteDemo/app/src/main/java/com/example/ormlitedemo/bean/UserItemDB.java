package com.example.ormlitedemo.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * create by wangzhen 2021/4/13
 */

@DatabaseTable(tableName = "item_user")
public class UserItemDB {

    @DatabaseField(generatedId = true)
    private Long id;

    @DatabaseField
    private String index;

    @DatabaseField
    private String name;

    @DatabaseField
    private String age;

    public UserItemDB() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
