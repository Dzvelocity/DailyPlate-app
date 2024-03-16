package com.myappproj.healthapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.myappproj.healthapp.adapter.MainMenuView
import com.myappproj.healthapp.model.MenuModel2
import com.myappproj.healthapp.model.MenuModel3

class MainResepFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MainMenuView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main_resep, container, false)

        recyclerView = view.findViewById(R.id.recyclerView_main)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Membuat adapter
        adapter = MainMenuView()
        recyclerView.adapter = adapter

        // Inisialisasi database reference untuk masing-masing child
        val resepRef = FirebaseDatabase.getInstance().reference.child("resep")
        val menusRef = FirebaseDatabase.getInstance().reference.child("menus")

        // Mengambil data dari child "resep"
        resepRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val dataList = mutableListOf<Any>()
                for (snapshot in dataSnapshot.children) {
                    val menu = snapshot.getValue(MenuModel2::class.java)
                    menu?.let {
                        dataList.add(it)
                    }
                }
                // Menambahkan data ke adapter
                adapter.setData(dataList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Penanganan kesalahan
            }
        })

        // Mengambil data dari child "menus"
        menusRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val dataList = mutableListOf<Any>()
                for (snapshot in dataSnapshot.children) {
                    val menu = snapshot.getValue(MenuModel3::class.java)
                    menu?.let {
                        dataList.add(it)
                    }
                }
                // Menambahkan data ke adapter
                adapter.setData(dataList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Penanganan kesalahan
            }
        })

        return view
    }
}
