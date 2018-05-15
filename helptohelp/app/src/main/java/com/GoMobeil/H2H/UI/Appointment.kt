package com.GoMobeil.H2H.UI

import android.Manifest
import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.design.widget.BottomSheetDialog
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.*
import com.GoMobeil.H2H.*
import com.GoMobeil.H2H.Adapters.AddressAdapter
import com.GoMobeil.H2H.Adapters.AnsAdapter
import com.GoMobeil.H2H.Extensions.toast
import com.GoMobeil.H2H.Models.AddressModel
import com.GoMobeil.H2H.Models.ServicesModel
import com.GoMobeil.H2H.Services.CalendarView
import com.GoMobeil.H2H.Services.CustomServices
import com.GoMobeil.H2H.Services.GPSTracker
import com.GoMobeil.H2H.UI.VendorList.Companion.zipcode
import com.beust.klaxon.JsonObject
import com.sdsmdg.tastytoast.TastyToast
import kotlinx.android.synthetic.main.appoint.*
import kotlinx.android.synthetic.main.appoint_new.*
import kotlinx.android.synthetic.main.bottomsheet_timepicker.*
import kotlinx.android.synthetic.main.service_qa.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by Admin on 21-02-18.
 */
class Appointment : BaseActivity() {
    override lateinit var context: Context
    override lateinit var activity: Activity
    var service_id: Int? = null
    var service_name: String? = null
    lateinit var alertdialog: android.support.v7.app.AlertDialog
    /* var lsAddLine1: String = ""
     var lsAddLine2: String = ""
     var lsLocation: String? = ""
     var lsCity: String = ""
     var lsState: String = ""
     var lsPincode: String = ""
     var lsCompleteAddress: String = ""*/
    var lsMessage = ""
    var pincode: String? = ""

    var city: String? = ""
    var state: String? = ""
    var lsStartTime: String? = ""
    var lsEndTime: String? = ""

    var lsaddline2: String = ""
    var alQuestionsArray: ArrayList<String> = ArrayList()
    var alAnswersArray: ArrayList<String> = ArrayList()
    var alAnsIdsArray: ArrayList<String> = ArrayList()
    var alQueIdsArray: ArrayList<String> = ArrayList()
    lateinit var model: AddressModel
    lateinit var gps: GPSTracker

    var liCurrentQuestion = 0;

