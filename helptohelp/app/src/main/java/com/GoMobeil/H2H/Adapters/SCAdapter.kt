package com.GoMobeil.H2H.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.GoMobeil.H2H.Models.SCModel
import android.widget.*
import com.GoMobeil.H2H.Extensions.loadBase64Image
import com.GoMobeil.H2H.R
import com.sdsmdg.tastytoast.TastyToast


/**
 * Created by niranjanshah on 29/01/18.
 */
class SCAdapter(dataSet : List<SCModel>) : RecyclerView.Adapter<SCAdapter.ViewHolder>()
{

    val dataSet : List<SCModel> = dataSet
    final var onClick : (View)-> Unit = {}

    lateinit var context : Context
    class ViewHolder(view : View) : RecyclerView.ViewHolder(view)
    {
        val ivImage = view.findViewById<ImageView>(R.id.ivImage)
        val tvDescription = view.findViewById<TextView>(R.id.tvDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewHolder : ViewHolder;
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.categories_row,parent,false)
        viewHolder = ViewHolder(view);
        view.setOnClickListener(View.OnClickListener{
            this.onClick(view)
           // Toast.makeText(context,"Click Event",Toast.LENGTH_LONG).show()

            TastyToast.makeText(context,"Click Event",TastyToast.LENGTH_LONG,TastyToast.INFO).show()

        } )

        context = parent.context;
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.let{ holder ->
            holder.tvDescription.text = dataSet[position].sc_name;
            val imageString = dataSet[position].sc_image

            try {


               // val imageBytes = Base64.decode(imageString, Base64.DEFAULT)
                /*
                val base64Image = imageString!!.split(",")[1];
                //val imageBytes = Base64.getDecoder().decode(imageString?.toByteArray(Charsets.UTF_8))

                //val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                val decodedString = Base64.decode(base64Image, Base64.DEFAULT)
                val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)


                holder.ivImage.setImageBitmap(decodedByte)
                */

                holder.ivImage.loadBase64Image(imageString);
            }
            catch ( e : Exception)
            {
                println("Exception e"+e.toString())
            }
        }
    }

    override fun getItemCount(): Int {
        return this.dataSet.size
    }




}

