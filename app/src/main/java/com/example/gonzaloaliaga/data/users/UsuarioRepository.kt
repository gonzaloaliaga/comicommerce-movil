package com.example.gonzaloaliaga.data.users

import com.example.gonzaloaliaga.model.Usuario
import kotlinx.coroutines.flow.Flow

class UsuarioRepository(private val dao: UsuarioDao) {

    val usuarios: Flow<List<Usuario>> = dao.getAll()

    suspend fun agregar(nombre: String, password: String, rol: String) {
        require(nombre.isNotBlank()) { "El nombre no puede estar vacío" }
        require(password.isNotBlank()) { "La contraseña no puede estar vacía" }
        require(rol.isNotBlank()) { "El rol no puede estar vacío" }

        val exist = dao.findByNombre(nombre)
        require(exist == null) { "Ya existe un usuario con este nombre" }

        dao.insert(Usuario(nombre = nombre.trim(), password = password, rol = rol.trim()))
    }

    suspend fun actualizar(id: Long, nombre: String, password: String, rol: String) {
        require(id > 0) { "Id inválido" }
        require(nombre.isNotBlank()) { "El nombre no puede estar vacío" }
        require(password.isNotBlank()) { "La contraseña no puede estar vacía" }
        require(rol.isNotBlank()) { "El rol no puede estar vacío" }

        dao.update(Usuario(id = id, nombre = nombre.trim(), password = password, rol = rol.trim()))
    }

    suspend fun eliminar(usuario: Usuario) = dao.delete(usuario)
    suspend fun obtener(id: Long) = dao.findById(id)
    suspend fun obtenerPorNombre(nombre: String) = dao.findByNombre(nombre)
    suspend fun obtenerPorRol(rol: String) = dao.findByRol(rol)
    suspend fun login(nombre: String, password: String): Usuario? {
        val usuario = dao.findByNombre(nombre)
        return if (usuario != null && usuario.password == password) usuario else null
    }
}