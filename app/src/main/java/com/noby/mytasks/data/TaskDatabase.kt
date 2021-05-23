package com.noby.mytasks.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.noby.mytasks.di.AppModule
import com.noby.mytasks.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

/**
 * Created by Ahmed Noby Ahmed on 5/19/21.
 */

@Database (entities = [Task::class] , version = 1)
  abstract class TaskDatabase :RoomDatabase() {

    abstract fun taskDao () :TaskDao

    class  Callback @Inject constructor(
      private  val database: Provider<TaskDatabase>,
      @ApplicationScope private  val applicationScope : CoroutineScope
    ):RoomDatabase.Callback (){

      override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        // add any operation need after create database

        val dao =  database.get().taskDao()

        applicationScope.launch {

          dao.insert(Task("finish Task Course"))
          dao.insert(Task("Update Resume "))
          dao.insert(Task("Start Work Hard", completed = true))
          dao.insert(Task("play pubge"))
          dao.insert(Task("Start in Kotlin" ,  imaportant = true))
         // dao.insert(Task("Start in Kotlin" ,  imaportant = true))
          dao.insert(Task("Assign New task"))
          dao.insert(Task("Start in New  Course"))


        }

      }
    }

}