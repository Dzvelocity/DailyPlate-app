package com.myappproj.healthapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth


class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi Firebase Auth
        val auth = FirebaseAuth.getInstance()

        // Access elements and maintain forgot password functionality
        val btnLogin = view.findViewById<Button>(R.id.btn_login2)
        val btnForgotPass = view.findViewById<TextView>(R.id.forgot_pass)

        btnForgotPass.setOnClickListener {
            val navController = findNavController()
            // Replace with the actual ID for your login fragment destination
            navController.navigate(R.id.action_loginFragment_to_forgotPassFragment2)
        }

        btnLogin.setOnClickListener {
            val email = view.findViewById<TextInputLayout>(R.id.email)?.editText?.text.toString()
            val sandi = view.findViewById<TextInputLayout>(R.id.sandi)?.editText?.text.toString()

            if (email.isEmpty() || sandi.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Silahkan isi data",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // Cek apakah email sudah terdaftar
            auth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Email sudah terdaftar
                        // Cek apakah passwordnya benar
                        auth.signInWithEmailAndPassword(email, sandi)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    // Login berhasil
                                    // Lakukan navigasi ke halaman selanjutnya
                                    val navController = findNavController()
                                    navController.navigate(R.id.action_loginFragment_to_homeFragment)
                                } else {
                                    // Password salah
                                    Toast.makeText(
                                        requireContext(),
                                        "Password salah!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    } else {
                        // Email belum terdaftar
                        Toast.makeText(
                            requireContext(),
                            "Email belum terdaftar!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }
}