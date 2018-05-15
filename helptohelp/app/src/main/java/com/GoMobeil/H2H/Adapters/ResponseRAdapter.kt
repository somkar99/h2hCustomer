package com.GoMobeil.H2H.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.GoMobeil.H2H.Extensions.loadBase64Image
import com.GoMobeil.H2H.Models.MyRequestModel
import com.GoMobeil.H2H.R
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by Admin on 03-05-18.
 */
class ResponseRAdapter(dataSet:List<MyRequestModel>, context: Context):SelectableAdapter<ResponseRAdapter.resViewHolder>() {

    var dataSet : List<MyRequestModel> = dataSet
    var context :Context = context

    //final var onClick : (View) ->Unit {}

    companion object {
        lateinit var responseAdater :ResponseRAdapter
        lateinit var mItemClickListener :onItemClickListener
        lateinit var mAddChangeListener : onAddChangeListener
    }

    override fun getItemCount(): Int {

        return this.dataSet.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): resViewHolder {

        var view = LayoutInflater.from(parent!!.context).inflate(R.layout.resp_rec_row,parent,false)
        responseAdater = this
        context = parent.context
        return resViewHolder(view)

    }

    override fun onBindViewHolder(holder: resViewHolder, position: Int) {

        holder?.let {
            holder ->
            var imageString = dataSet.get(position).lssrv_image
            if (!imageString.equals("") && !imageString.equals(null)){
                try {
                    holder!!.ivServiceImage.loadBase64Image(imageString)
                }catch (e:Exception){

                }
            }

            holder!!.tvServiceName.text = dataSet.get(position).lsServiceName

            var d= getDate(dataSet.get(position).lsCreatedAt!!)
            holder.tvBookingDate.text = d

          //  NestedRAdapter adapter = new NestedRAdapter(context)
        }



    }


    class resViewHolder(view:View):RecyclerView.ViewHolder(view),View.OnClickListener{


        internal var tvServiceName :TextView
        internal var tvBookingDate :TextView
        internal var ivServiceImage :ImageView
        internal var tvCancelRequest :TextView
        internal var rcvVendorList : RecyclerView
        //internal var adapter :NestedRAdapter

        init {
            tvServiceName = view.findViewById(R.id.tvRServiceName)
            tvBookingDate = view.findViewById(R.id.tvRBookDate)
            tvCancelRequest = view.findViewById(R.id.tvRCancelRequest)
            ivServiceImage = view.findViewById(R.id.ivRServImg)
            rcvVendorList = view.findViewById(R.id.rcvVendorList)
        }



        override fun onClick(v: View?) {
            when(v!!.id){

                R.id.tvRCancelRequest -> {

                }
            }
        }

    }

    interface onItemClickListener{
        fun onItemClick(pos: Int,view: View)
    }
    fun setOnCardClickListener(mListener :ResponseRAdapter.onItemClickListener){
        ResponseRAdapter.mItemClickListener = mListener
    }


    interface onAddChangeListener{
        fun onItemChangeAdd(position: Int,lsVal :String)
    }

    fun setOnAddChangeListener(mListener: onAddChangeListener){
        ResponseRAdapter.mAddChangeListener = mListener
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
        val dtonly = SimpleDateFormat("dd/MM/yyyy")

        val timeOnly = tdf.format(dt)
        val date_only = dtonly.format(dt)
        val dateOnly = dfmt.format(dt)
        val cur4 = dfmt.format(cur2)

        val Diff = getDateDiff(dfmt, dateOnly, cur4)

        if (Diff.toInt() == 0) {
            return "Today, "+ timeOnly
        } else if (Diff.toInt() > 0 && Diff.toInt() < 10) {
            return Diff.toString() +" Day Ago"
        } else {
            return date_only
        }

    }

    fun getDateDiff(format: SimpleDateFormat, oldDate: String, newDate: String): kotlin.Long {
        try {
            return TimeUnit.DAYS.convert(format.parse(newDate).time - format.parse(oldDate).time, TimeUnit.MILLISECONDS)
        } catch (e: Exception) {
            e.printStackTrace()
            return 0
        }
    }


}

