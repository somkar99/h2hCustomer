package com.GoMobeil.H2H.UI

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import com.GoMobeil.H2H.*
import com.GoMobeil.H2H.Extensions.toast
import com.GoMobeil.H2H.Models.ServicesModel
import com.GoMobeil.H2H.Services.GPSTracker
import com.GoMobeil.H2H.Services.TransperantProgressDialog
import com.GoMobeil.H2H.UI.VendorList.Companion.zipcode
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.example.admin.h2hpartner.Adapter.Address_Adapter
import com.example.admin.h2hpartner.Models.Address_Model
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.location.places.AutocompleteFilter
import com.google.android.gms.location.places.ui.PlaceAutocomplete
import com.sdsmdg.tastytoast.TastyToast
import kotlinx.android.synthetic.main.address_first.*
import org.json.JSONObject
import java.util.*
/**
 * Created by Admin on 22-03-18.
 */
class AddressFirst : BaseActivity() {
    override lateinit var context: Context
    override lateinit var activity: Activity
    var LOCATION_REQUEST_CODE: Int? = 100
    lateinit var gps: GPSTracker
    var pincode: String? = ""
    var location: String? = ""
    var addresLine1: String = ""
    var addressLine2: String = ""
    var city: String? = ""
    var state: String? = ""
    var addr: String? = ""
    var lattitude: Double? = 0.0
    var longitude: Double? = 0.0

    var lsFlatStreet: String = ""
    var lsLocationCityState: String = ""
    var curAdd: String? = null


    var lsAddLine1: String = ""
    var lsAddLine2: String = ""
    var lsLocation: String = ""
    var lsCity: String = ""
    var lsState: String = ""
    var lsPincode: String = ""

    var service_id: Int? = null
    var service_name: String? = null
    lateinit var pd: TransperantProgressDialog

    var PLACE_AUTOCOMPLETE_REQUEST_CODE: Int = 1000

    var alQuestionsArray: ArrayList<String> = ArrayList()
    var alAnswersArray: ArrayList<String> = ArrayList()
    var alAnsIdsArray: ArrayList<String> = ArrayList()
    var alQueIdsArray: ArrayList<String> = ArrayList()
    var cust_id: Int? = null
    var dateScheduled: String? = null
    var lsStartTime: String? = null
    var lsEndTime: String? = null

    lateinit var llm: LinearLayoutManager
    lateinit var rcvAdapter: RecyclerView.Adapter<Address_Adapter.ViewHolder>
    var adapter: Address_Adapter? = null
    lateinit var address_model: List<Address_Model>
    var lbIsAddChanged: Boolean = false
    lateinit var coder : Geocoder
    var address: List<Address>? = null

