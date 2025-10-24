package com.example.gonzaloaliaga.ui

data class UserFormState(
    val id: Long? = null,
    val nombre: String = "",
    val password: String = "",
    val rol: String = "",
    val error: String? = null
)