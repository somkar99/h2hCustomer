package com.GoMobeil.H2H.UI

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import com.GoMobeil.H2H.Adapters.OpenAdapter
import com.GoMobeil.H2H.BaseActivity
import com.GoMobeil.H2H.Extensions.OnItemClickListener
import com.GoMobeil.H2H.Extensions.addOnItemClickListener
import com.GoMobeil.H2H.Models.MyRequestModel
import com.GoMobeil.H2H.Models.ServicesModel
import com.GoMobeil.H2H.R
import com.GoMobeil.H2H.Services.TransperantProgressDialog
import com.GoMobeil.H2H.StaticRefs
import com.GoMobeil.H2H.prefs
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.sdsmdg.tastytoast.TastyToast
import kotlinx.android.synthetic.main.open_row.*
import kotlinx.android.synthetic.main.resp_rec_row.*
import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by Admin on 04-05-18.
 */
class Booking : BaseActivity() {

    override lateinit var activity: Activity
    override lateinit var context: Context

    var rcvMyList : RecyclerView? = null

    var service_id :Int? = null
    var service_name : String? = null

    var ivFilter :ImageView? = null

    var txn_id: Int? = null

    lateinit var pd:TransperantProgressDialog

    var openAdapter : OpenAdapter? = null
    lateinit var  rcvOpenAdapter : RecyclerView.Adapter<OpenAdapter.openViewHolder>
    lateinit var llm : LinearLayoutManager

    companion object {

        var HIRED = "get_hired"
        var OPEN = "open"
        var RESPONSE_RECEIVED = "get_response_received"
        var ONGOING = "get_ongoing"


    }
    var lsStatus :String = OPEN

