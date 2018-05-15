package com.GoMobeil.H2H.UI

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.*
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Toast
import com.GoMobeil.H2H.*
import com.GoMobeil.H2H.Adapters.VendorListAdapter
import com.GoMobeil.H2H.Extensions.OnItemClickListener
import com.GoMobeil.H2H.Extensions.addOnItemClickListener
import com.GoMobeil.H2H.Extensions.toast
import com.GoMobeil.H2H.Models.ServicesModel
import com.GoMobeil.H2H.Models.VendorListModel
import com.GoMobeil.H2H.Services.CustomDialog
import com.GoMobeil.H2H.Services.CustomServices
import com.GoMobeil.H2H.Services.GPSTracker
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.sdsmdg.tastytoast.TastyToast
import kotlinx.android.synthetic.main.vendor_list.*
import kotlinx.android.synthetic.main.vendor_list_row.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

/**
 * Created by ADMIN on 02-02-2018.
 */
class VendorList : BaseActivity()
{
    companion object {
        var TAG : String? = "VendorList"
        var zipcode : String? = null
    }

    lateinit var rcvVendorList :RecyclerView

    override lateinit var context : Context
    override lateinit var activity : Activity
    lateinit var rcvAdapter : RecyclerView.Adapter<VendorListAdapter.ViewHolder>
    var adapter :VendorListAdapter? = null
    lateinit var llm : LinearLayoutManager
    var service_id : Int? = null
    var service_name : String? = null
    var cat_value : String? = null
    lateinit var sharedPref : SharedPreferences
    var pincode : String? = null
    var location : String? = null
    var lsp_id : String? = null
    var LOCATION_REQUEST_CODE : Int? = 100
    private var locationManager : LocationManager? = null
    lateinit var gps : GPSTracker
    var alQuestionsArray : ArrayList<String> = ArrayList()
    var alAnswersArray : ArrayList<String> = ArrayList()
    var alAnsIdsArray : ArrayList<String> = ArrayList()
    var alQueIdsArray : ArrayList<String> = ArrayList()
    var alFinalQueAnsArray: ArrayList<String> = ArrayList()
    var jsonArrMain: JSONArray = JSONArray()

    var lsCompleteaddress :String? = null
    var lsStartTime :String? = null
    var lsEndTime :String? = null

    var lssp_id :Int? = null
    var sp_id :Int?=null

    var liAddSrNoOnSelection : Int? = null
    var lsAddLine1OnSelection : String? = ""
    var lsAddLine2OnSelection : String? = ""
    var lsAddCityOnSelection : String? = ""
    var lsAddStateOnSelection : String? = ""
    var lsAddPincodeOnSelection : String? = ""

