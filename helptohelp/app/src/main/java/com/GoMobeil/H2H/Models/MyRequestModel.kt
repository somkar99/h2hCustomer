package com.GoMobeil.H2H.Models

import com.beust.klaxon.JsonObject
import com.beust.klaxon.int
import com.beust.klaxon.string

/**
 * Created by Admin on 02-04-18.
 */
class MyRequestModel     {

    companion object {


        val SP_NAME = "service_provider_name"
        val PLANNEDDATE = "sr_planneddate"
        val STATUS = "sr_status"
        val SERVICENAME = "srv_name"
        val SP_IMAGE = "service_provider_logo"
        val TXN_ID = "sr_txnid"
        val CREATEDAT = "created_at"
        val UPDATEDAT = "updated_at"
        //val TXNID = "sr_txnid"
        val SPLSTATUS = "spl_status"

        var SRVIMG = "srv_image"
    }


    var lsSPName :String? = null
    var lsPlannedDate :String? = null
    var lsStatus:String? = null
    var lsServiceName:String? = null
    var lsSPImage:String? = null
    var lsTxnid:Int? = null
    //var lssTxnid:Int? = null
    var lsCreatedAt :String? = null
    var lsUpdatedAt :String? = null
    var lsspl_status :String? = null

    var lssrv_image :String? = null
    constructor(jsonObject: JsonObject){


        lsSPName = jsonObject.string(SP_NAME)
        lsPlannedDate = jsonObject.string(PLANNEDDATE)
        lsStatus = jsonObject.string(STATUS)
        lsServiceName = jsonObject.string(SERVICENAME)
        lsSPImage = jsonObject.string(SP_IMAGE)
        lsTxnid = jsonObject.int(TXN_ID)
        lsCreatedAt = jsonObject.string(CREATEDAT)
        lsUpdatedAt = jsonObject.string(UPDATEDAT)
       // lssTxnid = jsonObject.int(TXNID)
        lsspl_status = jsonObject.string(SPLSTATUS)

        lssrv_image = jsonObject.string(SRVIMG)
    }
}