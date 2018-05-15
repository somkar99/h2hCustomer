package com.GoMobeil.H2H


import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.github.kittinunf.fuel.core.FuelManager
import org.acra.ACRA
import org.acra.ReportingInteractionMode
import org.acra.annotation.ReportsCrashes


/**
 * Created by niranjanshah on 29/01/18.
 */

val prefs: Prefs by lazy {
    App.prefs!!
}

@ReportsCrashes(
        mailTo = "zadevinay15@gmail.com",
        mode = ReportingInteractionMode.DIALOG,
        resToastText = R.string.crash_toast_text,
        resDialogText = R.string.crash_dialog_text,
        resDialogIcon = android.R.drawable.ic_dialog_info,
        resDialogTitle = R.string.crash_dialog_title,
        resDialogCommentPrompt = R.string.crash_dialog_comment_prompt,
        resDialogOkToast = R.string.crash_dialog_ok_toast)


class App : Application() {
    companion object {
        var prefs: Prefs? = null
        var IMAGEURL = ""

    }


    override fun onCreate() {
        prefs = Prefs(applicationContext)
        FuelManager.instance.basePath = StaticRefs.BASEURL
        super.onCreate()
        ACRA.init(this)
    }

}


class Prefs(context: Context) {
    val PREFS_FILENAME = "prefs_h2h"

    val TOKEN = StaticRefs.TOKEN;
    val CUST_ID = StaticRefs.CUST_ID;
    val TXN_ID = StaticRefs.TXN_ID
    val SP_ID = StaticRefs.SP_ID
    val FNAME = StaticRefs.FNAME
    val LNAME = StaticRefs.LNAME
    val EMAIL = StaticRefs.EMAIL
    val MOBILE = StaticRefs.MOBILE
    val LANGUAGE = StaticRefs.LANGUAGE
    val DEVICETOKEN=StaticRefs.DEVICETOKEN
    val REMEMBER_ME = StaticRefs.REMEMBER_ME
    val USERID = StaticRefs.USERID
    val PASSWORD = StaticRefs.PASSWORD


    val USERNAME = StaticRefs.USERNAME


    val USER = "USER"
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0);

    var user: String
        get() = prefs.getString(USER, "");
        set(value) = prefs.edit().putString(USER, value).apply();

    var txnid : String
        get() = prefs.getString(TXN_ID,"")
        set(value) = prefs.edit().putString(TXN_ID,value).apply()

    var token: String
        get() = prefs.getString(TOKEN, "");
        set(value) = prefs.edit().putString(TOKEN, value).apply();

    var cust_id: Int
        get() = prefs.getInt(CUST_ID, 0);
        set(value) = prefs.edit().putInt(CUST_ID, value).apply();

    var sp_id : String
        get() = prefs.getString(SP_ID,"")
        set(value) = prefs.edit().putString(SP_ID,value).apply()

    var fname :String
        get() = prefs.getString(FNAME,"")
        set(value) = prefs.edit().putString(FNAME,value).apply()

    var lname :String
        get() = prefs.getString(LNAME,"")
        set(value) = prefs.edit().putString(LNAME,value).apply()

    var email :String
        get() = prefs.getString(EMAIL,"")
        set(value) = prefs.edit().putString(EMAIL,value).apply()

    var mobile :String
        get() = prefs.getString(MOBILE,"")
        set(value) = prefs.edit().putString(MOBILE,value).apply()

    var language :String
        get() = prefs.getString(LANGUAGE,"")
        set(value) = prefs.edit().putString(LANGUAGE,value).apply()

    var username : String
        get() = prefs.getString(USERNAME,"")
        set(value) = prefs.edit().putString(USERNAME,value).apply()
    var remember_me: Boolean
        get() = prefs.getBoolean(REMEMBER_ME, false);
        set(value) = prefs.edit().putBoolean(REMEMBER_ME, value).apply()

    var loginid: String
        get() = prefs.getString(USERID, "")
        set(value) = prefs.edit().putString(USERID, value).apply()

    var password: String
        get() = prefs.getString(PASSWORD, "")
        set(value) = prefs.edit().putString(PASSWORD, value).apply()



    var devicetoken
        get() = prefs.getString(DEVICETOKEN, "")
        set(value) = prefs.edit().putString(DEVICETOKEN, value).apply()





    val INSTALLATIONPIN = "PIN"

    val PINCODE_FIR = StaticRefs.PINCODE_FIRST
    val prefsPinLoc : SharedPreferences = context.getSharedPreferences(INSTALLATIONPIN ,0)

    var pincode : String
        get() = prefsPinLoc.getString(PINCODE_FIR,"")
        set(value) = prefsPinLoc.edit().putString(PINCODE_FIR,value).apply()

    val LOCATION_FIR = StaticRefs.LOCATION_FIRST

    var location : String
        get() = prefsPinLoc.getString(LOCATION_FIR,"")
        set(value) = prefsPinLoc.edit().putString(LOCATION_FIR,value).apply()
}