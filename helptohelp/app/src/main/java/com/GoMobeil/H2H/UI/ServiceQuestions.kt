package com.GoMobeil.H2H.UI

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.CalendarView
import android.widget.Toast
import com.GoMobeil.H2H.*
import com.GoMobeil.H2H.Adapters.AnsAdapter
import com.GoMobeil.H2H.Adapters.SCAdapter
import com.GoMobeil.H2H.Extensions.OnItemClickListener
import com.GoMobeil.H2H.Extensions.addOnItemClickListener
import com.GoMobeil.H2H.Extensions.toast
import com.GoMobeil.H2H.Models.SAModel
import com.GoMobeil.H2H.Models.SCModel
import com.GoMobeil.H2H.Models.SQModel
import com.GoMobeil.H2H.Models.ServicesModel
import com.GoMobeil.H2H.Services.CustomServices
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import kotlinx.android.synthetic.main.base_layout.*
import kotlinx.android.synthetic.main.service_qa.*
import kotlinx.android.synthetic.main.services_list.*
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import android.view.ViewGroup
import com.GoMobeil.H2H.Extensions.loadBase64Image
import com.sdsmdg.tastytoast.TastyToast
import kotlinx.android.synthetic.main.ans_row.*
import kotlinx.android.synthetic.main.service_details.*


/**
 * Created by ADMIN on 01-02-2018.
 */
class ServiceQuestions : BaseActivity()
{
    companion object {
        var lsDate : String? = null
    }
    override lateinit var context:Context
    override lateinit var activity:Activity
    var service_id:Int? = null
    var service_name : String? = null
    lateinit var serviceQuestions : List<SQModel>
    var liCurrentQuestion = 0;
    lateinit var adapter: AnsAdapter;
    lateinit var rcvAdapter : RecyclerView.Adapter<AnsAdapter.ViewHolder>
    lateinit var layoutManager : RecyclerView.LayoutManager
    var questionanswer=ArrayList<String>()

    var showAnswers=ArrayList<String>()
    var showQuestions=ArrayList<String>()
    var ansIdsArray = ArrayList<String>()
    var queIdsArray = ArrayList<String>()

    var lsInputType:String?=""

    var KEY:String ="2";

    var alAnswers = ArrayList<SAModel>();

    var lsanswerPosition:Int?=0
    var lsanswer : String? = null
    var lsquestion:String?=null
    var lianswerid:String?=null
    var liquestionid:String?=null
    lateinit var lsServImage :String

