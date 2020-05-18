package com.example.partybuddies.Fragments

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TextView
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class TimePickerFragment(checkStartTime: Boolean, tvwTime: TextView): DialogFragment(), TimePickerDialog.OnTimeSetListener{

    val checkTime = checkStartTime
    val tvwTime = tvwTime

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        return TimePickerDialog(activity, this, hour, minute,true)
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        if (checkTime){
            tvwTime.text = "Start: ${hourOfDay}:${minute}"
        }
        else{
            tvwTime.text = "End: ${hourOfDay}:${minute}"
        }

    }


}