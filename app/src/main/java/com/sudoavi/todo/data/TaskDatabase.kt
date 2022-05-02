package com.sudoavi.todo.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Task::class], exportSchema = false, version = 1)
abstract class TaskDatabase: RoomDatabase() {
    abstract fun taskDao() : TaskDao

//    companion object{
//        @Volatile
//        private var INSTANCE : TaskDatabase? = null
//
//        fun getDatabase(context : Context):TaskDatabase{
//            if (INSTANCE == null) {
//                synchronized(this) {
//                    INSTANCE = Room.databaseBuilder(
//                        context.applicationContext,
//                        TaskDatabase::class.java,
//                        "taskDB"
//                    ).build()
//                }
//            }
//            return INSTANCE!!
//        }
//    }

}