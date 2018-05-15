package com.GoMobeil.H2H.Models

import com.GoMobeil.H2H.StaticRefs
import com.beust.klaxon.JsonObject
import com.beust.klaxon.int
import com.beust.klaxon.string
import com.github.kittinunf.fuel.util.Base64
import android.graphics.BitmapFactory
import android.graphics.Bitmap



/**
 * Created by niranjanshah on 29/01/18.
 */


class SCModel
{

    companion object {
        val SC_ID = "sc_id";
        val SC_NAME = "sc_name";
        val SC_DESCRIPTION = "sc_description"
        val SC_IMAGE = "sc_image"

        val SRVID = "srvid"


    }
    var sc_id : Int? = null;
    var sc_name : String? = null;
    var sc_description : String? = null;
    var sc_image : String? = null;

    constructor(jsonObject: JsonObject)
    {
        sc_id = jsonObject.int(SC_ID)
        sc_name = jsonObject.string(SC_NAME)
        sc_description = jsonObject.string(SC_DESCRIPTION)
        sc_image = jsonObject.string(SC_IMAGE)
    }
}


class BannerModel
{
        companion object {
            val BN_IMAGE = "ART_IMAGE"
            val BN_ENTITYTYPE = "img_entitytype"
    }
    var ART_IMAGE : String? = null;
    var entity : String? = null

    constructor(jsonObject: JsonObject)
    {
        ART_IMAGE = jsonObject.string(BN_IMAGE)
        entity = jsonObject.string(BN_ENTITYTYPE)

       //  var bm : Bitmap?  = convertToBitmap(this!!.ART_IMAGE!!)

       //StaticRefs.banners!!.add()
    }

 fun convertToBitmap(encodedImage : String) : Bitmap
 {
     val decodedString = Base64.decode(encodedImage, Base64.DEFAULT)
     val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
     return decodedByte
 }
}