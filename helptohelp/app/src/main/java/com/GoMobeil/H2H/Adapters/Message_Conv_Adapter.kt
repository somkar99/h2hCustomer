package com.GoMobeil.H2H.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.GoMobeil.H2H.Extensions.loadBase64Image
import com.GoMobeil.H2H.Models.Message_Conv_Model
import com.GoMobeil.H2H.R

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by apple on 16/04/18.
 */
class Message_Conv_Adapter(dataSet: List<Message_Conv_Model>) : RecyclerView.Adapter<Message_Conv_Adapter.ViewHolder>() {
    val dataSet: List<Message_Conv_Model> = dataSet
    lateinit var context: Context

    companion object {
        lateinit var adapter: Message_Conv_Adapter
        lateinit var mItemClickListener: onItemClickListener

    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view),View.OnClickListener {

        val tvDisplayName = view.findViewById<TextView>(R.id.tvDisplayName)
        val tvDisplayTime = view.findViewById<TextView>(R.id.tvDisplayTime)
        val tvDisplayMessage = view.findViewById<TextView>(R.id.tvDisplayMessage)
        val ivDisplayPicture = view.findViewById<ImageView>(R.id.iv_DisplayPicture)

        val llrow = view.findViewById<LinearLayout>(R.id.llRow)

        init {

            llrow.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when (v!!.id) {
                R.id.llRow -> {
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClick(adapterPosition)
                    }
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.message_row, parent, false)
            return ViewHolder(itemView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

            viewHolder!!.tvDisplayMessage.setText(dataSet[position].tc_message)


            val imageString = dataSet[position].sp_image

            if (imageString!!.length>0){
                try {
                    viewHolder.ivDisplayPicture.loadBase64Image(imageString)
                }catch (e:Exception){

                }
            }

            val Name =dataSet[position].sp_firstname +" "+ dataSet[position].sp_lastname
            viewHolder!!.tvDisplayName.setText(Name)

            var lsDatetime = dataSet[position].created_at
            viewHolder.tvDisplayTime.setText(getDate(lsDatetime!!))
      }

    override fun getItemCount(): Int {
        return dataSet.size
    }


    private fun getDate(timeStampStr: String): String {

        val str = timeStampStr
        val fmt = "yyyy-MM-dd HH:mm:ss"
        val df = SimpleDateFormat(fmt)

        val date = Date()
        val date1 = df.format(date)

        val dt = df.parse(str)
        val cur2 = df.parse(date1)

        val tdf = SimpleDateFormat("HH:mm")
        val dfmt = SimpleDateFormat("HH:mm dd/MM/yyyy")

        val timeOnly = tdf.format(dt)
        val dateOnly = dfmt.format(dt)
        val cur4 = dfmt.format(cur2)

        val Diff = getDateDiff(dfmt, dateOnly, cur4)

        if (Diff.toInt() == 0) {
            return timeOnly
        } else if (Diff.toInt() > 0 && Diff.toInt() < 7) {
            return Diff.toString() +" Day Ago"
        } else {
            return dateOnly
        }

    }

    fun getDateDiff(format: SimpleDateFormat, oldDate: String, newDate: String): Long {
        try {
            return TimeUnit.DAYS.convert(format.parse(newDate).time - format.parse(oldDate).time, TimeUnit.MILLISECONDS)
        } catch (e: Exception) {
            e.printStackTrace()
            return 0
        }
    }




    fun setOnCardClickListener(mListener: onItemClickListener) {
        mItemClickListener = mListener
    }


    interface onItemClickListener {
        fun onItemClick(position: Int)

    }

}




