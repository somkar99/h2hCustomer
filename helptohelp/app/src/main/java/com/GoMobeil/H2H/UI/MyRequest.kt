package com.GoMobeil.H2H.UI

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import com.GoMobeil.H2H.*
import com.GoMobeil.H2H.Adapters.MyRequestAdapter
import com.GoMobeil.H2H.Adapters.OpenAdapter
import com.GoMobeil.H2H.Adapters.ResponseRAdapter
import com.GoMobeil.H2H.Extensions.OnItemClickListener
import com.GoMobeil.H2H.Extensions.addOnItemClickListener
import com.GoMobeil.H2H.Models.MyRequestModel
import com.GoMobeil.H2H.Models.ServicesModel
import com.GoMobeil.H2H.Services.TransperantProgressDialog
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.sdsmdg.tastytoast.TastyToast
import kotlinx.android.synthetic.main.booking.*
import kotlinx.android.synthetic.main.my_request_row.*
import kotlinx.android.synthetic.main.open_row.*
import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by Admin on 14-03-18.
 */
class MyRequest : BaseActivity() {

        override lateinit var context: Context
        override lateinit var activity: Activity

        lateinit var ivFilter : ImageView
        lateinit var rcvMyReqList :RecyclerView
        lateinit var rcvResponseRList :RecyclerView
        lateinit var rcvHiredVList :RecyclerView
        lateinit var rcvOngoingVList :RecyclerView
        var service_id  : Int? = null
        var service_name : String? = null




        var txn_id :Int? = null
        lateinit var llm : LinearLayoutManager

    lateinit var pd:TransperantProgressDialog



    var adapter :MyRequestAdapter? = null
    lateinit var rcvAdapter : RecyclerView.Adapter<MyRequestAdapter.ViewHolder>

    var openAdapter :OpenAdapter? = null
    lateinit var  rcvOpenAdapter : RecyclerView.Adapter<OpenAdapter.openViewHolder>

    var responseAdapter :ResponseRAdapter? = null
    lateinit var rcvResponseAdapter :RecyclerView.Adapter<ResponseRAdapter.resViewHolder>

