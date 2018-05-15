package com.GoMobeil.H2H.Models

import com.beust.klaxon.JsonObject
import com.beust.klaxon.string

/**
 * Created by Admin on 04-04-18.
 */
class PricingModel {

    companion object {
        val PST_SPID = "pst_spid"
        val PST_SRVID = "pst_servid"
        val PST_SRVTYPE = "pst_serv_type"
        val PST_COSTTYPE = "pst_costtype"
        val PST_COST_UNIT = "pst_cost_unit"
        val PST_COST_FROM = "pst_cost_from"
        val PST_COST_TO = "pst_cost_to"
        val PST_COST_REMARK = "pst_cost_remarks"
        val PST_COST_RATE = "pst_cost_rate"
        val PST_COST_FIXED = "pst_cost_fixed"
        val PST_COST_VISITING = "pst_cost_visiting"
        val PST_SRVNAME = "srv_name"
        val PST_SRVTYPEDESC = "srv_typedescription"
        val PST_PRICEUNIT = "price_unit"
        val PST_PRICETYPE = "price_type"


    }
    var pst_spid:String? = null
    var pst_srvid:String? = null
    var pst_srvtype:String? = null
    var pst_costtype:String? = null
    var pst_cost_unit:String? = null
    var pst_cost_from:String? = null
    var pst_cost_to:String? = null
    var pst_cost_remark:String? = null
    var pst_cost_rate:String? = null
    var pst_cost_fixed:String? = null
    var pst_cost_visiting:String? = null
    var pst_srvname:String? = null
    var pst_srvtypedesc:String? = null
    var pst_priceunit:String? = null
    var pst_pricetype:String? = null


    constructor(jsonObject: JsonObject){
        pst_spid = jsonObject.string(PST_SPID)
        pst_srvid = jsonObject.string(PST_SRVID)
        pst_srvtype = jsonObject.string(PST_SRVTYPE)
        pst_costtype = jsonObject.string(PST_COSTTYPE)
        pst_cost_unit = jsonObject.string(PST_COST_UNIT)
        pst_cost_from = jsonObject.string(PST_COST_FROM)
        pst_cost_to = jsonObject.string(PST_COST_TO)
        pst_cost_remark = jsonObject.string(PST_COST_REMARK)
        pst_cost_rate = jsonObject.string(PST_COST_RATE)
        pst_cost_fixed = jsonObject.string(PST_COST_FIXED)
        pst_cost_visiting = jsonObject.string(PST_COST_VISITING)
        pst_srvname = jsonObject.string(PST_SRVNAME)
        pst_srvtypedesc = jsonObject.string(PST_SRVTYPEDESC)
        pst_priceunit = jsonObject.string(PST_PRICEUNIT)
        pst_pricetype = jsonObject.string(PST_PRICETYPE)


    }
}
