package com.GoMobeil.H2H.UI

import android.os.Bundle
import com.GoMobeil.H2H.BaseActivity
import com.GoMobeil.H2H.Extensions.loadBase64Image
import com.GoMobeil.H2H.Models.ArticlesModel
import com.GoMobeil.H2H.R
import com.GoMobeil.H2H.StaticRefs
import com.GoMobeil.H2H.prefs
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.sdsmdg.tastytoast.TastyToast
import kotlinx.android.synthetic.main.articles_detail.*
import kotlinx.android.synthetic.main.customerreview_row.*
import org.json.JSONObject

class Articles_Detail :BaseActivity() {

    lateinit var id: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.articles_detail)
        setTitle("Article Detail")

        var title = getIntent().getStringExtra(ArticlesModel.ART_TITLE)
        var description = getIntent().getStringExtra(ArticlesModel.ART_DESCRIPTION)
        var img = getIntent().getStringExtra(ArticlesModel.IMG_NAME)
//        id = getIntent().getStringExtra(ArticlesModel.ART_ID)

        tvTitle.setText(title!!.toString())
        tvDescripton1.setText(description!!.toString())

        if (img!!.length > 0) {
            try {
                ivImage1.loadBase64Image(img);
            } catch (e: Exception) {
                println("Exception e" + e.toString())
            }
        }
    }

    /*fun getData() {
        Fuel.post(StaticRefs.URLSHOWARTICLE, listOf(ArticlesModel.ART_ID to id))
                .responseJson()
                { request,
                  response,
                  result ->
                    // parseProfile(result.get().content)
                    result.fold({ d ->
                        parseData(result.get().content)
                    }, { err ->
                        TastyToast.makeText(context, "Internet Network Down", TastyToast.LENGTH_LONG, TastyToast.ERROR)
                        //do something with error
                    })
                }
    }

    fun parseData(response: String) {

        var jsonob: JSONObject? = null
        // data = JSONObject(response).getJSONObject("data")
        val json = JSONObject(response)
        if (response.contains(StaticRefs.DATA) && json.getString(StaticRefs.DATA) != null && !json.getString(StaticRefs.DATA).equals("")) {

            val data = json.getJSONObject(StaticRefs.DATA)
            val data1 = json.getString(StaticRefs.DATA)
            val jsondata = JSONObject(data1)
            if (jsondata.length() > 0) {


               // addjson = data.getJSONArray(StaticRefs.RESIDENTIALADD)
            }
        }
    }
*/}

