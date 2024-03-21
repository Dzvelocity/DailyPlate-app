package com.myappproj.healthapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.myappproj.healthapp.model.MenuModel
import com.myappproj.healthapp.R

class HorizontalResepAll(private val context: Context) :
    RecyclerView.Adapter<HorizontalResepAll.ViewHolder>() {

    private var menuList: List<MenuModel> = listOf()
    private var itemClickListener: ((String) -> Unit)? = null

    fun setData(newMenuList: List<MenuModel>) {
        menuList = newMenuList
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: (String) -> Unit) {
        itemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.resep_makanan_all, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val menu = menuList[position]
        holder.bind(menu)
        holder.itemView.setOnClickListener {
            itemClickListener?.invoke(menu.menuName)
        }

        // Load image using Glide library
        Glide.with(context)
            .load(menu.imageURL)
            .placeholder(R.drawable.placeholder_img2)
            .centerCrop()
            .into(holder.menuImageView)

        holder.menuNameTextView.text = menu.menuName
        holder.calorieTextView.text = menu.calorieContent + " kal"
    }

    override fun getItemCount(): Int {
        return menuList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val menuImageView: ImageView = itemView.findViewById(R.id.menu_img)
        val menuNameTextView: TextView = itemView.findViewById(R.id.nama_makanan)
        val calorieTextView: TextView = itemView.findViewById(R.id.jml_kalori)

        fun bind(menu: MenuModel) {
            menuNameTextView.text = menu.menuName
        }
    }
}
