package com.GoMobeil.H2H.Adapters

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.GoMobeil.H2H.Models.VendorListModel
import com.GoMobeil.H2H.R
import com.GoMobeil.H2H.ServiceAdapter
import com.sdsmdg.tastytoast.TastyToast
import java.text.SimpleDateFormat


class VendorListAdapter(dataset: List<VendorListModel>, context: Context) : SelectableAdapter<VendorListAdapter.ViewHolder>() {
    val dataSet: List<VendorListModel> = dataset

    var context: Context? = context

    final var onClick: (View) -> Unit = {}

    companion object {
        lateinit var adapter: VendorListAdapter
        lateinit var mItemClickListener: onItemClickListener
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        internal var tvName: TextView
        internal var tvLocation: TextView
      //  internal var tvMemberSince: TextView
        internal var ivUncheck: ImageView
        internal var ivCheck: ImageView
        internal var tvHired: TextView
        internal var ivCall: ImageView
        internal  var cbViewDetails : Button
        internal var ivMessage : ImageView
        internal var linearlay : LinearLayout



        init {
            tvName = view.findViewById(R.id.tvName)
            tvLocation = view.findViewById(R.id.tvLocation)
           // tvMemberSince = view.findViewById(R.id.tvMemberSince)
            ivUncheck = view.findViewById(R.id.ivUncheck)
            ivCheck = view.findViewById(R.id.ivCheck)
            tvHired = view.findViewById(R.id.tvHired)
            ivCall = view.findViewById(R.id.ivCall)
            cbViewDetails = view.findViewById(R.id.cbViewDetails)
            ivMessage = view.findViewById(R.id.ivMessage)
            linearlay = view.findViewById(R.id.llLinearVendorlist)


            tvName.setOnClickListener(this)
            tvLocation.setOnClickListener(this)
            tvHired.setOnClickListener(this)
           // tvMemberSince.setOnClickListener(this)
            ivUncheck.setOnClickListener(this)
            ivCheck.setOnClickListener(this)
            ivCall.setOnClickListener(this)
            ivCall.setOnClickListener(this)
            cbViewDetails.setOnClickListener(this)
            ivMessage.setOnClickListener(this)
            linearlay.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            when (view.id) {
                R.id.tvName, R.id.tvLocation, R.id.tvHired,
                R.id.tvHired, R.id.ivCheck, R.id.ivUncheck -> {
                    adapter.toggleSelection(adapterPosition)
                    adapter.notifyItemChanged(adapterPosition)
                }

                R.id.ivCall -> {
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClick(adapterPosition, ivCall)
                    }
                }
                R.id.tvName ->{
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClick(adapterPosition, tvName)
                    }
                }
                R.id.tvLocation ->{
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClick(adapterPosition, tvLocation)
                    }
                }

                R.id.ivMessage ->{
                    if (mItemClickListener!=null){
                        mItemClickListener.onItemClick(adapterPosition,ivMessage)
                    }
                }

                R.id.cbViewDetails -> {
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClick(adapterPosition, cbViewDetails)
                    }
                }

                R.id.tvName ->{
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClick(adapterPosition, tvName)
                    }


                }
                R.id.llLinearVendorlist ->{
                    if (mItemClickListener!=null){
                        mItemClickListener.onItemClick(adapterPosition,linearlay)
                    }
                }

            }
        }
    }

/*
    class ViewHolder(view : View) : RecyclerView.ViewHolder(view)
    {
        val tvName = view.findViewById<TextView>(R.id.tvName)
        val tvLocation = view.findViewById<TextView>(R.id.tvLocation)
        val tvMemberSince = view.findViewById<TextView>(R.id.tvMemberSince)
    }
*/

    override
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
      //
        //
        // val viewHolder: ServiceAdapter.ViewHolder;
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.vendor_list_row, parent, false)
       // viewHolder = ServiceAdapter.ViewHolder(view);


            view.setOnClickListener(View.OnClickListener {
                this.onClick(view)
              //  Toast.makeText(context, "Click Event", Toast.LENGTH_LONG).show()

                TastyToast.makeText(context,"Click Event",TastyToast.LENGTH_LONG,TastyToast.INFO).show()
            })


        adapter = this
        context = parent.context;
        return ViewHolder(view)
    }

    override
    fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.let { holder ->
            holder.tvName.text = dataSet.get(position).lsFirstName + " " + dataSet.get(position).lsLastName
            holder.tvLocation.text = dataSet.get(position).sp_primaryBusiness
            var lsTimestamp = dataSet.get(position).lsMemberSince

          //  val formattedDate = formattedDate(lsTimestamp!!)

          //  holder.tvMemberSince.text = "Member since  $formattedDate"

            var lsCategory = dataSet.get(position).lsCategory

            if (lsCategory.equals("Differently Abled")) {
                setTextviewRightDrawable(holder.tvName, R.drawable.differently_abled)
            } else if (lsCategory.equals("housewive")) {
                setTextviewRightDrawable(holder.tvName, R.drawable.women)
            } else if (lsCategory.equals("elderly")) {
                setTextviewRightDrawable(holder.tvName, R.drawable.elderly)
            } else {
            }

            if (isSelected(position)) {
                holder.ivUncheck.visibility = View.GONE
                holder.ivCheck.visibility = View.VISIBLE
            } else {
                holder.ivCheck.visibility = View.GONE
                holder.ivUncheck.visibility = View.VISIBLE
            }
        }
    }

    fun formattedDate(lsTimeStamp: String): String {
        val sdf1 = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
        val d1 = sdf1.parse(lsTimeStamp)
        val sdf = SimpleDateFormat("MMM yy")
        val dateWithoutTime = sdf.format(d1)
        return dateWithoutTime
    }

    fun setTextviewRightDrawable(textView: TextView, drawable: Int) {
        textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable, 0);
    }

    override
    fun getItemCount(): Int {
        return this.dataSet.size
    }

    fun setOnCardClickListener(mListener: onItemClickListener) {
        mItemClickListener = mListener
    }

    interface onItemClickListener {
        fun onItemClick(positon: Int,view: View)

    }
}
