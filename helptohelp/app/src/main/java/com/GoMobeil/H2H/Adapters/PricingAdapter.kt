package com.GoMobeil.H2H.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.GoMobeil.H2H.Models.PricingModel
import com.GoMobeil.H2H.R

/**
 * Created by Admin on 04-04-18.
 */
class PricingAdapter (dataSet:List<PricingModel>):RecyclerView.Adapter<PricingAdapter.ViewHolder>() {


    val dataSet :List<PricingModel> = dataSet
    lateinit var context : Context

    class ViewHolder(view:View):RecyclerView.ViewHolder(view){

        val typedesc = view.findViewById<TextView>(R.id.tvServiceType)
        val tvCost = view.findViewById<TextView>(R.id.tvCost)
        val tvpricetyle= view.findViewById<TextView>(R.id.tvpricetype)
        val tvVisitingCharge = view.findViewById<TextView>(R.id.tvVisitingCharge)

        val tvRemark = view.findViewById<TextView>(R.id.tvRemark)

        val tvpriceunit = view.findViewById<TextView>(R.id.tvPriceUnit)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val viewHolder : PricingAdapter.ViewHolder
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.pricing_row,parent,false)
        viewHolder = PricingAdapter.ViewHolder(view)
        context = parent.context
        return PricingAdapter.ViewHolder(view)


    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder?.let { holder ->

            holder.typedesc.text = dataSet[position].pst_srvtypedesc

            if (!(dataSet[position].pst_cost_from.equals("") &&
                    dataSet[position].pst_cost_to.equals(""))){
                holder.tvCost.text = dataSet[position].pst_cost_from+"-"+dataSet[position].pst_cost_to

            }
            else if (!(dataSet[position].pst_cost_fixed.equals(""))){
                holder.tvCost.text = dataSet[position].pst_cost_fixed
            }

            holder.tvpricetyle.text = dataSet[position].pst_pricetype
            holder.tvVisitingCharge.text = dataSet[position].pst_cost_visiting
            holder.tvRemark.text = dataSet[position].pst_cost_remark
            holder.tvpriceunit.text = dataSet[position].pst_priceunit
        }


    }

    override fun getItemCount(): Int {
       return this.dataSet.size
    }


}