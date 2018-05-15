package com.GoMobeil.H2H.UI

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.RadioButton
import com.GoMobeil.H2H.BaseActivity
import com.GoMobeil.H2H.Extensions.toast
import com.GoMobeil.H2H.Models.ServicesModel
import com.GoMobeil.H2H.R
import com.GoMobeil.H2H.StaticRefs
import kotlinx.android.synthetic.main.service_category.*

/**
 * Created by Admin on 01-03-18.
 */
class ServiceCategory :BaseActivity(){
    override lateinit var activity: Activity
    override lateinit var context: Context

    var service_id:Int? = null
    var service_name : String? = null
    var alQuestionsArray : ArrayList<String> = ArrayList()
    var alAnswersArray : ArrayList<String> = ArrayList()
    var alAnsIdsArray : ArrayList<String> = ArrayList()
    var alQueIdsArray : ArrayList<String> = ArrayList()

    var lsCompleteaddress :String? = null
    var lsStartTime :String? = null
    var lsEndTime :String? = null

    var liAddSrNoOnSelection : Int? = null
    var lsAddLine1OnSelection : String? = ""
    var lsAddLine2OnSelection : String? = ""
    var lsAddCityOnSelection : String? = ""
    var lsAddStateOnSelection : String? = ""
    var lsAddPincodeOnSelection : String? = ""

    var shareNumber:String? = ""
    var message:String = ""

    var dateofappoint :String=""





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.service_category)

        service_id = getIntent().getIntExtra(ServicesModel.SRV_ID, 0)
        service_name = getIntent().getStringExtra(ServicesModel.SRV_NAME)

        alQuestionsArray = getIntent().getStringArrayListExtra("Questions")
        alAnswersArray = getIntent().getStringArrayListExtra("Answers")
        alAnsIdsArray = getIntent().getStringArrayListExtra("AnsIds")
        alQueIdsArray = getIntent().getStringArrayListExtra("QueIds")

        lsCompleteaddress = getIntent().getStringExtra("address")
        lsStartTime = getIntent().getStringExtra("startTime")
        lsEndTime = getIntent().getStringExtra("endTime")
        shareNumber = getIntent().getStringExtra("shareNo")
        message = getIntent().getStringExtra("message")
        dateofappoint = getIntent().getStringExtra("date")
        liAddSrNoOnSelection = getIntent().getIntExtra(StaticRefs.SRNO , 0)
        lsAddLine1OnSelection = getIntent().getStringExtra(StaticRefs.ADD_LINE1)
        lsAddLine2OnSelection = getIntent().getStringExtra(StaticRefs.ADD_LINE2)
        lsAddCityOnSelection = getIntent().getStringExtra(StaticRefs.ADD_CITY)
        lsAddStateOnSelection = getIntent().getStringExtra(StaticRefs.ADD_STATE)
        lsAddPincodeOnSelection = getIntent().getStringExtra(StaticRefs.ADD_PINCODE)

        setTitle("$service_name")


        activity = this
        context = this

        cbContinue.setOnClickListener {

            val cat_value = (rgServiceCat.findViewById<RadioButton>(rgServiceCat.getCheckedRadioButtonId())).text.toString()
          //  toast("you selected : $cat_value")

            val intent = Intent(context,VendorList::class.java)
            intent.putExtra(ServicesModel.SRV_ID, service_id!!)
            intent.putExtra(ServicesModel.SRV_NAME, service_name)
            intent.putExtra("Questions",alQuestionsArray)
            intent.putExtra("Answers", alAnswersArray)
            intent.putExtra("AnsIds", alAnsIdsArray)
            intent.putExtra("QueIds", alQueIdsArray)

            intent.putExtra("address",lsCompleteaddress)
            intent.putExtra("startTime",lsStartTime)
            intent.putExtra("endTime",lsEndTime)
            intent.putExtra(StaticRefs.SRNO , liAddSrNoOnSelection!!)
            intent.putExtra(StaticRefs.ADD_LINE1, lsAddLine1OnSelection)
            intent.putExtra(StaticRefs.ADD_LINE2, lsAddLine2OnSelection)
            intent.putExtra(StaticRefs.ADD_CITY, lsAddCityOnSelection)
            intent.putExtra(StaticRefs.ADD_STATE, lsAddStateOnSelection)
            intent.putExtra(StaticRefs.ADD_PINCODE, lsAddPincodeOnSelection)

            intent.putExtra("shareNo",shareNumber)
            intent.putExtra("message",message)
            intent.putExtra("date",dateofappoint)
            intent.putExtra("sp_category",cat_value)
            startActivity(intent)
        }
    }
}