package com.GoMobeil.H2H.UI

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.GoMobeil.H2H.BaseActivity
import com.GoMobeil.H2H.R
import com.sdsmdg.tastytoast.TastyToast
import kotlinx.android.synthetic.main.address_first.*
import kotlinx.android.synthetic.main.address_second.*

/**
 * Created by Admin on 22-03-18.
 */
class AddressSecond : BaseActivity(){

    override lateinit var context: Context
    override lateinit var activity: Activity

    var city:String=""
    var location:String=""
    var state:String=""
    var pincode:String=""

    var lsAddLine1:String=""
    var lsAddLine2:String=""
    var lsCity:String=""
    var lsState:String=""
    var lsLocation:String=""
    var lsPincode:String=""
    var lsBuilding : String = ""
    var lsStreet : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.address_second)

        city = getIntent().getStringExtra("city")
        location = getIntent().getStringExtra("location")
        state = getIntent().getStringExtra("state")
        pincode = getIntent().getStringExtra("pincode")

        lsBuilding = getIntent().getStringExtra("building")
        lsStreet = getIntent().getStringExtra("street")

        etLandmark.setText(location)
        etCity.setText(city)
        etState.setText(state)
        etPin.setText(pincode)
        etBuilding.setText(lsBuilding)
        etStreet.setText(lsStreet)

        cbOK.setOnClickListener {
            if(etBuilding.text.toString().length > 0 && etStreet.text.toString().length > 0 && etLandmark.text.toString().length > 0
                    && etCity.text.toString().length > 0 && etState.text.toString().length > 0 && etPin.text.toString().length > 0)
            {
                lsAddLine1 = etBuilding.text.toString() + " , "+ etStreet.text.toString()
                lsLocation = etLandmark.text.toString()
                lsCity = etCity.text.toString()
                lsState = etState.text.toString()
                lsPincode = etPin.text.toString()
                if (!(lsAddLine1.length==null && lsLocation.length>0 && lsCity.length>0 &&lsState.length>0 && lsPincode.length>0)){

                    val intent = Intent(context,AddressFirst::class.java)
                    intent.putExtra("lsAddLine1",lsAddLine1)
                    intent.putExtra("lsLocation",lsLocation)
                    intent.putExtra("lsCity",lsCity)
                    intent.putExtra("lsState",lsState)
                    intent.putExtra("lsPincode",lsPincode)
                    setResult(RESULT_OK,intent)
                    finish()
                }
            }
            else{
               // Toast.makeText(context,"Please fill all the fields",Toast.LENGTH_LONG).show()

                TastyToast.makeText(context,"Please fill all the fields",TastyToast.LENGTH_LONG,TastyToast.ERROR).show()
            }
        }

    }
}