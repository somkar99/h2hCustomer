package com.GoMobeil.H2H.Models

import com.beust.klaxon.JsonObject
import com.beust.klaxon.string

/**
 * Created by Admin on 02-04-18.
 */
class WorkImgModel {

    companion object {
        val SPI_SPID = "spi_spid"
        val SPI_SRNO = "spi_srno"
        val SPI_IMGTYPE = "spi_imagetype"
        val SPI_IMGNAME = "spi_imagename"
        val SPI_IMAGE = "spi_image"
        val SPI_ISACTIVE = "spi_isactive"
    }

    var spi_spid :String? = null
    var spi_srno :String? = null
    var spi_imagetype :String? = null
    var spi_imagename :String? = null
    var spi_image :String? = null
    var spi_isactive :String? = null

    constructor(jsonObject: JsonObject){

        spi_spid = jsonObject.string(SPI_SPID)
        spi_srno = jsonObject.string(SPI_SRNO)
        spi_imagetype = jsonObject.string(SPI_IMGTYPE)
        spi_imagename = jsonObject.string(SPI_IMGNAME)
        spi_image = jsonObject.string(SPI_IMAGE)
        spi_isactive = jsonObject.string(SPI_ISACTIVE)
    }



}