package com.GoMobeil.H2H.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.GoMobeil.H2H.Models.AddressModel
import com.GoMobeil.H2H.R
import kotlinx.android.synthetic.main.address_row.view.*

/**
 * Created by Admin on 16-03-18.
 */
class AddressAdapter (val items:MutableList<AddressModel>,val context: Context): RecyclerView.Adapter<AddressAdapter.ViewHolder>() {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val addline1=items[position].add_line1
        val addline2=items[position].add_line2
        val city=items[position].city
        val state=items[position].state
        val pincode=items[position].pincode
        val completeaddress=addline1+", "+addline2+", "+city+", "+state+", "+pincode


        holder?.tvFinalAdd?.text=completeaddress

    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.address_new_row,parent,false))


    }

    class ViewHolder(view:View) :RecyclerView.ViewHolder(view){

        val tvFinalAdd = view.tvAddress
    }
}