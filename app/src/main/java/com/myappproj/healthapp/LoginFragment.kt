package com.myappproj.healthapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

/**
 * Fragment untuk proses masuk pengguna menggunakan email/sandi atau Google Sign-In.
 */
class LoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    /**
     * Metode untuk membuat tata letak fragment.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    /**
     * Metode yang dipanggil setelah tampilan dibuat.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        // Konfigurasi Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        val btnLogin = view.findViewById<Button>(R.id.btn_login2)
        val btnForgotPass = view.findViewById<TextView>(R.id.forgot_pass)
        val btnGoogle = view.findViewById<Button>(R.id.btn_google)
        val btnDaftarAlt = view.findViewById<TextView>(R.id.btn_daftaralt)

        // Set listener untuk tombol "Lupa kata sandi?"
        btnForgotPass.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.action_loginFragment_to_forgotPassFragment2)
        }

        // Set listener untuk tombol "Daftar"
        btnDaftarAlt.setOnClickListener {
            val navController = findNavController()
            // Ganti dengan ID aktual untuk tujuan fragment pendaftaran
            navController.navigate(R.id.action_loginFragment_to_signUpFragment2)
        }

        // Set listener untuk tombol "Login"
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

            // Periksa apakah email sudah terdaftar
            auth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Email sudah terdaftar
                        // Periksa apakah sandi benar
                        auth.signInWithEmailAndPassword(email, sandi)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    // Login berhasil
                                    // Lakukan navigasi ke halaman selanjutnya
                                    val navController = findNavController()
                                    navController.navigate(R.id.action_loginFragment_to_homeFragment)
                                } else {
                                    // Sandi salah
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

        // Set listener untuk tombol "Login dengan Google"
        btnGoogle.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN, null)
        }
    }

    /**
     * Metode yang dipanggil ketika hasil aktivitas diharapkan, seperti hasil masuk Google.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign-In berhasil, autentikasi dengan Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                // Google Sign-In gagal
                Toast.makeText(requireContext(), "Google sign in failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Metode untuk melakukan autentikasi Firebase dengan Google Sign-In.
     */
    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount?) {
        // Gunakan Firebase Authentication untuk melakukan autentikasi dengan Google
        val credential = GoogleAuthProvider.getCredential(acct?.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Masuk berhasil, navigasi ke fragment beranda
                    val navController = findNavController()
                    navController.navigate(R.id.action_loginFragment_to_homeFragment)
                } else {
                    // Masuk gagal
                    Toast.makeText(requireContext(), "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    companion object {
        const val RC_GOOGLE_SIGN_IN = 9001
    }
}
