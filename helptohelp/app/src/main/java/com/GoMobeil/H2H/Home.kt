package com.GoMobeil.H2H

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import com.GoMobeil.H2H.Extensions.OnItemClickListener
import com.GoMobeil.H2H.Extensions.addOnItemClickListener
import com.GoMobeil.H2H.Extensions.toast

import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.daimajia.slider.library.Indicators.PagerIndicator
import com.daimajia.slider.library.SliderLayout
import com.daimajia.slider.library.SliderTypes.BaseSliderView
import com.daimajia.slider.library.SliderTypes.TextSliderView
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import org.json.JSONObject
import android.widget.RelativeLayout
import android.view.ViewGroup.MarginLayoutParams
import android.widget.HorizontalScrollView
import com.GoMobeil.H2H.Adapters.*
import com.GoMobeil.H2H.Models.*
import com.GoMobeil.H2H.Services.CustomServices
import com.GoMobeil.H2H.Services.GPSTracker
import com.GoMobeil.H2H.UI.*
import com.sdsmdg.tastytoast.TastyToast
import kotlinx.android.synthetic.main.base_layout.*
import kotlinx.android.synthetic.main.footer.*
import kotlinx.android.synthetic.main.home.*
import kotlinx.android.synthetic.main.services_list.*
import org.json.JSONArray
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by niranjanshah on 29/01/18.
 */


class Home : BaseActivity(){

    var bannerList: ArrayList<BannerModel> = ArrayList()
    lateinit var rcvList : RecyclerView
    lateinit var layoutManager : RecyclerView.LayoutManager
    lateinit var articlesLlm : RecyclerView.LayoutManager
    lateinit var repairlm : RecyclerView.LayoutManager
    lateinit var cleaninglm : RecyclerView.LayoutManager
    lateinit var searchlm : RecyclerView.LayoutManager
    override lateinit var context : Context
    override lateinit var activity : Activity
    var adapter: SCAdapter? = null
    var SAdapter :HomeAdapter? = null
    var SearchAdapter : SearchAdapter? = null
    lateinit var rcvAdapter : RecyclerView.Adapter<SCAdapter.ViewHolder>
    lateinit var rcvSAdater : RecyclerView.Adapter<HomeAdapter.ViewHolder>
    lateinit var rcvSearchAdapter : RecyclerView.Adapter<SearchAdapter.ViewHolder>
    var articlesAdapter : ArticlesAdapter? = null
    lateinit var rcvArticlesAdapter : RecyclerView.Adapter<ArticlesAdapter.ViewHolder>
    var alImages : ArrayList<String>? = ArrayList()
    var searchModel: MutableList<SearchModel>? = mutableListOf()
    var LOCATION_REQUEST_CODE : Int? = 100
    lateinit var rcvSearch: RecyclerView

    //var rcvSearch :RecyclerView? =null

    var pincode: String? = null
    lateinit var gps : GPSTracker

