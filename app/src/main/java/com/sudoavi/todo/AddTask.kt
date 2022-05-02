package com.sudoavi.todo

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.room.Room
import com.google.android.material.slider.Slider
import com.sudoavi.todo.data.Task
import com.sudoavi.todo.data.TaskDatabase
import kotlinx.android.synthetic.main.activity_add_task.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AddTask : AppCompatActivity(),DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener {

    var day = 0
    var month = 0
    var year = 0
    var hour = 0
    var minute = 0

    private lateinit var database:TaskDatabase

    var savedDay = 0
    var savedMonth = 0
    var savedYear = 0
    var savedhour = 0
    var savedMinute = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        pickdate()

        database = Room.databaseBuilder(applicationContext, TaskDatabase::class.java,"taskDB").build()


//        Toast.makeText(this, "$title_ $disc $date_ $time_ $prior_", Toast.LENGTH_SHORT).show()

        Add_task_btn.setOnClickListener {
            val title_ = title_usr.text.toString()
            val disc = discription_usr.text.toString()
            val date_ = set_date.text.toString()
            val time_ = set_time.text.toString()
            val prior_ = priority_status.text.toString()
            if(chackValues(title_,disc,date_,time_)){
//                val user_task_data = arrayOf( title_usr.text.toString() , discription_usr.text.toString(),set_date.text.toString(),set_time.text.toString(),priority_status.text.toString())
                GlobalScope.launch {
                    database.taskDao().InsertTask(Task(0,title_,disc,date_,time_,prior_))
                    for (item in database.taskDao().getAllTask()){
                        if(item.Description == disc && item.Title == title_ && item.Due_Date == date_){
                            TaskObject.setData(item.id,item.Title,item.Description,item.Due_Date,item.Due_Time,item.Priority)
                        }
                    }
                }



                startActivity(Intent(this,Home::class.java))
                finish()
            }else{
                Log.i(TAG, "In else");
                Toast.makeText(this, "Enter All The Details !", Toast.LENGTH_SHORT).show()
            }
        }

        // slider management
        priority_slider.addOnSliderTouchListener(object : Slider.OnSliderTouchListener{
            @SuppressLint("RestrictedApi")
            override fun onStartTrackingTouch(slider: Slider) {

            }
            @SuppressLint("RestrictedApi")
            override fun onStopTrackingTouch(slider: Slider) {
                if (slider.value.toDouble() == 0.0){
                    priority_status.text = "Low"
                }
                else if (slider.value.toDouble() == 1.0){
                    priority_status.text = "Medium"
                }
                else{
                    priority_status.text="High"
                }
            }
        })


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
        val sdf = SimpleDateFormat(myformate,Locale.UK)
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

