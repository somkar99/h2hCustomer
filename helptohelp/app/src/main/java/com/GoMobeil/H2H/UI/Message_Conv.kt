package com.GoMobeil.H2H.UI
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.GoMobeil.H2H.Adapters.Message_Conv_Adapter
import com.GoMobeil.H2H.BaseActivity
import com.GoMobeil.H2H.Models.Message_Conv_Model
import com.GoMobeil.H2H.R
import com.GoMobeil.H2H.Services.TransperantProgressDialog
import com.GoMobeil.H2H.StaticRefs
import com.GoMobeil.H2H.UI.Message
import com.GoMobeil.H2H.prefs
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.sdsmdg.tastytoast.TastyToast
import kotlinx.android.synthetic.main.message.*
import kotlinx.android.synthetic.main.message_conv.*

import org.json.JSONObject

/**
 * Created by apple on 16/04/18.
 */

class Message_Conv: BaseActivity() {

     override lateinit var context: Context
     override lateinit var activity: Activity
    lateinit var llm: LinearLayoutManager
    lateinit var lsMessage: String
    lateinit var rcvAdapter: RecyclerView.Adapter<Message_Conv_Adapter.ViewHolder>
    var adapter: Message_Conv_Adapter? = null
    lateinit var pd: TransperantProgressDialog
    var entity_type ="C"

    companion object {
        var TAG: String? = "Message_Conv"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.message_conv)
       // setTitle("Messages")

        activity = this
        context = this
        pd = TransperantProgressDialog(context)

        getChats()

        }

    fun getChats()
    {
            pd.show()

            Fuel.post(StaticRefs.CONVERSATIONS, listOf((StaticRefs.CHAT_ENTITY_ID to prefs.cust_id),StaticRefs.CHAT_ENTITY_TYPE to entity_type))
                    .timeoutRead(StaticRefs.TIMEOUTREAD)
                    .responseJson()
                    { request,
                      response,
                      result ->
                        result.fold({ d ->
                            parseJson(result.get().content)
                            pd.dismiss()
                        }, { err ->
                            pd.dismiss()
                            TastyToast.makeText(context, "Internet Network Down", TastyToast.LENGTH_LONG, TastyToast.ERROR)
                        })
                    }

        }

        fun parseJson(data: String) {
            val json = JSONObject(data)
            val response = json.getString(StaticRefs.DATA)
            val parser = Parser()
            val stringBuilder = StringBuilder(response)
            val model = parser.parse(stringBuilder) as JsonArray<JsonObject>
            val message = model.map { Message_Conv_Model(it) }.filterNotNull();

            llm = LinearLayoutManager(this);
            rcvMessageConv.layoutManager = llm;


            adapter = Message_Conv_Adapter(message);
            rcvAdapter = adapter!!
            rcvMessageConv.adapter = rcvAdapter;
            rcvAdapter.notifyDataSetChanged();

            adapter!!.setOnCardClickListener(object : Message_Conv_Adapter.onItemClickListener {
                override fun onItemClick(position: Int) {
                   // var custid = message.get(position).tc_custid.toString()
                    var txnid = message.get(position).tc_txnid.toString()
                    var vendorid = message.get(position).tc_spid.toString()
                    var vendorfirstname = message.get(position).sp_firstname
                    var vendorlastname = message.get(position).sp_lastname
                    var vendorImage = message.get(position).sp_image



                    val intent = Intent(context, Message::class.java)
                    intent.putExtra(Message_Conv_Model.SP_ID, vendorid)
                    intent.putExtra(Message_Conv_Model.SP_FIRSTNAME, vendorfirstname)
                    intent.putExtra(Message_Conv_Model.SP_LASTNAME, vendorlastname)
                    intent.putExtra(Message_Conv_Model.SP_IMAGE,vendorImage)
                    intent.putExtra(StaticRefs.KEY, "CONV")
                    intent.putExtra(Message_Conv_Model.TXN_ID, txnid)
                    startActivity(intent)
                }
            })
    }
}
