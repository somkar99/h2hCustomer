package com.GoMobeil.H2H.Adapters

import android.content.Context
import android.database.Cursor
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.GoMobeil.H2H.Models.Message_Model
import com.GoMobeil.H2H.R


import java.text.SimpleDateFormat

import java.util.ArrayList


class Message_Adapter(dataSet: List<Message_Model>) : RecyclerView.Adapter<Message_Adapter.ViewHolder>() {
    val dataSet: List<Message_Model> = dataSet
    lateinit var context: Context
    val MSG_RECEIVED:Int?= 0
    val MSG_SEND:Int?= 1

    companion object {
        lateinit var adapter: Message_Adapter
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val tvCMessage = view.findViewById<TextView>(R.id.tvCMessage)
        val tvCDateTime = view.findViewById<TextView>(R.id.tvCDateTime)
        val tvVMessage = view.findViewById<TextView>(R.id.tvVMessage)
        val tvVDateTime = view.findViewById<TextView>(R.id.tvVDateTime)
    }


    override fun getItemViewType(position: Int): Int {
        return if (dataSet[position].tc_sender.equals("V")) {
            MSG_RECEIVED!!
        } else
            MSG_SEND!!
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType == MSG_RECEIVED) {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.message_in, parent, false)
            return ViewHolder(itemView)
        } else {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.message_out, parent, false)
            return ViewHolder(itemView)

        }

    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        val itemType = getItemViewType(position)

        if (itemType == MSG_SEND) {
            viewHolder!!.tvVMessage.setText(dataSet[position].tc_message)
            var lsDatetime = dataSet[position].created_at
            viewHolder.tvVDateTime.setText(getDate(lsDatetime!!))
        } else {
            viewHolder!!.tvCMessage.setText(dataSet[position].tc_message)
            var lsDatetime = dataSet[position].created_at
            viewHolder.tvCDateTime.setText(getDate(lsDatetime!!))
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    private fun getDate(timeStampStr: String):String {

        val str = timeStampStr
        val fmt = "yyyy-MM-dd HH:mm:ss"
        val df = SimpleDateFormat(fmt)

        val dt = df.parse(str)

        val tdf = SimpleDateFormat("HH:mm:ss")
        val dfmt = SimpleDateFormat("HH:mm dd/MM/yyyy")

        val timeOnly = tdf.format(dt)
        val dateOnly = dfmt.format(dt)

        return dateOnly
    }

}




