package com.yogi.mylens.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yogi.mylens.R
import com.yogi.mylens.dataClass.CategoryData

class IdeaAdapter(private val listener:(CategoryData)->Unit) : RecyclerView.Adapter<IdeaAdapter.MyViewHolder>() {

    var epList= mutableListOf<CategoryData>()
        set(value){
            field=value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rec_idea, parent, false)
        return MyViewHolder(itemView)
    }
    override fun getItemCount(): Int {
        return epList.size
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        with(holder){
            img.setImageResource(epList[position].categoryImg)
            nam.text = epList[position].categoryName
        }

    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img: ImageView = itemView.findViewById(R.id.idea_img)
        val nam: TextView = itemView.findViewById(R.id.idea_name)
    }
}