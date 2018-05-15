package com.GoMobeil.H2H.Services

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.PopupWindow
import android.widget.TextView
import com.GoMobeil.H2H.R
import com.GoMobeil.H2H.StaticRefs
import com.GoMobeil.H2H.prefs
import com.github.kittinunf.fuel.core.Handler

/**
 * Created by ADMIN on 08-02-2018.
 */
class CustomDialog( val activity: Activity,  val context: Context) : PopupWindow(), View.OnClickListener {
    internal var popW: PopupWindow? = null
    internal var popUp: View? = null
    private var mClickListener: DialogButtonClick? = null

    internal var lbCancelVisible: Boolean? = null
    var pincodeFirst : String? =null
    var locationFirst : String? = null
    lateinit var tvFirstPin : TextView
    lateinit var tvSecondPin : TextView


    fun showDialog(zipcode : String , location : String) {

        pincodeFirst = prefs.pincode
        locationFirst = prefs.location

        popUp = LayoutInflater.from(context).inflate(R.layout.dialog, null)

        tvFirstPin = popUp!!.findViewById<TextView>(R.id.tvFirstPin)
        tvSecondPin = popUp!!.findViewById<TextView>(R.id.tvSecondPin)

        popW = PopupWindow(popUp, 0, 0, true)
        val b = CustomServices.getCoordinates(context, .80, .30)
        popW!!.width = b.getInt("WIDTH")
        popW!!.height = b.getInt("HEIGHT")
        popW!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        popW!!.isOutsideTouchable = false

        tvFirstPin.text = "$pincodeFirst  $locationFirst"
        tvSecondPin.text = "$zipcode  $location"


            popW!!.showAtLocation(activity.window.decorView, Gravity.CENTER, 0, 0)
    }


    fun setDialogButtonClickListener(listener: DialogButtonClick) {
        mClickListener = listener
    }


    interface DialogButtonClick {

        fun DialogButtonClicked(view: View)
    }

    override fun onClick(v: View) {
        mClickListener!!.DialogButtonClicked(v)
        if (popW != null) {
            popW!!.dismiss()

        } else {

            (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).removeView(popUp)
            popUp = null
        }
    }



    fun setCancel(lbEnableCancel: Boolean) {
        lbCancelVisible = lbEnableCancel
    }

}