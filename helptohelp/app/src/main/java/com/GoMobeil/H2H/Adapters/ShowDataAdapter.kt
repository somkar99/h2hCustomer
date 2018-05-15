package com.GoMobeil.H2H.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.GoMobeil.H2H.Models.SetDataModel
import com.GoMobeil.H2H.R

/**
 * Created by Admin on 03-04-18.
 */
class ShowDataAdapter (dataSet:List<SetDataModel>) : RecyclerView.Adapter<ShowDataAdapter.ViewHolder>() {

    val dataSet :List<SetDataModel> = dataSet
    lateinit var context : Context

    class ViewHolder(view:View):RecyclerView.ViewHolder(view){

        val sq = view.findViewById<TextView>(R.id.tvSQuestions)
        val sa = view.findViewById<TextView>(R.id.tvSAnswer)

    }





    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder : WorkImgAdapter.ViewHolder
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.set_data_row,parent,false)
        viewHolder = WorkImgAdapter.ViewHolder(view)

        context = parent.context
        return ShowDataAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder?.let { holder ->

            holder.sq.text = dataSet[position].sq_questions
            holder.sa.text = dataSet[position].srd_answer


        }
    }

    override fun getItemCount(): Int {
        return this.dataSet.size
    }



}