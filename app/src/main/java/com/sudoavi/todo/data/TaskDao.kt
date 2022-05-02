package com.sudoavi.todo.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TaskDao {

    @Insert
    suspend fun InsertTask(task:Task)

    @Update
    suspend fun UpadteTask(task:Task)

    @Delete
    suspend fun DeleteTask(task:Task)

    @Query("DELETE from task_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM task_table")
    fun GetTask() : LiveData<List<Task>>


    @Query("SELECT * FROM task_table")
    fun getAllTask() : List<Task>



}