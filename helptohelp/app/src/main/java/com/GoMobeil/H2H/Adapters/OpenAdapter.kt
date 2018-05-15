package com.GoMobeil.H2H.Adapters

import android.content.Context
import android.media.Image
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.GoMobeil.H2H.Extensions.loadBase64Image
import com.GoMobeil.H2H.Models.MyRequestModel
import com.GoMobeil.H2H.R
import com.github.siyamed.shapeimageview.CircularImageView
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by Admin on 03-05-18.
 */
class OpenAdapter(dataSet:List<MyRequestModel>,context: Context,sType :Int) : SelectableAdapter<OpenAdapter.openViewHolder>() {

   var dataSet :List<MyRequestModel> = dataSet
    var context :Context? = context
    var sType :Int = sType

    var view :View? = null


    final var onClick : (View) ->Unit = {}


    companion object {
        lateinit var openadapter : OpenAdapter
        lateinit var mItemClickListener :onItemClickListener
        lateinit var mAddChangeListener : onAddChangeListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): openViewHolder {

        if (sType==0){
            view  = LayoutInflater.from(parent!!.context).inflate(R.layout.open_row,parent,false)
            openadapter = this
            context = parent!!.context
            return openViewHolder(view!!)

        }
        else if (sType ==1){
            view = LayoutInflater.from(parent!!.context).inflate(R.layout.resp_rec_row,parent,false)
            openadapter = this
            context = parent!!.context
            return openViewHolder(view!!)

        }
        else if (sType ==2){
            view = LayoutInflater.from(parent!!.context).inflate(R.layout.hired_row,parent,false)
            openadapter = this
            context = parent!!.context
            return openViewHolder(view!!)


        }
        else if (sType ==3){
            view = LayoutInflater.from(parent!!.context).inflate(R.layout.ongoing_row,parent,false)
            openadapter = this
            context = parent!!.context

            return openViewHolder(view!!)
        }
        else{
            return openViewHolder(view!!)
        }
  }


    override fun onBindViewHolder(holder: openViewHolder, position: Int) {

        holder.let {

            holder->

            if (sType ==0){

                var imageString = dataSet.get(position).lssrv_image
                if (!imageString.equals("") && !imageString.equals(null)){
                    try {
                        holder!!.ivSericeImage.loadBase64Image(imageString)
                    }catch (e:Exception){

                    }
                }

                holder!!.tvServiceName.text = dataSet.get(position).lsServiceName

                var d= getDate(dataSet.get(position).lsCreatedAt!!)
                holder.tvBookDate.text = d

                holder!!.tvTxnid.text = dataSet.get(position).lsTxnid.toString()

            }

            if (sType ==1){

                var imageString = dataSet.get(position).lssrv_image
                if (!imageString.equals("") && !imageString.equals(null)){
                    try {
                        holder !!.ivRServImage.loadBase64Image(imageString)
                    }catch (e:Exception){

                    }
                }
                holder!!.tvRServiceName.text = dataSet.get(position).lsServiceName
                var d = getDate(dataSet.get(position).lsCreatedAt!!)
                holder.tvRBookingDate.text = d
            }

            if (sType == 2){

                var imageString = dataSet.get(position).lssrv_image
                if (!imageString.equals("") && !imageString.equals(null)){
                    try {
                        holder !!.ivHServImage.loadBase64Image(imageString)
                    }catch (e:Exception){

                    }
                }
                holder!!.tvHServiceName.text = dataSet.get(position).lsServiceName
                var d = getDate(dataSet.get(position).lsCreatedAt!!)
                holder.tvHBookingDate.text = d
            }

            if (sType == 3){

                var imageString = dataSet.get(position).lssrv_image
                if (!imageString.equals("") && !imageString.equals(null)){
                    try {
                        holder !!.ivOServImage.loadBase64Image(imageString)
                    }catch (e:Exception){

                    }
                }
                holder!!.tvOServiceName.text = dataSet.get(position).lsServiceName
                var d = getDate(dataSet.get(position).lsCreatedAt!!)
                holder.tvOBookingDate.text = d
            }



        }

    }


    override fun getItemCount(): Int {
        return this.dataSet.size
    }


    class openViewHolder (view: View):RecyclerView.ViewHolder(view){
        internal lateinit var tvServiceName :TextView
        internal lateinit var tvBookDate : TextView
        internal lateinit var ivSericeImage : CircularImageView
        internal lateinit var tvCancelRequest : TextView
        internal lateinit var tvTxnid : TextView
        internal lateinit var llOpen : LinearLayout



