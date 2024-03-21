package com.myappproj.healthapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.myappproj.healthapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate layout untuk fragment ini menggunakan data binding
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Mengganti fragment dengan BerandaFragment saat fragment dibuat
        replaceFragment(BerandaFragment())

        // Mengatur listener untuk bottom navigation view
        binding.bottomnavigation.setOnItemSelectedListener { menuItem ->
            // Mengganti fragment berdasarkan item yang dipilih
            when (menuItem.itemId) {
                R.id.home -> replaceFragment(BerandaFragment())
                R.id.profile -> replaceFragment(ProfileFragment())
                R.id.search -> replaceFragment(SearchFragment())
                R.id.resep -> replaceFragment(ResepFragment())
                else -> {
                    // Do nothing for other menu items
                }
            }
            true
        }
    }

    // Fungsi untuk mengganti fragment di container fragment
    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = childFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fl_wrapper, fragment)
        fragmentTransaction.commit()
    }
}
