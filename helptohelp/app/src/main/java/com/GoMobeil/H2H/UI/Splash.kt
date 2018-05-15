package com.GoMobeil.H2H.UI

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Toast
import com.GoMobeil.H2H.*
import com.GoMobeil.H2H.Services.RegisterForPushNotificationsAsync
import com.GoMobeil.H2H.Services.TransperantProgressDialog
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.sdsmdg.tastytoast.TastyToast
import kotlinx.android.synthetic.main.login.*
import me.pushy.sdk.Pushy
import org.json.JSONObject

/**
 * Created by ADMIN on 02-02-2018.
 */
class Splash : Activity()
{

    companion object {
        var TAG:String? = "Splash"
    }

    lateinit var context:Context
    lateinit var activity:Activity
    var rememberme: Boolean? = false
    lateinit var pd : TransperantProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Pushy.listen(this)

        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Request both READ_EXTERNAL_STORAGE and WRITE_EXTERNAL_STORAGE so that the
            // Pushy SDK will be able to persist the device token in the external storage
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), 0)
        }




        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.splash1)
        context = this
        activity = this

        val registerPushy = RegisterForPushNotificationsAsync()
        registerPushy.setContext(context)
        registerPushy.execute()
        Pushy.listen(this)
        pd = TransperantProgressDialog(context)


        val handler : Handler? = Handler()

        val ivLogoText = findViewById<ImageView>(R.id.ivLogoText)
        val ivLogoSlogan = findViewById<ImageView>(R.id.ivLogoSlogan)
        val an = AnimationUtils.loadAnimation(baseContext, R.anim.left_to_right)
        val an2 = AnimationUtils.loadAnimation(baseContext, R.anim.left_to_right)

        ivLogoSlogan.visibility = View.INVISIBLE

        ivLogoText.startAnimation(an)
        an.setAnimationListener(object : Animation.AnimationListener {

            override fun onAnimationStart(animation: Animation) {

            }

            override fun onAnimationEnd(animation: Animation) {

                handler!!.postDelayed(Runnable {
                    ivLogoSlogan.visibility = View.VISIBLE
                    ivLogoSlogan.startAnimation(an2)
                },500)

                an2.setAnimationListener(object : Animation.AnimationListener{
                    override fun onAnimationEnd(animation: Animation?) {
                        handler!!.postDelayed(Runnable {
                            rememberMe()

                           /* val intent = Intent(context,Home::class.java)
                            startActivity(intent)
                            finish()*/
                        },1000)

                    }

                    override fun onAnimationStart(animation: Animation?) {

                    }

                    override fun onAnimationRepeat(animation: Animation?) {

                    }

                })

            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })

    }
    fun rememberMe() {

        rememberme = prefs.remember_me
        if (rememberme == false) {
            val intent = Intent(context, Home::class.java)
            startActivity(intent)
            finish()
        } else {
            var lsUsername = prefs!!.loginid
            var lsPassword = prefs.password
            callLogin(lsUsername, lsPassword)
        }

    }

    fun callLogin(lsUserid:String,lsPassword:String)
    {

        var params :Map<String,String>


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

                       // prefs.user = this!!.lsUserid!!
                        println("Token is " + prefs.token);
                        val intent = Intent(this, Home::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else{
                        TastyToast.makeText(context,"No Data Found", Toast.LENGTH_SHORT, TastyToast.ERROR).show()
                    }
                    // toast(message);
                }
            }
        }
    }



}


