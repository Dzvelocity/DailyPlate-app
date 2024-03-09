package com.myappproj.healthapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.myappproj.healthapp.model.ItemModel
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myappproj.healthapp.adapter.HorizontalRecyclerView

class BerandaFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HorizontalRecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_beranda, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        val items = listOf(
            ItemModel(R.drawable.img_keluhan1, "Kolesterol"),
            ItemModel(R.drawable.img_keluhan2, "Diabetes"),
            ItemModel(R.drawable.img_keluhan3, "Pra Operasi"),
            ItemModel(R.drawable.img_keluhan4, "Hipertensi"),
        )
        adapter = HorizontalRecyclerView(items)

        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter

        val textBahan1 = view.findViewById<TextView>(R.id.all_1)
        val textBahan2 = view.findViewById<TextView>(R.id.all_2)

        textBahan1.setOnClickListener {
            // Navigasi ke BahanFragment1
            findNavController().navigate(R.id.action_homeFragment_to_bahanFragment1)
        }

        textBahan2.setOnClickListener {
            // Navigasi ke BahanFragment1
            findNavController().navigate(R.id.action_homeFragment_to_bahanFragment2)
        }

        return view
    }
}
