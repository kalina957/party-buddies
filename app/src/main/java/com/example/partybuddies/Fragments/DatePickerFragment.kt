package com.example.partybuddies.Fragments

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.activity_add_party.*
import java.util.*

class DatePickerFragment(date: TextView): DialogFragment(), DatePickerDialog.OnDateSetListener {

    val date = date

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

        this.date.text = "$year-$month-$days"
    }
}