package com.GoMobeil.H2H.UI

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.GoMobeil.H2H.*
import com.GoMobeil.H2H.Models.ServicesModel
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.sdsmdg.tastytoast.TastyToast
import kotlinx.android.synthetic.main.contact_share.*
import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by Admin on 22-02-18.
 */
class ContactShare : BaseActivity() {

    override lateinit var context : Context
    override lateinit var activity : Activity

     lateinit var shareNumber :String
     lateinit var message : String

    var service_id:Int? = null
    var service_name : String? = null
    var alQuestionsArray : ArrayList<String> = ArrayList()
    var alAnswersArray : ArrayList<String> = ArrayList()
    var alAnsIdsArray : ArrayList<String> = ArrayList()
    var alQueIdsArray : ArrayList<String> = ArrayList()

    var alFinalQueAnsArray: ArrayList<String> = ArrayList()

    var dateofappoint :String?=null
    var lsCompleteaddress :String? = null
    var lsStartTime :String? = null
    var lsEndTime :String? = null

    var lsAddLine1: String = ""
    var lsAddLine2: String = ""
    var lsLocation: String? = ""
    var lsCity: String = ""
    var lsState: String = ""
    var lsPincode: String = ""

    var liAddSrNoOnSelection : Int? = null
    var lsAddLine1OnSelection : String? = ""
    var lsAddLine2OnSelection : String? = ""
    var lsAddCityOnSelection : String? = ""
    var lsAddStateOnSelection : String? = ""
    var lsAddPincodeOnSelection : String? = ""
    companion object {

        internal var TAG = "ContactShare"
    }

