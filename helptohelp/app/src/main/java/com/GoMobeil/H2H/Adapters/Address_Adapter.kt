
package com.example.admin.h2hpartner.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import com.GoMobeil.H2H.Adapters.SelectableAdapter
import com.GoMobeil.H2H.R
import com.example.admin.h2hpartner.Models.Address_Model
import com.sdsmdg.tastytoast.TastyToast

class Address_Adapter(dataSet: List<Address_Model>) : SelectableAdapter<Address_Adapter.ViewHolder>() {
    val dataSet: List<Address_Model> = dataSet
    lateinit var context: Context

    companion object {
        lateinit var adapter: Address_Adapter
        lateinit var mItemClickListener: onItemClickListener
        lateinit var mAddChangeListener : onAddChangeListener
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {


       /* internal var ivUncheck: ImageView
        internal var ivCheck: ImageView*/
        internal var rbAddSelected : RadioButton
        internal var tvAddTypeHeader: TextView
        internal var ivChangeAdd: ImageView
        internal var tvAddressData: TextView

        init {
            /*ivUncheck = view.findViewById(R.id.ivUncheck)
            ivCheck = view.findViewById(R.id.ivCheck)*/
            rbAddSelected = view.findViewById(R.id.rbAddSelected)
            tvAddTypeHeader = view.findViewById(R.id.tvAddTypeHeader)
            ivChangeAdd = view.findViewById(R.id.ivChangeAdd)
            tvAddressData = view.findViewById(R.id.tvAddressData)

           /* ivUncheck.setOnClickListener(this)
            ivCheck.setOnClickListener(this)*/
            rbAddSelected.setOnClickListener(this)
            tvAddTypeHeader.setOnClickListener(this)
            tvAddressData.setOnClickListener(this)
            ivChangeAdd.setOnClickListener(this)

           // tvAddressData.performClick()
        }

        override fun onClick(v: View?) {
            when (v!!.id) {
                R.id.rbAddSelected, R.id.tvAddTypeHeader
                    , R.id.tvAddressData -> {
                    adapter.toggleSelection(adapterPosition)
                    adapter.notifyItemChanged(adapterPosition)
                    if(mItemClickListener != null)
                    {
                        mItemClickListener.onItemClick(adapterPosition)
                    }
                }

                R.id.ivChangeAdd ->
                {
                    adapter.showPopup(adapterPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.show_add_row, parent, false)

        adapter = this
        context = parent.context;
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.let { holder ->
            var lsAddType = dataSet[position].add_type

            if(lsAddType.equals("R"))
            {
               holder.tvAddTypeHeader.text = "Home"
            }
            else if(lsAddType.equals("W"))
            {
                holder.tvAddTypeHeader.text = "Office"
            }
            else
            {
                holder.tvAddTypeHeader.text = "Other"
            }

            val lsAddLine1 = dataSet[position].add_line1
            var lsAddLine2 = dataSet[position].add_line2
            val lsAddCity = dataSet[position].add_city
            val lsAddState = dataSet[position].add_state
            val lsAddPincode = dataSet[position].add_pincode

            holder.tvAddressData.text = "$lsAddLine1 \n $lsAddLine2 , $lsAddCity , $lsAddState , $lsAddPincode";

            if (isSelected(position)) {
                /*holder.ivUncheck.visibility = View.GONE
                holder.ivCheck.visibility = View.VISIBLE*/
                holder.rbAddSelected.isChecked = true
            } else {
               /* holder.ivCheck.visibility = View.GONE
                holder.ivUncheck.visibility = View.VISIBLE*/
                holder.rbAddSelected.isChecked = false
            }
        }
    }

    fun showPopup(pos : Int)
    {
        val layoutInflater = LayoutInflater.from(context)
        val dialogview = layoutInflater.inflate(R.layout.popup, null)
        val tvEditAdd = dialogview.findViewById<TextView>(R.id.tvEditAddress)
        val tvDeleteAdd = dialogview.findViewById<TextView>(R.id.tvDeleteAddress)

        val popup1 = android.support.v7.app.AlertDialog.Builder(context)
        popup1.setView(dialogview)

        var alertdialog = popup1.create()
        alertdialog.show()

        tvEditAdd.setOnClickListener {
            if(mAddChangeListener != null)
            {
                mAddChangeListener.onItemChangeAdd(pos,"EDIT")
            }

            alertdialog.dismiss()
        }
        tvDeleteAdd.setOnClickListener {
            if(mAddChangeListener != null)
            {
                mAddChangeListener.onItemChangeAdd(pos,"DELETE")
            }

            alertdialog.dismiss()
        }
    }

    override fun getItemCount(): Int {
        return this.dataSet.size
    }

    fun setOnItemClickListener(mListener: onItemClickListener) {
        mItemClickListener = mListener
    }

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnAddChangeListener(mListener: onAddChangeListener) {
        mAddChangeListener = mListener
    }

    interface onAddChangeListener {
        fun onItemChangeAdd(position: Int,lsVal : String)
    }
}
