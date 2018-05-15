package com.GoMobeil.H2H.UI

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.GoMobeil.H2H.*
import com.GoMobeil.H2H.Extensions.OnItemClickListener
import com.GoMobeil.H2H.Extensions.addOnItemClickListener
import com.GoMobeil.H2H.Extensions.toast
import com.GoMobeil.H2H.Models.SCModel
import com.GoMobeil.H2H.Models.ServicesModel
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.sdsmdg.tastytoast.TastyToast
import kotlinx.android.synthetic.main.base_layout.*
import kotlinx.android.synthetic.main.services_list.*
import kotlinx.android.synthetic.main.services_list_row.*
import org.json.JSONObject
/**
 * Created by ADMIN on 31-01-2018.
 */

class ServiceList : BaseActivity()
{

    override lateinit var context: Context
    override lateinit var activity : Activity
    var liCatId : Int? = null
    var liCatName : String? = null
    lateinit var llm : LinearLayoutManager
    lateinit var rcvAdapter : RecyclerView.Adapter<ServiceAdapter.ViewHolder>
    var adapter :ServiceAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.services_list)

        context = this
        activity = this

        initLayout()
    }

    fun initLayout() : Unit
    {

      /*  liCatId = 1
        liCatName = "Abc"*/

        liCatId = getIntent().getIntExtra(SCModel.SC_ID, 0)
        liCatName = getIntent().getStringExtra(SCModel.SC_NAME)

        setTitle("$liCatName")
        llm = LinearLayoutManager(applicationContext);
        rcvServiceList.layoutManager = llm;
        getServices(liCatId!!)
    }

    fun getServices(liCatId : Int)
    {
        Fuel.post(StaticRefs.SERVICES,listOf((StaticRefs.TOKEN to prefs.token),(SCModel.SC_ID to liCatId)))
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

    fun parseJson(data : String)
    {
        val json = JSONObject(data)

        val response = json.getString(StaticRefs.DATA)
        val parser = Parser()
        val stringBuilder = StringBuilder(response)
        val model =  parser.parse(stringBuilder) as JsonArray<JsonObject>
        val serviceModel = model.map{ ServicesModel(it) }.filterNotNull()
        adapter = ServiceAdapter(serviceModel);
        rcvAdapter = adapter!!
        rcvServiceList.adapter = rcvAdapter;
        rcvAdapter.notifyDataSetChanged();
        adapter!!.setMultiple(false)

        adapter!!.setOnCardClickListener(object : ServiceAdapter.onItemClickListener{

            override fun onItemClick(position: Int, view: View) {
                if (view.id == tvService.id){
                    var service_id = serviceModel.get(position).srv_id
                    var service_name = serviceModel.get(position).srv_name
                    var service_desc = serviceModel.get(position).srv_description

                    val intent = Intent(context, ServiceDetails::class.java)
                    intent.putExtra(ServicesModel.SRV_ID,service_id)
                    intent.putExtra(ServicesModel.SRV_NAME,service_name)
                    intent.putExtra(ServicesModel.SRV_DESCRIPTION,service_desc)
                    startActivity(intent)
                }

                if (view.id ==tvDescription.id){
                    var service_id = serviceModel.get(position).srv_id
                    var service_name = serviceModel.get(position).srv_name
                    var service_desc = serviceModel.get(position).srv_description

                    val intent = Intent(context, ServiceDetails::class.java)
                    intent.putExtra(ServicesModel.SRV_ID,service_id)
                    intent.putExtra(ServicesModel.SRV_NAME,service_name)
                    intent.putExtra(ServicesModel.SRV_DESCRIPTION,service_desc)
                    startActivity(intent)
                }

                if (view.id == ivService.id){
                    var service_id = serviceModel.get(position).srv_id
                    var service_name = serviceModel.get(position).srv_name
                    var service_desc = serviceModel.get(position).srv_description

                    val intent = Intent(context, ServiceDetails::class.java)
                    intent.putExtra(ServicesModel.SRV_ID,service_id)
                    intent.putExtra(ServicesModel.SRV_NAME,service_name)
                    intent.putExtra(ServicesModel.SRV_DESCRIPTION,service_desc)
                    startActivity(intent)
                }
            }
        })





    }
}