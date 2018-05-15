package com.GoMobeil.H2H.Models

import com.beust.klaxon.JsonObject
import com.beust.klaxon.int
import com.beust.klaxon.string
import org.json.JSONObject

/**
 * Created by Admin on 13-04-18.
 */
class SearchModel {

    companion object {
        val SRV_ID ="srv_id"
        val SRV_NAME = "srv_name"

    }

    var lsSrvId :Int? = null
    var lsSrvName :String? = null

   constructor(jsonObject: JsonObject){

       lsSrvId = jsonObject.int(SRV_ID)
       lsSrvName = jsonObject.string(SRV_NAME)


   }

}