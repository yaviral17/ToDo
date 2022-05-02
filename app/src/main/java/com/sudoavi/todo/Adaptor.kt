package com.sudoavi.todo

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sudoavi.todo.data.Task
import kotlinx.android.synthetic.main.task_view.view.*
import java.util.*

class Adaptor(private var Tasks: List<Task>) : RecyclerView.Adapter<Adaptor.viewHolder>() {
    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tskid = itemView.task_id
        var title = itemView.task_title
        var discription = itemView.task_discription
        var date = itemView.due_status
        var priority_status = itemView.Priority_status__

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val iemView = LayoutInflater.from(parent.context).inflate(R.layout.task_view, parent, false)
        return viewHolder(iemView)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {

//        when(Tasks[position].Priority){
//            "Low"->holder.priority_status.setTextColor(Color.parseColor(R.color.Low.toString()))
//            "Medium"->holder.priority_status.setTextColor(Color.parseColor(R.color.Medium.toString()))
//            "High"->holder.priority_status.setTextColor(Color.parseColor(R.color.High.toString()))
//        }



        holder.title.text = Tasks[position].Title
        if(Tasks[position].Description.length > 16){
        holder.discription.text = "${Tasks[position].Description.subSequence(0,15)}..."}
        else{
            holder.discription.text = Tasks[position].Description
        }
        holder.tskid.text = Tasks[position].id.toString()
        var due_status = ""

        val c = Calendar.getInstance()
        val cHr = c.get(Calendar.HOUR)
        val cMn = c.get(Calendar.MINUTE)
        val cMon = c.get(Calendar.MONTH)
        val cDate = c.get(Calendar.DATE)
        val cyear = c.get(Calendar.YEAR)

        println(Tasks[position].Due_Date)
        val eYear = Tasks[position].Due_Date.subSequence(6, 10).toString().toInt()
        val eMonth = Tasks[position].Due_Date.subSequence(3, 5).toString().toInt()
        val eDate = Tasks[position].Due_Date.subSequence(0, 2).toString().toInt()
        val eHr = Tasks[position].Due_Time.subSequence(0, 2).toString().toInt()
        val eMn = Tasks[position].Due_Time.subSequence(5, 7).toString().toInt()

        if (eYear > cyear) {
            due_status = "Due ${Tasks[position].Due_Date}"
        }else{
            if (eYear == cyear){
                if (eMonth > cMon){
                    due_status = "Due ${Tasks[position].Due_Date}"
                }else{
                    if (eMonth == cMon){
                        if (eDate > cDate){
                            if (eDate + 1 == cDate){
                                due_status = "Due Tomorrow ${Tasks[position].Due_Time}"
                            }else{
                                due_status = "Due ${Tasks[position].Due_Date}"
                            }

                        }
                        else{
                            if (eDate == cDate){
                                if (eHr >= cHr && eMn > cMn){
                                    due_status = "Due today ${Tasks[position].Due_Time}"
                                }
                            }
                        }
                    }
                }
            }else{
                due_status = "Task expired"
            }
        }

//        if (due_status ==  "Task expired")
//            holder.date.setTextColor(Color.parseColor(R.color.TaskExpired.toString()))
        holder.date.text = due_status
        holder.priority_status.text = Tasks[position].Priority

        holder.itemView.Edit_btn_item.setOnClickListener {
            val intent = Intent(holder.itemView.context,EditTask::class.java)
            intent.putExtra("id",Tasks[position].id)
            holder.itemView.context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return Tasks.size
    }
}