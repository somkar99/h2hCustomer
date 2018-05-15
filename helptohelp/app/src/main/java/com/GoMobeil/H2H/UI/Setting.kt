package com.GoMobeil.H2H.UI

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.GoMobeil.H2H.*
import kotlinx.android.synthetic.main.setting.*


class Setting : BaseActivity() {
    override lateinit var context: Context
    override lateinit var activity: Activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.setting)
        setTitle("Setting")

        initlayout()

       /* ivSetting.isEnabled = false
        tvSetting.isEnabled = false*/
    }

    fun initlayout() {
        tvName.setText(prefs.fname+" "+ prefs.lname)
        tvMobile.setText(prefs.mobile)


   llChangePassword.setOnClickListener {
            val intent = Intent(this, ChangePassword::class.java)
            intent.putExtra(StaticRefs.CHANGEPASSWORD, "changepassword")
            startActivity(intent)
        }


        llSettings.setOnClickListener {
            val intent = Intent(this, AccountSetting::class.java)
            startActivity(intent)
        }
        llProfile.setOnClickListener {

            val intent = Intent(this,MyProfile::class.java)
            startActivity(intent)

        }
     /*   ivbackPressed.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            intent.putExtra(StaticRefs.FROMSETTING,StaticRefs.YES)
            startActivity(intent)
            finish()

        }*/
    }

    override fun onBackPressed() {
        super.onBackPressed()
        }
}

