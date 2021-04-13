package com.example.ormlitedemo.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.ormlitedemo.bean.UserItemDB;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * create by wangzhen 2021/4/13
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "wz";
    private static final int DATABASE_VERSION = 1;

    private Dao<UserItemDB, Long> userItemDBS;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            // 创建表
            TableUtils.createTable(connectionSource, UserItemDB.class);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
       if (checkTableExist(database, "item_user")) {
           try {
               TableUtils.dropTable(connectionSource, UserItemDB.class, false);
               onCreate(database, connectionSource);
           } catch (SQLException e) {
               e.printStackTrace();
           }
       }
    }

    private boolean checkTableExist(SQLiteDatabase database, String tableName) {
        Cursor c = null;
        boolean tableExist = false;
        try {
            c = database.query(tableName, null, null, null, null, null, null);
            tableExist = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tableExist;
    }

    @Override
    public void close() {
        userItemDBS = null;
        super.close();
    }

    public Dao<UserItemDB, Long> getUserItemDBS() throws SQLException {
        if (userItemDBS == null) {
            userItemDBS = getDao(UserItemDB.class);
        }
        return userItemDBS;
    }

    public void clearTable() throws SQLException {
        TableUtils.clearTable(getConnectionSource(), UserItemDB.class);
    }
}
