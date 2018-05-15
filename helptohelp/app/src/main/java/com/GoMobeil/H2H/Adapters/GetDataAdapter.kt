package com.GoMobeil.H2H.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.GoMobeil.H2H.R
import kotlinx.android.synthetic.main.getdata_row.view.*

/**
 * Created by Admin on 15-03-18.
 */
 class GetDataAdapter(val items:ArrayList<String>,val context:Context) : RecyclerView.Adapter<GetDataAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.getdata_row,parent,false))

    }

    override fun getItemCount(): Int {

        return items.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var lsQuestion = items.get(position)

        var lsArr = lsQuestion.split("?")
        var lsStr1 = lsArr[0] + "?"
        var lsStr2 = lsArr[1]

        holder?.tvQuestions?.text = Html.fromHtml(lsStr1 +"<br><b>"+lsStr2+"</b>")

    }


    class ViewHolder(view:View) : RecyclerView.ViewHolder(view){

        val tvQuestions = view.tvQuestions


    }
}