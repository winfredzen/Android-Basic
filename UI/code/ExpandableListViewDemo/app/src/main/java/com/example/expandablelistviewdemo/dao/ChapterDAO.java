package com.example.expandablelistviewdemo.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.expandablelistviewdemo.bean.Chapter;
import com.example.expandablelistviewdemo.bean.ChapterItem;

import java.util.ArrayList;
import java.util.List;

/**
 * create by wangzhen 2021/8/1
 */
public class ChapterDAO {
    public List<Chapter> loadFromDb(Context context) {

        ChapterDbHelper dbHelper = ChapterDbHelper.getInstance(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        List<Chapter> chapterList = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + Chapter.TABLE_NAME, null);
        Chapter chapter = null;
        while (cursor.moveToNext()) {
            chapter = new Chapter();
            int id = cursor.getInt(cursor.getColumnIndex(Chapter.COL_ID));
            String name = cursor.getString(cursor.getColumnIndex(Chapter.COL_NAME));
            chapter.setId(id);
            chapter.setName(name);
            chapterList.add(chapter);
        }
        cursor.close();

        ChapterItem chapterItem = null;
        for (Chapter parent : chapterList) {
            int pid = parent.getId();
            //rawQuery 传入参数
            cursor = db.rawQuery("SELECT * FROM " + ChapterItem.TABLE_NAME + " WHERE " + ChapterItem.COL_PID + " = ?",
                    new String[] {pid + ""});
            while (cursor.moveToNext()) {
                chapterItem = new ChapterItem();
                int id = cursor.getInt(cursor.getColumnIndex(chapterItem.COL_ID));
                String name = cursor.getString(cursor.getColumnIndex(chapterItem.COL_NAME));
                chapterItem.setId(id);
                chapterItem.setName(name);
                parent.addChild(chapterItem);
            }
            cursor.close();
        }

        return chapterList;
    }

    public void insert2Db(Context context, List<Chapter> chapters) {
        if (context == null) {
            throw  new IllegalArgumentException("context can not be null");
        }
        if (chapters == null || chapters.isEmpty()) {
            return;
        }

        ChapterDbHelper dbHelper = ChapterDbHelper.getInstance(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.beginTransaction();

        ContentValues contentValues = null;
        //插入数据
        for (Chapter chapter : chapters) {
            contentValues = new ContentValues();
            contentValues.put(Chapter.COL_ID, chapter.getId());
            contentValues.put(Chapter.COL_NAME, chapter.getName());

            db.insertWithOnConflict(Chapter.TABLE_NAME,
                    null,
                    contentValues,
                    SQLiteDatabase.CONFLICT_REPLACE);

            List<ChapterItem> children = chapter.getChildren();

            for (ChapterItem chapterItem : children) {
                contentValues = new ContentValues();
                contentValues.put(ChapterItem.COL_ID, chapterItem.getId());
                contentValues.put(ChapterItem.COL_NAME, chapterItem.getName());
                contentValues.put(ChapterItem.COL_PID, chapterItem.getPid());

                db.insertWithOnConflict(ChapterItem.TABLE_NAME,
                        null,
                        contentValues,
                        SQLiteDatabase.CONFLICT_REPLACE);
            }

        }

        db.setTransactionSuccessful();
        db.endTransaction();

    }
}