    var shareNumber:String? = ""
    var message:String = ""
    var dateofappoint:String = ""
    var lbBookClicked: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.vendor_list)
        context = this
        activity = this

        initLayout()
    }

    fun initLayout() : Unit
    {
        service_id = getIntent().getIntExtra(ServicesModel.SRV_ID, 0)
        service_name = getIntent().getStringExtra(ServicesModel.SRV_NAME)
        cat_value = getIntent().getStringExtra("sp_category")
        pincode = prefs.pincode
        location = prefs.location
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

        rcvVendorList = (findViewById(R.id.rcvVendorList))


        locationManager = getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager?

        gps = GPSTracker(context)
        checkLocationPermission()

        setTitle("Vendors for $service_name")

        llm = LinearLayoutManager(applicationContext);
        rcvVendorList.layoutManager = llm;



        cbBook.setOnClickListener {
            lbBookClicked = true
            saveRecord()
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

        for (i in 0..alQuestionsArray.size - 1) {
            alFinalQueAnsArray.add(alQuestionsArray.get(i) + alAnswersArray.get(i))
        }




        cbPreview.setOnClickListener {

            val intent = Intent(context,Preview::class.java)
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

        getVendorList(service_id!!)
    }
    override fun onResume() {
        super.onResume()
        if (com.GoMobeil.H2H.prefs.user != null && com.GoMobeil.H2H.prefs.user != "") {
            if (lbBookClicked == true) {
               // toast("username is ${App.prefs!!.user}")
                saveRecord()
            }
        }
    }
    fun getVendorList(srv_id : Int)
    {
        Fuel.post(StaticRefs.SERVICES,listOf(
                (StaticRefs.TOKEN to prefs.token)
                ,(ServicesModel.SRV_ID to srv_id)
                ,(StaticRefs.SP_CATEGORY to cat_value)
                ))
                .responseJson()
                {
                    request,
                    response,
                    result ->

                    result.fold({ d ->
                        parseJson(result.get().content)
                    }, { err ->
                        TastyToast.makeText(context,"Internet Network Down",TastyToast.LENGTH_LONG,TastyToast.ERROR)
                    })


                }
    }

    fun parseJson(data : String) {
        val json = JSONObject(data)

        val response = json.getString(StaticRefs.DATA)
        val responseJSON = JSONArray(response)



            if (responseJSON.length() < 0) {
                TastyToast.makeText(context, "No Data Found", Toast.LENGTH_LONG, TastyToast.ERROR).show()

            } else {
                val parser = Parser()
                val stringBuilder = StringBuilder(response)
                val model = parser.parse(stringBuilder) as JsonArray<JsonObject>
                val vendorListModel = model.map { VendorListModel(it) }.filterNotNull();
                adapter = VendorListAdapter(vendorListModel, context);
                rcvAdapter = adapter!!
                rcvVendorList.adapter = rcvAdapter;
                rcvAdapter.notifyDataSetChanged();
                adapter!!.setMultiple(false)

                adapter!!.setOnCardClickListener(object : VendorListAdapter.onItemClickListener {
                    override fun onItemClick(positon: Int, view: View) {

                        if (view.id == ivCall.id) {

                            var lsMobile = vendorListModel.get(positon).lsMobile


                            //call_action(lsMobile)
                           // toast("Mobile $lsMobile")

                            if (isPermissionGranted()) {
                                call_action(lsMobile!!)
                            }
                        }

                        if (view.id == ivMessage.id){
                            var sp_id = vendorListModel[positon].sp_id
                            var sp_name = vendorListModel[positon].lsFirstName+" "+vendorListModel[positon].lsLastName

                            var intent = Intent(context, Message::class.java)
                            intent.putExtra("sp_id", sp_id.toString())
                            intent.putExtra("sp_name",sp_name)
                            intent.putExtra("key","2")
                            startActivity(intent)

                        }

                        if (view.id == cbViewDetails.id) {

                            sp_id= vendorListModel[positon].sp_id
                           // toast(" vendor id " + sp_id)

                            var intent = Intent(context, VendorsDetails::class.java)
                            intent.putExtra("sp_id", sp_id.toString())
                            startActivity(intent)

                        }
                        if (view.id == tvLocation.id){

                            sp_id = vendorListModel[positon].sp_id
                            toast("sp id is" + sp_id)
                        }
                        if (view.id==tvName.id){

                            sp_id = vendorListModel[positon].sp_id
                            toast("sp id is" + sp_id)
                        }


                        if (view.id == llLinearVendorlist.id){

                            lsp_id= vendorListModel[positon].sp_id.toString()

                        }

                    }
                })



                rcvVendorList.addOnItemClickListener(object : OnItemClickListener {
                    override fun onItemClicked(view: View, position: Int) {
                        sp_id = vendorListModel[position].sp_id
                        //toast("sp id is" + sp_id)

                    }
                }

                )

            }



    }

    fun isPermissionGranted(): Boolean {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.CALL_PHONE) === PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted")
                return true
            } else {

                Log.v(TAG, "Permission is revoked")
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), 1)
                return false
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted")
            return true
        }
    }

    @SuppressLint("MissingPermission")
    fun call_action(lsMobile : String) {
        val phnum = lsMobile
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel:" + phnum)
        startActivity(callIntent)
    }


    fun checkLocationPermission()
    {
        val permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    this!!.LOCATION_REQUEST_CODE!!)

            getLattitudeLng()
        }
        else
        {
            getLattitudeLng()
        }
    }

    fun getLattitudeLng()
    {
        if(gps.canGetLocation)
        {
            var lattitude = gps.getLatitude()
            var longitude = gps.getLongitude()

           // toast("Lat $lattitude Long $longitude")

            getPostal(lattitude, longitude)
        }
        else
        {
            gps.showSettingsAlert()
        }
    }

    fun getPostal(lat : Double , lng : Double)
    {
        val geocoder = Geocoder(this, Locale.ENGLISH)
        var addresses : List<Address>? = geocoder.getFromLocation(lat, lng, 1)
        var address : Address? = null
        var addr : String? = ""
       // var zipcode : String? = ""
        var city : String? = ""
        var state : String? = ""
        var location : String? = ""

        if(addresses != null && addresses.size > 0)
        {
            addr=addresses.get(0).getAddressLine(0)+"," +addresses.get(0).getSubAdminArea();
            city=addresses.get(0).getLocality();
            state=addresses.get(0).getAdminArea();

            for(i in 0..addresses.size)
            {
                address = addresses.get(i)
                if(address.getPostalCode()!=null){
                    zipcode=address.getPostalCode()
                    state = address.adminArea
                    location = address.subLocality

                    toast("PinCode Vendor $zipcode")
                    toast("Location Vendor $location")

                    break
                }
            }


        }

        if(zipcode.equals(pincode))
        {
            toast("Same location")
          //  showAlertDialog(zipcode!!, location!!)
        }
        else{
            toast("Not same location")
        }
    }

    private fun showAlertDialog(zipcode: String, location: String) {

        val dialog = CustomDialog(activity, context)
        //CustomServices.hideSoftKeyboard(this)
        dialog.setCancel(false)
        dialog.setOutsideTouchable(false)
        dialog.showDialog(zipcode , location)
    }




    fun saveRecord() {

        Fuel.post(StaticRefs.SERVICEREQUEST, listOf((StaticRefs.TOKEN to App.prefs!!.token),
                (StaticRefs.SR_CUSTID to prefs!!.cust_id),
                (StaticRefs.SR_SRVID to service_id),
                (StaticRefs.SP_ID to lsp_id),
                (StaticRefs.SR_SRVTYPEID to ""),
                (StaticRefs.SR_CUSTBUDGET to ""),
                (StaticRefs.SR_LOCATIONID to liAddSrNoOnSelection),
                (StaticRefs.SR_PLANNEDDATE to dateofappoint),
                (StaticRefs.SR_PLANNEDTIME to lsStartTime),
                (StaticRefs.SR_LOCATION to lsAddCityOnSelection),
                (StaticRefs.SR_CREATEDBY to "vinay"),
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
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            } else {
                lbBookClicked = false
                TastyToast.makeText(context, "Record Inserted Successfully", TastyToast.LENGTH_LONG,TastyToast.SUCCESS).show()
                val intent = Intent(context, Home::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
        }
    }


}


