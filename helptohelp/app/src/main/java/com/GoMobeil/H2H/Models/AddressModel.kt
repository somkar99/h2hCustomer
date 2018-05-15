package com.GoMobeil.H2H.Models

import com.beust.klaxon.JsonObject
import com.beust.klaxon.string
import org.json.JSONObject

/**
 * Created by Admin on 16-03-18.
 */
class AddressModel {
    companion object {
        val ADDLINE1 = "addline1"
        val ADDLINE2 = "addline2"

        val CITY = "city"
        val STATE = "state"
        val PINCODE = "pincode"
    }

    var add_line1:String? = null
    var add_line2:String? = null

    var city:String? = null
    var state:String? = null
    var pincode:String? = null
    var lsCompleteaddress:String?=null

    constructor(jsonObject: JsonObject){
        add_line1 = jsonObject.string(ADDLINE1)
        add_line2 = jsonObject.string(ADDLINE2)

        city = jsonObject.string(CITY)
        state = jsonObject.string(STATE)
        pincode = jsonObject.string(PINCODE)

    }

}