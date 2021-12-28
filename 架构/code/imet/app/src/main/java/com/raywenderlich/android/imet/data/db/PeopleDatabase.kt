/*
 *
 *  * Copyright (c) 2018 Razeware LLC
 *  *
 *  * Permission is hereby granted, free of charge, to any person obtaining a copy
 *  * of this software and associated documentation files (the "Software"), to deal
 *  * in the Software without restriction, including without limitation the rights
 *  * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  * copies of the Software, and to permit persons to whom the Software is
 *  * furnished to do so, subject to the following conditions:
 *  *
 *  * The above copyright notice and this permission notice shall be included in
 *  * all copies or substantial portions of the Software.
 *  *
 *  * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 *  * distribute, sublicense, create a derivative work, and/or sell copies of the
 *  * Software in any work that is designed, intended, or marketed for pedagogical or
 *  * instructional purposes related to programming, coding, application development,
 *  * or information technology.  Permission for such use, copying, modification,
 *  * merger, publication, distribution, sublicensing, creation of derivative works,
 *  * or sale is expressly withheld.
 *  *
 *  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  * THE SOFTWARE.
 *
 *
 */
 
package com.raywenderlich.android.imet.data.db

import android.app.Application
import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.os.AsyncTask
import com.raywenderlich.android.imet.data.model.People
import com.raywenderlich.android.imet.data.net.PeopleInfoProvider

// 1
@Database(entities = [People::class], version = 1)
abstract class PeopleDatabase : RoomDatabase() {

  abstract fun peopleDao(): PeopleDao

  // 2
  companion object {
    private val lock = Any()
    //数据库名称
    private const val DB_NAME = "People.db"
    //单例
    private var INSTANCE: PeopleDatabase? = null

    // 3
    fun getInstance(application: Application): PeopleDatabase {
      synchronized(PeopleDatabase.lock) {
        if (PeopleDatabase.INSTANCE == null) {
          PeopleDatabase.INSTANCE =
              Room.databaseBuilder(application, PeopleDatabase::class.java, PeopleDatabase.DB_NAME)
                  .allowMainThreadQueries()
                  .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                      super.onCreate(db)
                      PeopleDatabase.INSTANCE?.let {
                        PeopleDatabase.prePopulate(it, PeopleInfoProvider.peopleList)
                      }
                    }
                  })
                  .build()
        }
        return PeopleDatabase.INSTANCE!!
      }
    }

    //导入数据到数据库中
    fun prePopulate(database: PeopleDatabase, peopleList: List<People>) {
      for (people in peopleList) {
        AsyncTask.execute { database.peopleDao().insert(people) }
      }
    }

  }

}
