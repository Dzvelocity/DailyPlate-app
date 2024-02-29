package com.myappproj.healthapp

import android.content.Context
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

        // Create fade-in animation
        val fadeIn = AlphaAnimation(0f, 1f)
        fadeIn.duration = 1000 // Adjust duration as needed

        Handler(Looper.getMainLooper()).postDelayed({

            // Perform navigation or other actions
            if (onBoardingIsFinished()) {
                findNavController().navigate(R.id.navigate_splashFragment_to_onBoardingFragment)
            } else {
                findNavController().navigate(R.id.action_splashFragment_to_onBoardingFragment)
            }
        }, 2000) // Adjust delay as needed

        // Apply animation to views
        titleSplash.startAnimation(fadeIn)
        imgSplash.startAnimation(fadeIn)

        return view
    }


    private fun onBoardingIsFinished(): Boolean {

        val context = requireActivity().applicationContext
        val sharedPreferences = context.getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("finished", false)
    }
}
