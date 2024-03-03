package com.myappproj.healthapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import android.widget.TextView


class SignUpFragment : Fragment() {

    lateinit var btnLoginAlt: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Correct access of TextView using findViewById
        btnLoginAlt = view.findViewById<TextView>(R.id.btn_loginalt)

        btnLoginAlt.setOnClickListener {
            val navController = findNavController()
            // Replace with the actual ID for your login fragment destination
            navController.navigate(R.id.action_signUpFragment_to_loginFragment)
        }
    }
}