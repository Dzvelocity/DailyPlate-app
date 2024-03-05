package com.myappproj.healthapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable

class ForgotPassFragment : Fragment() {

    private lateinit var emailInputLayout: TextInputLayout
    private lateinit var btnKirim: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_forgot_pass, container, false)

        emailInputLayout = view.findViewById(R.id.email)
        btnKirim = view.findViewById(R.id.btn_kirim)

        btnKirim.setOnClickListener {
            val email = emailInputLayout.editText?.text.toString()

            // Check if email is empty
            if (email.isNullOrEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Please enter your email address!",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // Send password reset email using Firebase
            sendPasswordResetEmail(email)
        }

        return view
    }

    private fun sendPasswordResetEmail(email: String) {
        val auth = FirebaseAuth.getInstance()

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Password reset email sent successfully, show custom pop-up
                    showSuccessPopup()
                } else {
                    // Handle the failure if needed
                }
            }
    }

    private fun showSuccessPopup() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.popup_forgotpass)

        val closeButton = dialog.findViewById<Button>(R.id.btn_oke)
        closeButton.setOnClickListener {
            dialog.dismiss()
        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }
}