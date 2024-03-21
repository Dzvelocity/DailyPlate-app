package com.myappproj.healthapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myappproj.healthapp.adapter.NewsAdapter
import com.myappproj.healthapp.model.Article
import com.myappproj.healthapp.model.News
import com.myappproj.healthapp.util.NewsApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HealthNewsFragment : Fragment(), NewsItemClickListener { // Implement the interface

    private lateinit var adapter: NewsAdapter
    private val newsList = mutableListOf<Article>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_health_news, container, false)

        // Setup RecyclerView
        val rvNews = view.findViewById<RecyclerView>(R.id.rvNews)  // Ganti dengan ID RecyclerView yang sesuai
        adapter = NewsAdapter(requireContext(), newsList) // Kirim "this" sebagai listener
        rvNews.adapter = adapter

        // Set LayoutManager
        val layoutManager = LinearLayoutManager(context)  // Anda dapat menggunakan LayoutManagers lain seperti GridLayoutManager
        rvNews.layoutManager = layoutManager

        // Mengambil data dari API
        NewsApiClient.newsApi.getTopHeadlines("id", "health", "41f9dd9c57974d0dbf869693eb81e16a")
            .enqueue(object : Callback<News> {
                override fun onResponse(call: Call<News>, response: Response<News>) {
                    if (response.isSuccessful) {
                        val news = response.body()
                        if (news != null) {
                            newsList.clear()
                            newsList.addAll(news.articles)
                            adapter.notifyDataSetChanged()
                        }
                    }
                }

                override fun onFailure(call: Call<News>, t: Throwable) {
                    Log.e("HealthNewsFragment", "Gagal mengambil data", t)
                }
            })

        // Set up tombol kembali
        val backButton = view.findViewById<ImageView>(R.id.back_arrow)
        backButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        return view
    }

    // Implementasi metode onNewsItemClicked sesuai yang dibutuhkan oleh interfacenya
    override fun onNewsItemClicked(url: String?) {
        if (url != null) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            requireActivity().startActivity(intent)  // Gunakan konteks aktivitas fragmen
        }
    }
}

// Definisi antarmuka (jika belum didefinisikan di tempat lain)
interface NewsItemClickListener {
    fun onNewsItemClicked(url: String?)
}
