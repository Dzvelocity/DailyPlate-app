package com.myappproj.healthapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage


class UpResepFragment1 : Fragment() {

    private val PICK_IMAGE_REQUEST = 1
    private var filePath: Uri? = null
    private lateinit var storageReference: FirebaseStorage
    private lateinit var databaseReference: FirebaseDatabase
    private lateinit var imageView: ImageView
    private lateinit var uploadDescTextView: TextView
    private lateinit var isimenu: TextInputLayout
    private lateinit var isikalori: TextInputLayout
    private lateinit var isipenyakit: TextInputLayout
    private lateinit var spinner1: Spinner

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_up_resep1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi Firebase Storage dan Realtime Database
        storageReference = FirebaseStorage.getInstance()
        databaseReference = FirebaseDatabase.getInstance()

        // Inisialisasi views
        imageView = view.findViewById(R.id.pick_image)
        uploadDescTextView = view.findViewById(R.id.upload_desc)
        isimenu = view.findViewById(R.id.isimenu)
        isikalori = view.findViewById(R.id.isikalori)
        isipenyakit = view.findViewById(R.id.isipenyakit)
        spinner1 = view.findViewById(R.id.spinner1)

        // Inisialisasi Spinner
        val spinner1 = view.findViewById<Spinner>(R.id.spinner1)
        val spinnerData = arrayOf("Makanan", "Minuman", "Camilan")
        val adapter = ArrayAdapter(requireContext(), R.layout.bg_spinner, spinnerData)
        spinner1.adapter = adapter

        spinner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = spinnerData[position]
                // Lakukan sesuatu dengan item yang dipilih
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle jika tidak ada item yang dipilih
            }
        }

        // Atur listener untuk ImageView (pick_image)
        imageView.setOnClickListener {
            pickImageFromGallery()
        }

        // Atur listener untuk Button btn_next
        val buttonNext: Button = view.findViewById(R.id.btn_next)
        buttonNext.setOnClickListener {
            if (filePath != null) {
                uploadDataToFirebase()
                findNavController().navigate(R.id.action_upResepFragment1_to_upResepFragment2)
            } else {
                showToast("Silahkan upload gambar")
            }
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            filePath = data.data
            // Mengambil nama file dari URI
            val fileName = getFileName(filePath)
            // Menampilkan nama file di TextView
            uploadDescTextView.text = fileName
        }
    }

    private fun getFileName(uri: Uri?): String {
        var result: String? = null
        if (uri?.scheme == "content") {
            val cursor = requireContext().contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    result = it.getString(it.getColumnIndexOrThrow("_display_name"))
                }
            }
        }
        if (result == null) {
            result = uri?.path
            val cut = result?.lastIndexOf('/')
            if (cut != -1) {
                result = result?.substring(cut!! + 1)
            }
        }
        return result ?: ""
    }

    private fun uploadDataToFirebase() {
        if (filePath != null) {
            // Mendapatkan nilai dari TextInputLayout
            val menuName = isimenu.editText?.text.toString()
            val calorieContent = isikalori.editText?.text.toString()
            val diseases = isipenyakit.editText?.text.toString()
            val menuType = spinner1.selectedItem.toString()

            // Memasukkan data ke dalam struktur JSON
            val menuData = hashMapOf(
                "menuName" to menuName,
                "calorieContent" to calorieContent,
                "diseases" to diseases,
                "menuType" to menuType
            )

            // Mendapatkan referensi Firebase Storage
            val ref = storageReference.reference.child("images/${System.currentTimeMillis()}.jpg")

            ref.putFile(filePath!!)
                .addOnSuccessListener { taskSnapshot ->
                    // Jika berhasil mengunggah gambar, dapatkan URL gambar yang diunggah
                    ref.downloadUrl.addOnSuccessListener { uri ->
                        val imageURL = uri.toString()

                        // Tambahkan URL gambar ke data yang akan diunggah ke Realtime Database
                        menuData["imageURL"] = imageURL

                        // Mengirim data ke Firebase Realtime Database
                        val menuRef = databaseReference.getReference("menus").push()
                        menuRef.setValue(menuData)
                            .addOnSuccessListener {
                                // Berhasil mengirim data
                                showToast("Data berhasil dikirim")
                            }
                            .addOnFailureListener { e ->
                                // Gagal mengirim data
                                showToast("Gagal mengirim data: ${e.message}")
                            }
                    }
                }
                .addOnFailureListener {
                    // Gagal mengunggah gambar
                    showToast("Upload gambar gagal")
                }
        } else {
            showToast("Silakan pilih gambar terlebih dahulu")
        }
    }

    private fun showToast(message: String) {
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(requireContext(), message, duration)
        toast.show()
    }
}

