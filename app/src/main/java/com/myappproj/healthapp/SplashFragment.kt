package com.myappproj.healthapp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController


class SplashFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_splash, container, false)

        val titleSplash = view.findViewById<TextView>(R.id.hello)
        val imgSplash = view.findViewById<ImageView>(R.id.imageView2)

        // animasi fade
        val fadeIn = AlphaAnimation(0f, 1f)
        fadeIn.duration = 1000

        Handler(Looper.getMainLooper()).postDelayed({
            findNavController().navigate(R.id.action_splashFragment_to_onBoardingFragment)
        }, 2000) // Adjust delay as needed

        // Apply animasi ke view
        titleSplash.startAnimation(fadeIn)
        imgSplash.startAnimation(fadeIn)

        return view
    }
}
