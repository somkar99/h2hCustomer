package com.GoMobeil.H2H.UI

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.GoMobeil.H2H.Extensions.toast
import com.GoMobeil.H2H.Home
import com.GoMobeil.H2H.Models.ServicesModel
import com.GoMobeil.H2H.R
import com.GoMobeil.H2H.StaticRefs
import com.GoMobeil.H2H.prefs
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.sdsmdg.tastytoast.TastyToast
import kotlinx.android.synthetic.main.registration.*
import kotlinx.android.synthetic.main.registrations.*
import org.json.JSONObject

/**
 * Created by Admin on 21-03-18.
 */
class Register : AppCompatActivity(),AdapterView.OnItemSelectedListener{


    lateinit var activity : Activity
    lateinit var context : Context

    var lsMessage = ""
    var lsUsername = ""
    var lsFName=""
    var lsLName=""
    var lsEmail=""
    var lsMobile=""
    var lsLanguage=""
    var lsPassword=""
    var lsConfPassword=""
    var lsReferBy =""
    var text: String = ""

    var userid : String? = ""

    var srvId : Int? =null
    var servName : String? =null
    var dateScheduled : String? = null
    var alQuestionsArray : ArrayList<String> = ArrayList()
    var alAnswersArray : ArrayList<String> = ArrayList()
    var alAnsIdsArray : ArrayList<String> = ArrayList()
    var alQueIdsArray : ArrayList<String> = ArrayList()
    var lsStartTime : String? = null
    var lsEndTime : String? = null

