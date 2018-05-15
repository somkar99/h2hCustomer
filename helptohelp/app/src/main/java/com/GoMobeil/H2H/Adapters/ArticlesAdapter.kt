package com.GoMobeil.H2H.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.GoMobeil.H2H.Extensions.loadBase64Image
import com.GoMobeil.H2H.Models.ArticlesModel
import com.GoMobeil.H2H.Models.ServicesModel
import com.GoMobeil.H2H.Models.TestModel
import com.GoMobeil.H2H.R
import com.GoMobeil.H2H.ServiceAdapter
import com.github.siyamed.shapeimageview.CircularImageView

/**
 * Created by ADMIN on 09-02-2018.
 */
class ArticlesAdapter(dataSet : List<ArticlesModel>) : RecyclerView.Adapter<ArticlesAdapter.ViewHolder>()
{

    val dataSet : List<ArticlesModel> = dataSet
    final var onClick : (View)-> Unit = {}

    lateinit var context : Context
    class ViewHolder(view : View) : RecyclerView.ViewHolder(view)
    {
        val ivImage = view.findViewById<ImageView>(R.id.ivImage)
        val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder : ViewHolder;
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.articles_row,parent,false)
        viewHolder = ViewHolder(view);

        context = parent.context;
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.let{ holder ->
            holder.tvTitle.text = dataSet[position].art_title
            val imageString = dataSet[position].art_img

            if (imageString!!.length > 0) {
                try {
                    holder.ivImage.loadBase64Image(imageString);
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