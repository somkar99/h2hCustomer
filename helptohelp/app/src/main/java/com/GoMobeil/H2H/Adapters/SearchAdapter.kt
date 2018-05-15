package com.GoMobeil.H2H.Adapters

import android.content.Context
import android.service.autofill.Dataset
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.GoMobeil.H2H.Models.SearchModel
import com.GoMobeil.H2H.R
import com.sdsmdg.tastytoast.TastyToast

/**
 * Created by Admin on 13-04-18.
 */
class SearchAdapter (dataset: List<SearchModel>,context: Context):SelectableAdapter<SearchAdapter.ViewHolder>() {


    val dataSet : List<SearchModel> = dataset
    var context : Context? = context

    final var onClick : (View) -> Unit = {}

    companion object {
        lateinit var adapter :SearchAdapter
        lateinit var mItemClickListener : onItemClickListener


    }

    class ViewHolder(view:View):RecyclerView.ViewHolder(view){

        internal var tvSearchName: TextView

        init {
            tvSearchName = view.findViewById<TextView>(R.id.tvSearch)

            //tvSearchName.setOnClickListener(this)
        }

      /*  override fun onClick(v: View?) {

            when (v!!.id) {
                R.id.llSearch -> {
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClick(adapterPosition)
                    }
                }

            }

        }*/
    }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            holder?.let { holder ->

                holder.tvSearchName.text = dataSet.get(position).lsSrvName

            }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.search_row,parent,false)

        view.setOnClickListener(View.OnClickListener {
            this.onClick(view)
            //  Toast.makeText(context, "Click Event", Toast.LENGTH_LONG).show()

            TastyToast.makeText(context,"Click Event", TastyToast.LENGTH_LONG, TastyToast.INFO).show()
        })

        adapter = this
        context = parent.context

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return this.dataSet.size
    }


        fun setOnCardClickListener(mListener: onItemClickListener) {
            SearchAdapter.mItemClickListener = mListener
        }

        interface onItemClickListener {
            fun onItemClick(positon: Int)

        }





}