   /* var newAdapter :NewMyReqAdapter? = null
    lateinit var rcvNewAdapter : RecyclerView.Adapter<NewMyReqAdapter.ViewHolder>
*/
    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.booking)
            context = this
            activity = this

           pd = TransperantProgressDialog(context)




            initLayout()





    }

    fun initLayout(){

        setTitle("My Request")
        service_id = getIntent().getIntExtra(ServicesModel.SRV_ID, 0)
        service_name = getIntent().getStringExtra(ServicesModel.SRV_NAME)
        ivFilter= findViewById<ImageView>(R.id.ivFilter);

        rcvMyReqList = (findViewById(R.id.rcvList))






        llm = LinearLayoutManager(applicationContext);
        rcvMyReqList.layoutManager = llm;

        ivFilter.visibility = View.VISIBLE

        getMyRequest()


        val clickListener = View.OnClickListener { view ->
            when (view.id) {
                R.id.ivFilter -> {
                    showPopup(view)
                }
            }
        }
        ivFilter.setOnClickListener(clickListener)



    }
    fun getMyRequest(){

        pd.show()

        Fuel.post(StaticRefs.URLMYREQUEST, listOf(
                (StaticRefs.SR_CUSTID to prefs!!.cust_id),
                (StaticRefs.SR_STATUS to "open")
        )).responseJson(){
            request,
            response,
            result ->

            result.fold({ d ->
                parseJson(result.get().content)
            }, { err ->
                pd.dismiss()
                TastyToast.makeText(context,"Internet Network Down", TastyToast.LENGTH_LONG, TastyToast.ERROR)
                finish()
            })
        }
    }

    fun parseJson(data :String){

        val json = JSONObject(data)
        if(data.contains(StaticRefs.DATA)&&!json.getString(StaticRefs.DATA).equals(null)&&!json.getString(StaticRefs.DATA).equals("null")) {
            val response = json.getString(StaticRefs.DATA)


            val xx = JSONArray(response)

            if (xx.length() <= 0) {
                pd.dismiss()
                rcvMyReqList.visibility = View.GONE
                TastyToast.makeText(context, "No Data Found", Toast.LENGTH_LONG, TastyToast.ERROR).show()
            } else {


                val parser = Parser()
                val stringBuilder = StringBuilder(response)
                val model = parser.parse(stringBuilder) as JsonArray<JsonObject>
                val myRequestModel = model.map { MyRequestModel(it) }.filterNotNull();
                openAdapter = OpenAdapter(myRequestModel, context,0);
                rcvOpenAdapter = openAdapter!!
                rcvMyReqList.adapter = rcvOpenAdapter;
                setTitle("Open")
                rcvOpenAdapter.notifyDataSetChanged();
                openAdapter!!.setMultiple(false)

                pd.dismiss()

                openAdapter!!.setOnCardClickListener(object : OpenAdapter.onItemClickListener{

                    override fun onItemClick(position: Int, view: View) {

                        if (view.id == tvCancelReqest.id){
                            txn_id = myRequestModel[position].lsTxnid
                        }
                    }
                })


                openAdapter!!.setOnAddChangeListener(object :OpenAdapter.onAddChangeListener{



                    override fun onItemChangeAdd(position: Int, lsVal: String) {


                        if (lsVal.equals("YES")){
                            txn_id = myRequestModel[position].lsTxnid
                            //lssp_id = myRequestModel[position].sp

                            cancelReq()
                        }

                        else if (lsVal.equals("NO")){
                            TastyToast.makeText(context,"NO",TastyToast.LENGTH_LONG,TastyToast.ERROR).show()
                        }
                    }
                })

                rcvMyReqList.addOnItemClickListener(object : OnItemClickListener {
                    override fun onItemClicked(view: View, position: Int) {


                        txn_id = myRequestModel[position].lsTxnid
                        TastyToast.makeText(context, "Transaction id is $txn_id", TastyToast.LENGTH_LONG, TastyToast.INFO)

                        pd.dismiss()
                        val intent = Intent(context, MyRequestDetails::class.java)
                        intent.putExtra("txnid", txn_id.toString())

                        startActivity(intent)


                    }

                }

                )

                /*openAdapter!!.setOnCardClickListener(object : OpenAdapter.onItemClickListener {
                    override fun onItemClick(positon: Int, view: View) {
                        if (view.id == ivCancelReq.id) {

                            txn_id = myRequestModel[positon].lsTxnid
                            TastyToast.makeText(context,txn_id.toString(),TastyToast.LENGTH_LONG,TastyToast.SUCCESS).show()




                            TastyToast.makeText(context, "Cancel the request", TastyToast.LENGTH_LONG, TastyToast.SUCCESS).show()
                        }

                        if (view.id == tvclosereq.id) {
                            TastyToast.makeText(context, "Close the request", TastyToast.LENGTH_LONG, TastyToast.ERROR).show()
                        }
                    }
                })

                openAdapter!!.setOnAddChangeListener(object :OpenAdapter.onAddChangeListener{



                    override fun onItemChangeAdd(position: Int, lsVal: String) {


                        if (lsVal.equals("YES")){
                            txn_id = myRequestModel[position].lsTxnid
                            //lssp_id = myRequestModel[position].sp

                            cancelReq()
                        }

                        else if (lsVal.equals("NO")){
                            TastyToast.makeText(context,"NO",TastyToast.LENGTH_LONG,TastyToast.ERROR).show()
                        }
                    }
                })

                rcvMyReqList.addOnItemClickListener(object : OnItemClickListener {
                    override fun onItemClicked(view: View, position: Int) {


                        txn_id = myRequestModel[position].lsTxnid
                        TastyToast.makeText(context, "Transaction id is $txn_id", TastyToast.LENGTH_LONG, TastyToast.INFO)

                        pd.dismiss()
                        val intent = Intent(context, MyRequestDetails::class.java)
                        intent.putExtra("txnid", txn_id.toString())

                        startActivity(intent)


                    }

                }

                )*/

                //rcvList.clearOnChildAttachStateChangeListeners()
                //rcvMyReqList.clearOnChildAttachStateChangeListeners()
            }
        }else{
            TastyToast.makeText(context,"No Data Found",TastyToast.LENGTH_LONG,TastyToast.ERROR).show()
        }
    }

    fun getAccepted(){

        pd.show()

        Fuel.post(StaticRefs.URLMYREQUEST, listOf((StaticRefs.SR_CUSTID to prefs.cust_id),(
                StaticRefs.SR_STATUS to "get_response_received"
                ))).responseJson(){

            request,
            response,
            result ->
            result.fold({ d ->
                pd.dismiss()
                parseAcceptedJson(result.get().content)
            }, { err ->
                TastyToast.makeText(context,"Internet Network Down", TastyToast.LENGTH_LONG, TastyToast.ERROR)
            })


        }





    }

    fun parseAcceptedJson(data :String) {

        val json = JSONObject(data)
        if (json.has("") && json.has(null)) {
            TastyToast.makeText(context, "Data is null", TastyToast.LENGTH_LONG, TastyToast.ERROR).show()
            pd.dismiss()
        } else {
            val response = json.getString(StaticRefs.DATA)

            val parser = Parser()
            val stringBuilder = StringBuilder(response)
            val model = parser.parse(stringBuilder) as JsonArray<JsonObject>
            val myRequestModel = model.map { MyRequestModel(it) }.filterNotNull();
            responseAdapter = ResponseRAdapter(myRequestModel, context);
            rcvResponseAdapter = responseAdapter!!
            rcvMyReqList.adapter = rcvResponseAdapter;
            rcvResponseAdapter.notifyDataSetChanged();
            responseAdapter!!.setMultiple(false)

            pd.dismiss()

            responseAdapter!!.setOnCardClickListener(object : ResponseRAdapter.onItemClickListener{

                override fun onItemClick(pos: Int, view: View) {


                }

            })


            rcvResponseRList.addOnItemClickListener(object : OnItemClickListener {
                override fun onItemClicked(view: View, position: Int) {


                    txn_id = myRequestModel[position].lsTxnid
                    //  TastyToast.makeText(context,"Transaction id is $txn_id", TastyToast.LENGTH_LONG, TastyToast.INFO)

                    val intent = Intent(context, MyRequestDetails::class.java)
                    intent.putExtra("txnid", txn_id)
                    startActivity(intent)


                }

            }

            )
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
                getMyRequest()
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


                    getMyRequest()
                    setTitle("Open")


                    //TastyToast.makeText(context,"InProgress",Toast.LENGTH_LONG,TastyToast.SUCCESS).show()
                }
                R.id.PendingforAccept ->{
                    getAccepted()
                    setTitle("Response received")

                   // TastyToast.makeText(context,"Pending",Toast.LENGTH_LONG,TastyToast.INFO).show()
                }
                R.id.hired ->{
                    getHiredData()
                   // TastyToast.makeText(context,"Done",Toast.LENGTH_LONG,TastyToast.CONFUSING).show()
                }
                R.id.inprogress ->{
                    getOngoing()
                   // TastyToast.makeText(context,"In Progress",Toast.LENGTH_LONG,TastyToast.CONFUSING).show()
                }


            }
            true

        })
        popup.show()
    }




    fun getHiredData(){

        Fuel.post(StaticRefs.URLMYREQUEST, listOf((StaticRefs.SR_CUSTID to prefs.cust_id),(
                StaticRefs.SR_STATUS to "get_hired"
                ))).responseJson(){

            request,
            response,
            result ->
            result.fold({ d ->
                parseCompletedJson(result.get().content)
            }, { err ->
                TastyToast.makeText(context,"Internet Network Down", TastyToast.LENGTH_LONG, TastyToast.ERROR)
            })


        }





    }
    fun parseCompletedJson(data :String) {

        val json = JSONObject(data)
        if (json.has("") && json.has(null)) {
            TastyToast.makeText(context, "Data is null", TastyToast.LENGTH_LONG, TastyToast.ERROR).show()
        } else {

            val response = json.getString(StaticRefs.DATA)
            val parser = Parser()
            val stringBuilder = StringBuilder(response)
            val model = parser.parse(stringBuilder) as JsonArray<JsonObject>
            val myRequestModel = model.map { MyRequestModel(it) }.filterNotNull();
            rcvResponseRList.visibility = View.VISIBLE
            rcvMyReqList.visibility = View.GONE
            adapter = MyRequestAdapter(myRequestModel, context, 2);
            rcvAdapter = adapter!!
            rcvHiredVList.adapter = rcvAdapter;
            rcvAdapter.notifyDataSetChanged();
            adapter!!.setMultiple(false)


            rcvMyReqList.addOnItemClickListener(object : OnItemClickListener {
                override fun onItemClicked(view: View, position: Int) {


                    txn_id = myRequestModel[position].lsTxnid
                    TastyToast.makeText(context, "Transaction id is $txn_id", TastyToast.LENGTH_LONG, TastyToast.INFO)

                    val intent = Intent(context, MyRequestDetails::class.java)
                    intent.putExtra("txnid", txn_id)
                    startActivity(intent)


                }

            }

            )
        }

    }




    fun getOngoing(){
        Fuel.post(StaticRefs.URLMYREQUEST, listOf((StaticRefs.SR_CUSTID to prefs.cust_id),(
                StaticRefs.SR_STATUS to "get_ongoing"
                ))).responseJson(){

            request,
            response,
            result ->
            result.fold({ d ->
                parseOnGoing(result.get().content)
            }, { err ->
                TastyToast.makeText(context,"Internet Network Down", TastyToast.LENGTH_LONG, TastyToast.ERROR)
            })


        }

    }

    fun parseOnGoing(data: String){

        val json = JSONObject(data)
        if (json.has("") && json.has(null)){
            TastyToast.makeText(context,"Data is null",TastyToast.LENGTH_LONG,TastyToast.ERROR).show()
        }
        else {
            val response = json.getString(StaticRefs.DATA)

            if (response.equals("") || response.equals(null)) {
                TastyToast.makeText(context, "No Data Found", Toast.LENGTH_LONG, TastyToast.ERROR).show()
            } else {

                val parser = Parser()
                val strigBuilder = StringBuilder(response)
                val model = parser.parse(strigBuilder) as JsonArray<JsonObject>
                val myRequestModel = model.map { MyRequestModel(it) }.filterNotNull()

                adapter = MyRequestAdapter(myRequestModel, context, 4);
                rcvAdapter = adapter!!
                rcvMyReqList.adapter = rcvAdapter;
                rcvAdapter.notifyDataSetChanged();
                adapter!!.setMultiple(false)


            }


        }


    }




}