    var lsSearch :String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)
        context = this
        activity = this

        ivActivityTitle.visibility = View.VISIBLE

        CustomServices.hideKB(activity)


        setUpView();



    }


    fun setUpView()
    {  rcvSearch = findViewById(R.id.rcvSearch)
        ivBack.visibility = View.GONE
        tvActivityTitle.visibility = View.GONE
        ivMenu.visibility = View.VISIBLE
        tvActivityTitle.visibility = View.VISIBLE
        tvCustName.setText(prefs.fname)

        ivNotification.visibility = View.GONE
        if (prefs.user.equals("")|| prefs.user.equals(null)){

            ivLogout.visibility = View.GONE
            tvWelcome.visibility = View.GONE
            tvCustName.visibility = View.GONE
            ivLogin.visibility = View.VISIBLE

        }
        else{
            ivLogout.visibility = View.VISIBLE
            tvWelcome.visibility = View.VISIBLE
            tvCustName.visibility = View.VISIBLE
            ivLogin.visibility = View.GONE
            tvCustName.setText(prefs.fname)
        }


        ivLogout.setOnClickListener {

            val intent = Intent(this,Home::class.java)
            prefs.token = ""
            prefs.user = ""
            startActivity(intent)
            finish()
        }


        llFMsg.setOnClickListener {
            val intent = Intent(context, Message_Conv::class.java)
            startActivity(intent)
        }

        llFSetting.setOnClickListener {
            if (App.prefs!!.user.equals("") || App.prefs!!.user.equals(null)){

                TastyToast.makeText(context,"Please Log in to Change the setting",TastyToast.LENGTH_LONG,TastyToast.INFO).show()
            }
            else{

                val intent = Intent(this, Setting::class.java)
                startActivity(intent)

            }

        }




        pincode = prefs.pincode

        gps = GPSTracker(context)

        if(pincode == null || pincode.equals(""))
        {
            checkLocationPermission()
        }
        else
        {
            // toast("have pincode $pincode")
        }

        rcvList = findViewById(R.id.rcvCategory)
        //rcvSearch = findViewById(R.id.rcvSearch)
        layoutManager = GridLayoutManager(applicationContext,3) as RecyclerView.LayoutManager;
        rcvList.layoutManager = layoutManager;

        repairlm = LinearLayoutManager(applicationContext,LinearLayoutManager.HORIZONTAL,false)
        rcvRepairs.layoutManager =repairlm

        cleaninglm = LinearLayoutManager(applicationContext,LinearLayoutManager.HORIZONTAL,false)
        rcvCleaning.layoutManager = cleaninglm

        articlesLlm = LinearLayoutManager(applicationContext,LinearLayoutManager.HORIZONTAL, false)
        rcvArticles.layoutManager = articlesLlm


        searchlm = LinearLayoutManager(applicationContext);
        rcvSearch.layoutManager = searchlm;




        //showBanner()
        getData();
        getBanners();
        getArticles()
        getRepairServices()
        getCleaningServices()

        /*  etSearch.addTextChangedListener(object : TextWatcher {
       override fun afterTextChanged(s: Editable?) {
           val lsprimarybusiness = etPrimaryBusiness.text.toString().trim()
           if (lsprimarybusiness.length > 0) {
               if (serviceModel!!.size > 0) {
                   serviceModel?.clear()
               }
               rcvServicelist.visibility = View.VISIBLE
               getPrimaryBusiness(lsprimarybusiness)
           }
       }
   }*/

        etSearchServices.addTextChangedListener(object :  TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val lsSearch = etSearchServices.text.toString().trim()
                if (lsSearch.length>0){
                    if (searchModel!!.size > 0) {
                        searchModel?.clear()
                        adapter!!.notifyDataSetChanged()
                    }
                    rcvSearch!!.visibility = View.VISIBLE
                    searchServices(lsSearch)
                }
                else{
                    searchModel?.clear()
                    adapter!!.notifyDataSetChanged()
                    rcvSearch!!.visibility = View.GONE

                }

            }

        })
        etSearchServices.onFocusChangeListener = object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                rcvSearch.visibility = View.GONE
            }
        }
    }




    fun checkLocationPermission()
    {
        val permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    this!!.LOCATION_REQUEST_CODE!!)

            //    getLatLng()
        }
        else
        {
            //   getLatLng()
        }
    }


    fun  getData()
    {
        Fuel.post(StaticRefs.URLServiceCat,listOf(StaticRefs.TOKEN to prefs.token))
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

        /* if(json.getString(StaticRefs.STATUS).equals(StaticRefs.SUCCESS))
         {*/
        val response = json.getString(StaticRefs.DATA)
        val parser = Parser()
        val stringBuilder = StringBuilder(response)
        val model =  parser.parse(stringBuilder) as JsonArray<JsonObject>
        val scModel = model.map{ SCModel(it) }.filterNotNull();
        adapter = SCAdapter(scModel);
        rcvAdapter = adapter!!
        rcvList.adapter = rcvAdapter;
        rcvAdapter.notifyDataSetChanged();


        rcvList.addOnItemClickListener(object : OnItemClickListener
        {
            override fun onItemClicked(view: View, position: Int) {
                var cat_id = scModel.get(position).sc_id
                var cat_name = scModel.get(position).sc_name

//                    toast("Clicked over here $cat_name")

                val intent = Intent(context, ServiceList::class.java)
                intent.putExtra(SCModel.SC_ID,cat_id)
                intent.putExtra(SCModel.SC_NAME, cat_name)
                startActivity(intent)
            }
        })
        /*  }
          else if(json.getString(StaticRefs.STATUS).equals(StaticRefs.FAILED))
          {
              toast("check your connection")
          }
   */
        //getBanners();

    }


    fun getBanners()
    {

        Fuel.post(StaticRefs.BANNERS,listOf(StaticRefs.TOKEN to prefs.token))
                .responseJson()
                {
                    request,
                    response,
                    result ->

                    result.fold({ d ->
                        parseBanners(result.get().content)

                    }, { err ->
                        TastyToast.makeText(context,"Internet Network Down",TastyToast.LENGTH_LONG,TastyToast.ERROR)
                    })



                }

    }

    fun parseBanners(data: String)
    {
        alImages!!.clear()
        val json = JSONObject(data)

        val response = json.getString(StaticRefs.DATA)
        val jsonArray = JSONArray(response)
        if (jsonArray.length()>0){

            for (i in 0..jsonArray.length()-1){
                var jsonobj :JSONObject? = null
                jsonobj = jsonArray.get(i) as JSONObject?
                var lsImgUrl = jsonobj!!.getString("file_name")
                var lsFinalImgUrl = StaticRefs.BASEURL+"/"+lsImgUrl
                alImages!!.add(lsFinalImgUrl)

            }
            showBanner()
        }
        /*  val parser = Parser()
          val stringBuilder = StringBuilder(response)
          val model =  parser.parse(stringBuilder) as JsonArray<JsonObject>
          val bannerModel = model.map{ BannerModel(it) }.filterNotNull();*/
        //bannerList.add(bannerModel)


    }


    fun getArticles()
    {

        Fuel.post(StaticRefs.URLSHOWARTICLE,listOf(StaticRefs.TOKEN to prefs.token))
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
        val parser = Parser()
        val stringBuilder = StringBuilder(response)
        val model =  parser.parse(stringBuilder) as JsonArray<JsonObject>
        val articleModel = model.map{ ArticlesModel(it) }.filterNotNull();

        articlesAdapter = ArticlesAdapter(articleModel);
        rcvArticlesAdapter = articlesAdapter!!
        rcvArticles.adapter = rcvArticlesAdapter;
        rcvArticlesAdapter.notifyDataSetChanged();

        rcvArticles.addOnItemClickListener( object : OnItemClickListener
        {
            override fun onItemClicked(view: View, position: Int) {
                var title =articleModel.get(position).art_title
                var description =articleModel.get(position).art_description
                var img =articleModel.get(position).art_img
                var id =articleModel.get(position).art_id

                val intent = Intent(context, Articles_Detail::class.java)
                intent.putExtra(ArticlesModel.ART_TITLE, title)
                intent.putExtra(ArticlesModel.ART_DESCRIPTION, description)
                intent.putExtra(ArticlesModel.IMG_NAME, img)

                startActivity(intent)
                //toast("Hello article")
            }

        }
        )
    }

    fun showBanner() {
        val sliderShow = findViewById<SliderLayout>(R.id.slider)

        sliderShow.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Visible)

        /*alImages!!.add(R.drawable.abc)
        alImages!!.add(R.drawable.xyz)
        alImages!!.add(R.drawable.carp)*/
        for (i in alImages!!.indices) {
            val textSliderView = TextSliderView(this)

            textSliderView
                    .image(alImages!!.get(i))
                    .setScaleType(BaseSliderView.ScaleType.Fit)



            sliderShow.addSlider(textSliderView)
            sliderShow.setDuration(4000)
        }

    } //end of ShowBannerh




    fun getLatLng()
    {
        if(gps.canGetLocation)
        {
            var lattitude = gps.getLatitude()
            var longitude = gps.getLongitude()

            toast("Lat $lattitude Long $longitude")

            getPostalCode(lattitude, longitude)
        }
        else
        {
            gps.showSettingsAlert()
        }
    }

    fun getPostalCode(lat : Double , lng : Double)
    {
        val geocoder = Geocoder(this, Locale.ENGLISH)
        var addresses : List<Address>? = geocoder.getFromLocation(lat, lng, 1)
        var address : Address? = null
        var addr : String? = ""
        var zipcode : String? = ""
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

                    prefs.pincode = zipcode
                    prefs.location = location

                    break
                }
            }
        }
    }

    fun getRepairServices(){

        Fuel.post(StaticRefs.SERVICES,listOf((StaticRefs.TOKEN to prefs.token),("sc_id" to "2")))
                .responseJson()
                {
                    request,
                    response,
                    result ->
                    result.fold({ d ->
                        parseRJson(result.get().content)

                    }, { err ->
                        TastyToast.makeText(context,"Internet Network Down",TastyToast.LENGTH_LONG,TastyToast.ERROR)
                    })


                }
    }



    fun getCleaningServices(){

        Fuel.post(StaticRefs.SERVICES,listOf((StaticRefs.TOKEN to prefs.token),("sc_id" to "3")))
                .responseJson()
                {
                    request,
                    response,
                    result ->

                    result.fold({ d ->
                        parseCJson(result.get().content)

                    }, { err ->
                        TastyToast.makeText(context,"Internet Network Down",TastyToast.LENGTH_LONG,TastyToast.ERROR)
                    })


                }
    }

    fun parseRJson(data : String)
    {
        val json = JSONObject(data)

        val response = json.getString(StaticRefs.DATA)
        val parser = Parser()
        val stringBuilder = StringBuilder(response)
        val model =  parser.parse(stringBuilder) as JsonArray<JsonObject>
        val serviceModel = model.map{ ServicesModel(it) }.filterNotNull();
        SAdapter = HomeAdapter(serviceModel);
        rcvSAdater = SAdapter!!
        rcvRepairs.adapter = rcvSAdater;
        rcvSAdater.notifyDataSetChanged();

        rcvRepairs.addOnItemClickListener(object : OnItemClickListener
        {
            override fun onItemClicked(view: View, position: Int) {
                //  toast("Clicked over here "+serviceModel.get(position).srv_name )
                var service_id = serviceModel.get(position).srv_id
                var service_name = serviceModel.get(position).srv_name


                val intent = Intent(context, ServiceDetails::class.java)
                intent.putExtra(ServicesModel.SRV_ID,service_id)
                intent.putExtra(ServicesModel.SRV_NAME,service_name)
                startActivity(intent)
            }

        })
    }

    fun parseCJson(data : String)
    {
        val json = JSONObject(data)

        val response = json.getString(StaticRefs.DATA)
        val parser = Parser()
        val stringBuilder = StringBuilder(response)
        val model =  parser.parse(stringBuilder) as JsonArray<JsonObject>
        val serviceModel = model.map{ ServicesModel(it) }.filterNotNull();
        SAdapter = HomeAdapter(serviceModel);
        rcvSAdater = SAdapter!!
        rcvCleaning.adapter = rcvSAdater;
        rcvSAdater.notifyDataSetChanged();

        rcvCleaning.addOnItemClickListener(object : OnItemClickListener
        {
            override fun onItemClicked(view: View, position: Int) {
                // toast("Clicked over here "+serviceModel.get(position).srv_name )
                var service_id = serviceModel.get(position).srv_id
                var service_name = serviceModel.get(position).srv_name


                val intent = Intent(context, ServiceDetails::class.java)
                intent.putExtra(ServicesModel.SRV_ID,service_id)
                intent.putExtra(ServicesModel.SRV_NAME,service_name)
                startActivity(intent)
            }

        })
    }

    override fun onBackPressed() {
        finish()

    }




    fun searchServices(lsSearch :String){



        Fuel.post(StaticRefs.SEARCHSERVICE, listOf((StaticRefs.SRV_NAME to lsSearch)))
                .timeoutRead(StaticRefs.TIMEOUTREAD)
                .responseJson(){
                    request,
                    response,
                    result ->

                    result.fold({ d ->
                        parseSearchJson(result.get().content)

                    }, { err ->
                        TastyToast.makeText(context,"Internet Network Down",TastyToast.LENGTH_LONG,TastyToast.ERROR)
                    })

                }



    }

    fun parseSearchJson(response:String){


        val json = JSONObject(response)




        if (response.contains(StaticRefs.DATA) && json.getString(StaticRefs.DATA) != null && !json.getString(StaticRefs.DATA).equals("null"))
        {

            val data1=json.getJSONArray(StaticRefs.DATA)
            if(data1.length()>0) {
                val data = json.getString(StaticRefs.DATA)
                val parser = Parser()
                val stringBuilder = StringBuilder(data)
                val model = parser.parse(stringBuilder) as JsonArray<JsonObject>
                //  searchModel = model.map{ SearchModel(it) }.filterNotNull();
                searchModel = model.map { SearchModel(it) }.filterNotNull() as MutableList<SearchModel>
                SearchAdapter = SearchAdapter(searchModel!!, context);
                rcvSearchAdapter = SearchAdapter!!
                rcvSearch!!.adapter = rcvSearchAdapter;
                rcvSearchAdapter.notifyDataSetChanged();

                rcvSearch.addOnItemClickListener(object : OnItemClickListener {
                    override fun onItemClicked(view: View, position: Int) {
                        // toast("Clicked over here "+serviceModel.get(position).srv_name )
                        var service_id = searchModel!!.get(position).lsSrvId
                        var service_name = searchModel!!.get(position).lsSrvName


                        val intent = Intent(context, ServiceDetails::class.java)
                        intent.putExtra(ServicesModel.SRV_ID, service_id)
                        intent.putExtra(ServicesModel.SRV_NAME, service_name)
                        startActivity(intent)
                    }

                })
            }
            else{
                /* TastyToast.makeText(context, "Response is Empty", TastyToast.LENGTH_LONG, TastyToast.ERROR)
                 Log.d("Tag","Data is Empty")*/
                TastyToast.makeText(context, "no services exist with search specified", TastyToast.LENGTH_LONG, TastyToast.ERROR)
                rcvSearch!!.visibility = View.GONE

                /*  searchModel?.clear()
                  adapter!!.notifyDataSetChanged()*/
                // etSearchServices.setText("")
            }

        }
        else{
            TastyToast.makeText(context, "no services exist with search specified", TastyToast.LENGTH_LONG, TastyToast.ERROR)
            rcvSearch!!.visibility = View.GONE

            /*  searchModel?.clear()
              adapter!!.notifyDataSetChanged()*/
            // etSearchServices.setText("")
        }
    }


}
