package com.GoMobeil.H2H.Models

import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.int
import com.beust.klaxon.string

/**
 * Created by ADMIN on 31-01-2018.
 */
class ServicesModel {
    companion object {
        val SRV_ID = "srv_id";
        val SRV_NAME = "srv_name";
        val SRV_DESCRIPTION = "srv_description"
        val SRV_IMAGE = "srv_image"
        val SQ_SRVID = "sq_srvid"

    }

    var srv_id: Int? = null;
    var srv_name: String? = null;
    var srv_description: String? = null;
    var srv_image: String? = null;
    var sq_srvid: String? = null

    constructor(jsonObject: JsonObject) {
        srv_id = jsonObject.int(SRV_ID)
        srv_name = jsonObject.string(SRV_NAME)
        srv_description = jsonObject.string(SRV_DESCRIPTION)
        srv_image = jsonObject.string(SRV_IMAGE)
        sq_srvid = jsonObject.string(SQ_SRVID)
    }
}

class SQModel {

    companion object {

        // #table services_questions

        val SQ_SRVID = "sq_srvid"
        val SQ_LANGID = "sq_langid"
        val SQ_SORTINGORDER = "sq_sortingorder"
        val SQ_QUESTIONID = "sq_questionid"
        val SQ_QUESTION = "sq_question"
        val SQ_ISACTIVE = "sq_isactive"
        val SQ_CREATEDBY = "sq_createdby"
        val SQ_UPDATEDBY = "sq_updatedby"
        val SQ_CREATED_AT = "created_at"
        val SQ_UPDATED_AT = "updated_at"
        val SQ_ANSWERS = "sq_answerts";
    }

    var sq_questionid : String? = null
    var sq_question : String? = null
    var sq_srvid : String? = null
    var sq_answers : ArrayList<SAModel>

    constructor(jsonObject: JsonObject) {

        sq_questionid = jsonObject.int(SQ_QUESTIONID).toString()
        sq_question = jsonObject.string(SQ_QUESTION)
        sq_srvid = jsonObject.string(SQ_SRVID)
        sq_answers = ArrayList();
        println("question of the service :  $sq_question")

        val answers = jsonObject.get("answers")
        val jsonAnswers = answers as JsonArray<SAModel>

        for (i in 0..(jsonAnswers.size - 1)) {
            val jsonObject = jsonAnswers.get(i) as JsonObject
            val answer = SAModel(jsonObject);
            println("Option :  " + jsonObject.toString())
            sq_answers.add(answer)
        }

    }

}

class SAModel {
    // #table service_question_answer
    companion object {

        val SQA_QUESTIONID = "sqa_questionid"
        val SQA_LANGID = "sqa_langid"
        val SQA_SRNO = "sqa_srno"
        val SQA_ANSWER = "sqa_answer"
        val SQA_INPUTTYPE = "sqa_inputType"
        val SQA_ISOTHER = "sqa_isOther"
        val SQA_CREATEDBY = "sqa_createdBy"
        val SQA_CREATEDDATETIME = "sqa_createdDateTime"
        val SQA_UPDATEDBY = "sqa_updatedBy"
        val SQA_UPDATEDDATETIME = "sqa_updatedDateTime"
        val SQA_ISACTIVE = "sqa_isActive"
        val SQA_ISDEFAULT = "sqa_isdefault"

        val SQA_SELECTED = "sqa_selected"
        val SQA_TEXTANSWER = "sqa_texanswer"
    }

    var sqa_answer : String? = null
    var sqa_inputtype : String? = null
    var sqa_isother : String? = "N"
    var sqa_selected : String = "N"
    var sqa_val : String? = null
    var sqa_questionid : String? = null
    var sqa_textanswer = ""
    constructor(jsonObject: JsonObject) {
        println("Answer in SAModel $jsonObject.get(SQA_ANSWER")
        sqa_answer =  jsonObject.string(SQA_ANSWER)
        sqa_inputtype = jsonObject.string(SQA_INPUTTYPE)
        sqa_isother = jsonObject.string(SQA_ISOTHER)
        sqa_questionid = jsonObject.int(SQA_QUESTIONID).toString()
    }

    fun setSelected(lsValue: String)
    {
        sqa_selected = lsValue
    }

    fun getSelected() : String = sqa_selected

    fun setAnswerText(lsValue: String)
    {
        sqa_textanswer = lsValue
    }

    fun getAnswerText() : String =  sqa_textanswer;

    fun setValue(lsValue: String)
    {
        sqa_val = lsValue
    }

    fun getVal() : String? = sqa_val
}

