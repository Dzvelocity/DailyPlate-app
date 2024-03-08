package com.myappproj.healthapp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.google.firebase.auth.FirebaseAuth
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController


class SplashFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_splash, container, false)

        auth = FirebaseAuth.getInstance()

        val titleSplash = view.findViewById<TextView>(R.id.hello)
        val imgSplash = view.findViewById<ImageView>(R.id.imageView2)

        val fadeIn = AlphaAnimation(0f, 1f)
        fadeIn.duration = 1000

        Handler(Looper.getMainLooper()).postDelayed({
            // Cek apakah pengguna sudah login
            if (auth.currentUser != null) {
                // Jika sudah login, navigasikan ke HomeFragment
                findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
            } else {
                // Jika belum login, navigasikan ke OnBoardingFragment
                findNavController().navigate(R.id.action_splashFragment_to_onBoardingFragment)
            }
        }, 2000)

        titleSplash.startAnimation(fadeIn)
        imgSplash.startAnimation(fadeIn)

        return view
    }
}