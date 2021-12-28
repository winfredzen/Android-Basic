package com.example.listmaster.listcategory

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

/**
 *
 * create by wangzhen 2021/12/27
 */

@Dao
interface ListCategoryDao {

    @Query("SELECT * FROM list_categories")
    fun getAll(): List<ListCategory>

    @Insert
    fun insertAll(vararg listCategories: ListCategory)

}