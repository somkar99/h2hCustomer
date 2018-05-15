package com.GoMobeil.H2H.Adapters

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.GoMobeil.H2H.Extensions.loadBase64Image
import com.GoMobeil.H2H.Models.ServicesModel
import com.GoMobeil.H2H.Models.TestModel
import com.GoMobeil.H2H.R
import com.github.siyamed.shapeimageview.CircularImageView

/**
 * Created by ADMIN on 31-01-2018.
 */
class HomeAdapter(dataSet : List<ServicesModel>) : RecyclerView.Adapter<HomeAdapter.ViewHolder> ()
{

    val dataSet : List<ServicesModel> = dataSet
    final var onClick : (View) -> Unit = {}

    lateinit var context : Context



    class ViewHolder(view : View) : RecyclerView.ViewHolder(view)
    {
        val ivHomeService = view.findViewById<CircularImageView>(R.id.ivHomeService) as ImageView
        val tvTestName = view.findViewById<TextView>(R.id.tvTestName) as TextView


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val itemView = LayoutInflater.from(parent?.getContext()).inflate(R.layout.test_row, parent, false)
        return ViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.let { holder ->

            holder.tvTestName.text = dataSet[position].srv_name
            val imageString = dataSet[position].srv_image

            if (imageString!!.length > 0){
                try {
                    holder.ivHomeService.loadBase64Image(imageString)
                }
                catch (e:Exception){
                    println("Exception e" + e.toString())
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return dataSet.size
    }


}