package com.myappproj.healthapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.myappproj.healthapp.model.MenuModel
import jp.wasabeef.glide.transformations.RoundedCornersTransformation


class MainResepFragment : Fragment() {

    private lateinit var namaMakanan: TextView
    private lateinit var jmlKalori: TextView
    private lateinit var penyakit: TextView
    private lateinit var listAlat: TextView
    private lateinit var listBahan: TextView
    private lateinit var listLangkah: TextView
    private lateinit var imgResep: ImageView
    private lateinit var backArrow: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main_resep, container, false)

        namaMakanan = view.findViewById(R.id.nama_makanan)
        jmlKalori = view.findViewById(R.id.jml_kalori)
        penyakit = view.findViewById(R.id.penyakit)
        listAlat = view.findViewById(R.id.listalat)
        listBahan = view.findViewById(R.id.listbahan)
        listLangkah = view.findViewById(R.id.listlangkah)
        imgResep = view.findViewById(R.id.img_resep)
        backArrow = view.findViewById(R.id.back_arrow)

        backArrow.setOnClickListener {
            findNavController().popBackStack()
        }

        // Mengambil data dari Firebase berdasarkan nama makanan yang dikirim dari TabResepFragment2
        val menuName = arguments?.getString("menuName")
        val ref = FirebaseDatabase.getInstance().getReference("resep")
        ref.orderByChild("menuName").equalTo(menuName)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (menuSnapshot in snapshot.children) {
                            val menu = menuSnapshot.getValue(MenuModel::class.java)
                            menu?.let {
                                namaMakanan.text = it.menuName
                                jmlKalori.text = it.calorieContent
                                penyakit.text = it.diseases
                                listAlat.text = it.alat
                                listBahan.text = it.bahan.joinToString(", ")
                                listLangkah.text = it.langkah.joinToString(", ")
                                Glide.with(requireContext())
                                    .load(it.imageURL)
                                    .placeholder(R.drawable.imgview_resep) // Placeholder image
                                    .centerCrop() // Center crop
                                    .transform(
                                        RoundedCornersTransformation(80, 0,
                                            RoundedCornersTransformation.CornerType.TOP)
                                    )
                                    .into(imgResep)
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle errors
                }
            })

        return view
    }
}


