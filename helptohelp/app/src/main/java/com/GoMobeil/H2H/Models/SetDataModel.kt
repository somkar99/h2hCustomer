package com.GoMobeil.H2H.Models

import com.beust.klaxon.JsonObject
import com.beust.klaxon.string

/**
 * Created by Admin on 03-04-18.
 */
class SetDataModel {

    companion object {
        val SRD_QID = "srd_qid"
        val SQ_QUESTIONS = "sq_question"
        val SRD_ANSWER = "srd_answer"
    }

    var srd_qid :String?= null
    var sq_questions :String?= null
    var srd_answer = "srd_answer"

    constructor(jsonObject: JsonObject){

        srd_qid = jsonObject.string(SRD_QID)
        sq_questions = jsonObject.string(SQ_QUESTIONS)
        srd_answer = jsonObject.string(SRD_ANSWER)!!
    }
}