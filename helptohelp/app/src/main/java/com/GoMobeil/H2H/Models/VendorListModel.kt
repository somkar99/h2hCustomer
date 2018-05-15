package com.GoMobeil.H2H.Models

import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.int
import com.beust.klaxon.string

/**
 * Created by ADMIN on 02-02-2018.
 */
class VendorListModel {
    companion object {
        // #table provider_business
        val PB_ID = "pb_id"
        val PB_BUSINESSNAME = "pb_businessname"
        val PB_SRVID = "pb_srvId"
        val PB_ESTABLISHON = "pb_establishon"
        val PB_BUSINESSCONTACTNO = "pb_businesscontactno"
        val PB_EXPERIENCE = "pb_experience"
        val PB_WEBSITEURL = "pb_websiteurl"
        val PB_COSTOFSERVICE = "pb_costofservice"
        val PB_SERVICEAREA = "pb_servicearea"
        val PB_ISACTIVE = "pb_isactive"
        val PB_CREATEDBY = "pb_createdby"
        val PB_UPDATEDBY = "pb_updatedby"
        val PB_CREATEDAT = "created_at"
        val PB_UPDATEDAT = "updated_at"
        val PB_USP = "pb_usp"
        val PB_INTRODUCTION = "pb_introduction"

        // #table service_providers



        val SP_ID = "sp_id"
        val SP_FIRSTNAME = "sp_firstname"
        val SP_LASTNAME = "sp_lastname"
        val SP_DOB = "sp_dob"
        val SP_GENDER = "sp_gender"
        val SP_MOBILENO = "sp_mobileno"
        val SP_ALTMOBILENO = "sp_altmobileno"
        val SP_LANDLINENO = "sp_landlineno"
        val SP_EMAIL = "sp_email"
        val SP_PASSWORD = "sp_password"
        val SP_LANGUAGEPREFERENCE = "sp_languagepreference"
        val SP_CATEGORY = "sp_category"
        val SP_AADHARCARDNO = "sp_aadharcardno"
        val SP_PRIMARYBUSINESS = "sp_primarybusiness"
        val SP_IMAGE = "sp_image"
        val SP_STATUS = "sp_status"
        val SP_ISACTIVE = "sp_isactive"
        val SP_CREATEDBY = "sp_createdby"
        val SP_UPDATEDBY = "sp_updatedby"
        val SP_CREATEDAT = "created_at"
        val SP_UPDATEDAT = "updated_at"
        val SP_DETAILS = ""

        val SPL_SPID = "spl_spid"

        val SERVICEPROVIDERNAME = "service_provider_name"
        val SERVICEPROVIDERLOGO = "service_provider_logo"

    }

    var lsFirstName: String? = null
    var lsLastName: String? = null
    var lsCategory: String? = null
    var lsMemberSince: String? = null
    var lsMobile : String? = null
    var sp_id : Int? = null
    var sp_altMobileNo : String? = null
    var splandLineNo : String? = null
    var sp_email : String? = null
    var sp_aadharCardNo : String? = null
    var sp_primaryBusiness :String? = null
    var sp_image : String? = null
    var sp_status : String? = null

    var serviceprovidername :String? = null
    var serviceproviderlogo :String? = null

    var spl_spid :String? = null

    constructor(jsonObject: JsonObject) {

        /*lsServiceArea = jsonObject.string(PB_SERVICEAREA)
        val serviceProvider = jsonObject.get("service_provider")
        val jsonObject1 = serviceProvider as JsonObject*/

        lsFirstName = jsonObject.string(SP_FIRSTNAME)
        lsLastName = jsonObject.string(SP_LASTNAME)
        lsCategory = jsonObject.string(SP_CATEGORY)
        lsMemberSince = jsonObject.string(SP_CREATEDAT)
        lsMobile = jsonObject.string(SP_MOBILENO)
        sp_id = jsonObject.string(SP_ID)!!.toInt()
        sp_altMobileNo = jsonObject.string(SP_ALTMOBILENO)
        splandLineNo = jsonObject.string(SP_LANDLINENO)
        sp_email = jsonObject.string(SP_EMAIL)
        sp_aadharCardNo = jsonObject.string(SP_AADHARCARDNO)
        sp_primaryBusiness = jsonObject.string(SP_PRIMARYBUSINESS)
        sp_image = jsonObject.string(SP_IMAGE)
        sp_status = jsonObject.string(SP_STATUS)

        spl_spid= jsonObject.string(SPL_SPID)

        serviceprovidername = jsonObject.string(SERVICEPROVIDERNAME)
        serviceproviderlogo = jsonObject.string(SERVICEPROVIDERLOGO)


    }
}
