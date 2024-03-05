package com.myappproj.healthapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import android.widget.Button
import com.google.android.material.textfield.TextInputLayout
import android.text.InputType

class SignUpFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        val btnDaftar = view.findViewById<Button>(R.id.btn_daftar2)
        val btnLoginAlt = view.findViewById<TextView>(R.id.btn_loginalt)

        btnDaftar.setOnClickListener {
            // Panggil fungsi untuk melakukan pendaftaran
            registerUser()
        }

        btnLoginAlt.setOnClickListener {
            val navController = findNavController()
            // Replace with the actual ID for your login fragment destination
            navController.navigate(R.id.action_signUpFragment_to_loginFragment)
        }

        }

    private fun registerUser() {
        val nama = view?.findViewById<TextInputLayout>(R.id.nama)?.editText?.text.toString()
        val email = view?.findViewById<TextInputLayout>(R.id.email)?.editText?.text.toString()
        val nomorTelepon = view?.findViewById<TextInputLayout>(R.id.no_telepon)?.editText?.text.toString()
        val sandi = view?.findViewById<TextInputLayout>(R.id.sandi)?.editText?.text.toString()
        val konfirmasiSandi = view?.findViewById<TextInputLayout>(R.id.konf_sandi)?.editText?.text.toString()


        // Validasi input
        if (nama.isEmpty() || email.isEmpty() || nomorTelepon.isEmpty() || sandi.isEmpty() || konfirmasiSandi.isEmpty()) {
            showToast("Semua kolom harus diisi")
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showToast("Format email tidak valid")
            return
        }

        if (sandi.length < 8) {
            showToast("Kata sandi harus memiliki setidaknya 8 karakter")
            return
        }

        // Lakukan pendaftaran ke Firebase
        auth.createUserWithEmailAndPassword(email, sandi)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Pendaftaran berhasil, arahkan ke fragment beranda
                    findNavController().navigate(R.id.action_signUpFragment_to_homeFragment)
                    showToast("Pendaftaran berhasil")
                } else {
                    // Pendaftaran gagal, beri tahu pengguna atau lakukan tindakan yang sesuai
                    showToast("Email telah terdaftar")
                }
            }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
