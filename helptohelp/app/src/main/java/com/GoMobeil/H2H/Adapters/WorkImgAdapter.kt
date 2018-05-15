package com.GoMobeil.H2H.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.GoMobeil.H2H.Extensions.loadBase64Image
import com.GoMobeil.H2H.Models.WorkImgModel
import com.GoMobeil.H2H.R
import kotlinx.android.synthetic.main.work_img_row.view.*

/**
 * Created by Admin on 02-04-18.
 */
class WorkImgAdapter (dataSet: List<WorkImgModel>) : RecyclerView.Adapter<WorkImgAdapter.ViewHolder>() {

    val dataSet :List<WorkImgModel> = dataSet
    final var onClick : (View)->Unit = {}

    lateinit var context : Context
    class ViewHolder(view:View) :RecyclerView.ViewHolder(view){

        val ivImage = view.findViewById<ImageView>(R.id.ivWorkImage)
        val tvWorkImg = view.findViewById<TextView>(R.id.tvWorkImgName)


    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val viewHolder :ViewHolder
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.work_img_row,parent,false)
        viewHolder = ViewHolder(view)

        context = parent.context
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder?.let { holder->

            holder.tvWorkImg.text = dataSet[position].spi_imagename
            val imageString = dataSet[position].spi_image

            if (imageString!!.length>0){
                try {
                    holder.ivImage.loadBase64Image(imageString)

                }catch (e:Exception){

                }
            }

        }
    }
    override fun getItemCount(): Int {

        return this.dataSet.size

    }








}