package com.GoMobeil.H2H.UI

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.GoMobeil.H2H.Adapters.ShowDataAdapter
import com.GoMobeil.H2H.Adapters.VendorListAdapter
import com.GoMobeil.H2H.Adapters.WorkImgAdapter
import com.GoMobeil.H2H.BaseActivity
import com.GoMobeil.H2H.Extensions.OnItemClickListener
import com.GoMobeil.H2H.Extensions.addOnItemClickListener
import com.GoMobeil.H2H.Extensions.toast
import com.GoMobeil.H2H.Models.SetDataModel
import com.GoMobeil.H2H.Models.VendorListModel
import com.GoMobeil.H2H.R
import com.GoMobeil.H2H.R.color.black
import com.GoMobeil.H2H.R.color.green_50
import com.GoMobeil.H2H.StaticRefs
import com.GoMobeil.H2H.prefs
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.sdsmdg.tastytoast.TastyToast
import kotlinx.android.synthetic.main.base_layout.*
import kotlinx.android.synthetic.main.my_request.*
import kotlinx.android.synthetic.main.vendor_details.*
import kotlinx.android.synthetic.main.vendor_list_row.*
import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by Admin on 03-04-18.
 */
class MyRequestDetails :BaseActivity(){

    override lateinit var context: Context
    override lateinit var activity: Activity

    var txnid:String? = null
    var serv_name:String? = null


    var addline1:String?=null
    var addline2:String?=null
    var lsCity:String?=null
    var lsState:String?=null
    var lsPincode:String?=null

    var lsDateofService:String?=null
    var lsTimeOfService:String?=null
    var lssp_id :Int? = null

    // var hrsp_id :Int? = null




    var setDataAdapter: ShowDataAdapter? = null

    lateinit var rcvQuesAns: RecyclerView.Adapter<ShowDataAdapter.ViewHolder>
    lateinit var quesanslm: RecyclerView.LayoutManager

