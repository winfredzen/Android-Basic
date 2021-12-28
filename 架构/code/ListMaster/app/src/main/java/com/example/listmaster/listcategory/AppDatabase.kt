package com.example.listmaster.listcategory

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 *
 * create by wangzhen 2021/12/27
 */

@Database(entities = [ListCategory::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun listCategoryDao(): ListCategoryDao

}