    var stType:Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.booking)

        context = this
        activity = this

        pd = TransperantProgressDialog(context)
        rcvMyList = findViewById(R.id.rcvList)
        ivFilter= findViewById<ImageView>(R.id.ivFilter);
        ivFilter!!.visibility = View.VISIBLE

        initLayout()
    }

    fun initLayout(){
        service_id = getIntent().getIntExtra(ServicesModel.SRV_ID, 0)
        service_name = getIntent().getStringExtra(ServicesModel.SRV_NAME)



        llm = LinearLayoutManager(applicationContext);
        rcvMyList!!.layoutManager = llm;



        val clickListener = View.OnClickListener { view ->
            when (view.id) {
                R.id.ivFilter -> {
                    showPopup(view)
                }
            }
        }
        ivFilter!!.setOnClickListener(clickListener)
    }


    override fun onResume() {
        super.onResume()

        initLayout()
        if (lsStatus.equals(OPEN)){
            stType = 0
            setTitle(getString(R.string.open)).toString()
            showData(lsStatus,stType!!)
        }
        else if (lsStatus.equals(RESPONSE_RECEIVED)){

            stType = 1
            setTitle(getString(R.string.response_received)).toString()
            showData(lsStatus,stType!!)
        }
        else if (lsStatus.equals(HIRED)){
            stType = 2
            setTitle(getString(R.string.Hired)).toString()
            showData(lsStatus,stType!!)
        }
        else if (lsStatus.equals(ONGOING)){
            stType = 3
            setTitle(getString(R.string.ongoing)).toString()
            showData(lsStatus,stType!!)
        }

    }

    fun showData(input:String,sType: Int){

        pd.show()

       val stType = sType

        Fuel.post(StaticRefs.URLMYREQUEST, listOf((StaticRefs.SR_CUSTID to prefs.cust_id),
                (StaticRefs.SR_STATUS to input))).timeoutRead(StaticRefs.TIMEOUTREAD)

                .responseJson(){

                    request,
                    response,
                    result ->

                    result.fold({ d ->
                        parseJson(result.get().content)

                    }, { err ->
                        pd.dismiss()
                        TastyToast.makeText(context, getString(R.string.Internet_Down), TastyToast.LENGTH_LONG, TastyToast.ERROR)
                    })
                }

    }

    fun parseJson (data :String){


        val json =JSONObject(data)
        if (json.has(StaticRefs.STATUS) && json.getString(StaticRefs.STATUS)!= null){

            var lsMessage = ""

            if (json.has(StaticRefs.MESSAGE) && json.getString(StaticRefs.MESSAGE)!= null){

                lsMessage = json.getString(StaticRefs.MESSAGE)
            }

            if (json.getString(StaticRefs.STATUS).equals(StaticRefs.FAILED)){

                pd.dismiss()
                TastyToast.makeText(this, getString(R.string.Parameters_Incorrect), TastyToast.LENGTH_LONG, TastyToast.WARNING).show()
            }
            else{
                pd.dismiss()
                if (data.contains(StaticRefs.DATA)){

                    val lsnewleadsdata = json.getString(StaticRefs.DATA)
                    if(!(lsnewleadsdata.equals(null) ||lsnewleadsdata.equals("null"))){

                        val jsondata = JSONArray(lsnewleadsdata)
                        if (jsondata.length()>0){

                            val response = json.getString(StaticRefs.DATA)
                            val parser = Parser()
                            val stringBuilder = StringBuilder(response)
                            val model = parser.parse(stringBuilder) as JsonArray<JsonObject>
                            var myRequestModel = model.map { MyRequestModel(it) }.filterNotNull();
                            openAdapter = OpenAdapter(myRequestModel, context,stType!!);
                            rcvOpenAdapter = openAdapter!!
                            rcvMyList!!.adapter = rcvOpenAdapter;
                            rcvOpenAdapter.notifyDataSetChanged();
                            openAdapter!!.setMultiple(false)


                            openAdapter!!.setOnCardClickListener(object : OpenAdapter.onItemClickListener{
                                override fun onItemClick(pos: Int, view: View) {

                                    if (view.id ==tvCancelReqest.id){
                                        txn_id = myRequestModel[pos].lsTxnid
                                        showPopup(pos)

                                    }
                                    else if (view.id == llopen.id){
                                        txn_id = myRequestModel[pos].lsTxnid
                                        TastyToast.makeText(context, "Transaction id is $txn_id", TastyToast.LENGTH_LONG, TastyToast.INFO)
                                        pd.dismiss()
                                        val intent = Intent(context, MyRequestDetails::class.java)
                                        intent.putExtra("txnid", txn_id.toString())
                                        intent.putExtra("key",1)

                                        startActivity(intent)




                                    }
                                    else if (view.id == llresponse.id){
                                        txn_id = myRequestModel[pos].lsTxnid
                                        TastyToast.makeText(context, "Transaction id is $txn_id", TastyToast.LENGTH_LONG, TastyToast.INFO)
                                        pd.dismiss()
                                        val intent = Intent(context, MyRequestDetails::class.java)
                                        intent.putExtra("txnid", txn_id.toString())
                                        intent.putExtra("key",2)

                                        startActivity(intent)

                                    }

                                    else if (view.id == tvRCancelRequest.id){
                                        txn_id = myRequestModel[pos].lsTxnid
                                        showPopup(pos)

                                    }
                                  /*


                                    else if (view.id == llresponse.id){
                                        txn_id = myRequestModel[pos].lsTxnid
                                        TastyToast.makeText(context, "Transaction id is $txn_id", TastyToast.LENGTH_LONG, TastyToast.INFO)

                                    }*/


                                }


                            })

                            openAdapter!!.setOnAddChangeListener(object :OpenAdapter.onAddChangeListener{
                                override fun onItemChangeAdd(position: Int, lsVal: String) {

                                    if (lsVal.equals("YES")){

                                        txn_id = myRequestModel[position].lsTxnid
                                        cancelReq()
                                    }

                                    else if (lsVal.equals("NO")){


                                    }

                                }

                            })



                           /* rcvMyList!!.addOnItemClickListener(object :OnItemClickListener{

                                override fun onItemClicked(view: View, position: Int) {
                                    txn_id = myRequestModel[position].lsTxnid
                                    TastyToast.makeText(context, "Transaction id is $txn_id", TastyToast.LENGTH_LONG, TastyToast.INFO)

                                    pd.dismiss()
                                }

                            })*/


                        }

                    }




                }
            }
        }
    }

    fun showPopup(pos : Int)
    {
        val layoutInflater = LayoutInflater.from(context)
        val dialogview = layoutInflater.inflate(R.layout.myreq_popup,null)

        val tvYES = dialogview.findViewById<TextView>(R.id.tvYES)
        val tvNO = dialogview.findViewById<TextView>(R.id.tvNO)

        val popup1 = android.support.v7.app.AlertDialog.Builder(this!!.context!!)

        popup1.setView(dialogview)

        var alertdialog = popup1.create()
        alertdialog.show()

        tvYES.setOnClickListener {
            /*  if(OpenAdapter.mAddChangeListener != null)
              {
                  OpenAdapter.mAddChangeListener.onItemChangeAdd(pos,"YES")
              }

              alertdialog.dismiss()*/

            if (OpenAdapter.mAddChangeListener!=null){
                OpenAdapter.mAddChangeListener.onItemChangeAdd(pos,"YES")
            }
            alertdialog.dismiss()
        }
        tvNO.setOnClickListener {
            /*  if(OpenAdapter.mAddChangeListener != null)
              {
                  OpenAdapter.mAddChangeListener.onItemChangeAdd(pos,"NO")
              }

              alertdialog.dismiss()*/
            if (OpenAdapter.mAddChangeListener!=null){
                OpenAdapter.mAddChangeListener.onItemChangeAdd(pos,"No")
            }
            alertdialog.dismiss()
        }
    }

    fun cancelReq(){

        Fuel.post(StaticRefs.CANCELREQUEST, listOf((StaticRefs.SPL_TXNID to txn_id),(StaticRefs.SPL_STATUS to "X")))
                .responseJson(){
                    request,
                    response,
                    result ->
                    result.fold({ d ->
                        parseCancel(result.get().content)
                    }, { err ->
                        pd.dismiss()
                        TastyToast.makeText(context,"Internet Network Down", TastyToast.LENGTH_LONG, TastyToast.ERROR)
                        finish()
                    })

                }


    }

    fun parseCancel(response:String){


        val json = JSONObject(response)

        if (json.has(StaticRefs.STATUS) && json.getString(StaticRefs.STATUS)!=null){
            var message = ""
            if (json.has(StaticRefs.MESSAGE)&& json.getString(StaticRefs.MESSAGE)!=null){
                message = json.getString(StaticRefs.MESSAGE)
            }
            if (json.getString(StaticRefs.STATUS).equals(StaticRefs.FAILED)){
                val error = json.getString("error")

                TastyToast.makeText(context,error.toString(),TastyToast.LENGTH_LONG,TastyToast.ERROR).show()
            }

            else{
                TastyToast.makeText(context,"Request Cancelled Successfully",TastyToast.LENGTH_LONG,TastyToast.SUCCESS).show()
                //rcvAdapter.notifyDataSetChanged();
                showData(lsStatus,stType!!)
            }
        }

    }
    fun showPopup(view: View){

        var popup : PopupMenu? = null
        popup = PopupMenu(this,view)

        popup.inflate(R.menu.booking_menu)

        popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item : MenuItem? ->

            when (item!!.itemId){
                R.id.open ->{


                    lsStatus = OPEN
                    showData(lsStatus,0)
                    setTitle("Open")


                }
                R.id.PendingforAccept ->{

                    lsStatus = RESPONSE_RECEIVED
                    showData(lsStatus,1)
                    setTitle("Response received")

                }
                R.id.hired ->{

                    lsStatus = HIRED
                    showData(lsStatus,2)
                    setTitle("Hired")

                }
                R.id.inprogress ->{

                    lsStatus = ONGOING
                    showData(lsStatus,3)
                    setTitle("In Progress")
                }


            }
            true

        })
        popup.show()
    }
}