        internal lateinit var tvRServiceName : TextView
        internal lateinit var tvRBookingDate : TextView
        internal lateinit var ivRServImage : CircularImageView
        internal lateinit var tvRCancelRequest : TextView
        internal lateinit var llResponse : LinearLayout

        internal lateinit var tvHServiceName : TextView
        internal lateinit var tvHBookingDate : TextView
        internal lateinit var ivHServImage : CircularImageView
        internal lateinit var ivHCallicon : ImageView
        internal lateinit var ivHMessageicon : ImageView

        internal lateinit var tvOServiceName : TextView
        internal lateinit var tvOBookingDate : TextView
        internal lateinit var ivOServImage : CircularImageView
        internal lateinit var ivOCallicon : ImageView
        internal lateinit var ivOMessageicon : ImageView



        init {
            if (openadapter.sType ==0) {
                tvServiceName = view.findViewById(R.id.tvServiceName)
                tvBookDate = view.findViewById(R.id.tvBookDate)
                ivSericeImage = view.findViewById(R.id.ivServImg)
                tvCancelRequest = view.findViewById(R.id.tvCancelReqest)
                llOpen = view.findViewById(R.id.llopen)
                tvTxnid = view.findViewById(R.id.tvTxnId)

                llOpen.setOnClickListener {
                    if (mItemClickListener!=null){
                        mItemClickListener.onItemClick(adapterPosition,llOpen)
                    }

                }

                tvCancelRequest.setOnClickListener {

                    if (mItemClickListener!=null){
                        mItemClickListener.onItemClick(adapterPosition,tvCancelRequest)
                    }
                }


            }
            else if (openadapter.sType==1) {

                tvRServiceName = view.findViewById(R.id.tvRServiceName)
                tvRBookingDate = view.findViewById(R.id.tvRBookDate)
                ivRServImage = view.findViewById(R.id.ivRServImg)
                tvRCancelRequest = view.findViewById(R.id.tvRCancelRequest)
                llResponse = view.findViewById(R.id.llresponse)

                llResponse.setOnClickListener {

                    if (mItemClickListener!=null){
                        mItemClickListener.onItemClick(adapterPosition,llResponse)
                    }
                }

                tvRCancelRequest.setOnClickListener {

                    if (mItemClickListener!=null){
                        mItemClickListener.onItemClick(adapterPosition,tvRCancelRequest)
                    }
                }



            }
            else if (openadapter.sType ==2){

                tvHServiceName = view.findViewById(R.id.tvHServiceName)
                tvHBookingDate = view.findViewById(R.id.tvHBookDate)
                ivHServImage = view.findViewById(R.id.ivHServImg)
                ivHCallicon = view.findViewById(R.id.ivHCall)
                ivHMessageicon = view.findViewById(R.id.ivHMessage)



                ivHCallicon.setOnClickListener {
                    if (mItemClickListener!=null){
                        mItemClickListener.onItemClick(adapterPosition,ivHCallicon)
                    }

                }

                ivHMessageicon.setOnClickListener {
                    if (mItemClickListener!=null){
                        mItemClickListener.onItemClick(adapterPosition,ivHMessageicon)
                    }

                }


            }

            else if (openadapter.sType ==3){

                tvOServiceName = view.findViewById(R.id.tvOServiceName)
                tvOBookingDate = view.findViewById(R.id.tvOBookDate)
                ivOServImage = view.findViewById(R.id.ivOServImg)
                ivOCallicon = view.findViewById(R.id.ivOCall)
                ivOMessageicon = view.findViewById(R.id.ivOMessage)



                ivOCallicon.setOnClickListener {

                    if (mItemClickListener!=null){
                        mItemClickListener.onItemClick(adapterPosition,ivOCallicon)
                    }
                }

                ivOMessageicon.setOnClickListener {

                    if (mItemClickListener!=null){
                        mItemClickListener.onItemClick(adapterPosition,ivOMessageicon)
                    }
                }
            }

        }




    }



    interface onItemClickListener{
        fun onItemClick(pos: Int,view: View)
    }
    fun setOnCardClickListener(mListener :OpenAdapter.onItemClickListener){
        mItemClickListener = mListener
    }


    interface onAddChangeListener{
        fun onItemChangeAdd(position: Int,lsVal :String)
    }

    fun setOnAddChangeListener(mListener: onAddChangeListener){
        mAddChangeListener = mListener
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
    }}