    var selAnswer = ArrayList<SAModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.service_qa)
        context = this
        activity = this

        initLayout()

    }




    fun initLayout() : Unit
    {   questionanswer= ArrayList()
        service_id = getIntent().getIntExtra(ServicesModel.SRV_ID, 0)
        service_name = getIntent().getStringExtra(ServicesModel.SRV_NAME)
        lsServImage = getIntent().getStringExtra(StaticRefs.SRV_IMAGE)

        if (!lsServImage.equals(null) && !lsServImage.equals("null") && !lsServImage.equals("")){

            ivqServImage.loadBase64Image(lsServImage)
        }
        else{
            TastyToast.makeText(context,"No Image Found",TastyToast.LENGTH_LONG,TastyToast.ERROR).show()
        }

        setTitle("$service_name")
        getQuestions(service_id!!)
    }

    fun getQuestions(service_id : Int)
    {
        Fuel.post(StaticRefs.URLServiceQuestions,listOf((StaticRefs.TOKEN to prefs.token),(ServicesModel.SQ_SRVID to service_id)))
                .responseJson()
                {
                    request,
                    response,
                    result ->

                    result.fold({ d ->
                        parseJson(result.get().content)

                    }, { err ->
                        TastyToast.makeText(context,"Internet Network Down",TastyToast.LENGTH_LONG,TastyToast.ERROR)
                    })


                }
    }

    fun parseJson(data : String)
    {
        val json = JSONObject(data)

        val response = json.getString(StaticRefs.DATA)
        val parser = Parser()
        val stringBuilder = StringBuilder(response)
        val model =  parser.parse(stringBuilder) as JsonArray<JsonObject>

        serviceQuestions = model.map{ SQModel(it) }.filterNotNull()
        if (serviceQuestions.size == 0)
        {
           // showAppointment()
            return
        }
        showQuestion();

        /* if(liCurrentQuestion == 0)
         {
             cbNext.setLayoutParams(ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, R.attr.height))
         }*/

        cbNext.setOnClickListener {
            if(alAnswers[lsanswerPosition!!].getVal() != null && alAnswers[lsanswerPosition!!].getVal() != "")
            {
                if (liCurrentQuestion <= (serviceQuestions.size - 1)) {

                    setArrayData()

                    liCurrentQuestion = liCurrentQuestion + 1
                }
                showQuestion();

                if (liCurrentQuestion == serviceQuestions.size-1){
                    cbNext.setOnClickListener {

                        if(alAnswers[lsanswerPosition!!].getVal() != null && alAnswers[lsanswerPosition!!].getVal() != "") {

                            if (liCurrentQuestion<liCurrentQuestion-1){
                                liCurrentQuestion = liCurrentQuestion+1
                            }
                            else{}
                            setArrayData()



                            showAppointment()
                        }
                        else {
                           // toast("Answer required")
                            TastyToast.makeText(context,"Answer required",TastyToast.LENGTH_LONG,TastyToast.ERROR).show()
                        }
                    }
                }
            }
            else
            {
                TastyToast.makeText(context,"Answer required!!",TastyToast.LENGTH_LONG,TastyToast.ERROR).show()
            }
        }

        cbPrevious.setOnClickListener {

            /*  if(liCurrentQuestion == 0)
              {
                  cbNext.setLayoutParams(ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, R.attr.height))
              }*/

            if ((liCurrentQuestion > 0))
            {
                liCurrentQuestion = liCurrentQuestion - 1;

                cbNext.setOnClickListener {
                  if (liCurrentQuestion<serviceQuestions.size - 1){

                     // setArrayData()
                      liCurrentQuestion = liCurrentQuestion+1
                  }
                    else{

                      showAppointment()
                  }
                    showQuestion()
                }



            }

            else
            {
//                toast("You are on first question")
                TastyToast.makeText(context,"You are on first questions",TastyToast.LENGTH_LONG,TastyToast.INFO).show()
                cbNext.setOnClickListener {
                    if (liCurrentQuestion<serviceQuestions.size - 1){
                        liCurrentQuestion = liCurrentQuestion+1
                    }
                    else{
                        showAppointment()
                    }
                    showQuestion()
                }
            }
            showQuestion()


        }
    }

    fun setArrayData()
    {

        lsanswer = alAnswers[lsanswerPosition!!].getVal()
            // toast("Value : $lsanswer")

        lsanswerPosition = 0

        /* val json=JSONObject()
         json.put(lsquestion,lsanswer)
         questionanswer.add(json.toString())*/
        showQuestions.add(lsquestion!!)
        showAnswers.add(lsanswer!!)
        ansIdsArray.add(lianswerid!!)
        queIdsArray.add(liquestionid!!)

    }

    override fun startActivityForResult(intent: Intent?, requestCode: Int) {
        super.startActivityForResult(intent, requestCode)

    }
    fun showQuestion()
    {
        if (serviceQuestions.size > liCurrentQuestion) {
            tvQuestion.setText(serviceQuestions[liCurrentQuestion].sq_question)
            lsquestion=serviceQuestions[liCurrentQuestion].sq_question
            liquestionid=serviceQuestions[liCurrentQuestion].sq_questionid
            showAnswer()
    }
    }

    fun showAppointment()
    {
        val intent = Intent(context,Appointment::class.java)
        intent.putExtra(ServicesModel.SRV_ID, service_id!!)
        intent.putExtra(ServicesModel.SRV_NAME, service_name)
        intent.putExtra("Questions",showQuestions)
        intent.putExtra("Answers", showAnswers)
        intent.putExtra("AnsIds", ansIdsArray)
        intent.putExtra("QueIds", queIdsArray)

        intent.putExtra(ServicesModel.SRV_NAME, service_name)
        startActivity(intent)
    }

    fun showAnswer()
    {
        alAnswers = serviceQuestions[liCurrentQuestion].sq_answers
        adapter = AnsAdapter(alAnswers, this)
        rcvAdapter = adapter!!
        layoutManager = LinearLayoutManager(this)
        rcvList.adapter = rcvAdapter
        rcvList.layoutManager = layoutManager
        rcvAdapter.notifyDataSetChanged()

            rcvList.addOnItemClickListener(object : OnItemClickListener
        {
            override fun onItemClicked(view: View, position: Int) {
                lsInputType = alAnswers[position].sqa_inputtype;
                // lsanswer=alAnswers[position].sqa_etval
                lsanswerPosition = position
                lianswerid=alAnswers[position].sqa_questionid
                var lbSelected = false

                if (lsInputType.equals("RB")) {

                    for (i in 0..alAnswers.size - 1) {
                        if (i != position) {
                            alAnswers.get(i).setSelected("N")
                            adapter.notifyItemChanged(i)
                        }

                    }
                    if (alAnswers.get(position).getSelected().equals("Y")) {
                        alAnswers.get(position).setSelected("N")
                    } else {
                        alAnswers.get(position).setSelected("Y")
                    }
                    adapter.notifyItemChanged(position)
                }


                if (lsInputType.equals("RB_N")) {

                    for (i in 0..alAnswers.size - 1) {
                        if (i != position) {
                            alAnswers.get(i).setSelected("N")
                            adapter.notifyItemChanged(i)
                        }

                    }
                    if (alAnswers.get(position).getSelected().equals("Y")) {
                        alAnswers.get(position).setSelected("N")
                    } else {
                        alAnswers.get(position).setSelected("Y")
                    }
                    adapter.notifyItemChanged(position)
                }

                if (lsInputType.equals("CB")){
                    if (alAnswers.get(position).getSelected().equals("Y")) {
                        alAnswers.get(position).setSelected("N")
                    } else {
                        alAnswers.get(position).setSelected("Y")
                    }
                    adapter.notifyItemChanged(position)
                }

                if (lsInputType.equals("CB_A")){
                    if (alAnswers.get(position).getSelected().equals("Y")) {
                        alAnswers.get(position).setSelected("N")
                    } else {
                        alAnswers.get(position).setSelected("Y")
                    }
                    adapter.notifyItemChanged(position)
                }

                if (lsInputType.equals("RB_CL")){
                    for (i in 0..alAnswers.size - 1) {
                        if (i != position) {
                            alAnswers.get(i).setSelected("N");
                            adapter.notifyItemChanged(i)
                        }
                    }
                    if (alAnswers.get(position).getSelected().equals("Y")) {
                        alAnswers.get(position).setSelected("N")
                    } else {
                        alAnswers.get(position).setSelected("Y")
                    }
                    adapter.notifyItemChanged(position)
                }
            }
        })
    }
}