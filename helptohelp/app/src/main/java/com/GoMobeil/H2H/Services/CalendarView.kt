package com.GoMobeil.H2H.Services

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.CheckBox
import android.widget.DatePicker
import android.widget.PopupWindow
import android.widget.TextView

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog

import java.util.Calendar


class CalendarView(internal var activity: Activity, internal var context: Context, internal var lbLimit: Boolean) : PopupWindow(), DatePickerDialog.OnDateSetListener {

    private val dateTextView: TextView? = null
    private val modeDarkDate: CheckBox? = null
    private val modeCustomAccentDate: CheckBox? = null
    private val vibrateDate: CheckBox? = null
    private val dismissDate: CheckBox? = null
    private val titleDate: CheckBox? = null
    private val showYearFirst: CheckBox? = null
    private val showVersion2: CheckBox? = null
    private val limitSelectableDays: CheckBox? = null
    private val highlightDays: CheckBox? = null
    lateinit var textView: TextView
    internal var popView: View? = null
    internal var popW: PopupWindow? = null
    lateinit var mOnDateChanged: CVOnDateChanged
    lateinit var dpd: DatePickerDialog


    fun setupPopUp(tvDate: TextView, lsCurrentDate: String?, color: Int) {
        this.textView = tvDate

        var liYear: Int
        var liMonth: Int
        var liDayOfMonth: Int

        val now = Calendar.getInstance()
        liYear = now.get(Calendar.YEAR)
        liMonth = now.get(Calendar.MONTH)
        liDayOfMonth = now.get(Calendar.DAY_OF_MONTH)
        if (lsCurrentDate != null && lsCurrentDate.length > 0) {
            liYear = Integer.valueOf(lsCurrentDate.substring(6, 10))!!
            liMonth = Integer.valueOf(lsCurrentDate.substring(3, 5))!! - 1
            liDayOfMonth = Integer.valueOf(lsCurrentDate.substring(0, 2))!!

        }

        dpd = DatePickerDialog.newInstance(
                this,
                liYear,
                liMonth,
                liDayOfMonth
        )

        dpd.accentColor = color
        dpd.minDate = Calendar.getInstance()
        dpd.show(activity.fragmentManager, "Show Dates")
    }

    override fun onDateSet(view: DatePickerDialog, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        var monthOfYear = monthOfYear
        var date = "You picked the following date: " + dayOfMonth + "/" + ++monthOfYear + "/" + year
        var lsDay: String
        var lsMonth: String
        lsDay = dayOfMonth.toString()
        lsMonth = monthOfYear.toString()
        if (dayOfMonth < 10) {
            lsDay = "0$dayOfMonth"
        }
        if (monthOfYear < 10) {
            lsMonth = "0$monthOfYear"
        }
        date = "$year/$lsMonth/$lsDay"
        //date = "$lsDay/$lsMonth/$year"
        mOnDateChanged.CVDateChanged(textView, date)
        textView.text = date
        dpd.dismiss()
    }

    interface CVOnDateChanged {
        fun CVDateChanged(tvDate: TextView, lsDate: String)
    }

    fun setOnDateChanged(mDateChanged: CVOnDateChanged) {
        this.mOnDateChanged = mDateChanged
    }


}


