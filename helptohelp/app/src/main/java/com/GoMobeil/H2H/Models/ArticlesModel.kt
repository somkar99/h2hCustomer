package com.GoMobeil.H2H.Models

import com.beust.klaxon.JsonObject
import com.beust.klaxon.int
import com.beust.klaxon.long
import com.beust.klaxon.string

/**
 * Created by ADMIN on 09-02-2018.
 */
class  ArticlesModel
{
    companion object {
        val ART_ID = "art_id";
        val ART_SERVID = "art_servid";
        val ART_TITLE = "art_title"
        val ART_DESCRIPTION = "art_description"
        val ART_ISACTIVE = "art_isactive"
        val ART_CREATEDBY = "art_createdby"
        val ART_UPDATEDBY = "art_updatedby"
        val ART_CREATEDAT = "created_at"
        val ART_UPDATEDAT = "art_updatedat"

        val IMG_NAME = "img_name"


    }

    var art_id: String? = null;
    var art_servid: String? = null;
    var art_title: String? = null;
    var art_description: String? = null;
    var art_isactive: String? = null;
    var art_img : String? = null;


    constructor(jsonObject: JsonObject) {
        art_id = jsonObject.string(ART_ID)
        art_servid = jsonObject.string(ART_SERVID)
        art_title = jsonObject.string(ART_TITLE)
        art_description = jsonObject.string(ART_DESCRIPTION)
        art_isactive = jsonObject.string(ART_ISACTIVE)
        art_img = jsonObject.string(IMG_NAME)

    }
}