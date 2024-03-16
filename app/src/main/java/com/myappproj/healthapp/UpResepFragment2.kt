package com.myappproj.healthapp

import BahanInputView
import LangkahInputView
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.FirebaseDatabase

class UpResepFragment2 : Fragment() {

    private lateinit var spinner1: Spinner
    private lateinit var isialat: TextInputLayout
    private lateinit var inputBahanRecyclerView: RecyclerView
    private lateinit var inputLangkahRecyclerView: RecyclerView
    private lateinit var btnNext: Button
    private lateinit var btnAddBahan: TextView
    private lateinit var btnAddLangkah: TextView
    private lateinit var bahanAdapter: BahanInputView
    private lateinit var langkahAdapter: LangkahInputView
    private val bahanList: MutableList<String> = mutableListOf()
    private val langkahList: MutableList<String> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_up_resep2, container, false)
        spinner1 = view.findViewById(R.id.spinner1)
        isialat = view.findViewById(R.id.isialat)
        inputBahanRecyclerView = view.findViewById(R.id.recyclerView)
        inputLangkahRecyclerView = view.findViewById(R.id.recyclerView2)
        btnNext = view.findViewById(R.id.btn_next)
        btnAddBahan = view.findViewById(R.id.btn_bahan)
        btnAddLangkah = view.findViewById(R.id.btn_step)

        // Inisialisasi RecyclerView dan Adapter untuk input bahan dan langkah
        initRecyclerViews()

        // Inisialisasi Spinner
        initSpinner()

        btnNext.setOnClickListener {
            uploadDataToFirebase()
            findNavController().navigate(R.id.action_upResepFragment2_to_homeFragment)
        }

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
                // Lakukan sesuatu dengan item yang dipilih
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle jika tidak ada item yang dipilih
            }
        }
    }

    private fun uploadDataToFirebase() {
        val selectedMenuType = spinner1.selectedItem.toString()
        val alat = isialat.editText?.text.toString()
        val bahan = getBahanListFromAdapter()
        val langkah = getLangkahListFromAdapter()

        // Simpan data ke Firebase Database
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("resep")

        val resepData = hashMapOf(
            "menuType" to selectedMenuType,
            "alat" to alat,
            "bahan" to bahan,
            "langkah" to langkah
        )

        ref.push().setValue(resepData)
    }

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