    var adapter : VendorListAdapter? = null
    lateinit var rcvAdapter : RecyclerView.Adapter<VendorListAdapter.ViewHolder>
    lateinit var rcvVendorList :RecyclerView
    lateinit var llm : LinearLayoutManager
    var key:Int? = 0




    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_request)


        txnid = getIntent().getStringExtra("txnid")
        key = getIntent().getIntExtra("key",0)
        initLayout()

        getData()
        getAceeptVendors()
    }

    fun initLayout(){
        context = this
        activity = this

        if (key==1){
            llHeaderDetails.visibility = View.GONE
            llHeader2.visibility = View.GONE
            llVendorlist.visibility = View.GONE
        }


        quesanslm = LinearLayoutManager(applicationContext,LinearLayoutManager.VERTICAL,false)
        rcvGetData.layoutManager = quesanslm

        rcvVendorList = (findViewById(R.id.rcvMyReqVendors))

        llm = LinearLayoutManager(applicationContext);
        rcvVendorList.layoutManager = llm;

        tvDetails.setOnClickListener {
            viewDetails.setBackgroundColor(resources.getColor(black))
            viewVendorlist.setBackgroundColor(resources.getColor(green_50))
            llDeatils.visibility = View.VISIBLE
            llVendorlist.visibility = View.GONE


        }

        tvVendorlist.setOnClickListener {
            viewDetails.setBackgroundColor(resources.getColor(green_50))
            viewVendorlist.setBackgroundColor(resources.getColor(black))
            llDeatils.visibility = View.GONE
            llVendorlist.visibility = View.VISIBLE

        }


        setTitle("My Request Details")


        cbHire.setOnClickListener {

            hiredVendor()
        }



    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun getData(){

        Fuel.post(StaticRefs.MYREQDETAILS, listOf((StaticRefs.TXN_ID to txnid)))
                .responseJson(){
                    request,
                    response,
                    result ->


                    result.fold({ d ->
                        parseJson(result.get().content)
                    }, { err ->
                        TastyToast.makeText(context,"Internet Network Down", TastyToast.LENGTH_LONG, TastyToast.ERROR)
                    })
                }

    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun parseJson(response:String){


        val json = JSONObject(response)

        if (response.contains(StaticRefs.DATA)&& !json.getString(StaticRefs.DATA).equals(null) &&!json.getString(StaticRefs.DATA).equals("null") ) {


            val data = JSONObject(response).getJSONObject(StaticRefs.DATA)


            if (data.length() <= 0 && data == null) {
                TastyToast.makeText(context, "No data found", TastyToast.LENGTH_LONG, TastyToast.ERROR).show()
            } else {
                if (data.length() == 0) {

                }

                serv_name = data.getString("srv_name")
                addline1 = data.getString("add_line1")
                addline2 = data.getString("add_line2")
                lsCity = data.getString("add_city")
                lsState = data.getString("add_state")
                lsPincode = data.getString("add_pincode")

                lsDateofService = data.getString("sr_planneddate")
                lsTimeOfService = data.getString("sr_plannedtime")


                tvServiceName.setText(serv_name)

                tvFullAddress.setText(addline1 + ", " + addline2 + ", " + lsCity + ", " + lsState + ", " + lsPincode)

                tvDateOfService.setText(lsDateofService)

                tvTimeOfService.setText(lsTimeOfService)

                val jsonQuesAns = json.getString(StaticRefs.DATA)
                val jsonMain = JSONObject(jsonQuesAns)

                val QuesArr = jsonMain.getString("question_answers")

                val parser = Parser()
                val stringBuilder = StringBuilder(QuesArr)
                val model = parser.parse(stringBuilder) as JsonArray<JsonObject>

                val setDataModel = model.map { SetDataModel(it) }.filterNotNull()

                setDataAdapter = ShowDataAdapter(setDataModel)
                rcvQuesAns = setDataAdapter!!
                rcvGetData.adapter = rcvQuesAns

                rcvQuesAns.notifyDataSetChanged()


            }
        }
        else{
            TastyToast.makeText(context,"No Data Found",TastyToast.LENGTH_LONG,TastyToast.ERROR).show()
        }

    }

    fun getAceeptVendors(){

        Fuel.post(StaticRefs.ACCEPTVENDORS, listOf((StaticRefs.SPL_TXNID to txnid),(StaticRefs.SPL_STATUS to "response_received")))
                .responseJson(){
                    request,
                    response,
                    result ->


                    result.fold({ d ->
                        parseVendorlist(result.get().content)
                    }, { err ->
                        TastyToast.makeText(context,"Internet Network Down", TastyToast.LENGTH_LONG, TastyToast.ERROR)
                    })
                }

    }

    fun parseVendorlist(data: String){

        val json = JSONObject(data)
        if(data.contains(StaticRefs.DATA)&&!json.getString(StaticRefs.DATA).equals(null)&&!json.getString(StaticRefs.DATA).equals("null")){


        val response = json.getString(StaticRefs.DATA)

        val resArr = JSONArray(response)
            if(resArr.length()>0) {
                if (response.equals("Not Found")) {
                    TastyToast.makeText(context, "No Vendor Found", TastyToast.LENGTH_LONG, TastyToast.ERROR)
                } else {
                    val parser = Parser()
                    val stringBuilder = StringBuilder(response)
                    val model = parser.parse(stringBuilder) as JsonArray<JsonObject>

                    val vendorListModel = model.map { VendorListModel(it) }.filterNotNull()

                    adapter = VendorListAdapter(vendorListModel, context);
                    rcvAdapter = adapter!!
                    rcvVendorList.adapter = rcvAdapter;
                    rcvAdapter.notifyDataSetChanged();
                    adapter!!.setMultiple(false)


                    adapter!!.setOnCardClickListener(object : VendorListAdapter.onItemClickListener {
                        override fun onItemClick(position: Int, view: View) {

                            if (view.id == ivCall.id) {

                                var lsMobile = vendorListModel.get(position).lsMobile


                                //call_action(lsMobile)
                                // toast("Mobile $lsMobile")

                                if (isPermissionGranted()) {
                                    call_action(lsMobile!!)
                                }
                            }

                            if (view.id == cbViewDetails.id) {

                                var sp_id = vendorListModel[position].sp_id
                                // toast(" vendor id "+sp_id)

                                var intent = Intent(context, VendorsDetails::class.java)
                                intent.putExtra("sp_id", sp_id.toString())
                                startActivity(intent)

                            }

                            if (view.id == llLinearVendorlist.id){

                                 lssp_id= vendorListModel[position].sp_id
                                TastyToast.makeText(context,"SPid is $lssp_id",TastyToast.LENGTH_LONG,TastyToast.SUCCESS)

                            }





                        }
                    })




                  /*  rcvVendorList.addOnItemClickListener(object : OnItemClickListener {
                        override fun onItemClicked(view: View, position: Int) {
                            lssp_id = vendorListModel[position].sp_id
                            toast("sp id is"+lssp_id)

                        }
                    }

                    )*/
                }


            }
            else{
                TastyToast.makeText(context,"No Data Found",TastyToast.LENGTH_LONG,TastyToast.ERROR).show()
            }


    }
        else{
            TastyToast.makeText(context,"No Data Found",TastyToast.LENGTH_LONG,TastyToast.ERROR).show()
            cbHire.visibility = View.GONE
            tvVendorList.visibility = View.GONE
            tvNoVendorFound.visibility = View.VISIBLE
        }
    }

    fun isPermissionGranted(): Boolean {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.CALL_PHONE) === PackageManager.PERMISSION_GRANTED) {
                Log.v(VendorList.TAG, "Permission is granted")
                return true
            } else {

                Log.v(VendorList.TAG, "Permission is revoked")
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), 1)
                return false
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(VendorList.TAG, "Permission is granted")
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


    fun hiredVendor(){

        Fuel.post(StaticRefs.CHANGESTATUS, listOf((StaticRefs.SPL_SPID to lssp_id),(StaticRefs.SPL_TXNID to txnid),
                StaticRefs.SPL_STATUS to "H"))

                .responseJson(){
                    request,
                    response,
                    result ->

                    result.fold({ d ->
                        parseHiredVendors(result.get().content)
                    }, { err ->
                        TastyToast.makeText(context,"Internet Network Down", TastyToast.LENGTH_LONG, TastyToast.ERROR)
                    })

                }
    }


    fun parseHiredVendors(response: String) {


        val json = JSONObject(response)
        if (json.has(StaticRefs.STATUS) && json.getString(StaticRefs.STATUS) != null) {

            var message = ""
            if (json.has(StaticRefs.MESSAGE) && json.getString(StaticRefs.MESSAGE) != null) {

                message = json.getString(StaticRefs.MESSAGE)
            }
            if (json.getString(StaticRefs.STATUS).equals(StaticRefs.FAILED)) {

                TastyToast.makeText(context, message, TastyToast.LENGTH_LONG, TastyToast.ERROR).show()
            }

            else{
                TastyToast.makeText(context,"Hire Successfully",TastyToast.LENGTH_LONG,TastyToast.SUCCESS).show()
                finish()
            }


        }
    }

}