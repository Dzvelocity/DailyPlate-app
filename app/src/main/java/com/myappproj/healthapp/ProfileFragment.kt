package com.myappproj.healthapp

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.myappproj.healthapp.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate layout untuk fragment ini menggunakan data binding
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Ambil informasi pengguna dari Google Sign-In
        val account = GoogleSignIn.getLastSignedInAccount(requireActivity())

        // Set TextView dengan informasi pengguna jika ada
        account?.let {
            val username = it.displayName ?: ""
            val email = it.email ?: ""
            binding.tvName.text = username
            binding.tvEmail.text = email

            // Ambil URL foto profil jika tersedia dan tampilkan menggunakan Glide
            val photoUrl = it.photoUrl
            photoUrl?.let { url ->
                Glide.with(requireContext())
                    .load(url)
                    .circleCrop()
                    .into(binding.ppProfil2) // Menggunakan ID pp_profil2
            }
        }

        // Set OnClickListener pada tombol logout
        binding.btnLogout.setOnClickListener {
            showLogoutConfirmationDialog()
        }

        // Set OnClickListener pada tombol untuk menuju ke halaman ganti sandi
        binding.gantiSandi.setOnClickListener {
            navigateToGantiSandi()
        }
    }

    // Menampilkan dialog konfirmasi logout
    private fun showLogoutConfirmationDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.popup_logout, null)
        builder.setView(dialogView)
        val alertDialog = builder.create()

        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnYes = dialogView.findViewById<Button>(R.id.btn_yes)
        val btnNo = dialogView.findViewById<Button>(R.id.btn_no)

        // Set OnClickListener pada tombol Yes untuk logout
        btnYes.setOnClickListener {
            // Handle logout
            alertDialog.dismiss()
            navigateToLogin()
        }

        // Set OnClickListener pada tombol No untuk membatalkan logout
        btnNo.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    // Navigasi ke halaman login
    private fun navigateToLogin() {
        // Navigasi ke LoginFragment menggunakan NavController
        findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
    }

    // Navigasi ke halaman ganti sandi
    private fun navigateToGantiSandi() {
        // Navigasi ke GantiSandiFragment menggunakan NavController
        findNavController().navigate(R.id.action_homeFragment_to_gantiSandiFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Hapus binding saat view dihancurkan untuk mencegah memory leak
        _binding = null
    }
}
