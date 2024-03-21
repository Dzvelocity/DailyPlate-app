package com.myappproj.healthapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.myappproj.healthapp.adapter.MyMenuView
import com.myappproj.healthapp.databinding.FragmentSearchHasilBinding
import com.myappproj.healthapp.model.MenuModel

/**
 * Fragment untuk menampilkan hasil pencarian dari database Firebase.
 */
class SearchHasilFragment : Fragment(), MyMenuView.MenuClickListener {

    private lateinit var binding: FragmentSearchHasilBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var searchResultAdapter: MyMenuView
    private lateinit var backArrow: ImageView
    private val searchResults: MutableList<MenuModel> = mutableListOf()

    /**
     * Metode untuk membuat tampilan fragment.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchHasilBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Metode yang dipanggil setelah tampilan dibuat.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        databaseReference = FirebaseDatabase.getInstance().reference.child("resep")

        setupRecyclerView()

        val query = arguments?.getString("query")
        val disease = arguments?.getString("disease")
        val menuType = arguments?.getString("menuType")
        if (!query.isNullOrEmpty()) {
            // Lakukan pencarian dengan query yang diberikan
            performSearch(query, disease, menuType)
        }

        // Dapatkan referensi SearchView dari layout
        val searchView = view.findViewById<SearchView>(R.id.searchview2)

        // Tambahkan listener untuk menangani event pencarian
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // Panggil metode performSearch dengan query yang diberikan saat pengguna menekan tombol cari
                performSearch(query, disease, menuType)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                // Tindakan tambahan yang ingin Anda lakukan saat pengguna mengetik teks pencarian
                return false
            }
        })

        backArrow = view.findViewById(R.id.back_arrow)

        backArrow.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    /**
     * Metode untuk menyiapkan RecyclerView.
     */
    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        binding.recyclerViewMenu.layoutManager = layoutManager
        searchResultAdapter = MyMenuView(this) // Inisialisasi adapter dengan MenuClickListener
        binding.recyclerViewMenu.adapter = searchResultAdapter
    }

    /**
     * Metode untuk melakukan pencarian di database Firebase.
     */
    private fun performSearch(query: String, disease: String?, menuType: String?) {
        // Buat query pencarian menggunakan Firebase Database Reference
        var searchQuery = databaseReference.orderByChild("menuName").startAt(query).endAt(query + "\uf8ff")

        // Filter berdasarkan penyakit jika disediakan
        if (!disease.isNullOrEmpty()) {
            searchQuery = searchQuery.orderByChild("diseases").equalTo(disease)
        }

        // Filter berdasarkan menuType jika disediakan
        if (!menuType.isNullOrEmpty()) {
            searchQuery = searchQuery.orderByChild("menuType").equalTo(menuType)
        }

        // Tambahkan listener untuk mendapatkan hasil pencarian
        searchQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Bersihkan hasil pencarian sebelumnya
                searchResults.clear()
                // Iterasi melalui hasil pencarian dan tambahkan ke daftar hasil pencarian
                for (menuSnapshot in snapshot.children) {
                    val menu = menuSnapshot.getValue(MenuModel::class.java)
                    menu?.let {
                        searchResults.add(it)
                    }
                }
                // Set data hasil pencarian ke adapter
                searchResultAdapter.setData(searchResults)
            }

            override fun onCancelled(error: DatabaseError) {
                // Tangani pembatalan
            }
        })
    }

    /**
     * Metode yang dipanggil ketika item menu diklik.
     */
    override fun onMenuClicked(menuName: String) {
        // Tangani kejadian klik menu di sini
        val action = R.id.action_searchHasilFragment_to_mainResepFragment
        val bundle = Bundle()
        bundle.putString("menuName", menuName)
        findNavController().navigate(action, bundle)
    }
}
