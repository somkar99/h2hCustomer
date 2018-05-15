package com.GoMobeil.H2H.Adapters

import android.content.Context
import android.renderscript.ScriptGroup
import android.support.v7.widget.RecyclerView
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.GoMobeil.H2H.Models.SAModel
import com.GoMobeil.H2H.R
import com.GoMobeil.H2H.Services.CustomServices
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.GoMobeil.H2H.R.id.etAnswer
import com.GoMobeil.H2H.Services.CalendarView
import com.GoMobeil.H2H.UI.ServiceQuestions
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by niranjanshah on 29/01/18.
 */
class AnsAdapter(dataSet: List<SAModel>, activity: ServiceQuestions) : RecyclerView.Adapter<AnsAdapter.ViewHolder>() {
    val dataSet: List<SAModel> = dataSet
    val activity: ServiceQuestions = activity
    var lsChangedDate: String? = ""

    lateinit var context: Context
    lateinit var lsAnswer: String
    lateinit var mItemClickListener :onItemClickListener

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivCB = view.findViewById<ImageView>(R.id.ivCB)
        val ivRB = view.findViewById<ImageView>(R.id.ivRB)
        val tvAnswer = view.findViewById<TextView>(R.id.tvAnswer)
        val etAnswer = view.findViewById<EditText>(R.id.etAnswer)
        val tvCalDate = view.findViewById<TextView>(R.id.tvCalDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.ans_row, parent, false)
        context = parent.context;
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.let { holder ->
            holder.tvAnswer.text = dataSet[position].sqa_answer;
            setInvisible(holder)
            val lsInputType = dataSet[position].sqa_inputtype;
            var lbSelected = false;
            if (lsInputType.equals("RB")) {
                holder.ivRB.visibility = View.VISIBLE
                if (dataSet[position].getSelected().equals("Y")) {
                    if (dataSet[position].sqa_isother.equals("Y")) {
                        holder.etAnswer.visibility = View.VISIBLE

                        holder.etAnswer.setOnEditorActionListener(object : TextView.OnEditorActionListener {
                            override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {
                                if (event != null && event!!.getKeyCode() === KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                                    println("Parameter Value " + v.text.toString())
                                    //Log.d("Paramater value",v.text.toString())
                                    dataSet.get(position).setValue(v.text.toString())
                                    //  dataSet.notifyItemChanged(position)
                                }
                                return false
                            }
                        })

                    } else {
                        holder.etAnswer.visibility = View.GONE

                    }
                    holder.ivRB.setImageDrawable(context.resources.getDrawable(R.drawable.radiochecked))

                    dataSet.get(position).setValue(dataSet.get(position).sqa_answer!!)

                } else {

                    holder.etAnswer.visibility = View.GONE
                    holder.ivRB.setImageDrawable(context.resources.getDrawable(R.drawable.radiounchecked))

                }

            }


            if (lsInputType.equals("RB_N")) {
                holder.ivRB.visibility = View.VISIBLE
                if (lsInputType.equals("RB_N")) {
                    holder.etAnswer.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED
                }
                if (dataSet[position].getSelected().equals("Y")) {
                    if (dataSet[position].sqa_isother.equals("Y")) {
                        holder.etAnswer.visibility = View.GONE

                        holder.etAnswer.setOnEditorActionListener(object : TextView.OnEditorActionListener {
                            override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {
                                if (event != null && event!!.getKeyCode() === KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                                    println("Parameter Value " + v.text.toString())
                                    //dataSet.get(position).setValue(v.text.toString())
                                    dataSet.get(position).setValue(holder.etAnswer.text.toString())
                                    //  dataSet.notifyItemChanged(position)
                                }
                                return false
                            }
                        })

                    } else {
                        holder.etAnswer.visibility = View.GONE

                    }
                    holder.etAnswer.visibility = View.VISIBLE
                    holder.ivRB.setImageDrawable(context.resources.getDrawable(R.drawable.radiochecked))

                    dataSet.get(position).setValue(dataSet.get(position).sqa_answer!!)

                } else {

                    holder.etAnswer.visibility = View.GONE
                    holder.ivRB.setImageDrawable(context.resources.getDrawable(R.drawable.radiounchecked))

                }

            }




            else if (lsInputType.equals("RB_CL")) {
                holder.ivRB.visibility = View.VISIBLE
                if (dataSet[position].getSelected().equals("Y")) {
                    if (dataSet[position].sqa_isother.equals("Y")) {
                        holder.etAnswer.visibility = View.VISIBLE
                        CustomServices.setDate(holder.etAnswer, context)

                        holder.etAnswer.setOnEditorActionListener(object : TextView.OnEditorActionListener {
                            override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {
                                if (event != null && event!!.getKeyCode() === KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                                    println("Parameter Value " + v.text.toString())
                                    dataSet.get(position).setValue(v.text.toString())
                                    //  dataSet.notifyItemChanged(position)
                                }
                                return false
                            }
                        })

                    } else {

                        holder.etAnswer.visibility = View.GONE
                    }
                    holder.tvCalDate.visibility = View.VISIBLE
                    setupDate(holder.tvCalDate, position)!!
                    holder.ivRB.setImageDrawable(context.resources.getDrawable(R.drawable.radiochecked))

                } else {

                    holder.etAnswer.visibility = View.GONE
                    holder.ivRB.setImageDrawable(context.resources.getDrawable(R.drawable.radiounchecked))
                   // Toast.makeText(context, "", Toast.LENGTH_SHORT).show()
                }

            } else if (lsInputType.equals("CB")) {
                holder.ivCB.visibility = View.VISIBLE
                if (lsInputType.equals("CB")) {
                    holder.ivCB.visibility = View.VISIBLE
                    if (dataSet[position].getSelected().equals("Y")) {
                        if (dataSet[position].sqa_isother.equals("Y")) {
                            holder.etAnswer.visibility = View.VISIBLE
                            holder.etAnswer.setOnEditorActionListener(object : TextView.OnEditorActionListener {
                                override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {
                                    if (event != null && event!!.getKeyCode() === KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                                        println("Parameter Value " + v.text.toString())
                                        dataSet.get(position).setValue(v.text.toString())
                                        //  dataSet.notifyItemChanged(position)
                                    }
                                    return false
                                }
                            })

                        } else {
                            holder.etAnswer.visibility = View.GONE
                        }
                        holder.ivCB.setImageDrawable(context.resources.getDrawable(R.drawable.ic_checkedbox))

                        dataSet.get(position).setValue(dataSet.get(position).sqa_answer!!)

                    } else {
                        holder.etAnswer.visibility = View.GONE
                        holder.ivCB.setImageDrawable(context.resources.getDrawable(R.drawable.ic_uncheckedbox))
                    }
                }
            }

            else if (lsInputType.equals("CB_A")) {
                holder.ivCB.visibility = View.VISIBLE
                if (lsInputType.equals("CB_N")) {
                    holder.etAnswer.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED
                }
                if (lsInputType.equals("CB_A")) {
                    holder.ivCB.visibility = View.VISIBLE
                    if (dataSet[position].getSelected().equals("Y")) {
                        if (dataSet[position].sqa_isother.equals("Y")) {
                            holder.etAnswer.visibility = View.VISIBLE
                            holder.etAnswer.setOnEditorActionListener(object : TextView.OnEditorActionListener {
                                override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {
                                    if (event != null && event!!.getKeyCode() === KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                                        println("Parameter Value " + v.text.toString())
                                        //dataSet.get(position).setValue(v.text.toString())
                                        dataSet.get(position).setValue(dataSet.get(position).sqa_answer!!)
                                        //  dataSet.notifyItemChanged(position)
                                    }
                                    return false
                                }
                            })


                        } else {
                            holder.etAnswer.visibility = View.GONE
                        }
                        holder.ivCB.setImageDrawable(context.resources.getDrawable(R.drawable.ic_checkedbox))

                        holder.etAnswer.visibility = View.VISIBLE
                        dataSet.get(position).setValue(dataSet.get(position).sqa_answer!!)

                    } else {
                        holder.etAnswer.visibility = View.GONE
                        holder.ivCB.setImageDrawable(context.resources.getDrawable(R.drawable.ic_uncheckedbox))
                    }
                }
            }



            else if (lsInputType != null && lsInputType.length > 1) {
                if (lsInputType.substring(0, 2).equals("ET")) {
                    holder.etAnswer.visibility = View.VISIBLE

                    if (lsInputType.equals("ET_N")) {
                        holder.etAnswer.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED
                    }

                    holder.etAnswer.setOnEditorActionListener(object : TextView.OnEditorActionListener {
                        override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {
                            if (event != null && event!!.getKeyCode() === KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                                println("Parameter Value " + v.text.toString())
                                dataSet.get(position).setValue(v.text.toString())
                                //  dataSet.notifyItemChanged(position)
                            }
                            return false
                        }
                    })
                }
            }



        }

    }

    fun setupDate(tvCalDate: TextView, pos: Int) {
        val calendarPop = com.GoMobeil.H2H.Services.CalendarView(activity, context, true)
        var lsDate: String? = tvCalDate.getText().toString()
        if (lsDate == null || lsDate == "") {
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            lsDate = sdf.format(Date()).toString()
        }

        val mOnDateChanged = object : CalendarView.CVOnDateChanged {
            override fun CVDateChanged(tvDate: TextView, lsDate: String) {
                tvCalDate.setText(lsDate)
                //  lsChangedDate = tvCalDate.getText().toString()
                dataSet.get(pos).setValue(tvCalDate.getText().toString())
            }
        }
        calendarPop.setOnDateChanged(mOnDateChanged)
        calendarPop.setupPopUp(tvCalDate, lsDate, context.resources.getColor(R.color.colorPrimary))
    }

    override fun getItemCount(): Int {
        return this.dataSet.size
    }

    fun setInvisible(holder: ViewHolder) {
        holder.ivCB.visibility = View.GONE
        holder.etAnswer.visibility = View.GONE
        holder.ivRB.visibility = View.GONE
    }

    interface onItemClickListener {
        fun onItemClick(positon: Int,view: View)

    }

    fun setOnCardClickListener(mListener: AnsAdapter.onItemClickListener) {
        mItemClickListener = mListener
    }


}

