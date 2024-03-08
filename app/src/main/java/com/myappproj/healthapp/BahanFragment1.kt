package com.myappproj.healthapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController

class BahanFragment1 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_bahan1, container, false)
        val textBahan1 = view.findViewById<ImageView>(R.id.back_arrow)

        textBahan1.setOnClickListener {
            // Navigasi ke BahanFragment1
            findNavController().navigate(R.id.action_bahanFragment1_to_homeFragment2)
        }

        return view
    }
}
