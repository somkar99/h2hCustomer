package com.example.admin.h2hpartner.Models

import com.beust.klaxon.JsonObject
import com.beust.klaxon.double
import com.beust.klaxon.int
import com.beust.klaxon.string
import java.util.zip.DeflaterOutputStream

/**
 * Created by apple on 06/03/18.
 */
class Address_Model {
    companion object {
        val ADD_SRNO = "add_srno"
        val ADD_LINE1 = "add_line1";
        val ADD_LINE2 = "add_line2";
        val ADD_CITY = "add_city";
        val ADD_STATE = "add_state";
        val ADD_PINCODE = "add_pincode";
        val ADD_TYPE = "add_type";

    }

    var add_srno : Int? = null
    var add_line1: String? = null;
    var add_line2: String? = null;
    var add_city: String? = null;
    var add_state: String? = null;
    var add_pincode: String? = null;
    var add_type: String? = null;

    constructor(jsonObject: JsonObject) {
        add_srno = jsonObject.string(ADD_SRNO)!!.toInt()
        add_line1 = jsonObject.string(ADD_LINE1)
        add_line2 = jsonObject.string(ADD_LINE2)
        add_city = jsonObject.string(ADD_CITY)
        add_pincode = jsonObject.string(ADD_PINCODE)
        add_state = jsonObject.string(ADD_STATE)
        add_type = jsonObject.string(ADD_TYPE)
      }
}