package com.sudoavi.todo

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.google.android.material.slider.Slider
import com.sudoavi.todo.data.Task
import com.sudoavi.todo.data.TaskDatabase
import kotlinx.android.synthetic.main.activity_add_task.date_and_time_btn
import kotlinx.android.synthetic.main.activity_add_task.discription_usr
import kotlinx.android.synthetic.main.activity_add_task.priority_slider
import kotlinx.android.synthetic.main.activity_add_task.priority_status
import kotlinx.android.synthetic.main.activity_add_task.set_date
import kotlinx.android.synthetic.main.activity_add_task.set_time
import kotlinx.android.synthetic.main.activity_add_task.title_usr
import kotlinx.android.synthetic.main.activity_edit_task.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class EditTask : AppCompatActivity(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener  {


    var day = 0
    var month = 0
    var year = 0
    var hour = 0
    var minute = 0

    private lateinit var database: TaskDatabase
    private var tsk_Id : Int = -1
    private lateinit var gotTsk:Task

    var savedDay = 0
    var savedMonth = 0
    var savedYear = 0
    var savedhour = 0
    var savedMinute = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_task)

        database = Room.databaseBuilder(applicationContext, TaskDatabase::class.java,"taskDB").build()
        pickdate()
        tsk_Id = intent.getIntExtra("id",-1)
        if (tsk_Id!=-1){

            gotTsk = TaskObject.getData(tsk_Id)

            task_id_edit_.setText(gotTsk.id.toString())
            title_usr.setText(gotTsk.Title)
            discription_usr.setText(gotTsk.Description)
            set_date.setText(gotTsk.Due_Date)
            set_time.setText(gotTsk.Due_Time)
            priority_status.setText(gotTsk.Priority)

            Delete_task_btn.setOnClickListener {
                GlobalScope.launch {
                    database.taskDao().DeleteTask(Task(gotTsk.id,gotTsk.Title,gotTsk.Description,gotTsk.Due_Date,gotTsk.Due_Time,gotTsk.Priority))
                    for (item in database.taskDao().getAllTask()){
                        if(item.Description == gotTsk.Description && item.Title == gotTsk.Title && item.Due_Date == gotTsk.Due_Date){
                            TaskObject.setData(item.id,item.Title,item.Description,item.Due_Date,item.Due_Time,item.Priority)
                        }
                    }
                }

                startActivity(Intent(this,Home::class.java))
                finish()
            }


            Edit_task_btn.setOnClickListener {
                val iD = task_id_edit_.text.toString().toInt()
                val title_ = title_usr.text.toString()
                val disc = discription_usr.text.toString()
                val date_ = set_date.text.toString()
                val time_ = set_time.text.toString()
                val prior_ = priority_status.text.toString()
                if(chackValues(title_,disc,date_,time_)){
                    GlobalScope.launch {
                        database.taskDao().UpadteTask(Task(iD,title_,disc,date_,time_,prior_))
                        TaskObject.updateList(database.taskDao().getAllTask())
                    }
                    startActivity(Intent(this,Home::class.java))
                    finish()
                }else{
                    Log.i(ContentValues.TAG, "In else");
                    Toast.makeText(this, "Enter All The Details !", Toast.LENGTH_SHORT).show()
                }
            }

            priority_slider.addOnSliderTouchListener(object : Slider.OnSliderTouchListener{
                @SuppressLint("RestrictedApi")
                override fun onStartTrackingTouch(slider: Slider) {

                }
                @SuppressLint("RestrictedApi")
                override fun onStopTrackingTouch(slider: Slider) {
                    if (slider.value.toDouble() == 0.0){
                        priority_status.text = "Low"
//                        priority_status.setTextColor(Color.parseColor(R.color.Low.toString()))
                    }
                    else if (slider.value.toDouble() == 1.0){
                        priority_status.text = "Medium"
//                        priority_status.setTextColor(Color.parseColor(R.color.Medium.toString()))
                    }
                    else{
                        priority_status.text="High"
//                        priority_status.setTextColor(Color.parseColor(R.color.High.toString()))
                    }
                }
            })
        }




    }
    private fun chackValues(title:String,disc:String,date:String,time:String): Boolean {
        return title.trim(' ').isNotEmpty() &&  disc.trim(' ').isNotEmpty() && date != "dd-mm-yyyy" && time != "hh : mm"

    }


    private fun getDateTimeCalendar(){
        val cal : Calendar = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
        hour = cal.get(Calendar.HOUR)
        minute = cal.get(Calendar.MINUTE)
    }


    private fun pickdate() {
        date_and_time_btn.setOnClickListener {
            DatePickerDialog(this,this,year,month,day).show()
        }

    }

    private fun updateLable(mycalander: Calendar) {
        val myformate = "dd-mm-yyyy"
        val sdf = SimpleDateFormat(myformate, Locale.UK)
        set_date.text = sdf.format(mycalander.time)
    }



    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        savedDay = p3
        savedMonth = p2+1
        savedYear = p1

        getDateTimeCalendar()

        TimePickerDialog(this,this,hour,minute,true).show()
    }

    override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
        savedhour = p1
        savedMinute = p2

        if (savedDay<10){
            if (savedMonth < 10){
                if(savedYear.toString().length == 1){
                    set_date.text = "0${savedDay}-0${savedMonth}-${savedYear}000"
                }else{
                    set_date.text = "0${savedDay}-0${savedMonth}-${savedYear}"
                }
            }else{
                set_date.text = "0${savedDay}-${savedMonth}-${savedYear}"
            }
        }else{
            if (savedMonth < 10){
                if(savedYear.toString().length == 1){
                    set_date.text = "0${savedDay}-0${savedMonth}-${savedYear}000"
                }else {
                    set_date.text = "${savedDay}-0${savedMonth}-${savedYear}"
                }
            }else{
                set_date.text = "${savedDay}-${savedMonth}-${savedYear}"
            }
        }



        if (savedhour<10){
            if (savedMinute < 10){
                set_time.text = "0${savedhour} : 0${savedMinute}"
            }
            else{
                set_time.text = "0${savedhour} : ${savedMinute}"
            }
        }
        else{
            if (savedMinute < 10){
                set_time.text = "${savedhour} : 0${savedMinute}"
            }
            else{
                set_time.text = "${savedhour} : ${savedMinute}"
            }
        }

    }

}