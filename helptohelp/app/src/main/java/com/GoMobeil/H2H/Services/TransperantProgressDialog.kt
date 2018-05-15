package com.GoMobeil.H2H.Services

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import com.GoMobeil.H2H.R



/**
 * Created by ADMIN on 16-11-2017.
 */


class TransperantProgressDialog(context: Context) : Dialog(context, R.style.MyTheme) {

    init {

        val wlmp = window!!.attributes

        wlmp.gravity = Gravity.CENTER_HORIZONTAL
        window!!.attributes = wlmp
        setTitle(null)
        setCancelable(true)
        setOnCancelListener(null)
        val view = LayoutInflater.from(context).inflate(
                R.layout.progress_bar, null)
        setContentView(view)
    }
}
