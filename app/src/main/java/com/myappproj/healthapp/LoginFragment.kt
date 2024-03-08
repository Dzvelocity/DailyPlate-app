package com.myappproj.healthapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.content.Intent
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


class LoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        // Configure Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        val btnLogin = view.findViewById<Button>(R.id.btn_login2)
        val btnForgotPass = view.findViewById<TextView>(R.id.forgot_pass)
        val btnGoogle = view.findViewById<Button>(R.id.btn_google)
        val btnDaftarAlt = view.findViewById<TextView>(R.id.btn_daftaralt)

        btnForgotPass.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.action_loginFragment_to_forgotPassFragment2)
        }

        btnDaftarAlt.setOnClickListener {
            val navController = findNavController()
            // Replace with the actual ID for your login fragment destination
            navController.navigate(R.id.action_loginFragment_to_signUpFragment2)
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

        btnGoogle.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN, null)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign-In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                // Google Sign-In failed
                Toast.makeText(requireContext(), "Google sign in failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount?) {
        // Use Firebase Authentication to authenticate with Google
        val credential = GoogleAuthProvider.getCredential(acct?.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, navigate to home fragment
                    val navController = findNavController()
                    navController.navigate(R.id.action_loginFragment_to_homeFragment)
                } else {
                    // Sign in failed
                    Toast.makeText(requireContext(), "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    companion object {
        const val RC_GOOGLE_SIGN_IN = 9001
    }
}