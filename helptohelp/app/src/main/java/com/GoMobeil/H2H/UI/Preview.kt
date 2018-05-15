package com.GoMobeil.H2H.UI

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.Html
import android.widget.Toast
import com.GoMobeil.H2H.*
import com.GoMobeil.H2H.Adapters.GetDataAdapter
import com.GoMobeil.H2H.App.Companion.prefs
import com.GoMobeil.H2H.Extensions.toast
import com.GoMobeil.H2H.Models.ServicesModel
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.sdsmdg.tastytoast.TastyToast
import kotlinx.android.synthetic.main.preview.*
import org.json.JSONArray
import org.json.JSONObject
import java.net.CacheResponse

/**
 * Created by Admin on 22-02-18.
 */
class Preview : BaseActivity() {
    var service_id: Int? = null
    var service_name: String? = null
    var lsStartTime: String? = null
    var lsEndTime: String? = null
    var lsDateOfAppointment: String? = null
    var lsShareContact: String? = null
    var lsMessage: String? = null
    var alQuestionsArray: ArrayList<String> = ArrayList()
    var alAnswersArray: ArrayList<String> = ArrayList()
    var alFinalQueAnsArray: ArrayList<String> = ArrayList()
    var alAnsIdsArray: ArrayList<String> = ArrayList()
    var alQueIdsArray: ArrayList<String> = ArrayList()
    var jsonArrMain: JSONArray = JSONArray()
    var lbBookClicked: Boolean = false

    var liAddSrNoOnSelection: Int? = null
    var lsAddLine1OnSelection: String? = ""
    var lsAddLine2OnSelection: String? = ""
    var lsAddCityOnSelection: String? = ""
    var lsAddStateOnSelection: String? = ""
    var lsAddPincodeOnSelection: String? = ""
    var lssp_id = ""

    var lsFullAdd: String = ""
var lsname= prefs!!.fname
    var lsCompleteAddress: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.preview)

        initLayout()
    }

    fun initLayout() {
        service_id = getIntent().getIntExtra(ServicesModel.SRV_ID, 0)
        service_name = getIntent().getStringExtra(ServicesModel.SRV_NAME)

        lsStartTime = getIntent().getStringExtra("startTime")
        lsEndTime = getIntent().getStringExtra("endTime")
        lsDateOfAppointment = getIntent().getStringExtra("date")

        alQuestionsArray = getIntent().getStringArrayListExtra("Questions")
        alAnswersArray = getIntent().getStringArrayListExtra("Answers")
        alAnsIdsArray = getIntent().getStringArrayListExtra("AnsIds")
        alQueIdsArray = getIntent().getStringArrayListExtra("QueIds")
        lsCompleteAddress = getIntent().getStringExtra("address")

        lsShareContact = getIntent().getStringExtra("shareNo")
        lsMessage = getIntent().getStringExtra("message")

        liAddSrNoOnSelection = getIntent().getIntExtra(StaticRefs.SRNO, 0)
        lsAddLine1OnSelection = getIntent().getStringExtra(StaticRefs.ADD_LINE1)
        lsAddLine2OnSelection = getIntent().getStringExtra(StaticRefs.ADD_LINE2)
        lsAddCityOnSelection = getIntent().getStringExtra(StaticRefs.ADD_CITY)
        lsAddStateOnSelection = getIntent().getStringExtra(StaticRefs.ADD_STATE)
        lsAddPincodeOnSelection = getIntent().getStringExtra(StaticRefs.ADD_PINCODE)

        lsFullAdd = "$lsAddLine1OnSelection , $lsAddLine2OnSelection , $lsAddCityOnSelection , $lsAddStateOnSelection , $lsAddPincodeOnSelection"


        cbBook.setOnClickListener {
            lbBookClicked = true
            saveRecord()
        }
        cbCancel.setOnClickListener {
            finish()
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

        setTitle("Preview")

        tvServiceName.setText(Html.fromHtml("Selected service : <b>$service_name</b>"))
        // tvSerReqFor.setText("You need"+" "+"$service_name"+" "+"For")
        for (i in 0..alQuestionsArray.size - 1) {
            alFinalQueAnsArray.add(alQuestionsArray.get(i) + alAnswersArray.get(i))
        }

        rcvGetData.layoutManager = LinearLayoutManager(this)
        rcvGetData.adapter = GetDataAdapter(alFinalQueAnsArray, this)

        tvStartTime.setText(lsStartTime)
        tvEndTime.setText(lsEndTime)
        tvServiceDate.setText(lsDateOfAppointment)
        tvCompleteAddress.setText(lsFullAdd)

    }

    override fun onResume() {
        super.onResume()
        if (com.GoMobeil.H2H.prefs.user != null && com.GoMobeil.H2H.prefs.user != "") {
            if (lbBookClicked == true) {
               // toast("username is ${prefs!!.user}")
                saveRecord()
            }
        }
    }

    fun saveRecord() {

        Fuel.post(StaticRefs.SERVICEREQUEST, listOf((StaticRefs.TOKEN to prefs!!.token),
                (StaticRefs.SR_CUSTID to App.prefs!!.cust_id),
                (StaticRefs.SR_SRVID to service_id),
                (StaticRefs.SP_ID to lssp_id),
                (StaticRefs.SR_SRVTYPEID to ""),
                (StaticRefs.SR_CUSTBUDGET to ""),
                (StaticRefs.SR_LOCATIONID to liAddSrNoOnSelection),
                (StaticRefs.SR_PLANNEDDATE to lsDateOfAppointment),
                (StaticRefs.SR_PLANNEDTIME to lsStartTime),
                (StaticRefs.SR_LOCATION to lsAddCityOnSelection),
                (StaticRefs.SR_CREATEDBY to "Customername"),
                (StaticRefs.SR_ENABLEMESSAGING to lsMessage),
                (StaticRefs.SR_SHAREMOBILENO to lsShareContact),
                (StaticRefs.DATA to jsonArrMain.toString())
        )).responseJson()

        { request,
          response,
          result ->
            result.fold({ d ->
                parseResponse(result.get().content)

            }, { err ->
                TastyToast.makeText(context, "Internet Network Down", TastyToast.LENGTH_LONG, TastyToast.ERROR)
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
                lbBookClicked = false
                //Toast.makeText(context, "Record Inserted Successfully", Toast.LENGTH_LONG).show()

                TastyToast.makeText(context,"Record Inserted Successfully",TastyToast.LENGTH_LONG,TastyToast.SUCCESS).show()
                val intent = Intent(context, Home::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
        }
    }
}