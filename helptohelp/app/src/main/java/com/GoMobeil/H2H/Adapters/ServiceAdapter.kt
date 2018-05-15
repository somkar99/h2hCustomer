package com.GoMobeil.H2H

import android.app.Application
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.GoMobeil.H2H.Models.SCModel
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.widget.*
import com.GoMobeil.H2H.Adapters.SelectableAdapter
import com.GoMobeil.H2H.Extensions.loadBase64Image
import com.GoMobeil.H2H.Models.ServicesModel
import com.github.siyamed.shapeimageview.CircularImageView


/**
 * Created by niranjanshah on 29/01/18.
 */
class ServiceAdapter(dataSet : List<ServicesModel>) : SelectableAdapter<ServiceAdapter.ViewHolder>()
{

    val dataSet : List<ServicesModel> = dataSet
    final var onClick : (View)-> Unit = {}

    lateinit var context : Context

    companion object {
        lateinit var adapter: ServiceAdapter
        lateinit var mItemClickListener: onItemClickListener
    }


    class ViewHolder(view : View) : RecyclerView.ViewHolder(view),View.OnClickListener
    {
        internal lateinit var ivService : CircularImageView
        internal lateinit var tvService : TextView
        internal lateinit var tvDescription : TextView

        init {

            ivService = view.findViewById(R.id.ivService)
            tvService = view.findViewById(R.id.tvService)
            tvDescription = view.findViewById(R.id.tvDescription)


            ivService.setOnClickListener(this)
            tvService.setOnClickListener(this)
            tvDescription.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when(v!!.id){
                R.id.tvDescription ->{
                    if (mItemClickListener!=null){
                        mItemClickListener.onItemClick(adapterPosition,tvDescription)
                    }
                }

                R.id.tvService ->{
                    if (mItemClickListener!=null){
                        mItemClickListener.onItemClick(adapterPosition,tvService)
                    }
                }

                R.id.ivService ->{
                    if (mItemClickListener!=null){
                        mItemClickListener.onItemClick(adapterPosition,ivService)
                    }
                }
            }
        }


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {



        val viewHolder : ViewHolder;
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.services_list_row,parent,false)

        viewHolder = ViewHolder(view);

        context = parent.context;
        return ViewHolder(view)



    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.let{ holder ->
            holder.tvService.text = dataSet[position].srv_name;
            holder.tvDescription.text = dataSet[position].srv_description
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

    fun setOnCardClickListener(mListener: onItemClickListener) {
        mItemClickListener = mListener
    }

    interface onItemClickListener {
        fun onItemClick(positon: Int,view: View)

    }


}

