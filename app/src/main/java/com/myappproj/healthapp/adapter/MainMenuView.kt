package com.myappproj.healthapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.myappproj.healthapp.R
import com.myappproj.healthapp.model.MenuModel2
import com.myappproj.healthapp.model.MenuModel3

class MainMenuView : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var menuList = emptyList<Any>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.resep_recyclerview, parent, false)
        return when (viewType) {
            MENU_MODEL_2 -> MenuModel2ViewHolder(view)
            MENU_MODEL_3 -> MenuModel3ViewHolder(view)
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMenu = menuList[position]
        when (holder) {
            is MenuModel2ViewHolder -> {
                val menuModel2 = currentMenu as MenuModel2
                holder.bind(menuModel2)
            }
            is MenuModel3ViewHolder -> {
                val menuModel3 = currentMenu as MenuModel3
                holder.bind(menuModel3)
            }
        }
    }

    override fun getItemCount(): Int {
        return menuList.size
    }

    fun setData(newMenuList: List<Any>) {
        menuList = newMenuList
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when (menuList[position]) {
            is MenuModel2 -> MENU_MODEL_2
            is MenuModel3 -> MENU_MODEL_3
            else -> throw IllegalArgumentException("Invalid data type")
        }
    }

    inner class MenuModel2ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val listBahan: TextView = itemView.findViewById(R.id.listbahan)
        private val listAlat: TextView = itemView.findViewById(R.id.listalat)
        private val listLangkah: TextView = itemView.findViewById(R.id.listlangkah)

        fun bind(menuModel2: MenuModel2) {
            listAlat.text = menuModel2.alat
            listBahan.text = menuModel2.bahan.joinToString("\n")
            listLangkah.text = menuModel2.langkah.joinToString("\n")

        }
    }

    inner class MenuModel3ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.img_resep)
        private val namaMakananTextView: TextView = itemView.findViewById(R.id.nama_makanan)
        private val jmlKaloriTextView: TextView = itemView.findViewById(R.id.jml_kalori)
        private val namaPenyakit: TextView = itemView.findViewById(R.id.penyakit)

        fun bind(menuModel3: MenuModel3) {
            Glide.with(itemView.context)
                .load(menuModel3.imageURL)
                .placeholder(R.drawable.placeholder_img) // Placeholder image while loading
                .centerCrop() // Mengatur ukuran gambar sesuai dengan ukuran aslinya
                .into(imageView)
            namaMakananTextView.text = menuModel3.menuName
            jmlKaloriTextView.text = menuModel3.calorieContent
            namaPenyakit.text = menuModel3.diseases

        }
    }

    companion object {
        private const val MENU_MODEL_2 = 0
        private const val MENU_MODEL_3 = 1
    }
}


