package com.myappproj.healthapp

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.myappproj.healthapp.adapter.HorizontalResepAll
import com.myappproj.healthapp.model.MenuModel

class TabResepFragment1 : Fragment() {

    private lateinit var recyclerViewMyMenu: RecyclerView
    private lateinit var recyclerViewMyMenu2: RecyclerView
    private lateinit var recyclerViewMyMenu3: RecyclerView
    private lateinit var menuAdapter1: HorizontalResepAll
    private lateinit var menuAdapter2: HorizontalResepAll
    private lateinit var menuAdapter3: HorizontalResepAll
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate layout untuk fragment tab resep 1
        return inflater.inflate(R.layout.fragment_tab_resep1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi RecyclerViews
        recyclerViewMyMenu = view.findViewById(R.id.recyclerview_mymenu)
        recyclerViewMyMenu2 = view.findViewById(R.id.recyclerview_mymenu2)
        recyclerViewMyMenu3 = view.findViewById(R.id.recyclerview_mymenu3)

        // Inisialisasi menuAdapters
        menuAdapter1 = HorizontalResepAll(requireContext())
        menuAdapter2 = HorizontalResepAll(requireContext())
        menuAdapter3 = HorizontalResepAll(requireContext())

        // Setup RecyclerViews
        setupRecyclerView(recyclerViewMyMenu, menuAdapter1)
        setupRecyclerView(recyclerViewMyMenu2, menuAdapter2)
        setupRecyclerView(recyclerViewMyMenu3, menuAdapter3)

        // Inisialisasi Database Reference
        database = FirebaseDatabase.getInstance().reference

        // Set listener untuk item klik pada adapter
        menuAdapter1.setOnItemClickListener { menuName ->
            navigateToMainResepFragment(menuName)
        }
        menuAdapter2.setOnItemClickListener { menuName ->
            navigateToMainResepFragment(menuName)
        }
        menuAdapter3.setOnItemClickListener { menuName ->
            navigateToMainResepFragment(menuName)
        }

        // Muat data menu
        loadMenuData()

        // Set listener pada tombol upgrade
        view.findViewById<Button>(R.id.btn_upgrade1).setOnClickListener {
            showUpgradeDialog()
        }

        view.findViewById<Button>(R.id.btn_upgrade2).setOnClickListener {
            showUpgradeDialog()
        }
    }

    // Fungsi untuk navigasi ke MainResepFragment dengan memberikan argumen menuName
    private fun navigateToMainResepFragment(menuName: String) {
        val bundle = Bundle()
        bundle.putString("menuName", menuName)

        findNavController().navigate(R.id.action_homeFragment_to_mainResepFragment, bundle)
    }

    // Fungsi untuk setup RecyclerView dengan LinearLayoutManager horizontal
    private fun setupRecyclerView(recyclerView: RecyclerView, menuAdapter: HorizontalResepAll) {
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = menuAdapter

        // Set click listener untuk item RecyclerView
        menuAdapter.setOnItemClickListener { menuName ->
            // Handle klik item di sini
            val bundle = Bundle()
            bundle.putString("menuName", menuName)

            // Navigasi ke MainResepFragment menggunakan NavController dan meneruskan menuName sebagai argumen
            findNavController().navigate(R.id.action_homeFragment_to_mainResepFragment, bundle)
        }
    }

    // Fungsi untuk memuat data menu dari Firebase Database
    private fun loadMenuData() {
        val query1 = database.child("resep").orderByChild("menuType").equalTo("Makanan")
        val query2 = database.child("resep").orderByChild("menuType").equalTo("Camilan")
        val query3 = database.child("resep").orderByChild("menuType").equalTo("Minuman")

        // Mengambil data menu dari Firebase Database untuk setiap jenis menu
        query1.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val menuList1 = mutableListOf<MenuModel>()
                for (snapshot in dataSnapshot.children) {
                    val menu = snapshot.getValue(MenuModel::class.java)
                    val calorieContent = menu?.calorieContent?.toDoubleOrNull() ?: 0.0
                    if (calorieContent < 130) {
                        menuList1.add(menu!!)
                    }
                }
                Log.d("Query1", "MenuList1 size: ${menuList1.size}")
                menuAdapter1.setData(menuList1)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("Query1", "Database error: $databaseError")
                // Handle error
            }
        })

        query2.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val menuList2 = mutableListOf<MenuModel>()
                for (snapshot in dataSnapshot.children) {
                    val menu = snapshot.getValue(MenuModel::class.java)
                    val calorieContent = menu?.calorieContent?.toDoubleOrNull() ?: 0.0
                    if (calorieContent < 130) {
                        menuList2.add(menu!!)
                    }
                }
                Log.d("Query2", "MenuList2 size: ${menuList2.size}")
                menuAdapter2.setData(menuList2)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("Query2", "Database error: $databaseError")
                // Handle error
            }
        })

        query3.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val menuList3 = mutableListOf<MenuModel>()
                for (snapshot in dataSnapshot.children) {
                    val menu = snapshot.getValue(MenuModel::class.java)
                    if (menu != null) {
                        menuList3.add(menu)
                    }
                }
                Log.d("Query3", "MenuList3 size: ${menuList3.size}")
                menuAdapter3.setData(menuList3)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("Query3", "Database error: $databaseError")
                // Handle error
            }
        })
    }

    // Fungsi untuk menampilkan dialog upgrade
    private fun showUpgradeDialog() {
        // Inflate layout dialog dengan tampilan kustom
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.popup_upgrade, null)

        // Inisialisasi AlertDialogBuilder
        val dialogBuilder = AlertDialog.Builder(requireContext())
            .
            setView(dialogView)

        // Inisialisasi objek AlertDialog
        val alertDialog = dialogBuilder.create()

        // Set background drawable untuk membuat latar belakang dialog transparan
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // Dapatkan layout latar belakang dari dialog
        val bgPopup = dialogView.findViewById<LinearLayout>(R.id.bg_popup)

        // Set click listener untuk layout latar belakang untuk menutup dialog
        bgPopup.setOnClickListener {
            // Tutup dialog
            alertDialog.dismiss()
        }

        // Tampilkan dialog
        alertDialog.show()
    }
}
