package com.GoMobeil.H2H.UI

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Toast
import com.GoMobeil.H2H.Extensions.isConnectionAvailable
import com.GoMobeil.H2H.Extensions.toast
import com.GoMobeil.H2H.Home
import com.GoMobeil.H2H.R
import com.GoMobeil.H2H.Services.TransperantProgressDialog
import com.GoMobeil.H2H.StaticRefs
import com.GoMobeil.H2H.prefs
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.sdsmdg.tastytoast.TastyToast
import kotlinx.android.synthetic.main.login.*
import org.json.JSONObject
import java.net.CacheResponse

class LoginActivity : AppCompatActivity() {

    lateinit var activity : Activity;
    lateinit var context : Context;
    lateinit var pd : TransperantProgressDialog
    var lsUserid:String?=""
    var lsPassword:String?=""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        activity = this;
        context = this;

        pd = TransperantProgressDialog(context)
        initlayout()
    }
    fun initlayout(){
        cbLogin.setOnClickListener {
            if (isConnectionAvailable(context)) {
                callLogin()
            }
            else
            {
                TastyToast.makeText(context,"No Network available, kindly enable Wifi,Internet connection",TastyToast.LENGTH_LONG,TastyToast.ERROR).setGravity(Gravity.CENTER,0,0)
            }

        }
        tvSignUp.setOnClickListener {
            val intent = Intent(context,Register::class.java)
            startActivity(intent)
            finish()
        }

    }
    fun callLogin()
    {

        var params :Map<String,String>
        lsUserid = etUsername.text.toString()
       lsPassword = etPassword.text.toString()

        remember_me()

        pd.show()
        Fuel.post(StaticRefs.URLLogin,listOf(StaticRefs.USERID to lsUserid,(StaticRefs.PASSWORD to lsPassword),(StaticRefs.DEVICETOKEN to prefs.devicetoken)))
                .responseJson()
                {
                    request,
                    response,
                    result -> parseResponse(result.get().content)


                }

    }
    fun parseResponse(response:String){

        val json = JSONObject(response);
        if (json.getString(StaticRefs.STATUS)=="Failed"){

            pd.dismiss()
            TastyToast.makeText(context,"Incorrect Userid or password", Toast.LENGTH_LONG, TastyToast.ERROR).show()
        }else{

            val json1 = json.getString(StaticRefs.DATA)
            val jsonMainData = JSONObject(json1)
            if (json.has(StaticRefs.STATUS) && json.getString(StaticRefs.STATUS) != null) {
                var message = "";
                if (json.has(StaticRefs.MESSAGE) && json.getString(StaticRefs.MESSAGE) != null) {
                    message = json.getString(StaticRefs.MESSAGE)
                }
                if (json.getString(StaticRefs.STATUS).equals(StaticRefs.FAILED)) {
                    //NGS CustomToast
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                } else {
                    if(jsonMainData.length()>0) {
                        prefs.cust_id = jsonMainData.getInt(StaticRefs.CUST_ID)
                        prefs.fname = jsonMainData.getString(StaticRefs.FNAME)
                        prefs.lname = jsonMainData.getString(StaticRefs.LNAME)
                        prefs.email = jsonMainData.getString(StaticRefs.EMAIL)
                        prefs.mobile = jsonMainData.getString(StaticRefs.MOBILE)
                        prefs.language = jsonMainData.getString(StaticRefs.LANGUAGE)
                        prefs.token = json.getString(StaticRefs.TOKEN)

                        prefs.user = this!!.lsUserid!!
                        println("Token is " + prefs.token);
                        val intent = Intent(this, Home::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else{
                        TastyToast.makeText(context,"No Data Found",Toast.LENGTH_SHORT,TastyToast.ERROR).show()
                    }
                   // toast(message);
                    }
            }
        }
    }
    fun remember_me() {
        if (cbRemeberMe.isChecked == false) {
            prefs.remember_me = false
        } else {
            prefs.remember_me = true
            prefs.loginid = lsUserid!!
            prefs.password = lsPassword!!


        }
    }
}
