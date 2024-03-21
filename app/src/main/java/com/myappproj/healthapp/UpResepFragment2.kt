package com.myappproj.healthapp

import BahanInputView
import LangkahInputView
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

/**
 * Fragment untuk mengunggah resep bagian kedua dengan detail resep, bahan, langkah, dan gambar.
 */
class UpResepFragment2 : Fragment() {

    private lateinit var spinner1: Spinner
    private lateinit var isialat: TextInputLayout
    private lateinit var inputBahanRecyclerView: RecyclerView
    private lateinit var inputLangkahRecyclerView: RecyclerView
    private lateinit var btnUpload: Button
    private lateinit var btnAddBahan: TextView
    private lateinit var btnAddLangkah: TextView
    private lateinit var bahanAdapter: BahanInputView
    private lateinit var langkahAdapter: LangkahInputView
    private lateinit var auth: FirebaseAuth
    private val bahanList: MutableList<String> = mutableListOf()
    private val langkahList: MutableList<String> = mutableListOf()
    private var filePath: Uri? = null
    private var menuName: String = ""
    private var calorieContent: String = ""
    private var diseases: String = ""
    private var menuType: String = "Makanan" // Inisialisasi dengan nilai default

    /**
     * Metode untuk membuat tampilan fragment.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_up_resep2, container, false)
        auth = Firebase.auth
        spinner1 = view.findViewById(R.id.spinner1)
        isialat = view.findViewById(R.id.isialat)
        inputBahanRecyclerView = view.findViewById(R.id.recyclerView)
        inputLangkahRecyclerView = view.findViewById(R.id.recyclerView2)
        btnUpload = view.findViewById(R.id.btn_next2)
        btnAddBahan = view.findViewById(R.id.btn_bahan)
        btnAddLangkah = view.findViewById(R.id.btn_step)

        // Inisialisasi RecyclerView dan Adapter untuk input bahan dan langkah
        initRecyclerViews()

        // Inisialisasi Spinner
        initSpinner()

        // entri default untuk bahan dan langkah
        bahanList.add("")
        langkahList.add("")

        // Set listener untuk tombol tambah bahan
        btnAddBahan.setOnClickListener {
            val newItem = ""
            bahanList.add(newItem)
            bahanAdapter.notifyItemInserted(bahanList.size - 1)
        }

        // Set listener untuk tombol tambah langkah
        btnAddLangkah.setOnClickListener {
            val newItem = ""
            langkahList.add(newItem)
            langkahAdapter.notifyItemInserted(langkahList.size - 1)
        }
        return view
    }

    /**
     * Metode yang dipanggil setelah tampilan dibuat.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Mendapatkan data dari Bundle jika ada
        arguments?.let {
            // Ambil data yang disimpan di fragment pertama
            filePath = it.getParcelable("filePath")
            menuName = it.getString("menuName", "")
            calorieContent = it.getString("calorieContent", "")
            diseases = it.getString("diseases", "")
            menuType = it.getString("menuType", "Makanan") // Menambahkan menuType di sini
        }

        // Set listener untuk btnNext
        btnUpload.setOnClickListener {
            uploadDataToFirebase()
            showPopupDialog()
        }
    }

    /**
     * Metode untuk menginisialisasi RecyclerView untuk input bahan dan langkah.
     */
    private fun initRecyclerViews() {
        // Inisialisasi RecyclerView dan Adapter untuk input bahan
        bahanAdapter = BahanInputView(bahanList)
        inputBahanRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = bahanAdapter
        }

