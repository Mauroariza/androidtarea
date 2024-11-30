package com.example.aplicacion.models

data class CartItem(val id: Int, val name: String, val price: Double, val imageUrl: String, var quantity: Int = 1)