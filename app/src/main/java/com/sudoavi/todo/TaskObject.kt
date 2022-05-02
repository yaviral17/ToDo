package com.sudoavi.todo

import android.icu.text.CaseMap
import com.sudoavi.todo.data.Task

object TaskObject {

        var listdata = mutableListOf<Task>()


    fun setData(id:Int,Title:String,Discription:String,Date:String,Time:String,Priority:String){
        listdata.add(Task(id,Title,Discription,Date,Time,Priority))
    }

    fun updateList(list_tsk : List<Task>){
        listdata.clear()
        listdata = list_tsk as MutableList<Task>

    }

    fun getAllData():List<Task>{
        return listdata
    }

    fun deleteAll(){
        listdata.clear()
    }

    fun deleteOneTask(id:Int):Boolean{
        for (i in 0..listdata.size){
            if (listdata[i].id == id){
                listdata.removeAt(i)
                return true
            }
        }
        return false
    }

    fun getData(taskid:Int):Task{
        lateinit var tsk:Task
        for(i in listdata){
            if (i.id == taskid){
                tsk = i
                break
            }
        }
        return tsk
    }

}