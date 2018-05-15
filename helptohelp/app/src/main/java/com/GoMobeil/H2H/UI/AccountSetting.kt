package com.GoMobeil.H2H.UI

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import com.GoMobeil.H2H.BaseActivity
import com.GoMobeil.H2H.R
import com.GoMobeil.H2H.Services.TransperantProgressDialog
import com.GoMobeil.H2H.StaticRefs
import com.GoMobeil.H2H.prefs
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.sdsmdg.tastytoast.TastyToast
import kotlinx.android.synthetic.main.account_setting.*
import org.json.JSONObject
import java.util.*


class AccountSetting : BaseActivity() {

    override lateinit var context: Context
    override lateinit var activity: Activity
    lateinit var language: String
    var text: String = ""
    lateinit var pd: TransperantProgressDialog
    var languageToLoad ="en"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_setting)
        setTitle("Account Settings")
       // hideFooter(true)

        context = this
        activity = this

        pd = TransperantProgressDialog(context)

    getData()

    }

    override fun onBackPressed() {
        val popup1 = android.support.v7.app.AlertDialog.Builder(this)
        popup1.setTitle("Language Setting")
                .setMessage("app needs to be restarted for language setting to be effective.close app now?")
                .setPositiveButton("YES", DialogInterface.OnClickListener {

                    dialog, which ->
                    // finish()
                    if (rbEnglish.isChecked) {
                        text = "EN"
                        languageToLoad = "en"

                    } else if (rbHindi.isChecked) {
                        text = "HI"
                        languageToLoad ="hi"

                    } else if (rbMarathi.isChecked) {
                        text = "MA"
                        languageToLoad ="mr"
                    }
                    if (language.equals(text)) {
                        TastyToast.makeText(context, " Default language is same as default", TastyToast.LENGTH_LONG, TastyToast.SUCCESS)
                    }
                    else
                    {

                       saveData()
                    }


                })
                .setNegativeButton("No", null)
                .show()


    }

   fun saveData() {

        if (rbEnglish.isChecked) {
            text = "EN"
        } else if (rbHindi.isChecked) {
            text = "HI"
        } else if (rbMarathi.isChecked) {
            text = "MA"
        }
        TastyToast.makeText(context, text, TastyToast.LENGTH_LONG, TastyToast.SUCCESS)

        Fuel.post(StaticRefs.EDITPROFILE, listOf(StaticRefs.CUST_ID to prefs.cust_id,
                (StaticRefs.LANGUAGEPREFERENCE to text)))
                .responseJson() { request,
                                  response,
                                  result ->
                    result.fold({ d ->
                        parseJsonSave(result.get().content)
                    }, { err ->
                        TastyToast.makeText(context, "Internet Network Down", TastyToast.LENGTH_LONG, TastyToast.ERROR)
                    })
                }
    }

    fun parseJsonSave(response: String) {

        val json = JSONObject(response)
        if (json.has(StaticRefs.STATUS) && json.getString(StaticRefs.STATUS) != null) {

            var message = ""
            if (json.has(StaticRefs.MESSAGE) && json.getString(StaticRefs.MESSAGE) != null) {
                message = json.getString(StaticRefs.MESSAGE)
            }
            if (json.getString(StaticRefs.STATUS).equals(StaticRefs.FAILED)) {
                TastyToast.makeText(this, message, Toast.LENGTH_LONG, TastyToast.ERROR).show()
            } else if (json.getString(StaticRefs.STATUS).equals(StaticRefs.STATUSSUCCESS)) {
                prefs.language = text
                val locale = Locale(prefs.language)
                Locale.setDefault(locale)
                val config = Configuration()
                config.locale = locale
                resources.updateConfiguration(config, resources.displayMetrics)
                finishAffinity ();
                // System.exit(0)
                Runtime.getRuntime().exit(0)

            }

        }
    }

    fun showdata(language: String) {

        if (language.equals("EN")) rbEnglish.isChecked = true
        else if (language.equals("HI")) rbHindi.isChecked = true
        else if (language.equals("MA")) rbMarathi.isChecked = true
        else if (language.equals("")) rbEnglish.isChecked = true

    }

  fun getData() {
        pd.show()
        Fuel.post(StaticRefs.DISPLAYPROFILE , listOf(StaticRefs.CUST_ID to prefs.cust_id))
                .responseJson()
                { request,
                  response,
                  result ->
                    result.fold({ d ->
                        //do something with data
                        pd.show()
                        parseProfile(result.get().content)

                    }, { err ->
                        TastyToast.makeText(context, "Internet Network Down", TastyToast.LENGTH_LONG, TastyToast.ERROR)
                        //do something with error
                    })
                }
    }

    fun parseProfile(response: String) {
        pd.dismiss()
        var jsonob: JSONObject? = null

        if (response.contains(StaticRefs.DATA) && !StaticRefs.USERNAME.equals(null)) {
            val json = JSONObject(response)
            val data = json.getJSONObject(StaticRefs.DATA)
            language = data.getString(StaticRefs.LANGUAGE)

            showdata(language)

        } else {
            pd.dismiss()
            TastyToast.makeText(context, "No Records Found ", TastyToast.LENGTH_LONG, TastyToast.WARNING).show()
        }
    }

}



