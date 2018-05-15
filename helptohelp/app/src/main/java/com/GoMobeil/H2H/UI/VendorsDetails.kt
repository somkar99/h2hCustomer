package com.GoMobeil.H2H.UI

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.GoMobeil.H2H.Adapters.PricingAdapter
import com.GoMobeil.H2H.Adapters.WorkImgAdapter
import com.GoMobeil.H2H.BaseActivity
import com.GoMobeil.H2H.Extensions.loadBase64Image
import com.GoMobeil.H2H.Extensions.toast
import com.GoMobeil.H2H.Models.PricingModel
import com.GoMobeil.H2H.Models.WorkImgModel
import com.GoMobeil.H2H.R
import com.GoMobeil.H2H.R.color.*
import com.GoMobeil.H2H.StaticRefs
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.sdsmdg.tastytoast.TastyToast
import kotlinx.android.synthetic.main.vendor_details.*
import org.json.JSONObject

/**
 * Created by Admin on 25-03-18.
 */
class VendorsDetails : BaseActivity() {

    override lateinit var activity: Activity
    override lateinit var context: Context


    var priBusiness: String? = null

    var vendorFName: String? = null
    var vendorLName: String? = null
    var vendorMobile: String? = null

    var vendorImage: String? = null

    var pb_businessname :String? = null
    var pb_establishon :String? = null
    var pb_usp :String? = null
    var pb_introduction :String? = null
    var pb_websiteURL :String? = null
    var pb_serviceArea :String? = null
    var pb_busi_contactno :String? = null

    var pb_addlime1:String? = null
    var pb_addlime2:String? = null
    var pb_city:String? = null
    var pb_state:String? = null
    var pb_pincode:String? = null

    var pst_typedesc :String? = null
    var pst_priceunit:String? = null
    var pst_pricetype:String? = null
    var pst_visitingcharge:String? = null
    var pst_cost_from:String? = null
    var pst_cost_to:String? = null
    var pst_cost_remark:String? = null





    var workImagAdter: WorkImgAdapter? = null
    lateinit var rcvWorkImgAdapter: RecyclerView.Adapter<WorkImgAdapter.ViewHolder>
    lateinit var workimgLlm: RecyclerView.LayoutManager

    var pricingAdapter : PricingAdapter? = null
    lateinit var rcvPricingAdapter:RecyclerView.Adapter<PricingAdapter.ViewHolder>
    lateinit var pricinglm:RecyclerView.LayoutManager


