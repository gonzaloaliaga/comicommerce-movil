package com.example.gonzaloaliaga.data.products

data class ProductFormState(
    val id: Long? = null,
    val nombre: String = "",
    val precio: Double = 0.0,
    val descripcion: String = "",
    val categoria: String = "",
    val error: String? = null
)