    var jsonArrMain: JSONArray = JSONArray()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.contact_share)
        context = this
        activity = this

        service_id = getIntent().getIntExtra(ServicesModel.SRV_ID, 0)
        service_name = getIntent().getStringExtra(ServicesModel.SRV_NAME)
        alQuestionsArray = getIntent().getStringArrayListExtra("Questions")
        alAnswersArray = getIntent().getStringArrayListExtra("Answers")
        alAnsIdsArray = getIntent().getStringArrayListExtra("AnsIds")
        alQueIdsArray = getIntent().getStringArrayListExtra("QueIds")
        dateofappoint = getIntent().getStringExtra("date")
        lsCompleteaddress = getIntent().getStringExtra("address")
        lsLocation = getIntent().getStringExtra("location")
        lsStartTime = getIntent().getStringExtra("startTime")
        lsEndTime = getIntent().getStringExtra("endTime")

        liAddSrNoOnSelection = getIntent().getIntExtra(StaticRefs.SRNO , 0)
        lsAddLine1OnSelection = getIntent().getStringExtra(StaticRefs.ADD_LINE1)
        lsAddLine2OnSelection = getIntent().getStringExtra(StaticRefs.ADD_LINE2)
        lsAddCityOnSelection = getIntent().getStringExtra(StaticRefs.ADD_CITY)
        lsAddStateOnSelection = getIntent().getStringExtra(StaticRefs.ADD_STATE)
        lsAddPincodeOnSelection = getIntent().getStringExtra(StaticRefs.ADD_PINCODE)




        setTitle("$service_name")

        if (toggleButton2.isChecked){
            message = "Y"


        }
        else{
            message = "N"
        }

        if (toggleButton1.isChecked){
            shareNumber ="Y"
            cbNext.visibility= View.GONE
            cbBook.visibility = View.VISIBLE
        }
        else{
            shareNumber = "N"
        }


        toggleButton1.setOnClickListener {

            if (toggleButton1.isChecked){

                shareNumber = "Y"
                cbNext.visibility = View.GONE
                cbBook.visibility = View.VISIBLE

            }
            else{
                shareNumber = "N"

                cbNext.visibility = View.VISIBLE
                cbBook.visibility = View.GONE

            }
        }
        toggleButton2.setOnClickListener {
            if (toggleButton1.isChecked){
                message = "Y"
            }
            else{
                message = "N"
            }
        }



        cbNext.setOnClickListener {

            if (toggleButton1.isChecked==false){
                val intent = Intent(context, ServiceCategory::class.java)

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
                startActivity(intent)

            }

            else{
              //  Toast.makeText(context,"Plz select No and then press yes",Toast.LENGTH_LONG).show()
            }


        }

        for (i in 0..alQuestionsArray.size - 1) {
            var jsonObjMain = JSONObject()

            var jsonArr1 = JSONArray()
            var jsonObj2 = JSONObject()
            jsonObj2!!.put("srd_aId", alAnsIdsArray.get(i))
            jsonObj2!!.put("srd_answer", alAnswersArray.get(i))
            jsonArr1.put(jsonObj2)

            jsonObjMain.put("srd_qId", alQueIdsArray.get(i))
            jsonObjMain.put("ans", jsonArr1)

            jsonArrMain.put(jsonObjMain)
        }

        println("'data' : ${jsonArrMain.toString()}")

        for (i in 0..alQuestionsArray.size - 1) {
            alFinalQueAnsArray.add(alQuestionsArray.get(i) + alAnswersArray.get(i))
        }


        cbPreview.setOnClickListener {
            val intent = Intent(context,Preview::class.java)
            intent.putExtra(ServicesModel.SRV_ID, service_id!!)
            intent.putExtra("Questions",alQuestionsArray)
            intent.putExtra("Answers", alAnswersArray)
            intent.putExtra("AnsIds", alAnsIdsArray)
            intent.putExtra("QueIds", alQueIdsArray)
            intent.putExtra("date",dateofappoint)
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
            intent.putExtra(ServicesModel.SRV_NAME, service_name)
            startActivity(intent)
        }

        cbBook.setOnClickListener {

            saveRecord()
        }


    }

    fun saveRecord() {

        Fuel.post(StaticRefs.SERVICEREQUEST, listOf((StaticRefs.TOKEN to App.prefs!!.token),
                (StaticRefs.SR_CUSTID to App.prefs!!.cust_id),
                (StaticRefs.SR_SRVID to service_id),
                (StaticRefs.SP_ID to ""),
                (StaticRefs.SR_SRVTYPEID to ""),
                (StaticRefs.SR_CUSTBUDGET to ""),
                (StaticRefs.SR_LOCATIONID to liAddSrNoOnSelection),
                (StaticRefs.SR_PLANNEDDATE to dateofappoint),
                (StaticRefs.SR_PLANNEDTIME to lsStartTime),
                (StaticRefs.SR_LOCATION to lsAddCityOnSelection),
                (StaticRefs.SR_CREATEDBY to prefs.fname),
                (StaticRefs.SR_ENABLEMESSAGING to message),
                (StaticRefs.SR_SHAREMOBILENO to shareNumber),
                (StaticRefs.DATA to jsonArrMain.toString())
        )).responseJson()

        { request,
          response,
          result ->
            result.fold({ d ->
                parseResponse(result.get().content)

            }, { err ->
                TastyToast.makeText(context, "Internet Network Down", TastyToast.LENGTH_LONG, TastyToast.ERROR)
                Log.d(TAG,"Error is :"+err.toString())
            })


        }


    }

    fun parseResponse(response: String) {

        val json = JSONObject(response);
        if (json.has(StaticRefs.STATUS) && json.getString(StaticRefs.STATUS) != null) {
            var message = "";
            if (json.has(StaticRefs.MESSAGE) && json.getString(StaticRefs.MESSAGE) != null) {
                message = json.getString(StaticRefs.MESSAGE)
            }
            if (json.getString(StaticRefs.STATUS).equals(StaticRefs.FAILED)) {
                //NGS CustomToast
               // Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                TastyToast.makeText(context,message,TastyToast.LENGTH_LONG,TastyToast.ERROR).show()
            } else {

             //   Toast.makeText(context, "Record Inserted Successfully", Toast.LENGTH_LONG).show()
                TastyToast.makeText(context,"Record Inserted Successfully",TastyToast.LENGTH_LONG,TastyToast.SUCCESS).show()
                val intent = Intent(context, Home::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
        }
    }
}