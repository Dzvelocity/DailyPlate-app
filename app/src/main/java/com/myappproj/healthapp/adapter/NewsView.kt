package com.myappproj.healthapp.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.myappproj.healthapp.model.Article
import com.myappproj.healthapp.R

class NewsView(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)  // Replace with your actual TextView ID

    fun bind(article: Article) {
        tvTitle.text = article.title

    }
}
