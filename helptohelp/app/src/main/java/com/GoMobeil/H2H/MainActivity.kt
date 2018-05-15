package com.GoMobeil.H2H

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.Toast
import com.GoMobeil.H2H.Extensions.isConnectionAvailable
import com.GoMobeil.H2H.Extensions.toast
import com.GoMobeil.H2H.Models.ServicesModel
import com.GoMobeil.H2H.Services.TransperantProgressDialog
import com.GoMobeil.H2H.UI.AddressFirst
import com.GoMobeil.H2H.UI.ChangePassword
import com.GoMobeil.H2H.UI.MyRequest
import com.GoMobeil.H2H.UI.Register
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.sdsmdg.tastytoast.TastyToast
import kotlinx.android.synthetic.main.appoint.*
import kotlinx.android.synthetic.main.login.*
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    companion object {
        var TAG : String? = "Login"
    }

    lateinit var activity : Activity;
    lateinit var context : Context
    var userid : String? = ""

    var  lsUserid:String? = ""
    var srvId : Int? =null
    var servName : String? =null
    var dateScheduled : String? = null
    var alQuestionsArray : ArrayList<String> = ArrayList()
    var alAnswersArray : ArrayList<String> = ArrayList()
    var alAnsIdsArray : ArrayList<String> = ArrayList()
    var alQueIdsArray : ArrayList<String> = ArrayList()
    var lsStartTime : String? = null
    var lsEndTime : String? = null
    var lsPassword:String?=""
    lateinit var pd : TransperantProgressDialog

    var l:Int? = 1


    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login)
        activity = this;
        context = this;

        pd = TransperantProgressDialog(context)

        l =getIntent().getIntExtra("login",1)



        if ((l==2)){
            srvId = getIntent().getIntExtra(ServicesModel.SRV_ID, 0)
            servName = getIntent().getStringExtra(ServicesModel.SRV_NAME)
            dateScheduled = getIntent().getStringExtra("date")
            alQuestionsArray = getIntent().getStringArrayListExtra("Questions")
            alAnswersArray = getIntent().getStringArrayListExtra("Answers")
            alAnsIdsArray = getIntent().getStringArrayListExtra("AnsIds")
            alQueIdsArray = getIntent().getStringArrayListExtra("QueIds")
            lsStartTime = getIntent().getStringExtra("startTime")
            lsEndTime = getIntent().getStringExtra("endTime")

        }
        else{

        }




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



            if (!srvId.toString().equals("") && !srvId.toString().equals(null) &&  !servName.equals("") && !servName.equals(null) && !dateScheduled.equals("") && !dateScheduled.equals(null)
                    && alQueIdsArray.size > 0 && alAnswersArray.size>0 && alAnsIdsArray.size > 0&& alQueIdsArray.size > 0
                    && !lsStartTime.equals("") && !lsStartTime.equals(null) && !lsEndTime.equals("") && !lsEndTime.equals(null)){

                val intent = Intent(context,Register::class.java)
                intent.putExtra(ServicesModel.SRV_ID, srvId!!)
                intent.putExtra(ServicesModel.SRV_NAME, servName)
                //intent.putExtra(StaticRefs.CUST_ID,1)
                intent.putExtra("date",dateScheduled)
                intent.putExtra("Questions",alQuestionsArray)
                intent.putExtra("Answers", alAnswersArray)
                intent.putExtra("AnsIds", alAnsIdsArray)
                intent.putExtra("QueIds", alQueIdsArray)

                intent.putExtra("startTime",lsStartTime)
                intent.putExtra("endTime",lsEndTime)
                intent.putExtra("register",2)
                startActivity(intent)
                finish()

            }

            else{


                val intent = Intent(context,Register::class.java)

                startActivity(intent)
                finish()

            }

        }
        tvForgetPassword.setOnClickListener {
            val intent = Intent(this, ChangePassword::class.java)
            intent.putExtra(StaticRefs.CHANGEPASSWORD, "forgot_password")
            startActivity(intent)
            finish()
        }

    }

    fun callLogin() {

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
            result ->


            parseResponse(result.get().content)


        }

    }

    fun parseResponse(response : String) {
        val json = JSONObject(response);
        if (json.getString(StaticRefs.STATUS)=="Failed"){

            pd.dismiss()
            TastyToast.makeText(context,"Incorrect Userid or password",Toast.LENGTH_LONG,TastyToast.ERROR).show()
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
                prefs.cust_id = jsonMainData.getInt(StaticRefs.CUST_ID)
                prefs.fname = jsonMainData.getString(StaticRefs.FNAME)
                prefs.lname = jsonMainData.getString(StaticRefs.LNAME)
                prefs.email = jsonMainData.getString(StaticRefs.EMAIL)
                prefs.mobile = jsonMainData.getString(StaticRefs.MOBILE)
                prefs.language = jsonMainData.getString(StaticRefs.LANGUAGE)
                prefs.token = json.getString(StaticRefs.TOKEN)
                prefs.user = this!!.lsUserid!!
                println("Token is " + prefs.token);
                toast(message);

                if (!srvId.toString().equals("") && !srvId.toString().equals(null) && !servName.equals("") && !servName.equals(null) && !dateScheduled.equals("") && !dateScheduled.equals(null)
                        && alQueIdsArray.size > 0 && alAnswersArray.size > 0 && alAnsIdsArray.size > 0 && alQueIdsArray.size > 0
                        && !lsStartTime.equals("") && !lsStartTime.equals(null) && !lsEndTime.equals("") && !lsEndTime.equals(null)) {



                    val intent = Intent(this, AddressFirst::class.java)
                    intent.putExtra(ServicesModel.SRV_ID, srvId)
                    intent.putExtra(ServicesModel.SRV_NAME, servName)
                    //intent.putExtra(StaticRefs.CUST_ID,1)
                    intent.putExtra("date", dateScheduled)
                    intent.putExtra("Questions", alQuestionsArray)
                    intent.putExtra("Answers", alAnswersArray)
                    intent.putExtra("AnsIds", alAnsIdsArray)
                    intent.putExtra("QueIds", alQueIdsArray)

                    intent.putExtra("startTime", lsStartTime)
                    intent.putExtra("endTime", lsEndTime)
                    pd.dismiss()
                    startActivity(intent)
                    finish()

                } else {


                    val intent = Intent(context, Home::class.java)
                    pd.dismiss()
                    startActivity(intent)
                    finish()




                }
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

    override fun onBackPressed() {
        super.onBackPressed()
    }

}