    var sp_id: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.vendor_details)

        activity = this
        context = this
        sp_id = getIntent().getStringExtra("sp_id")

        initLayout()

        //    getWorkImages()


    }

    fun initLayout() {


        setTitle("Vendor Details")


        tvWorkAlbum.setOnClickListener {
            viewOverview.setBackgroundColor(resources.getColor(green_50))
            viewWorkAlbum.setBackgroundColor(resources.getColor(black))
            viewReview.setBackgroundColor(resources.getColor(green_50))
            llWorkAlbum.visibility = View.VISIBLE
            llOverview.visibility = View.GONE
            llPricing.visibility = View.GONE

            llOverview1.visibility=View.GONE

            view1.visibility=View.GONE



        }
        tvPricing.setOnClickListener {
            viewReview.setBackgroundColor(resources.getColor(black))
            viewOverview.setBackgroundColor(resources.getColor(green_50))
            viewWorkAlbum.setBackgroundColor(resources.getColor(green_50))
            llWorkAlbum.visibility = View.GONE
            llOverview.visibility = View.GONE
            llPricing.visibility = View.VISIBLE
            llOverview1.visibility=View.GONE

            view1.visibility=View.GONE

        }
        tvOverview.setOnClickListener {
            viewOverview.setBackgroundColor(resources.getColor(black))
            viewReview.setBackgroundColor(resources.getColor(green_50))
            viewWorkAlbum.setBackgroundColor(resources.getColor(green_50))
            llWorkAlbum.visibility = View.GONE
            llOverview.visibility = View.VISIBLE
            llPricing.visibility = View.GONE

            llOverview1.visibility=View.VISIBLE

            view1.visibility=View.VISIBLE

        }

        workimgLlm = GridLayoutManager(applicationContext, 2)
        rcvWorkAlbum.layoutManager = workimgLlm

        pricinglm = LinearLayoutManager(applicationContext,LinearLayoutManager.VERTICAL,false)
        rcvPricing.layoutManager = pricinglm

        getVendorsDetails()
        getBusinessDetails()
        getPricingDetails()
    }

    fun getVendorsDetails() {

        Fuel.post(StaticRefs.VENDORSDETAILS, listOf((StaticRefs.SP_ID to sp_id)))
                .responseJson()
                { request,
                  response,
                  result ->


                    result.fold({ d ->
                        parseJson(result.get().content)

                    }, { err ->
                        TastyToast.makeText(context, "Internet Network Down", TastyToast.LENGTH_LONG, TastyToast.ERROR)
                    })
                }

    }


    fun parseJson(response: String) {


        val json = JSONObject(response)
        val data = JSONObject(response).getJSONObject(StaticRefs.DATA)

        priBusiness = data.getString("sp_primarybusiness")
        vendorFName = data.getString("sp_firstname")
        vendorLName = data.getString("sp_lastname")
        vendorMobile = data.getString("sp_mobileno")
        vendorImage = data.getString("sp_image")

        tvSp_primarybusiness.setText(priBusiness)


        if (!vendorImage.equals(null) && !vendorImage.equals("null") && !vendorImage.equals("")) {

            ivVendor.loadBase64Image(vendorImage)
        } else {
//            toast("Image null")
            TastyToast.makeText(context,"Image null",TastyToast.LENGTH_LONG,TastyToast.ERROR).show()
        }

        tvName.setText(vendorFName + vendorLName)

        val jsonWrkImg = json.getString(StaticRefs.DATA)
        val jsonMain = JSONObject(jsonWrkImg)

        val wrkImgArr = jsonMain.getString("workimages")


        val parser = Parser()
        val stringBuilder = StringBuilder(wrkImgArr)
        val model = parser.parse(stringBuilder) as JsonArray<JsonObject>

        val workimageModel = model.map { WorkImgModel(it) }.filterNotNull()

        workImagAdter = WorkImgAdapter(workimageModel)
        rcvWorkImgAdapter = workImagAdter!!
        rcvWorkAlbum.adapter = rcvWorkImgAdapter

        rcvWorkImgAdapter.notifyDataSetChanged()


    }



    fun getBusinessDetails(){

        Fuel.post(StaticRefs.BUSINESSDETAILS, listOf((StaticRefs.PB_SPID to sp_id)))
                .responseJson(){

                    request,
                    response,
                    result ->

                    result.fold({ d ->
                        parseBusiness(result.get().content)
                    }, { err ->
                        TastyToast.makeText(context,"Internet Network Down", TastyToast.LENGTH_LONG, TastyToast.ERROR)
                    })
                }
    }


    fun parseBusiness(response: String){

        val json = JSONObject(response)
        val data = JSONObject(response).getJSONObject(StaticRefs.DATA)

        pb_businessname = data.getString("pb_businessname")
        pb_introduction = data.getString("pb_introduction")
        pb_usp = data.getString("pb_usp")
        pb_establishon = data.getString("pb_establishon")
        pb_busi_contactno = data.getString("pb_businesscontactno")
        pb_websiteURL = data.getString("pb_websiteurl")
        pb_serviceArea = data.getString("pb_servicearea")


        tvBusinessName.setText(pb_businessname)
        tvBusinessIntro.setText(pb_introduction)
        tvBusinessUSP.setText(pb_usp)
        tvBusinessEstablishOn.setText(pb_establishon)
        tvBusinessContactNo.setText(pb_busi_contactno)



        val jsonBusiness =  json.getString(StaticRefs.DATA)
        val jsonMain = JSONObject(jsonBusiness)

      /*  val BusinessArr = jsonMain.getString("businessadd")

        val businessData = JSONObject(StaticRefs.DATA).getJSONObject("businessadd")

        pb_addlime1 = businessData.getString("add_line1")
        pb_addlime2 = businessData.getString("add_line2")
        pb_city = businessData.getString("add_city")
        pb_state = businessData.getString("add_state")
        pb_pincode = businessData.getString("add_pincode")*/

    }


    fun getPricingDetails(){

        Fuel.post(StaticRefs.PRICINGDETAILS, listOf((StaticRefs.PST_SPID to sp_id)))
                .responseJson(){

                    request,
                    response,
                    result ->
                    result.fold({ d ->
                        parsePricingDetails(result.get().content)
                    }, { err ->
                        TastyToast.makeText(context,"Internet Network Down", TastyToast.LENGTH_LONG, TastyToast.ERROR)
                    })

                }
    }


    fun parsePricingDetails(response: String){
        val json = JSONObject(response)
        val data = JSONObject(response).getString(StaticRefs.DATA)

        val parser = Parser()
        val stringBuilder = StringBuilder(data)
        val model = parser.parse(stringBuilder) as JsonArray<JsonObject>

        var pricingModel = model.map { PricingModel(it) }

        pricingAdapter = PricingAdapter(pricingModel)
        rcvPricingAdapter = pricingAdapter!!
        rcvPricing.adapter = rcvPricingAdapter

        rcvPricingAdapter.notifyDataSetChanged()




    }


}










