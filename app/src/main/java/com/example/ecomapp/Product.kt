package com.example.ecomapp

data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    val weight: String,
    val imageResId: Int = 0,
    val sellerName: String = "",
    val phoneNumber: String = "",
    val date: String = "",
    val title: String = "",
    val description: String = "",
    val imageUrl: String = ""
)