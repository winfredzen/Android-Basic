package com.example.greendaodemo;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.example.greendaodemo.model.DaoMaster;
import com.example.greendaodemo.model.DaoSession;

/**
 * create by wangzhen 2021/7/29
 */
public class MyApplication extends Application {
    public static DaoSession sDaoSession;


    @Override
    public void onCreate() {
        super.onCreate();

        initDB();
    }

    /**
     * 连接数据库并创建会话
     */
    public void initDB() {
        //获取需要连接的数据库
        DaoMaster.DevOpenHelper  devOpenHelper = new DaoMaster.DevOpenHelper(this, "demo.db");
        SQLiteDatabase db = devOpenHelper.getWritableDatabase();
        //创建数据库连接
        DaoMaster daoMaster = new DaoMaster(db);
        //创建数据库会话
        sDaoSession = daoMaster.newSession();
    }
}
