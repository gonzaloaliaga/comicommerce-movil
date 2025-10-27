package com.example.gonzaloaliaga.data.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gonzaloaliaga.model.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UsuarioViewModel(private val repo: UsuarioRepository): ViewModel() {

    val usuarios: StateFlow<List<Usuario>> =
        repo.usuarios.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _form = MutableStateFlow(UserFormState())
    val form: StateFlow<UserFormState> = _form.asStateFlow()

    fun editar(usuario: Usuario?) {
        _form.value = if (usuario == null) {
            UserFormState()
        } else {
            UserFormState(
                id = usuario.id,
                nombre = usuario.nombre,
                password = usuario.password,
                rol = usuario.rol
            )
        }
    }

    fun onNombreChange(v: String) { _form.update { it.copy(nombre = v) } }
    fun onPasswordChange(v: String) { _form.update { it.copy(password = v) } }
    fun onRolChange(v: String) { _form.update { it.copy(rol = v) } }
    fun limpiarError() { _form.update { it.copy(error = null) } }

    fun guardar(oAlFinal: () -> Unit = {}) = viewModelScope.launch {
        try {
            val f = _form.value
            if (f.id == null) {
                repo.agregar(f.nombre, f.password, f.rol)
            } else {
                repo.actualizar(f.id, f.nombre, f.password, f.rol)
            }
            editar(null)
            oAlFinal()
        } catch (e: Exception) {
            _form.update { it.copy(error = e.message ?: "Error desconocido") }
        }
    }

    fun eliminar(usuario: Usuario) = viewModelScope.launch {
        repo.eliminar(usuario)
    }

    private val _currentUser = MutableStateFlow<Usuario?>(null)
    val currentUser: StateFlow<Usuario?> = _currentUser.asStateFlow()

    fun login(onSuccess: () -> Unit = {}) = viewModelScope.launch {
        try {
            val f = _form.value
            val usuario = repo.login(f.nombre.trim(), f.password)
                ?: throw IllegalArgumentException("Usuario o contraseña inválidos")
            _currentUser.value = usuario
            _form.update { UserFormState() }
            onSuccess()
        } catch (e: Exception) {
            _form.update { it.copy(error = e.message ?: "Error desconocido") }
        }
    }

    fun logout() { _currentUser.value = null }
}