package com.example.gonzaloaliaga.data.users

data class UserFormState(
    val id: Long? = null,
    val nombre: String = "",
    val password: String = "",
    val rol: String = "",
    val error: String? = null
)