        // Inisialisasi RecyclerView dan Adapter untuk input langkah
        langkahAdapter = LangkahInputView(langkahList)
        inputLangkahRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = langkahAdapter
        }
    }

    /**
     * Metode untuk menginisialisasi Spinner.
     */
    private fun initSpinner() {
        val spinnerData = arrayOf("Makanan", "Minuman", "Camilan")
        val adapter = ArrayAdapter(requireContext(), R.layout.bg_spinner, spinnerData)
        spinner1.adapter = adapter

        spinner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                menuType = spinnerData[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle jika tidak ada item yang dipilih
            }
        }
    }

    /**
     * Metode untuk mengunggah data resep ke Firebase Storage dan Realtime Database.
     */
    private fun uploadDataToFirebase() {
        val alat = isialat.editText?.text.toString()
        val bahan = getBahanListFromAdapter()
        val langkah = getLangkahListFromAdapter()

        val userId = auth.currentUser?.uid

        // Simpan data ke Firebase Storage
        val storageRef = FirebaseStorage.getInstance().reference
        val imageRef = storageRef.child("images/${System.currentTimeMillis()}.jpg")

        filePath?.let { fileUri ->
            val uploadTask = imageRef.putFile(fileUri)

            uploadTask.addOnSuccessListener { _ ->
                // Gambar berhasil diunggah, dapatkan URL-nya
                imageRef.downloadUrl.addOnSuccessListener { imageUrl ->
                    // Dapatkan URL gambar yang diunggah
                    val imageURL = imageUrl.toString()

                    // Tambahkan URL gambar ke data resep
                    val resepData = hashMapOf(
                        "menuName" to menuName,
                        "calorieContent" to calorieContent,
                        "diseases" to diseases,
                        "menuType" to menuType,
                        "imageURL" to imageURL,
                        "alat" to alat,
                        "bahan" to bahan,
                        "langkah" to langkah,
                        "userId" to userId
                    )

                    // Simpan data resep ke database Firebase
                    val database = FirebaseDatabase.getInstance()
                    val ref = database.getReference("resep")
                    ref.push().setValue(resepData)
                        .addOnSuccessListener {
                            // Data berhasil diunggah
                            showToast("Data berhasil diunggah")
                        }
                        .addOnFailureListener { exception ->
                            // Data gagal diunggah
                            showToast("Gagal mengunggah data: ${exception.message}")
                        }
                }
            }
                .addOnFailureListener { exception ->
                    // Upload gambar gagal
                    showToast("Gagal mengunggah gambar: ${exception.message}")
                }
        } ?: showToast("Silakan pilih gambar terlebih dahulu")
    }

    /**
     * Metode untuk menampilkan dialog pop-up setelah berhasil mengunggah data resep.
     */
    private fun showPopupDialog() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.popup_upresep)

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // Mendapatkan referensi ke tombol "OK" di dalam popup
        val btnOk: Button = dialog.findViewById(R.id.btn_oke)

        // Menambahkan pendengar klik untuk tombol "OK"
        btnOk.setOnClickListener {
            // Menutup dialog
            dialog.dismiss()
            // Melakukan navigasi ke halaman baru (misalnya, HomeFragment2)
            findNavController().navigate(R.id.action_upResepFragment2_to_homeFragment2)
        }

        dialog.show()
    }

    /**
     * Metode untuk menampilkan pesan Toast.
     */
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    /**
     * Metode untuk mendapatkan daftar bahan dari adapter.
     */
    private fun getBahanListFromAdapter(): List<String> {
        val bahanList: MutableList<String> = mutableListOf()
        for (i in 0 until inputBahanRecyclerView.childCount) {
            val viewHolder = inputBahanRecyclerView.findViewHolderForAdapterPosition(i) as BahanInputView.ViewHolder
            val newItem = viewHolder.itemView.findViewById<TextInputLayout>(R.id.input_bahan).editText?.text.toString()
            if (newItem.isNotBlank()) {
                bahanList.add(newItem)
            }
        }
        return bahanList
    }

    /**
     * Metode untuk mendapatkan daftar langkah dari adapter.
     */
    private fun getLangkahListFromAdapter(): List<String> {
        val langkahList: MutableList<String> = mutableListOf()
        for (i in 0 until inputLangkahRecyclerView.childCount) {
            val viewHolder = inputLangkahRecyclerView.findViewHolderForAdapterPosition(i) as LangkahInputView.ViewHolder
            val newItem = viewHolder.itemView.findViewById<TextInputLayout>(R.id.input_langkah).editText?.text.toString()
            if (newItem.isNotBlank()) {
                langkahList.add(newItem)
            }
        }
        return langkahList
    }
}
