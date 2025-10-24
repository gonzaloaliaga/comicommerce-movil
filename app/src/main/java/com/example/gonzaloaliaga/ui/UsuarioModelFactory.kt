package com.example.gonzaloaliaga.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gonzaloaliaga.data.AppDatabase
import com.example.gonzaloaliaga.data.users.UsuarioRepository
import kotlin.getValue

class UsuarioViewModelFactory(app: Application) : ViewModelProvider.Factory {

    private val repo: UsuarioRepository by lazy {
        val dao = AppDatabase.get(app).usuarioDao()
        UsuarioRepository(dao)
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UsuarioViewModel::class.java)) {
            return UsuarioViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}