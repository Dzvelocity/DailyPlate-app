package com.myappproj.healthapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.myappproj.healthapp.R
import com.myappproj.healthapp.model.ItemModel
class HorizontalRecyclerView(private val items: List<ItemModel>) : RecyclerView.Adapter<HorizontalRecyclerView.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imgkeluhan)
        private val textView: TextView = itemView.findViewById(R.id.titleimg)

        fun bind(item: ItemModel) {
            imageView.setImageResource(item.imageResId)
            textView.text = item.text
        }
    }
}