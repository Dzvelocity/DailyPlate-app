package com.myappproj.healthapp.model

data class MenuModel2(
    val menuName: String? = "",
    val alat: String = "",
    val bahan: List<String> = emptyList(),
    val imageURL: String? = "",
    val menuType: String? = "",
    val langkah: List<String> = emptyList()
)
