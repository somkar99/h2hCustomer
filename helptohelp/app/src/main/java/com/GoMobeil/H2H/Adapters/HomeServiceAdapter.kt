package com.GoMobeil.H2H.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.GoMobeil.H2H.Extensions.loadBase64Image
import com.GoMobeil.H2H.Models.ServicesModel
import com.GoMobeil.H2H.R
import com.github.siyamed.shapeimageview.CircularImageView

/**
 * Created by Admin on 16-02-18.
 */
class HomeServiceAdapter (dataSet : List<ServicesModel>) : RecyclerView.Adapter<HomeServiceAdapter.HomeViewHolder>() {

    val dataSet : List<ServicesModel> = dataSet
    final var onClick : (View)-> Unit = {}

    lateinit var context : Context
    class HomeViewHolder(view : View) : RecyclerView.ViewHolder(view)
    {
        val ivService = view.findViewById<CircularImageView>(R.id.ivHomerow)
        val tvService = view.findViewById<TextView>(R.id.tvHomerow)



    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {



        val viewHolder : HomeViewHolder;
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.home_services,parent,false)

        viewHolder = HomeViewHolder(view);

        context = parent.context;
        return HomeViewHolder(view)



    }


    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder?.let{ holder ->
            holder.tvService.text = dataSet[position].srv_name;
            val imageString = dataSet[position].srv_image

            if (imageString!!.length > 0) {
                try {
                    holder.ivService.loadBase64Image(imageString);
                } catch (e: Exception) {
                    println("Exception e" + e.toString())
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return this.dataSet.size
    }



}