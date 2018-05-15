package com.GoMobeil.H2H

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.widget.DrawerLayout
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.GoMobeil.H2H.Services.CustomDialog
import com.GoMobeil.H2H.Services.CustomServices
import com.GoMobeil.H2H.UI.*
import com.sdsmdg.tastytoast.TastyToast
import kotlinx.android.synthetic.main.articles_row.*
import kotlinx.android.synthetic.main.base_layout.*
import kotlinx.android.synthetic.main.home.*

/**
 * Created by ADMIN on 06-02-2018.
 */
open class BaseActivity : Activity(), View.OnClickListener {

    open lateinit var context: Context
    open lateinit var activity: Activity
    internal var intent: Intent? = null
    internal var sp: SharedPreferences? = null

    internal var l :Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.setContentView(R.layout.base_layout)

        context = this
        activity = this

        ivMenu.setOnClickListener(this)
        drawer.setOnClickListener(this)
        llDrawerAboutUs.setOnClickListener(this)
        llDrawerMyReq.setOnClickListener(this)
        llDrawerSetting.setOnClickListener(this)
        llDrawerReferFrnds.setOnClickListener(this)
        llDrawerWriteToUs.setOnClickListener(this)
        llDrawerRegAsParent.setOnClickListener(this)
        llDrawerTermsAndCond.setOnClickListener(this)
        llDrawerRateApp.setOnClickListener(this)
        llDrawerLogout.setOnClickListener(this)
        ivBack.setOnClickListener(this)
        ivLogin.setOnClickListener(this)




    }

    fun closeDrawer() {
        if (drawer != null) {
            drawer!!.closeDrawers()
        }
    }

    override fun setContentView(id: Int) {

        val inflater = baseContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(id, baseLayout)
        //setFont(linBase,typeFace);

    }




    override
    fun setTitle(ls_title: CharSequence) {
        tvActivityTitle.setText(ls_title)
        //Toast.makeText(this, ls_title, Toast.LENGTH_SHORT).show();
    }

    override
    fun onClick(v: View) {
        when (v!!.id) {

            R.id.ivMenu -> if (drawer!!.isDrawerOpen(Gravity.LEFT)) {
                closeDrawer()
            } else {
                drawer!!.openDrawer(leftMenu)
                //setupMenu();
            }

            R.id.llDrawerAboutUs ->
            {

                val intent = Intent(this,MyRequest::class.java)
                startActivity(intent)
            }
            R.id.llDrawerMyReq ->
            {

                if (App.prefs!!.user.equals("")|| App.prefs!!.user.equals(null)){

                    val i = Intent(context,MainActivity::class.java)
                    i.putExtra("login",l)
                    startActivity(i)

                }
                else{
                    val intent = Intent(this,Booking::class.java)
                    intent.putExtra("login",2)
                    startActivity(intent)

                }

            }
            R.id.llDrawerSetting ->
            {
                if (App.prefs!!.user.equals("") || App.prefs!!.user.equals(null)){

                    TastyToast.makeText(context,"Please Log in to Change the setting",TastyToast.LENGTH_LONG,TastyToast.INFO).show()
                }
                else{

                    val intent = Intent(this,Setting::class.java)
                    startActivity(intent)

                }
            }
            R.id.llDrawerReferFrnds ->
            {

                val intent = Intent(this,Demo::class.java)
                startActivity(intent)
            }
            R.id.llDrawerWriteToUs ->
            {
                val intent = Intent(this,MyRequest::class.java)
                startActivity(intent)

            }
            R.id.llDrawerRegAsParent ->
            {
                val intent = Intent(this,MyRequest::class.java)
                startActivity(intent)

            }
            R.id.llDrawerTermsAndCond ->
            {
                val intent = Intent(this,MyRequest::class.java)
                startActivity(intent)

            }
            R.id.llDrawerRateApp ->
            {
                val intent = Intent(this,MyRequest::class.java)
                startActivity(intent)

            }
            R.id.llDrawerLogout ->
            {

                if (prefs.user.equals("") || prefs.user.equals(null)){

                    TastyToast.makeText(context,"You are not logged in",TastyToast.LENGTH_LONG,TastyToast.INFO).show()
                }

                else{

                val intent = Intent(this,Home::class.java)
                prefs.token = ""
                prefs.user = ""
                    prefs.loginid=""
                    prefs.password=""
                    prefs.remember_me=false
                startActivity(intent)
                finish()
                }
            }

            R.id.ivBack ->{
                if (drawer.isDrawerOpen(Gravity.LEFT)){
                    closeDrawer()
                }
                else{
                    finish()
                }
            }
            R.id.ivLogin ->{
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
                finish()

            }

         /*   R.id.ivBack ->{

                val am = context.getSystemService(Context.ACTIVITY_SERVICE)as ActivityManager
                val cn = am.getRunningTasks(1)[0].topActivity
                var an = cn.shortClassName

                if (an.equals("UI.Profile")){

                }
            }*/




        }

    }

    override
    fun onBackPressed() {

           if(drawer.isDrawerOpen(Gravity.LEFT))
        {
            closeDrawer();
        }

    /*    else if(etSearch.getVisibility() == View.VISIBLE)
        {
            etSearch.setVisibility(View.GONE);
            ivClose.setVisibility(View.GONE);
            ivSearch.setVisibility(View.VISIBLE);
            tvTitle.setVisibility(View.VISIBLE);
            CustomServices.hideSoftKeyboard(getParent());
        }*/
        else
        {
            super.onBackPressed();
        }

        //super.onBackPressed()
    }


    companion object {

        internal var TAG = "BaseActivity"
    }
}