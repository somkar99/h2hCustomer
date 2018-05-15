package com.GoMobeil.H2H.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.GoMobeil.H2H.Extensions.loadBase64Image
import com.GoMobeil.H2H.Extensions.toast
import com.GoMobeil.H2H.Models.MyRequestModel
import com.GoMobeil.H2H.R
import com.GoMobeil.H2H.ServiceAdapter
import com.example.admin.h2hpartner.Adapter.Address_Adapter
import com.github.siyamed.shapeimageview.CircularImageView
import com.sdsmdg.tastytoast.TastyToast
import kotlinx.android.synthetic.main.my_request_row.view.*
import kotlinx.android.synthetic.main.vendor_details.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by Admin on 02-04-18.
 */
class MyRequestAdapter (dataSet:List<MyRequestModel>,context: Context,lbClick :Int) :SelectableAdapter<MyRequestAdapter.ViewHolder>() {

    val dataSet :List<MyRequestModel> = dataSet
    var context :Context? = context
    var lbClick :Int? = lbClick
    val calendar = Calendar.getInstance()

    final var onClick :(View) ->Unit = {}

    companion object {
        lateinit var adapter:MyRequestAdapter
        lateinit var mItemClickListener :onItemClickListener
        lateinit var mAddChangeListener : onAddChangeListener
    }



    class ViewHolder(view:View) : RecyclerView.ViewHolder(view),View.OnClickListener{

        internal var tvServiceName :TextView
        internal var tvDate : TextView
        internal var tvVendorName:TextView
        internal var tvStatus :TextView
        internal var ivVendorImage:CircularImageView
        internal var ivCancelReq : ImageView
        internal var tvCloseReq : TextView


        init {
            tvServiceName =  view.findViewById(R.id.tvServiceName)
            tvDate =  view.findViewById(R.id.tvServiceDate)
            tvStatus =  view.findViewById(R.id.tvServiceStatus)
            tvVendorName =  view.findViewById(R.id.tvVendorName)
            ivVendorImage =  view.findViewById(R.id.ivVendorImage)
            ivCancelReq = view.findViewById(R.id.ivCancelReq)
            tvCloseReq = view.findViewById(R.id.tvclosereq)

            ivCancelReq.setOnClickListener(this)
            tvCloseReq.setOnClickListener(this)

           // tvVendorName.setOnClickListener(this)
        }



        override fun onClick(view:View) {

            when(view.id){

                R.id.tvVendorName ->{
                    if (mItemClickListener!=null){

                        mItemClickListener.onItemClick(adapterPosition,tvVendorName)
                    }
                }

                R.id.ivCancelReq ->{
                    adapter.showPopup(adapterPosition)
                }

                R.id.tvclosereq ->{
                    if (mItemClickListener!=null){
                        mItemClickListener.onItemClick(adapterPosition,tvCloseReq)
                    }
                }
            }

        }

    }

    fun showPopup(pos : Int)
    {
        val layoutInflater = LayoutInflater.from(context)
        val dialogview = layoutInflater.inflate(R.layout.myreq_popup,null)

        val tvYES = dialogview.findViewById<TextView>(R.id.tvYES)
        val tvNO = dialogview.findViewById<TextView>(R.id.tvNO)

        val popup1 = android.support.v7.app.AlertDialog.Builder(this!!.context!!)

        popup1.setView(dialogview)

        var alertdialog = popup1.create()
        alertdialog.show()

        tvYES.setOnClickListener {
            if(MyRequestAdapter.mAddChangeListener != null)
            {
                MyRequestAdapter.mAddChangeListener.onItemChangeAdd(pos,"YES")
            }

            alertdialog.dismiss()
        }
        tvNO.setOnClickListener {
            if(MyRequestAdapter.mAddChangeListener != null)
            {
                MyRequestAdapter.mAddChangeListener.onItemChangeAdd(pos,"NO")
            }

            alertdialog.dismiss()
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder?.let { holder ->


            var imageString = dataSet.get(position).lsSPImage

            if (!imageString.equals("") && imageString.equals(null)){
                try {

                    holder.ivVendorImage.loadBase64Image(imageString)
                }catch (e:Exception){

                }


            }

            if (lbClick== 1){
                holder.ivCancelReq.visibility = View.VISIBLE
                holder.tvCloseReq.visibility = View.GONE

            }
            else if (lbClick ==4){
                holder.ivCancelReq.visibility = View.GONE
                holder.tvCloseReq.visibility = View.VISIBLE
            }



            if (dataSet.get(position).lsSPName==null){
                holder.tvVendorName.text = "Service Provider will get in touch with you."
            }
            else{
                holder.tvVendorName.text = dataSet.get(position).lsSPName
            }
            if (dataSet.get(position).lsStatus.equals("open")){
                holder.tvStatus.text = "Open"
                holder.ivVendorImage.visibility = View.INVISIBLE
                holder.tvVendorName.visibility = View.INVISIBLE

            }
            else if (dataSet.get(position).lsspl_status.equals("hired")){
                holder.tvStatus.text = "hired"
            }
            else if (dataSet.get(position).lsStatus.equals("response_received")){
                holder.tvStatus.text = "Pending For Accept"
            }
            else if (dataSet.get(position).lsStatus.equals("ongoing")){
                holder.tvStatus.text = "ongoing"
            }




            else
            {
               // TastyToast.makeText(context,"Image Not found",Toast.LENGTH_LONG,TastyToast.ERROR).show()
            }

         /*   if (!vendorImage.equals(null) && !vendorImage.equals("null") && !vendorImage.equals("")) {

                ivVendor.loadBase64Image(vendorImage)
            } else {
                toast("Image null")
            }*/

           /* holder.tvServiceName.text = dataSet.get(position).lsServiceName
            var d = getDate(dataSet.get(position).lsPlannedDate!!)
            holder.tvDate.text = d
*/

            holder.tvServiceName.text = dataSet.get(position).lsServiceName
            var d = getDate(dataSet.get(position).lsCreatedAt!!)
            holder.tvDate.text = d

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       //val viewHolder : ServiceAdapter
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.my_request_row,parent,false)

        view.setOnClickListener(View.OnClickListener {
          //  this.onClick(view)
          //  Toast.makeText(context, "Click Event", Toast.LENGTH_LONG).show()

           // TastyToast.makeText(context,"click Event",TastyToast.LENGTH_LONG,TastyToast.INFO).show()


        })

        adapter = this
        context = parent.context
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return this.dataSet.size
    }

    interface onItemClickListener {
        fun onItemClick(positon: Int,view: View)

    }

    fun setOnCardClickListener(mListener: MyRequestAdapter.onItemClickListener) {
        mItemClickListener = mListener
    }


    fun setOnAddChangeListener(mListener: onAddChangeListener) {
        mAddChangeListener = mListener
    }

    interface onAddChangeListener {
        fun onItemChangeAdd(position: Int,lsVal : String)
    }


    // Send Date String in format = yyyy-MM-dd HH:mm:ss

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


