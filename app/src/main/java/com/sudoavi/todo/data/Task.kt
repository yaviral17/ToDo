package com.sudoavi.todo.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_table")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val Title:String,
    val Description:String,
    val Due_Date:String,
    val Due_Time:String,
    val Priority:String
)