    var l:Int? = 1






    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registrations)

        activity = this
        context = this

        l =getIntent().getIntExtra("register",1)

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




        val personNames = arrayOf("English","Marathi","Hindi")
        val spinner = findViewById<Spinner>(R.id.spLanguage)
        if (spinner != null) {
            val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, personNames)
            spinner.adapter = arrayAdapter

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    lsLanguage = personNames.get(position)
                    if(lsLanguage.equals("English")){
                        text="EN"
                    }else if(lsLanguage.equals("Hindi")){
                        text="HI"
                    }
                    else if(lsLanguage.equals("Marathi")){
                        text="MR"
                    }


                    TastyToast.makeText(context,   " " + lsLanguage, Toast.LENGTH_SHORT,TastyToast.INFO).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Code to perform some action when nothing is selected
                }
            }
        }

        initLayout()




    }

    fun initLayout(){

        //lsUsername = editUserName.text.toString()
        lsFName = editFirstName.text.toString()
        lsLName = editLastName.text.toString()
        lsEmail = editEmail.text.toString()
        lsMobile = editMobile.text.toString()
        lsPassword = editPassword.text.toString()
        lsConfPassword = editConfPassword.text.toString()
        lsReferBy = etReferalcode.text.toString()






        cbRegister.setOnClickListener {
            sendDetails()

        }









    }

    fun sendDetails(){
        //lsUsername = editUserName.text.toString()
        lsFName = editFirstName.text.toString()
        lsLName = editLastName.text.toString()
        lsEmail = editEmail.text.toString()
        lsMobile = editMobile.text.toString()
        lsPassword = editPassword.text.toString()
        lsConfPassword = editConfPassword.text.toString()
        lsReferBy = etReferalcode.text.toString()






        var lbProceedAhead = true


        if (!((lsLName != null && lsLName.length >0)))
        {
            genMessage(" Last Name ");
            lbProceedAhead = false;
        }




        if (!((lsEmail != null && lsEmail.length >0)))
        {
            genMessage(" Email")
            lbProceedAhead = false;
        }




        if (!((lsMobile != null && lsMobile.length >0)))
        {
            genMessage("Mobile No")
            lbProceedAhead = false;
        }




        if (!((lsConfPassword  != null && lsConfPassword.length >0)))
        {
            genMessage("Password")
            lbProceedAhead = false;
        }



        if (!((lsPassword  != null && lsPassword.length >0 )))
        {
            genMessage("Password")
            lbProceedAhead = false;
        }




        if (lbProceedAhead){


            if (!StaticRefs.isValidEmail(lsEmail)) {
                editEmail.setError("enter a valid email")
                lbProceedAhead = false
                TastyToast.makeText(context, "enter a valid email", TastyToast.LENGTH_LONG,TastyToast.ERROR).show()
                return
            }

            if (!StaticRefs.isValidContact(lsMobile)) {
                editMobile.setError("enter a valid mobile number")
                lbProceedAhead = false
                TastyToast.makeText(context, "enter a valid mobile number", TastyToast.LENGTH_LONG,TastyToast.ERROR).show()
                return
            }

            if (!StaticRefs.isValidUser(lsFName)) {

                editFirstName.setError("enter a valid first name")
                lbProceedAhead = false
                TastyToast.makeText(context, "enter a valid first number", TastyToast.LENGTH_LONG,TastyToast.ERROR).show()
                return
            }

            if (!StaticRefs.isValidUser(lsLName)) {
                editLastName.setError("enter a valid last number")
                lbProceedAhead = false
                TastyToast.makeText(context, "enter a valid last name", TastyToast.LENGTH_LONG,TastyToast.ERROR).show()
                return
            }

            if (!StaticRefs.isValidPassword(lsPassword)) {
                editPassword.setError("enter a valid password")
                lbProceedAhead = false
                TastyToast.makeText(context, "enter a valid password", TastyToast.LENGTH_LONG,TastyToast.ERROR).show()
                return
            }

            if (!lsPassword.equals(lsConfPassword)) {
                editConfPassword.setError("password should match")
                lbProceedAhead = false
                Toast.makeText(context, "password should match", Toast.LENGTH_LONG).show()
                return
            }


        }

        if (!(lbProceedAhead) && lsMessage.length > 0)
        {
            if (lsMessage.length > 50)
            {
                Toast.makeText(context,"Please enter all details",Toast.LENGTH_LONG).show()
            }
            else {
                Toast.makeText(context, "Please enter valid " + lsMessage, Toast.LENGTH_LONG).show()
            }
            return
        }
        else
        {
            registration()
        }

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {


    }



    fun genMessage(msg : String)
    {
        if (lsMessage.length > 0)
        {
            lsMessage = lsMessage + ", "+msg;
        }
        else {
            lsMessage = lsMessage + " " + msg;
        }
    }

    fun registration() {

        //lsUsername = editUserName.text.toString()
        lsFName = editFirstName.text.toString()
        lsLName = editLastName.text.toString()
        lsEmail = editEmail.text.toString()
        lsMobile = editMobile.text.toString()
        lsPassword = editPassword.text.toString()
        lsConfPassword = editConfPassword.text.toString()
        lsReferBy = etReferalcode.text.toString()


        var params: Map<String, String>

        Fuel.post(StaticRefs.URLREGISTER, listOf(StaticRefs.USERNAME to lsFName,
                (StaticRefs.FNAME to lsFName),
                (StaticRefs.LNAME to lsLName),
                (StaticRefs.EMAIL to lsEmail),
                (StaticRefs.MOBILE to lsMobile),
                (StaticRefs.PASSWORD to lsPassword),
                (StaticRefs.LANGUAGE to text),
                (StaticRefs.CREATEDBY to lsFName + lsLName),
                (StaticRefs.DEVICETOKEN to prefs.devicetoken),
                (StaticRefs.CUST_REFERBY to lsReferBy)
        ))

                .responseJson()
                { request,
                  response,
                  result ->
                    result.fold({ d ->
                        parseResponse(result.get().content)

                    }, { err ->
                        TastyToast.makeText(context, "Internet Network Down", TastyToast.LENGTH_LONG, TastyToast.ERROR)
                    })


                }
    }

    fun parseResponse(response:String){
            val json = JSONObject(response)


        if (json.has(StaticRefs.STATUS) && json.getString(StaticRefs.STATUS)!=null){

            var message = ""
            if (json.has(StaticRefs.MESSAGE)&& json.getString(StaticRefs.MESSAGE)!=null){

                message = json.getString(StaticRefs.MESSAGE)
            }
            if (json.getString(StaticRefs.STATUS).equals(StaticRefs.FAILED)){
                val error = json.getString("errors")

                TastyToast.makeText(context,error.toString(),TastyToast.LENGTH_LONG,TastyToast.ERROR).show()
            }

            else {

                if(response.contains(StaticRefs.DATA)&&json.getString(StaticRefs.DATA)!=null&& !json.getString(StaticRefs.DATA).equals("null"))
                {

                val json1 = json.getJSONObject(StaticRefs.DATA)

                if (json1.length()<0){
                    TastyToast.makeText(context,"No value for data",TastyToast.LENGTH_LONG,TastyToast.ERROR).show()
                }
                else {
                    val json2 = json.getString(StaticRefs.DATA)
                    val jsonMainData = JSONObject(json2)
                    TastyToast.makeText(context, "Register Successfully", TastyToast.LENGTH_LONG, TastyToast.SUCCESS).show()
                    prefs.cust_id = jsonMainData.getInt(StaticRefs.CUST_ID)

                    prefs.token = jsonMainData.getString(StaticRefs.TOKEN)
                    prefs.user = this!!.lsEmail!!
                    println("Token is " + prefs.token);
                    //toast(message);


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
                        startActivity(intent)
                        finish()

                    } else {


                        val intent = Intent(context, Home::class.java)
                        startActivity(intent)
                        finish()


                    }
                }
            }
                else{

                    TastyToast.makeText(context,"No Data Found",TastyToast.LENGTH_LONG,TastyToast.ERROR).show()

                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

}