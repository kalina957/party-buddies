package com.example.partybuddies.Fragments

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.partybuddies.FindPartiesFragment
import kotlinx.android.synthetic.main.activity_add_party.*
import java.text.SimpleDateFormat
import java.util.*

class DatePickerFragment2(f: FindPartiesFragment): DialogFragment(), DatePickerDialog.OnDateSetListener {

    val t = f

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        return DatePickerDialog(requireActivity(), this, year, month, day)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val month = month + 1
        var days = dayOfMonth.toString()
        if (dayOfMonth < 10){
            days = "0${dayOfMonth}"
        }
            t.t = "$year-$month-$days"
        t.refreshList()
    }
}