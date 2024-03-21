package com.myappproj.healthapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

import com.myappproj.healthapp.adapter.HorizontalBahan
import com.myappproj.healthapp.adapter.HorizontalRecyclerView
import com.myappproj.healthapp.adapter.MyMenuView
import com.myappproj.healthapp.model.BahanModel
import com.myappproj.healthapp.model.ItemModel
import com.myappproj.healthapp.model.MenuModel
import com.myappproj.healthapp.R

class BerandaFragment : Fragment(), HorizontalRecyclerView.ItemClickListener, MyMenuView.MenuClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewMenu: RecyclerView
    private lateinit var recyclerView2: RecyclerView
    private lateinit var recyclerView3: RecyclerView
    private lateinit var adapter: HorizontalRecyclerView
    private lateinit var adapter2: HorizontalBahan
    private lateinit var adapter3: HorizontalBahan
    private lateinit var menuAdapter: MyMenuView
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_beranda, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerViewMenu = view.findViewById(R.id.recyclerViewMenu)
        recyclerView2 = view.findViewById(R.id.recyclerView2)
        recyclerView3 = view.findViewById(R.id.recyclerView3)
        database = FirebaseDatabase.getInstance().reference.child("keluhan")

        // Setting up Horizontal RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        adapter = HorizontalRecyclerView(emptyList())
        recyclerView.adapter = adapter
        adapter.setItemClickListener(this)

        recyclerView2.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        adapter2 = HorizontalBahan(requireContext())
        recyclerView2.adapter = adapter2

        recyclerView3.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        adapter3 = HorizontalBahan(requireContext())
        recyclerView3.adapter = adapter3

        // Setting up Menu RecyclerView
        recyclerViewMenu.layoutManager = LinearLayoutManager(requireContext())
        menuAdapter = MyMenuView(this)
        recyclerViewMenu.adapter = menuAdapter

        // Fetch data from Firebase
        fetchDataFromFirebase()
        retrieveDataFromFirebase() // Retrieve bahan data from Firebase
        retrieveUnhealthyBahanDataFromFirebase() // Retrieve unhealthy bahan data from Firebase
        retrieveUnhealthyBahanDataFromFirebase2() // Retrieve unhealthy bahan data from Firebase for recyclerView3

        // Set click listeners for text views
        val textBahan1 = view.findViewById<TextView>(R.id.all_1)
        val textBahan2 = view.findViewById<TextView>(R.id.all_2)

        textBahan1.setOnClickListener {
            // Navigasi ke BahanFragment1
            findNavController().navigate(R.id.action_homeFragment_to_bahanFragment1)
        }

        textBahan2.setOnClickListener {
            // Navigasi ke BahanFragment2
            findNavController().navigate(R.id.action_homeFragment_to_bahanFragment2)
        }

        val altUpresepButton = view.findViewById<Button>(R.id.alt_upresep)
        altUpresepButton.setOnClickListener {
            // Navigasi ke TabResepFragment2
            findNavController().navigate(R.id.action_homeFragment_to_upResepFragment12)
        }

        val btnAll = view.findViewById<Button>(R.id.btn_all)
        btnAll.setOnClickListener {
            // Navigasi ke TabResepFragment1
            findNavController().navigate(R.id.action_homeFragment_to_categoryResepFragment)
        }

        val cekFoodieFolio = view.findViewById<TextView>(R.id.cek_foodiefolio)
        cekFoodieFolio.setOnClickListener {
            // Navigasi ke HealthNewsFragment
            findNavController().navigate(R.id.action_homeFragment_to_healthNewsFragment)
        }


        return view
    }

    private fun fetchDataFromFirebase() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val items = mutableListOf<ItemModel>()
                for (data in snapshot.children) {
                    val imageURL = data.child("imageURL").getValue(String::class.java) ?: ""
                    val diseases = data.child("diseases").getValue(String::class.java) ?: ""
                    val item = ItemModel(imageURL, diseases)
                    items.add(item)
                }
                // Update Horizontal RecyclerView adapter with fetched data
                adapter.setItems(items)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun retrieveDataFromFirebase() {
        val ref = FirebaseDatabase.getInstance().getReference("resep")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val menuList = mutableListOf<MenuModel>()
                var count = 0
                for (menuSnapshot in snapshot.children) {
                    if (count >= 3) break  // Memeriksa apakah sudah mencapai batas 3 item
                    val menu = menuSnapshot.getValue(MenuModel::class.java)
                    menu?.let {
                        menuList.add(it)
                        count++
                    }
                }
                // Update Menu RecyclerView adapter with fetched data
                menuAdapter.setData(menuList)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun retrieveUnhealthyBahanDataFromFirebase() {
        val ref = FirebaseDatabase.getInstance().getReference("bahan").orderByChild("jenis").equalTo("sehat")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val bahanList = mutableListOf<BahanModel>()
                for (bahanSnapshot in snapshot.children) {
                    val bahan = bahanSnapshot.getValue(BahanModel::class.java)
                    bahan?.let {
                        bahanList.add(it)
                    }
                }
                // Update Unhealthy Bahan RecyclerView adapter with fetched data
                val bahanAdapter = recyclerView2.adapter as? HorizontalBahan
                bahanAdapter?.setData(bahanList)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun retrieveUnhealthyBahanDataFromFirebase2() {
        val ref = FirebaseDatabase.getInstance().getReference("bahan").orderByChild("jenis").equalTo("tidak")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val bahanList = mutableListOf<BahanModel>()
                for (bahanSnapshot in snapshot.children) {
                    val bahan = bahanSnapshot.getValue(BahanModel::class.java)
                    bahan?.let {
                        bahanList.add(it)
                    }
                }
                // Update Unhealthy Bahan RecyclerView adapter with fetched data
                val bahanAdapter = recyclerView3.adapter as? HorizontalBahan
                bahanAdapter?.setData(bahanList)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    override fun onMenuClicked(menuName: String) {
        // Redirect to MainResepFragment and pass the menu name
        val bundle = Bundle().apply {
            putString("menuName", menuName)
        }
        findNavController().navigate(R.id.action_homeFragment_to_mainResepFragment, bundle)
    }

    override fun onItemClick(item: ItemModel) {
        // Handle item click event for Horizontal RecyclerView
        val bundle = Bundle().apply {
            putString("diseases", item.diseases)
        }
        findNavController().navigate(R.id.mainKeluhanFragment, bundle)
    }
}





