package com.myappproj.healthapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class BerandaFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_beranda, container, false)

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
