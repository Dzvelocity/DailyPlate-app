package com.myappproj.healthapp.model

data class MenuModel(
    val menuName: String = "",
    val calorieContent: String = "",
    val alat: String = "",
    val imageURL: String = "",
    val diseases: String? = "",
    val bahan: List<String> = emptyList(),
    val langkah: List<String> = emptyList(),
    val menuType: String = "",
    val userId: String = ""
)
