package com.myappproj.healthapp

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.myappproj.healthapp.adapter.MyMenuView
import com.myappproj.healthapp.model.MenuModel

class TabResepFragment2 : Fragment() {

    private lateinit var adapter: MyMenuView
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyresep1: TextView
    private lateinit var emptyresep2: TextView
    private lateinit var emptyresep3: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_tab_resep2, container, false)
        recyclerView = view.findViewById(R.id.recyclerview_mymenu)
        emptyresep1 = view.findViewById(R.id.empty_titleresep)
        emptyresep2 = view.findViewById(R.id.empty_titleresep2)
        emptyresep3 = view.findViewById(R.id.empty_resepbg)

        adapter = MyMenuView()
        // Set layout manager dan adapter untuk RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        retrieveDataFromFirebase()

                val addResepButton: FloatingActionButton = view.findViewById(R.id.add_resep)
                addResepButton.setOnClickListener {
                    // Pindah ke halaman UpResepFragment1
                    findNavController().navigate(R.id.action_homeFragment_to_upResepFragment12)
                }
                return view
            }

            private fun retrieveDataFromFirebase() {
                val ref = FirebaseDatabase.getInstance().getReference("menus")

                ref.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val menuList = mutableListOf<MenuModel>()
                        for (menuSnapshot in snapshot.children) {
                            val menu = menuSnapshot.getValue(MenuModel::class.java)
                            menu?.let {
                                menuList.add(it)
                            }
                        }

                        // Tampilkan atau sembunyikan tampilan awal berdasarkan keberadaan data resep
                        if (menuList.isEmpty()) {
                            recyclerView.visibility = View.GONE
                            emptyresep1.visibility = View.VISIBLE
                            emptyresep2.visibility = View.VISIBLE
                            emptyresep3.visibility = View.VISIBLE
                        } else {
                            recyclerView.visibility = View.VISIBLE
                            emptyresep1.visibility = View.GONE
                            emptyresep2.visibility = View.GONE
                            emptyresep3.visibility = View.GONE
                            // Menambahkan data ke adapter RecyclerView
                            adapter.setData(menuList)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle errors
                        Log.e(TAG, "Failed to read value.", error.toException())
                    }
                })
            }
        }


