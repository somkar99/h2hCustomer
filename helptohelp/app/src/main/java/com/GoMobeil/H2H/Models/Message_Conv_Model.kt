package com.GoMobeil.H2H.Models

import com.GoMobeil.H2H.Models.Message_Model.Companion.CHAT_DATETIME
import com.GoMobeil.H2H.StaticRefs
import com.beust.klaxon.JsonObject
import com.beust.klaxon.int
import com.beust.klaxon.string


/**
 * Created by apple on 16/04/18.
 */

class Message_Conv_Model {

    companion object {
        val LAST_CONV = StaticRefs.CHATMESSAGE
        val DATETIME = StaticRefs.CHAT_DATETIME
       // val IMG = StaticRefs.CHATMESSAGE
        val CUST_FNAME = StaticRefs.CUSTOMERFIRSTNAME
        val CUST_LNAME = StaticRefs.CUSTOMERLASTNAME
        val SRV_NAME = StaticRefs.SRV_NAME
        val TXN_ID = StaticRefs.CHATTXNID
        val CUST_ID = StaticRefs.CHATCUSTOMERID
        val CHATSENDER = StaticRefs.CHATSENDER
        val SP_ID = StaticRefs.TC_SPID
        val SP_FIRSTNAME = StaticRefs.SP_FIRSTNAME
        val SP_LASTNAME = StaticRefs.SP_LASTNAME
        val SP_IMAGE = StaticRefs.SP_IMAGE

    }

    var tc_custid: Int? = null;
    var tc_spid: Int? = null;
    var tc_txnid: Int? = null;
    var tc_sender: String? = null;
    var tc_message: String? = null;
    var created_at: String? = null;
    var cust_firstname: String? = null;
    var cust_lastname: String? = null;
    var srv_name: String? = null;
    var sp_image :String? = null

    var sp_firstname :String? = null
    var sp_lastname :String? = null

    constructor(jsonObject: JsonObject) {
        tc_custid = jsonObject.string(CUST_ID)!!.toInt()
        tc_spid = jsonObject.string(SP_ID)!!.toInt()
        sp_firstname = jsonObject.string(SP_FIRSTNAME)
        sp_lastname = jsonObject.string(SP_LASTNAME)
        tc_txnid = jsonObject.string(TXN_ID)!!.toInt()
       cust_lastname = jsonObject.string(CUST_LNAME)
        tc_message = jsonObject.string(LAST_CONV)
        created_at = jsonObject.string(CHAT_DATETIME)
        cust_firstname = jsonObject.string(CUST_FNAME)
        srv_name = jsonObject.string(SRV_NAME)
        tc_sender = jsonObject.string(CHATSENDER)
        sp_image = jsonObject.string(SP_IMAGE)

    }
}

