package com.example.studentridenet.adapter

import android.content.Context
import android.graphics.Movie
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.studentridenet.R
import com.example.studentridenet.di.offerOrRequest
import kotlinx.android.synthetic.main.hori_trip.view.*
import kotlinx.android.synthetic.main.nav_header_main.view.*

class TripAdapter(
    //
    //
    // This class never be used before.
    //
    //
    var OOrRData: ArrayList<offerOrRequest>,
    val clickListener: (offerOrRequest)->Unit ) : RecyclerView.Adapter<TripAdapter.RecyclerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        // inflate the Layout with the UI that we have created for the RecyclerView
        val v = LayoutInflater.from(parent.context).inflate(R.layout.hori_trip, parent, false)
        return RecyclerViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        // set the data in items of the RecyclerView
        holder.dpName.text = OOrRData[position].user_id
        holder.dpDeparture.text = OOrRData[position].departure_location
        holder.dpDestination.text = OOrRData[position].final_location
        val time  = OOrRData[position].pickup_time.toString()
        holder.dpTime.text =time
        //PosterLoader.getInstance().loadURL(movieData[position].poster_path, holder.IV_style)
        holder.bind(OOrRData[position],clickListener)
    }


    override fun getItemCount(): Int {
        //return the item count
        return OOrRData.size
    }

    inner class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //connecting od with the text views
        internal var dpName: TextView = itemView.findViewById(R.id.name_text) as TextView
        internal var dpDeparture: TextView = itemView.findViewById(R.id.departure_text) as TextView
        internal var dpDestination: TextView = itemView.findViewById(R.id.destination_text) as TextView
        internal var dpTime: TextView = itemView.findViewById(R.id.date) as TextView
        fun bind(oRR: offerOrRequest, clickListener: (offerOrRequest) -> Unit) {
            itemView.msg.setOnClickListener{
                clickListener(oRR)
            }
        }
        //need to check if the images is being clicked or not so we can jump to messager
    }
}