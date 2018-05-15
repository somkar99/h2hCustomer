package com.GoMobeil.H2H

import android.graphics.Bitmap
import android.os.Environment
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection
import java.util.regex.Pattern

/**
 * Created by niranjanshah on 29/01/18.
 */

class StaticRefs
{
    companion object {

        val KEY = "key"

        val APPNAME = "H2H"
        val REMEMBER_ME = "remember_me"

        val BASEURL = "http://help2help.weapplify.tech"
        val INSTALLATIONPIN = "PIN"
        val PINCODE_FIRST = "PINCODE_FIRST"
        val LOCATION_FIRST = "LOCATION_FIRST"
        val TOKEN = "token"
        val FAILED = "Failed"
        var SUCCESS = "success";
        var STATUSSUCCESS="Success"
        var STATUS  = "status";
        var MESSAGE = "message";
        var DEVICETOKEN="cust_deviceid"
        var DATA = "data"
        val URLLogin = "/api/customers/login"
        val URLREGISTER = "/api/customers/register"
        val URLServiceCat = "/api/servicecategory/show"
        val URLImageBanners = "/api/images/filter/B"
        val BANNERS = "/api/slider/active"
        val URLImageServices = "/api/images/filter/S"
        val URLArticles = "/api/servicearticles/filter/A"
        val URLSHOWARTICLE = "/api/articles/show"
        val URLServiceFaqs = "/api/servicefaqs"
        val SHOWFAQ = "/api/faqs/show"
        val URLServices = "/api/services"
        val URLServiceQuestions = "/api/servicequestion/show"
        val URLVendorList = "/api/services/show"
        //val URLVendorList = "/api/serviceproviders"


        val CHANGEPASSWORD = "changepassword"

        val URLMYREQUEST = "/api/servicerequest/list"

        val VENDORSDETAILS = "/api/vendors/details"

        val SP_CATEGORY = "sp_category"
        val CUST_REFERBY = "cust_referal"


        val SERVICE_PROVIDERS_DETAILS = "service_provider_details"

        val SERVICEREQUEST = "/api/servicerequest/create"
        val SERVICES = "/api/services/show"

        val MYREQDETAILS = "/api/servicerequest/details"


        val BUSINESSDETAILS = "/api/providerbusiness/show"
        val PRICINGDETAILS = "/api/providerservicetypes/show"

        val ACCEPTVENDORS = "/api/serviceproviderleads/vendorlist"

        val CHANGESTATUS = "/api/serviceproviderleads/changestatus"

        val CANCELREQUEST = "/api/serviceproviderleads/cancel"

        val DISPLAYPROFILE = "/api/customers/details"
        val EDITPROFILE = "/api/customers/edit"

        val SEARCHSERVICE = "/api/services/searchbyname"



        val CUSTOMERADD = "customer_address_details"

        val PB_SPID = "pb_spid"
        val PST_SPID = "pst_spid"

        val SPL_TXNID = "spl_txnid"

        val SPL_SPID = "spl_spid"
        val TC_SPID = "tc_spid"
        val SP_FIRSTNAME = "sp_firstname"
        val SP_LASTNAME = "sp_lastname"
        val SP_IMAGE = "sp_image"


        var SPL_STATUS = "spl_status"

         var CHANGEPASSOWRDURL = "/api/customers/changepassword"

        var NEWPASSWORD = "new_cust_password"
        var OLDPASSWORD = "old_cust_password"

        val lsImageDirectory = Environment.getExternalStorageDirectory().toString() + "/" + APPNAME + "/Images"
        val lsLangId = "sq_langid"

        val USERID = "userid"
        val PASSWORD = "cust_password"
        val ARTICLEIMAGES = "articleimages"

        val ARTICLES= "articles"
        val FAQ = "faq"
        val WORKINGIMGS = "workimages"

        val SR_STATUS ="sr_status"

        val SR_CUSTID = "sr_custid"
        val SRV_ID = "srv_id"
        val SR_SRVID = "sr_srvid"
        val SRV_IMAGE = "srv_image"
        val SR_SRVTYPEID = "sr_srvtypeid"
        val SR_CUSTBUDGET = "sr_customerbudget"
        val SR_LOCATIONID = "sr_locationid"
        val SP_ID = "sp_id"
        val SR_PLANNEDDATE = "sr_planneddate"
        val SR_PLANNEDTIME = "sr_plannedtime"
        val SR_LOCATION = "sr_location"
        val SR_CREATEDBY = "sr_createdby"
        val SR_ENABLEMESSAGING = "sr_enablemessaging"
        val SR_SHAREMOBILENO = "sr_sharemobileno"
        val banners : ArrayList<String>? = ArrayList()

        val GENDER = "cust_gender"

        val USERNAME = "cust_username"
        val FNAME = "cust_firstname"
        val LNAME = "cust_lastname"
        val EMAIL = "cust_email"
        val MOBILE = "cust_mobileno"
        val LANGUAGE = "cust_languagepreferred"
        val CREATEDBY = "cust_createdby"
        val CUST_PROFILEIMAGE = "cust_profileimage"
        val CUSTOMERUPDATEDBY = "cust_updatedby"
        val LANGUAGEPREFERENCE = "cust_languagepreferred"

        val GETADDRESS = "/api/customers/showaddress"

        var CUST_ID = "cust_id"
        var TXN_ID  = "sr_txnid"

        val URLAddressCreate = "/api/address/create"
        val SRNO = "srno"
        val ADD_ENTITYID = "add_entityid"
        val ADD_ENTITYTYPE = "add_entitytype"
        val ADD_TYPE = "add_type"
        val ADD_LINE1 = "add_line1"
        val ADD_LINE2 = "add_line2"
        val ADD_CITY = "add_city"
        val ADD_STATE = "add_state"
        val ADD_PINCODE = "add_pincode"
        val ADD_LATTITUDE = "add_latitude"
        val ADD_LONGITUDE = "add_longitude"


        var TIMEOUTREAD=60000
        val CUSTOMERFIRSTNAME = "cust_firstname"
        val CUSTOMERLASTNAME = "cust_lastname"

        var TIMEOUT=30000




        val SRV_NAME = "srv_name"

        //CHAT
        val CHAT_ID="tc_id"
        val CHATCUSTOMERID="tc_custid"
        val CHATVENDORID="tc_spid"
        val CHATTXNID="tc_txnid"
        val CHATSENDER="tc_sender"
        val CHATMESSAGE="tc_message"
        val CHAT_DATETIME="created_at"
        val CUST_NAME="cust_name"
       // val SRV_NAME="srv_name"

        val CHAT_ENTITY_TYPE="entity_type"
        val CHAT_ENTITY_ID="entity_id"

        val CHATSENDMESSAGE="/api/txnchat/create"
        val CHATSHOW="/api/txnchat/show"
        val CONVERSATIONS="/api/txnchat/getconv"

        //Notification
       val NOTIFICATIONTYPE="notification_type"
        val NOTIFICATIONDATA="data"
        val NOTIFICATIONMESSAGE="message"

        //Notifciation_type=message
        val CUSTOMERNAME="cust_name"
        val VENDORNAME="sp_name"




        fun isValidEmail(email: String): Boolean {
            val EMAIL_PATTERN = "^[_a-zA-Z0-9]+(\\.[_a-zA-Z0-9]+)*@" + "[a-z0-9]+(\\.[a-z0-9]+)*(\\.[a-z]{2,3})$"

            val pattern = Pattern.compile(EMAIL_PATTERN)
            val matcher = pattern.matcher(email)
            return matcher.matches()
        }

        fun isValidContact(contact: String): Boolean {
            val CONTACT_NAME = "[0-9]{10,11}"
            val pattern = Pattern.compile(CONTACT_NAME)
            val matcher = pattern.matcher(contact)
            return matcher.matches()
        }


        fun isValidUser(name: String): Boolean {
            val USER_NAME = "[a-zA-Z]{3,50}"
            val pattern = Pattern.compile(USER_NAME)
            val matcher = pattern.matcher(name)
            return matcher.matches()

        }

        fun isValidPassword(password: String): Boolean {
            val PASSWORD = "[a-zA-Z0-9\\.\\(\\)\\/\\,\\@,\\$,\\%,\\&,\\!]{6,18}"
            val pattern = Pattern.compile(PASSWORD)
            val matcher = pattern.matcher(password)
            return matcher.matches()

        }


    }





}

