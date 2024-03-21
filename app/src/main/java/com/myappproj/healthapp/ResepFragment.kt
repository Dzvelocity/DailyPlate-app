package com.myappproj.healthapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2

class ResepFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_resep, container, false)

        val viewPager: ViewPager2 = view.findViewById(R.id.viewpager)

        // Atur adapter untuk ViewPager
        viewPager.adapter = ViewPagerAdapter(requireActivity())

        // Atur listener untuk button 1
        val button1: Button = view.findViewById(R.id.button1)
        button1.setOnClickListener {
            viewPager.setCurrentItem(0, true)  // Ganti ke fragment 1
        }

        // Atur listener untuk button 2
        val button2: Button = view.findViewById(R.id.button2)
        button2.setOnClickListener {
            viewPager.setCurrentItem(1, true)  // Ganti ke fragment 2
        }

        // Atur listener untuk ViewPager
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        button1.setBackgroundResource(R.drawable.bg_tabselected)
                        button2.setBackgroundResource(R.drawable.bg_tab_no_selected)

                        button1.typeface = resources.getFont(R.font.sf_medium)
                        button2.typeface = resources.getFont(R.font.sf_regular)

                        // Ubah teks TextView menjadi "Resep Makanan"
                        view.findViewById<TextView>(R.id.page_resep).text = "Resep Makanan"
                    }
                    1 -> {
                        button2.setBackgroundResource(R.drawable.bg_tabselected)
                        button1.setBackgroundResource(R.drawable.bg_tab_no_selected)

                        button2.typeface = resources.getFont(R.font.sf_medium)
                        button1.typeface = resources.getFont(R.font.sf_regular)

                        // Ubah teks TextView menjadi "Resep Saya"
                        view.findViewById<TextView>(R.id.page_resep).text = "Resep Saya"
                    }
                }
            }
        })

        return view
    }

    // Adapter untuk ViewPager
    private class ViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> TabResepFragment1()
                1 -> TabResepFragment2()
                else -> throw IllegalArgumentException("Invalid position: $position")
            }
        }
    }
}