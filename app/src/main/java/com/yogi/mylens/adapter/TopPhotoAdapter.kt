package com.yogi.mylens.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yogi.mylens.dataClass.TopPhotoData
import com.squareup.picasso.Picasso
import com.yogi.mylens.R

class TopPhotoAdapter(private val listener: (TopPhotoData) -> Unit) :
    RecyclerView.Adapter<TopPhotoAdapter.MyViewHolder>() {


    var epList = mutableListOf<TopPhotoData>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.rec_top_photographer, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return epList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        with(holder) {
            with(epList[position]) {

                Picasso.get().load(this.PhotographerImg).into(pImg)
                pName.text = this.PhotographerName
                pRating.text = this.PhotographerRating.toString()
                pPrice.text = this.PhotographerPrice
            }
        }
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val pImg: ImageView = itemView.findViewById(R.id.photographer_img)
        val pName: TextView = itemView.findViewById(R.id.photographer_name)
        val pRating: TextView = itemView.findViewById(R.id.photographer_rating)
        val pPrice: TextView = itemView.findViewById(R.id.photographer_price)

        init {
            itemView.setOnClickListener {
                listener.invoke(epList[adapterPosition])
            }
        }

    }
}