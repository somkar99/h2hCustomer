package com.GoMobeil.H2H.UI

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.DragEvent
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import com.GoMobeil.H2H.Adapters.ArticlesAdapter
import com.GoMobeil.H2H.Adapters.FaqsAdapter
import com.GoMobeil.H2H.Adapters.VendorListAdapter
import com.GoMobeil.H2H.BaseActivity
import com.GoMobeil.H2H.Extensions.OnItemClickListener
import com.GoMobeil.H2H.Extensions.addOnItemClickListener
import com.GoMobeil.H2H.Extensions.loadBase64Image
import com.GoMobeil.H2H.Extensions.toast
import com.GoMobeil.H2H.Models.ArticlesModel
import com.GoMobeil.H2H.Models.FaqsModel
import com.GoMobeil.H2H.Models.SCModel
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
import kotlinx.android.synthetic.main.base_layout.*
import kotlinx.android.synthetic.main.faq_row.*
import kotlinx.android.synthetic.main.home.*
import kotlinx.android.synthetic.main.service_details.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by ADMIN on 03-02-2018.
 */
class ServiceDetails : BaseActivity()
{
    companion object {
        var TAG : String? = "ServiceDetails"
    }

    lateinit var popup: FAQPopup
    lateinit var rcvsFAQ : RecyclerView

     var lsImag :String? = null

     lateinit var ivFAQ :ImageView
    lateinit var pd: TransperantProgressDialog


    override lateinit var context : Context
    override lateinit var activity : Activity
        var service_id  : Int? = null
        var service_name : String? = null
    var articlesAdapter : ArticlesAdapter? = null
    lateinit var rcvArticlesAdapter : RecyclerView.Adapter<ArticlesAdapter.ViewHolder>
    lateinit var articlesLlm : RecyclerView.LayoutManager

