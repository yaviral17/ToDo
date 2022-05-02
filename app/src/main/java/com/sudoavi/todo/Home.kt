package com.sudoavi.todo

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.sudoavi.todo.data.Task
import com.sudoavi.todo.data.TaskDatabase
import kotlinx.android.synthetic.main.activity_add_task.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch



@DelicateCoroutinesApi
class Home : AppCompatActivity() {

    private lateinit var database : TaskDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val animDrawable = backgroud_home.background as AnimationDrawable
        animDrawable.setEnterFadeDuration(1000)
        animDrawable.setExitFadeDuration(3000)
        animDrawable.start()

        database = Room.databaseBuilder(applicationContext,TaskDatabase::class.java,"taskDB").build()

        setList()
        setRecycler()


        delete_task_btn.setOnClickListener {
            GlobalScope.launch {
                database.taskDao().deleteAll()
            }
            TaskObject.deleteAll()
            setRecycler()
        }

//
//        Add_task_btn.setOnClickListener {
//            startActivity(Intent(this,AddTask::class.java))
//            finish()
//        }

        addTaskButton.setOnClickListener {
            startActivity(Intent(this,AddTask::class.java))
        }


    }

    private fun setRecycler(){

        Recycler_view_.adapter = Adaptor(TaskObject.getAllData())
        Recycler_view_.layoutManager = LinearLayoutManager(this)
    }

    private fun setList(){
        GlobalScope.launch{
            TaskObject.deleteAll()
            for(i in database.taskDao().getAllTask()){
                TaskObject.setData(i.id,i.Title,i.Description,i.Due_Date,i.Due_Time,i.Priority)
            }
        }
    }

}