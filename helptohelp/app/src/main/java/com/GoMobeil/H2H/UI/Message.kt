package com.GoMobeil.H2H.UI

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import com.GoMobeil.H2H.Adapters.Message_Adapter
import com.GoMobeil.H2H.Extensions.loadBase64Image
import com.GoMobeil.H2H.Models.Message_Conv_Model
import com.GoMobeil.H2H.Models.Message_Model
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
import kotlinx.android.synthetic.main.message.*

import org.json.JSONObject
import java.net.CacheResponse

/**
 * Created by apple on 12/04/18.
 */
class Message :Activity()
{

    lateinit var context: Context
    lateinit var activity: Activity
    lateinit var llm: LinearLayoutManager
    lateinit var lsMessage:String
    lateinit var rcvAdapter: RecyclerView.Adapter<Message_Adapter.ViewHolder>
    var adapter: Message_Adapter? = null
    lateinit var pd: TransperantProgressDialog
    var vendorid:String?=""
    var vendorname:String?=""
    var vendorimage:String?=""
    var txn_id:String?=""
    var Key:String?=""

    var vendorfirstname:String? = ""
    var vendorlastname:String? = ""





    companion object {
        var TAG: String? = "Message"
        val CHATSENDER="C"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.message)

        activity = this
        context = this
        pd = TransperantProgressDialog(context)


        Key=getIntent().getStringExtra(StaticRefs.KEY)

        if (Key.equals("2")){

            vendorid = getIntent().getStringExtra(("sp_id"))
            vendorname = getIntent().getStringExtra("sp_name")
            txn_id = getIntent().getStringExtra(Message_Conv_Model.TXN_ID)


        }


        if(Key.equals(StaticRefs.NOTIFICATIONTYPE)){

           /* val txnid = getIntent().getStringExtra(StaticRefs.TRANSACTIONID)
            txn_id=txnid.toInt()*/

        }
        else if (Key.equals("CONV")){

            txn_id = getIntent().getStringExtra(Message_Conv_Model.TXN_ID)
            vendorfirstname = getIntent().getStringExtra(Message_Conv_Model.SP_FIRSTNAME)
            vendorlastname = getIntent().getStringExtra(Message_Conv_Model.SP_LASTNAME)
            vendorid = getIntent().getStringExtra(Message_Conv_Model.SP_ID)
            vendorimage = getIntent().getStringExtra(Message_Conv_Model.SP_IMAGE)

        }


        if (Key=="CONV"){
            tvTitle.setText(vendorfirstname+" "+vendorlastname)

            if (vendorimage!!.length>0){
                try {
                    ivChatProfileImage.loadBase64Image(vendorimage)
                }
                catch (e:Exception){

                }

            }

        }
        else if (Key =="2"){
            tvTitle.setText(vendorname)
        }


        setTitle(vendorname)
        getChats()
        llm = LinearLayoutManager(this);
        rcvMessage.layoutManager = llm;
        initlayout()
    }
    fun initlayout(){
      //  ivChatProfileImage.loadBase64Image(prefs.profileimage)
        ivSendMessage.setOnClickListener {
            lsMessage=etSendMessage.text.toString()
            if(!lsMessage.equals("")){
                sendMessage()
            }
        }
        ivBack.setOnClickListener {
            onBackPressed()
        }
    }

    fun getChats()
    {
        pd.show()
        Fuel.post(StaticRefs.CHATSHOW, listOf((StaticRefs.CHATVENDORID to vendorid), (StaticRefs.CHATCUSTOMERID to prefs.cust_id)))
                .responseJson()
                { request,
                  response,
                  result ->
                    result.fold({ d ->
                        parseJson(result.get().content)
                       // pd.dismiss()
                    }, { err ->
                      //  pd.dismiss()
                        TastyToast.makeText(context, "Internet Network Down", TastyToast.LENGTH_LONG, TastyToast.ERROR)
                    })
                }

    }

    fun parseJson(data: String) {

        val json = JSONObject(data)
        if (json.has(StaticRefs.STATUS) && json.getString(StaticRefs.STATUS) != null) {

            var message = ""
            if (json.has(StaticRefs.MESSAGE) && json.getString(StaticRefs.MESSAGE) != null) {

                message = json.getString(StaticRefs.MESSAGE)
            }
            if (json.getString(StaticRefs.STATUS).equals(StaticRefs.FAILED)) {

                val error = json.getString("errors")

                TastyToast.makeText(this, error, Toast.LENGTH_LONG, TastyToast.ERROR).show()

            } else {


                val json = JSONObject(data)
                val response = json.getString(StaticRefs.DATA)
                val parser = Parser()
                val stringBuilder = StringBuilder(response)
                val model = parser.parse(stringBuilder) as JsonArray<JsonObject>
                val price_model = model.map { Message_Model(it) }.filterNotNull();

                adapter = Message_Adapter(price_model);
                rcvAdapter = adapter!!
                rcvMessage.adapter = rcvAdapter;
                llm.setStackFromEnd(true)
                rcvAdapter.notifyDataSetChanged();
                pd.dismiss()


            }
        }


    }
    fun sendMessage(){

        Fuel.post(StaticRefs.CHATSENDMESSAGE, listOf((StaticRefs.CHATVENDORID to vendorid),(StaticRefs.CHATSENDER to CHATSENDER),
                (StaticRefs.CHATMESSAGE to lsMessage),(StaticRefs.CHATTXNID to "1"),
                (StaticRefs.CHATCUSTOMERID to prefs.cust_id)))
                .responseJson()
                { request,
                  response,
                  result ->
                    result.fold({ d ->
                        ParseSendMessageResponse(result.get().content)
                        // pd.dismiss()
                    }, { err ->
                        //  pd.dismiss()
                        TastyToast.makeText(context, "Internet Network Down", TastyToast.LENGTH_LONG, TastyToast.ERROR)
                    })
                }

    }
    fun ParseSendMessageResponse(response:String){
        val json = JSONObject(response)
        if (json.has(StaticRefs.STATUS) && json.getString(StaticRefs.STATUS) != null) {

            var message = ""
            if (json.has(StaticRefs.MESSAGE) && json.getString(StaticRefs.MESSAGE) != null) {

                message = json.getString(StaticRefs.MESSAGE)
            }
            if (json.getString(StaticRefs.STATUS).equals(StaticRefs.FAILED)) {

                val error = json.getString("errors")

                TastyToast.makeText(this, error, Toast.LENGTH_LONG, TastyToast.ERROR).show()

            } else {


                etSendMessage.setText("")
                getChats()


            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}