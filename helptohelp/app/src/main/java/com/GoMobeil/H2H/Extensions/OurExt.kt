package com.GoMobeil.H2H.Extensions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.util.Base64
import android.widget.ImageView
import android.widget.Toast
import java.io.IOException

/**
 * Created by niranjanshah on 30/01/18.
 *
 * https://stackoverflow.com/questions/45582732/let-also-apply-takeif-takeunless-in-kotlin
 */

fun ImageView.loadBase64Image(image: String?) {
    if (!(image.isNullOrEmpty())) {
        val base64Image = image!!.split(",")[1];
        val decodedString = Base64.decode(base64Image, Base64.DEFAULT)
        val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        setImageBitmap(decodedByte)
    }
    //return decodedByte;

}

@Throws(IOException::class)
fun Context.getBitmapFromUri(uri: Uri): Bitmap {
    val parcelFileDescriptor = contentResolver.openFileDescriptor(uri, "r")
    val fileDescriptor = parcelFileDescriptor!!.fileDescriptor
    val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
    parcelFileDescriptor.close()
    return image
}


fun Context.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
}


fun Context.toastLong(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
}

fun String.isNumber(): Boolean = this.matches("[0-9]+".toRegex())


fun isConnectionAvailable(context: Context): Boolean {
    val connectivity = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (connectivity != null) {
        val info = connectivity.allNetworkInfo
        if (info != null)
            for (i in info.indices)
                if (info[i].state == NetworkInfo.State.CONNECTED) {
                    return true
                }


    }
    return false
}

