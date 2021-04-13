package com.example.ormlitedemo.db;

import android.content.Context;
import android.util.Log;

import com.example.ormlitedemo.bean.UserItemDB;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;

import java.sql.SQLException;
import java.util.List;

/**
 * create by wangzhen 2021/4/13
 */
public class DatabaseManager {
    private final String TAG = DatabaseManager.this.getClass().getSimpleName();
    private final Context mContext;
    private static DatabaseManager INSTANCE;
    private DatabaseHelper databaseHelper;

    private Dao<UserItemDB, Long> userItemDao;
    private static String INDEX = "index";
    private static String NAME = "name";
    private static String AGE = "age";
    private static String ID = "id";

    public DatabaseManager(Context mContext) {
        Log.i(TAG, "DatabaseManager");

        this.mContext = mContext;

        databaseHelper = OpenHelperManager.getHelper(mContext, DatabaseHelper.class);

        try {
            userItemDao = databaseHelper.getUserItemDBS();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }


    public static DatabaseManager getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new DatabaseManager(context);
        }
        return INSTANCE;
    }


    public void releaseDB() {
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
            INSTANCE = null;
        }
    }

    public int clearAllData() {
        if (databaseHelper == null) {
            return -1;
        }
        try {
            databaseHelper.clearTable();
            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public boolean isUserExisting(String index) {
        QueryBuilder queryBuilder = userItemDao.queryBuilder();
        boolean flag = false;

        try {
            if (queryBuilder.where().eq(INDEX, index).countOf() > 0) {
                flag = true;
            } else {
                flag = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }

    public int insertUserItem(UserItemDB userItemDB, boolean isEdit) {
        int count = 0;

        try {
            UpdateBuilder updateBuilder = userItemDao.updateBuilder();
            String index = userItemDB.getIndex() != null ? userItemDB.getIndex() : "";
            String name = userItemDB.getName() != null ? userItemDB.getName() : "";
            String age = userItemDB.getAge() != null ? userItemDB.getAge() : "";

            if (userItemDao == null) {
                return -1;
            }

            if (isUserExisting(index)) {
                Log.i(TAG, "this user exist");
                count = 1;

                if (isEdit) {
                    deleteUser(index);
                    userItemDao.create(userItemDB);
                }
            } else {
                count = 0;
                userItemDao.create(userItemDB);
            }
            return count;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }


    }

    public int deleteUser(String index) {

        try {
            if (userItemDao == null) return -1;
            DeleteBuilder deleteBuilder = userItemDao.deleteBuilder();

            if (index != null || !index.isEmpty()) {
                deleteBuilder.where().eq(INDEX, index);
            }
            deleteBuilder.delete();
            Log.i(TAG,"user deleted");
            return 0;

        } catch (SQLException e) {
            e.printStackTrace();

            return -1;
        }
    }

    public List<UserItemDB> getAllUsers() {
        try {
            if (userItemDao == null) {
                return null;
            }
            return userItemDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}





