    var lsCurrentDate :String? = null
    lateinit var dialog: BottomSheetDialog
    lateinit var firstTimePicker: TimePicker
    var lsCurrent: String = "";
    var lsTime: String = "";
    lateinit var tvDone: TextView
    lateinit var tvCancel: TextView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.appoint_new)

        context = this
        activity = this

        initLayout()
    }



    @RequiresApi(Build.VERSION_CODES.O)
    fun initLayout() {
        service_id = getIntent().getIntExtra(ServicesModel.SRV_ID, 0)
        service_name = getIntent().getStringExtra(ServicesModel.SRV_NAME)
        alQuestionsArray = getIntent().getStringArrayListExtra("Questions")
        alAnswersArray = getIntent().getStringArrayListExtra("Answers")
        alAnsIdsArray = getIntent().getStringArrayListExtra("AnsIds")
        alQueIdsArray = getIntent().getStringArrayListExtra("QueIds")

    /*    val current = LocalDateTime.now()

        val formatter = DateTimeFormatter.BASIC_ISO_DATE
        lsCurrentDate = current.format(formatter)

        println("Current Date is: $lsCurrentDate")
        tvNewDateSchedule.setText(lsCurrentDate)*/


        setTitle("Appointment Schedule")





        init_modal_bottomsheet();

        cbNewNext.setOnClickListener {
            if (tvNewDateSchedule.text.toString().length > 0 && tvNewStartTime.text.toString().length > 0 && tvNewEndTime.text.toString().length > 0) {
                if (App.prefs!!.user.equals(null) || App.prefs!!.user.equals("")) {
                    val intent = Intent(context, MainActivity::class.java)
                    intent.putExtra(ServicesModel.SRV_ID, service_id!!)
                    intent.putExtra(ServicesModel.SRV_NAME, service_name!!)
                    intent.putExtra("date", tvNewDateSchedule.text.toString())
                    intent.putExtra("Questions", alQuestionsArray)
                    intent.putExtra("Answers", alAnswersArray)
                    intent.putExtra("AnsIds", alAnsIdsArray)
                    intent.putExtra("QueIds", alQueIdsArray)

                    intent.putExtra("login",2)
                    intent.putExtra("startTime", tvNewStartTime.text.toString())
                    intent.putExtra("endTime", tvNewEndTime.text.toString())

                    startActivity(intent)

                } else {
                    val intent = Intent(context, AddressFirst::class.java)

                    intent.putExtra(ServicesModel.SRV_ID, service_id!!)
                    intent.putExtra(ServicesModel.SRV_NAME, service_name!!)
                    // intent.putExtra(StaticRefs.CUST_ID,1)
                    intent.putExtra("date", tvNewDateSchedule.text.toString())
                    intent.putExtra("Questions", alQuestionsArray)
                    intent.putExtra("Answers", alAnswersArray)
                    intent.putExtra("AnsIds", alAnsIdsArray)
                    intent.putExtra("QueIds", alQueIdsArray)
                    intent.putExtra("startTime", tvNewStartTime.text.toString())
                    intent.putExtra("endTime", tvNewEndTime.text.toString())
                    startActivity(intent)
                }
            } else {
                TastyToast.makeText(context, "Please select preffered Date/Time", 30, TastyToast.ERROR).show()
            }

        }

        cbNewPrevious.setOnClickListener {
            onBackPressed()

           /* val intet = Intent(context,ServiceQuestions::class.java)
            intet.putStringArrayListExtra("Questions", alQuestionsArray)
            intet.putStringArrayListExtra("Answers", alAnswersArray)
            intet.putStringArrayListExtra("AnsIds", alAnsIdsArray)
            intet.putStringArrayListExtra("QueIds", alQueIdsArray)
            intet.putExtra("KEY","1")
            startActivity(intet)*/



        }
        tvNewDateSchedule.setOnClickListener(this)
        cvNewDateSchedule.setOnClickListener(this)
        ivNewCalendar.setOnClickListener(this)

        tvNewStartTime.setOnClickListener(View.OnClickListener {
            dialog.show();
            lsCurrent = "STARTTIME";
        })
        tvNewEndTime.setOnClickListener(View.OnClickListener {
            if (tvNewStartTime.text.toString().length > 0) {
                dialog.show();
                lsCurrent = "ENDTIME";
            } else {
                // Toast.makeText(getApplicationContext(), "Please select start time first.", Toast.LENGTH_SHORT).show();
                TastyToast.makeText(context, "Please select start time first.", 30, TastyToast.WARNING).show()
            }
        })
    }



    fun init_modal_bottomsheet() {
        val modalbottomsheet = layoutInflater.inflate(R.layout.bottomsheet_timepicker, null)

        dialog = BottomSheetDialog(this)
        dialog.setContentView(modalbottomsheet)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)

        firstTimePicker = modalbottomsheet.findViewById<TimePicker>(R.id.simpleTimePicker)
        tvDone = modalbottomsheet.findViewById<TextView>(R.id.tvDone)
        tvCancel = modalbottomsheet.findViewById<TextView>(R.id.tvCancel)

        tvDone.setOnClickListener(this)
        tvCancel.setOnClickListener(this)

        firstTimePicker.setIs24HourView(false)
        firstTimePicker.setOnTimeChangedListener(TimePicker.OnTimeChangedListener { view, hourOfDay, minute ->

            val hour: String
            val min: String
            if (hourOfDay >= 10) {
                hour = hourOfDay.toString()
            } else {
                hour = "0" + hourOfDay.toString()
            }

            if (minute >= 10) {
                min = minute.toString()
            } else {
                min = "0" + minute.toString()
            }

            if (lsCurrent == "STARTTIME") {
                lsTime = "$hour:$min"
            } else if (lsCurrent == "ENDTIME") {
                lsTime = "$hour:$min"
            }
        })

    }


    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.tvNewDateSchedule, R.id.cvNewDateSchedule, R.id.ivNewCalendar -> {
                setupDate(tvNewDateSchedule, context)
            }

            R.id.tvDone -> {
                if (lsCurrent.equals("STARTTIME")) {
                    tvNewStartTime.text = lsTime;
                    dialog.hide();
                } else if (lsCurrent.equals("ENDTIME")) {
                    if (CustomServices.TimeValidator(tvNewStartTime.text.toString(), lsTime) == true) {
                        tvNewEndTime.text = lsTime;
                        dialog.hide();
                    } else {
                        //Toast.makeText(getApplicationContext(), " End time should be greater than start time", Toast.LENGTH_SHORT).show();
                        TastyToast.makeText(context, "End time should be greater than start time", 30, TastyToast.WARNING).show()
                    }
                }
            }

            R.id.tvCancel -> {
                lsTime = "";
                dialog.hide();
            }
        }
    }

    fun setupDate(tvCalDate: TextView, context: Context) {
        val calendarPop = com.GoMobeil.H2H.Services.CalendarView(activity, context, true)
        var lsDate: String? = tvCalDate.getText().toString()
        if (lsDate == null || lsDate == "") {
            val sdf = SimpleDateFormat("dd/MM/yyyy")
           // val sdf = SimpleDateFormat("yyyy/MM/dd")
            lsDate = sdf.format(Date()).toString()
          // var  lsNewDate = lsDate.substring(0)
        }

        val mOnDateChanged = object : CalendarView.CVOnDateChanged {
            override fun CVDateChanged(tvDate: TextView, lsDate: String) {
                tvCalDate.setText(lsDate)

                tvNewDateSchedule.setText(tvCalDate.getText().toString())
            }
        }
        calendarPop.setOnDateChanged(mOnDateChanged)
        calendarPop.setupPopUp(tvCalDate, lsDate, context.resources.getColor(R.color.colorPrimary))
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}