    var faqsAdapter : FaqsAdapter? = null
    lateinit var rcvFaqsAdapter : RecyclerView.Adapter<FaqsAdapter.ViewHolder>
    lateinit var faqsLlm : RecyclerView.LayoutManager
    var popW : PopupWindow? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.service_details)

        context = this
        activity = this



        initLayout()
    }

    fun initLayout() : Unit
    {
        service_id = getIntent().getIntExtra(ServicesModel.SRV_ID, 0)
        service_name = getIntent().getStringExtra(ServicesModel.SRV_NAME)
        ivFAQ = findViewById<ImageView>(R.id.ivFAQ);

        pd = TransperantProgressDialog(context)

        setTitle("$service_name")


        ivFAQ.visibility = View.VISIBLE
      /*  cbVendorList.setOnClickListener(View.OnClickListener {

            val intent = Intent(context, VendorList::class.java)
            intent.putExtra(ServicesModel.SRV_ID, service_id!!)
            intent.putExtra(ServicesModel.SRV_NAME, service_name)
            startActivity(intent)
        })
*/
        getServiceArticles(service_id)
        getServiceData(service_id)

        cbContinue.setOnClickListener(View.OnClickListener {
            val intent = Intent(context, ServiceQuestions::class.java)
            intent.putExtra(ServicesModel.SRV_ID, service_id!!)
            intent.putExtra(ServicesModel.SRV_NAME, service_name)
            intent.putExtra(StaticRefs.SRV_IMAGE,lsImag)
            startActivity(intent)
        })

        articlesLlm = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
        rcvDetailsArticles.layoutManager = articlesLlm

        faqsLlm = LinearLayoutManager(applicationContext)




        ivFAQ.setOnClickListener {

             getServiceFaqs(service_id)
        }
    }

    fun getServiceData(serv_id : Int?){
        pd.show()
        Fuel.post(StaticRefs.SERVICES,listOf((StaticRefs.TOKEN to prefs.token),(ServicesModel.SRV_ID to serv_id)))
                .responseJson()
                {
                    request,
                    response,
                    result ->
                    result.fold({ d ->
                        parseJson(result.get().content)

                    }, { err ->
                        pd.dismiss()
                        TastyToast.makeText(context,"Internet Network Down",TastyToast.LENGTH_LONG,TastyToast.ERROR)
                    })


                }

    }

    fun parseJson(response: String){

        val json = JSONObject(response)
        val data = json.getString(StaticRefs.DATA)

        try {
            var jsonArr = JSONArray(data)
            lsImag = jsonArr.getJSONObject(0).getString(StaticRefs.SRV_IMAGE)


            if (!lsImag.equals(null) && !lsImag.equals("null") && !lsImag.equals("")){

                pd.dismiss()
                ivServiceImage.loadBase64Image(lsImag)
            }
            else{
                pd.dismiss()
                TastyToast.makeText(context,"No Image Found",TastyToast.LENGTH_LONG,TastyToast.ERROR).show()
            }

        }
        catch (e : Exception){
            e.printStackTrace()
        }







    }

    fun getServiceArticles(serv_id : Int?)
    {

        Fuel.post(StaticRefs.SERVICES,listOf((StaticRefs.TOKEN to prefs.token),(ServicesModel.SRV_ID to serv_id)))
                .responseJson()
                {
                    request,
                    response,
                    result ->
                    result.fold({ d ->
                        parseArticles(result.get().content)

                    }, { err ->

                        TastyToast.makeText(context,"Internet Network Down",TastyToast.LENGTH_LONG,TastyToast.ERROR)
                    })


                }
    }

    fun parseArticles(data: String)
    {
            val json = JSONObject(data)
            val response = json.getString(StaticRefs.DATA)

            try {
                var jsonArray = JSONArray(response)

                var jsonAs: String? = null
                for (i in 0..jsonArray.length() - 1) {
                    var jsonObj = jsonArray.getJSONObject(i)
                    jsonAs = jsonObj.getString(StaticRefs.ARTICLES)
                }

                var jsonOb = JSONObject()
                jsonOb.put(StaticRefs.DATA, jsonAs)
                parseArticle1(jsonOb.toString())

                /*val parser = Parser()
                val stringBuilder = StringBuilder(jsonAs)
                val model =  parser.parse(stringBuilder) as JsonArray<JsonObject>
                val articleModel = model.map{ ArticlesModel(it) }.filterNotNull();
                articlesAdapter = ArticlesAdapter(articleModel);
                rcvArticlesAdapter = articlesAdapter!!
                rcvDetailsArticles.adapter = rcvArticlesAdapter;
                rcvArticlesAdapter.notifyDataSetChanged();

                var jsonOb = JSONObject()
                jsonOb.put(StaticRefs.DATA, jsonAs)*/
            } catch (e: Exception) {
            }

    }

    fun parseArticle1(response : String)
    {
        val json = JSONObject(response)
        val response1 = json.getString(StaticRefs.DATA)

        val parser = Parser()
        val stringBuilder = StringBuilder(response1)
        val model =  parser.parse(stringBuilder) as JsonArray<JsonObject>
        val articleModel = model.map{ ArticlesModel(it) }.filterNotNull();
        articlesAdapter = ArticlesAdapter(articleModel);
        rcvArticlesAdapter = articlesAdapter!!
        rcvDetailsArticles.adapter = rcvArticlesAdapter;
        rcvArticlesAdapter.notifyDataSetChanged()

        rcvArticles.addOnItemClickListener( object : OnItemClickListener
        {
            override fun onItemClicked(view: View, position: Int) {
                //toast("Hello Details article")
            }
        })
    }


    fun getServiceFaqs(serv_id : Int?)
    {
        Fuel.post(StaticRefs.SERVICES,listOf((StaticRefs.TOKEN to prefs.token),(ServicesModel.SRV_ID to serv_id)))
                .responseJson()
                {
                    request,
                    response,
                    result -> parseFaqs(result.get().content)
                }
    }

    fun parseFaqs(data: String)
    {
        val json = JSONObject(data)
        val response = json.getString(StaticRefs.DATA)

        try {
            var jsonArray = JSONArray(response)
            var jsonAs: String? = null
            for (i in 0..jsonArray.length() - 1) {
                var jsonObj = jsonArray.getJSONObject(i)
                jsonAs = jsonObj.getString(StaticRefs.FAQ)
            }

            var jsonOb = JSONObject()
            jsonOb.put(StaticRefs.DATA, jsonAs)
            parseFAQS1(jsonOb.toString())

            /*val parser = Parser()
            val stringBuilder = StringBuilder(jsonAs)
            val model =  parser.parse(stringBuilder) as JsonArray<JsonObject>
            val articleModel = model.map{ ArticlesModel(it) }.filterNotNull();
            articlesAdapter = ArticlesAdapter(articleModel);
            rcvArticlesAdapter = articlesAdapter!!
            rcvDetailsArticles.adapter = rcvArticlesAdapter;
            rcvArticlesAdapter.notifyDataSetChanged();

            var jsonOb = JSONObject()
            jsonOb.put(StaticRefs.DATA, jsonAs)*/
        } catch (e: Exception) {
        }
    }

    fun parseFAQS1(response : String)
    {
        val json = JSONObject(response)
        val response1 = json.getString(StaticRefs.DATA)

        val parser = Parser()
        val stringBuilder = StringBuilder(response1)
        val model =  parser.parse(stringBuilder) as JsonArray<JsonObject>
        val faqModel = model.map{ FaqsModel(it) }.filterNotNull();

        popup = FAQPopup(context,faqModel,service_name)

        popW = PopupWindow(popup!!.popView,LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT)
        popW!!.showAtLocation(popup.popView.findViewById(R.id.faqParent), Gravity.NO_GRAVITY, Gravity.NO_GRAVITY,0);




        popup.ivBack.setOnClickListener{
            popW!!.dismiss()
        }




        }

    override fun onBackPressed() {
        if (popW != null && popW!!.isShowing)
        {
            popW!!.dismiss()
            return
        }

        super.onBackPressed()
    }




    }