    var liAddSrNoOnSelection : Int? = null
    var lsAddLine1OnSelection : String? = ""
    var lsAddLine2OnSelection : String? = ""
    var lsAddCityOnSelection : String? = ""
    var lsAddStateOnSelection : String? = ""
    var lsAddPincodeOnSelection : String? = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.address_first)
        setTitle("Address")
        context = this
        activity = this

        initLayout()

    }

    fun initLayout() {

        coder = Geocoder(context)
        pd = TransperantProgressDialog(context)

        llm = LinearLayoutManager(this);
        rcvShowAdd.layoutManager = llm;

           service_id = getIntent().getIntExtra(ServicesModel.SRV_ID, 0)
           service_name = getIntent().getStringExtra(ServicesModel.SRV_NAME)
          // cust_id = getIntent().getIntExtra(StaticRefs.CUST_ID, 0)
           alQuestionsArray = getIntent().getStringArrayListExtra("Questions")
           alAnswersArray = getIntent().getStringArrayListExtra("Answers")
           alAnsIdsArray = getIntent().getStringArrayListExtra("AnsIds")
           alQueIdsArray = getIntent().getStringArrayListExtra("QueIds")
           dateScheduled = getIntent().getStringExtra("date")
           lsStartTime = getIntent().getStringExtra("startTime")
           lsEndTime = getIntent().getStringExtra("endTime")

        getAddresses()

        tvChangeAddress.setOnClickListener {
            val intent = Intent(context, AddressSecond::class.java)

            var building : String = ""
            var street : String = ""

            if(etBuilding1.text.toString().contains(","))
            {
                var add1 = etBuilding1.text.toString().split(",")
                building = add1[0]
                street = add1[1]

                intent.putExtra("building", building)
                intent.putExtra("street", street)
            }
            else if(etBuilding1.text.toString().length > 0)
            {
                intent.putExtra("building", etBuilding1.text.toString())
                intent.putExtra("street", street)
            }
            else
            {
                intent.putExtra("building", building)
                intent.putExtra("street", street)
            }

            var add =  etLocality.text.toString().split(",")
            var addline2 = add[0]
            var addcity = add[1]
            var addstate = add[2]
            var addpincode = add[3]

            intent.putExtra("location", addline2)
            intent.putExtra("city", addcity)
            intent.putExtra("state", addstate)
            intent.putExtra("pincode", addpincode)

            startActivityForResult(intent, 2)
        }

    /*    tvChangeAddress.setOnClickListener {
            autocomplete()

        }*/

        cbSave.setOnClickListener(View.OnClickListener {
            if(etBuilding1.text.toString().length > 0)
            {
                if(etLocality.text.toString().length > 0)
                {
                    saveAddress()
                }
               else
                {
                    TastyToast.makeText(context, "Provide service location info please!!", 30, TastyToast.ERROR).show()
                }
            }
            else
            {
                TastyToast.makeText(context, "Provide Building/Street info please!!", 30, TastyToast.ERROR).show()
            }

        })

        cbContinue.setOnClickListener(View.OnClickListener {
            val intent = Intent(context, ContactShare::class.java)
            intent.putExtra(ServicesModel.SRV_ID, service_id!!)
            intent.putExtra(ServicesModel.SRV_NAME, service_name)
            intent.putExtra("Questions", alQuestionsArray)
            intent.putExtra("Answers", alAnswersArray)
            intent.putExtra("AnsIds", alAnsIdsArray)
            intent.putExtra("QueIds", alQueIdsArray)
            intent.putExtra("date", dateScheduled)
            intent.putExtra("startTime", lsStartTime)
            intent.putExtra("endTime", lsEndTime)

            intent.putExtra(StaticRefs.SRNO , liAddSrNoOnSelection)
            intent.putExtra(StaticRefs.ADD_LINE1, lsAddLine1OnSelection)
            intent.putExtra(StaticRefs.ADD_LINE2, lsAddLine2OnSelection)
            intent.putExtra(StaticRefs.ADD_CITY, lsAddCityOnSelection)
            intent.putExtra(StaticRefs.ADD_STATE, lsAddStateOnSelection)
            intent.putExtra(StaticRefs.ADD_PINCODE, lsAddPincodeOnSelection)
            startActivity(intent)
        })

        tvAddNewAddress.setOnClickListener(View.OnClickListener {
            addNewAddTab()
        })
    }

    fun saveAddress() {
        pd.show()
        var addType: String? = null
        var addTypeVal = (findViewById<RadioButton>(rgAdd.getCheckedRadioButtonId())).text.toString()

        if (addTypeVal.equals("Home")) {
            addType = "R"
        } else if (addTypeVal.equals("Office")) {
            addType = "W"
        } else if (addTypeVal.equals("Other")) {
            addType = "O"
        } else {
            addType = "O"
        }

        var building : String = ""
        var street : String = ""

        if(etBuilding1.text.toString().contains(","))
        {
            var buldingInfo = etBuilding1.text.toString().split(",")
            building = buldingInfo[0]
            street = buldingInfo[1]

        }
        else if(etBuilding1.text.toString().length > 0)
        {
            building = etBuilding1.text.toString()
        }

        var addline1 : String? = null

        if(street.length > 0)
        {
            addline1 = building +" ,"+street
        }
        else
        {
            addline1 = building
        }

        var lsFullAdd = etLocality.text.toString()

        var add =  lsFullAdd.split(",")
        var addline2 = add[0]
        var addcity = add[1]
        var addstate = add[2]
        var addpincode = add[3]

        getCoordinatesFromAdd(lsFullAdd)


        Fuel.post(StaticRefs.URLAddressCreate, listOf(StaticRefs.TOKEN to App.prefs!!.token,
                StaticRefs.ADD_ENTITYID to prefs.cust_id,
                StaticRefs.ADD_ENTITYTYPE to "C",
                StaticRefs.ADD_TYPE to addType,
                StaticRefs.ADD_LINE1 to addline1,
                StaticRefs.ADD_LINE2 to addline2,
                StaticRefs.ADD_CITY to addcity,
                StaticRefs.ADD_STATE to addstate,
                StaticRefs.ADD_PINCODE to addpincode,
                StaticRefs.ADD_LATTITUDE to lattitude,
                StaticRefs.ADD_LONGITUDE to longitude
        )).responseJson()
        { request,
          response,
          result ->
            parseResponse(result.get().content,"SAVE_ADD")
        }
    }

    fun parseResponse(response: String, lsVal : String) {

        val json = JSONObject(response);
        if (json.has(StaticRefs.STATUS) && json.getString(StaticRefs.STATUS) != null) {
            var message = "";
            if (json.has(StaticRefs.MESSAGE) && json.getString(StaticRefs.MESSAGE) != null) {
                message = json.getString(StaticRefs.MESSAGE)
            }
            if (json.getString(StaticRefs.STATUS).equals(StaticRefs.FAILED)) {
                //NGS CustomToast
                pd.dismiss()
                //Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                TastyToast.makeText(context,message,TastyToast.LENGTH_LONG,TastyToast.ERROR).show()
            } else {
                pd.dismiss()
               // Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                TastyToast.makeText(context,message,TastyToast.LENGTH_LONG,TastyToast.INFO).show()

                if(lsVal.equals("SAVE_ADD") || lsVal.equals("UPDATE_ADD"))
                {
                    etBuilding1.setText("")
                    etLocality.setText("")

                    llRCVShowAdd.visibility = View.VISIBLE
                    llAddAddress.visibility = View.GONE
                    cbContinue.visibility = View.VISIBLE
                    cbSave.visibility = View.GONE
                    cbUpdate.visibility = View.GONE
                }
                getAddresses()
                lbIsAddChanged = false
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2 && resultCode == RESULT_OK) {

            lbIsAddChanged = true

            if (data!!.getStringExtra("lsAddLine1") != null) {
                lsAddLine1 = data!!.getStringExtra("lsAddLine1")
            }
            //lsAddLine2 = data!!.getStringExtra("lsAddLine2")
            if (data!!.getStringExtra("lsLocation") != null) {
                lsLocation = data!!.getStringExtra("lsLocation")
            }

            if (data!!.getStringExtra("lsCity") != null) {
                lsCity = data!!.getStringExtra("lsCity")
            }

            if (data!!.getStringExtra("lsState") != null) {
                lsState = data!!.getStringExtra("lsState")
            }

            if (data!!.getStringExtra("lsPincode") != null) {
                lsPincode = data!!.getStringExtra("lsPincode")
            }

            lsFlatStreet = lsAddLine1
            lsLocationCityState = lsLocation + ", " + lsCity + ", " + lsState+", "+lsPincode

            etBuilding1.setText(lsFlatStreet)
            etLocality.setText(lsLocationCityState)
        }
        else if(requestCode==PLACE_AUTOCOMPLETE_REQUEST_CODE){
            if(resultCode== RESULT_OK){
                var place=PlaceAutocomplete.getPlace(context,data)
                var address=place.address


                lattitude=place.latLng.latitude
                longitude=place.latLng.longitude
                val geocoder=Geocoder(context, Locale.ENGLISH)

                etLocality.setText(address)




            }else if(requestCode==PlaceAutocomplete.RESULT_ERROR){
                var Status=PlaceAutocomplete.getStatus(context,data)
                TastyToast.makeText(context,Status.toString(),Toast.LENGTH_SHORT,TastyToast.ERROR)

            }
        }
    }

   /* override fun onResume() {
        super.onResume()
        if (lbIsAddChanged == false) {
            getAddresses()
        }
    }*/

    fun getAddresses() {
        pd.show()
        Fuel.post(StaticRefs.GETADDRESS, listOf(StaticRefs.CUST_ID to prefs.cust_id))
                .responseJson()
                { request,
                  response,
                  result ->
                    parseJson(result.get().content)
                }
    }


    fun parseJson(data: String) {
        pd.dismiss()
        val json = JSONObject(data)
        val response = json.getString(StaticRefs.DATA)
        val parser = Parser()
        val stringBuilder = StringBuilder(response)
        val model = parser.parse(stringBuilder) as JsonArray<JsonObject>
        address_model = model.map { Address_Model(it) }.filterNotNull();
        adapter = Address_Adapter(address_model)
        rcvAdapter = adapter!!
        rcvShowAdd.adapter = rcvAdapter
        rcvAdapter.notifyDataSetChanged()
        adapter!!.setMultiple(false)

        if (address_model.size <= 0) {
            addNewAddTab()
        } else {
            llRCVShowAdd.visibility = View.VISIBLE
            llAddAddress.visibility = View.GONE
            cbContinue.visibility = View.VISIBLE
            cbSave.visibility = View.GONE
            cbUpdate.visibility = View.GONE
        }

        Handler().postDelayed(Runnable
        {
            if(address_model.size > 0)
            {
                rcvShowAdd.findViewHolderForAdapterPosition(0).itemView.findViewById<TextView>(R.id.tvAddressData).performClick()
            }
        }, 100)

        adapter!!.setOnItemClickListener(object : Address_Adapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                liAddSrNoOnSelection = address_model.get(position).add_srno
                lsAddLine1OnSelection = address_model.get(position).add_line1
                lsAddLine2OnSelection = address_model.get(position).add_line2
                lsAddCityOnSelection = address_model.get(position).add_city
                lsAddStateOnSelection = address_model.get(position).add_state
                lsAddPincodeOnSelection = address_model.get(position).add_pincode

               // toast("Selected add $liAddSrNoOnSelection")
            }
        })

        adapter!!.setOnAddChangeListener(object : Address_Adapter.onAddChangeListener {
            override fun onItemChangeAdd(position: Int, lsValue: String) {
                if (lsValue.equals("EDIT")) {
                    var lsAddLine1 = address_model.get(position).add_line1
                    var lsAddLine2 = address_model.get(position).add_line2
                    var lsCity = address_model.get(position).add_city
                    var lsState = address_model.get(position).add_state
                    var lsPincode = address_model.get(position).add_pincode
                    var liSrNo = address_model.get(position).add_srno

                    updateAddTab(lsAddLine1!!,lsAddLine2!!,lsCity!!,lsState!!,lsPincode!!,liSrNo!!)
                }
                else if (lsValue.equals("DELETE"))
                {
                    var liSrNo = address_model.get(position).add_srno
                    deleteAddress(liSrNo!!)
                }
            }

        })
    }

    fun deleteAddress(srno : Int)
    {
        pd.show()
        Fuel.post("http://help2help.weapplify.tech/api/address/delete", listOf(StaticRefs.SRNO to srno))
                .responseJson()
                { request,
                  response,
                  result ->
                    parseResponse(result.get().content,"DELETE_ADD")
                }
    }

    fun updateAddTab(lsAddLine1 : String, lsAddLine2 : String, lsCity : String, lsState : String , lsPincode : String , liSrNo : Int) {
        lbIsAddChanged = true
        llRCVShowAdd.visibility = View.GONE
        llAddAddress.visibility = View.VISIBLE
        cbContinue.visibility = View.GONE
        cbSave.visibility = View.GONE
        cbUpdate.visibility = View.VISIBLE

        etBuilding1.setText(lsAddLine1)
        etLocality.setText(lsAddLine2 + ", " + lsCity + ", " + lsState + ", "+lsPincode)

        cbUpdate.setOnClickListener(View.OnClickListener {
            updateAddress(liSrNo)
        })
    }

    fun updateAddress(liSrNo : Int)
    {
        pd.show()
        var addType: String? = null
        var addTypeVal = (findViewById<RadioButton>(rgAdd.getCheckedRadioButtonId())).text.toString()

        if (addTypeVal.equals("Home")) {
            addType = "R"
        } else if (addTypeVal.equals("Office")) {
            addType = "W"
        } else if (addTypeVal.equals("Other")) {
            addType = "O"
        } else {
            addType = "O"
        }

        var building : String = ""
        var street : String = ""

        if(etBuilding1.text.toString().contains(","))
        {
            var buldingInfo = etBuilding1.text.toString().split(",")
            building = buldingInfo[0]
            street = buldingInfo[1]

        }
        else if(etBuilding1.text.toString().length > 0)
        {
            building = etBuilding1.text.toString()
        }

        var addline1 : String? = null

        if(street.length > 0)
        {
            addline1 = building +" ,"+street
        }
        else
        {
            addline1 = building
        }

        var lsFullAdd = etLocality.text.toString()
        var add =  lsFullAdd.split(",")
        var addline2 = add[0]
        var addcity = add[1]
        var addstate = add[2]
        var addpincode = add[3]

        getCoordinatesFromAdd(lsFullAdd)

        Fuel.post("http://help2help.weapplify.tech/api/address/edit", listOf(StaticRefs.TOKEN to prefs.token,
                "add_srno" to liSrNo,
                StaticRefs.ADD_ENTITYID to prefs.cust_id,
                StaticRefs.ADD_ENTITYTYPE to "C",
                StaticRefs.ADD_TYPE to addType,
                StaticRefs.ADD_LINE1 to addline1,
                StaticRefs.ADD_LINE2 to addline2,
                StaticRefs.ADD_CITY to addcity,
                StaticRefs.ADD_STATE to addstate,
                StaticRefs.ADD_PINCODE to addpincode,
                StaticRefs.ADD_LATTITUDE to lattitude,
                StaticRefs.ADD_LONGITUDE to longitude
                ))
                .responseJson()
                { request,
                  response,
                  result ->
                    parseResponse(result.get().content,"UPDATE_ADD")
                }
    }

    fun addNewAddTab() {
        lbIsAddChanged = true
        llRCVShowAdd.visibility = View.GONE
        llAddAddress.visibility = View.VISIBLE
        cbContinue.visibility = View.GONE
        cbSave.visibility = View.VISIBLE
        cbUpdate.visibility = View.GONE
        gps = GPSTracker(context)
        checkLocationPermission()

        etLocality.setText(location + " ," + city + " ," + state +" ,"+ zipcode)
    }

    fun checkLocationPermission() {
        val permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    this!!.LOCATION_REQUEST_CODE!!)

            getLattitudeLng()
        } else {
            getLattitudeLng()
        }
    }

    fun getLattitudeLng() {
        if (gps.canGetLocation) {
            lattitude = gps.getLatitude()
            longitude = gps.getLongitude()
            getPostal(lattitude!!, longitude!!)
        } else {
            gps.showSettingsAlert()
        }
    }

    fun getPostal(lat: Double, lng: Double) {
        val geocoder = Geocoder(this, Locale.ENGLISH)
        var addresses: List<Address>? = geocoder.getFromLocation(lat, lng, 1)
        var address: Address? = null
        if (addresses != null && addresses.size > 0) {
            addr = addresses.get(0).getAddressLine(0) + "," + addresses.get(0).getSubAdminArea();
            city = addresses.get(0).getLocality();
            state = addresses.get(0).getAdminArea();
            addresLine1 = addresses.get(0).subLocality
            addressLine2 = addresses.get(0).subLocality
            curAdd = addresses.get(0).getAddressLine(0)

            for (i in 0..addresses.size) {
                address = addresses.get(i)
                if (address.getPostalCode() != null) {
                    VendorList.zipcode = address.getPostalCode()
                    state = address.adminArea
                    location = address.subLocality
                    //  addresLine1 = address.getPremises()
                    addressLine2 = address.getSubAdminArea()

                    break
                }
            }
        }

        if (VendorList.zipcode.equals(pincode)) {
          //  toast("Same location")
            TastyToast.makeText(context,"Same Location",TastyToast.LENGTH_LONG,TastyToast.INFO).show()
        } else {
            //toast("Not same location")

            TastyToast.makeText(context,"Not Same Location",TastyToast.LENGTH_LONG,TastyToast.ERROR).show()
        }
    }

    fun getCoordinatesFromAdd(lsAddVal : String){
        try {
            address = coder.getFromLocationName(lsAddVal,5);
            if (address == null) {
               // toast("Address is empty")
                TastyToast.makeText(context,"address is empty",TastyToast.LENGTH_LONG,TastyToast.ERROR).show()
            }
            val location = address!!.get(0);
            lattitude = location.getLatitude();
            longitude = location.getLongitude();

           // toast("MyLat $lattitude , MyLong $longitude")
            TastyToast.makeText(context,"My Latitude is $lattitude",TastyToast.LENGTH_LONG,TastyToast.INFO).show()

            TastyToast.makeText(context,"My Longitude is $longitude",TastyToast.LENGTH_LONG,TastyToast.INFO).show()
        }
        catch (e : Exception)
        {
            println(e)
        }
    }

    fun autocomplete(){
        try {
            val typeFilter = AutocompleteFilter.Builder()
                    .setCountry("IN")
                    .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                    .build()
            val intent = PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).setFilter(typeFilter).build(this)
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        }catch (e: GooglePlayServicesRepairableException){

        }catch (e: GooglePlayServicesNotAvailableException){

        }

    }
}