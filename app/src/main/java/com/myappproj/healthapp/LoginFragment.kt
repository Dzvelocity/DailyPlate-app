package com.myappproj.healthapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController


class LoginFragment : Fragment() {
    lateinit var btnForgotPass: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Correct access of TextView using findViewById
        btnForgotPass = view.findViewById<TextView>(R.id.forgot_pass)

        btnForgotPass.setOnClickListener {
            val navController = findNavController()
            // Replace with the actual ID for your login fragment destination
            navController.navigate(R.id.action_loginFragment_to_forgotPassFragment2)
        }
    }
}