package com.example.gonzaloaliaga.data.users

import androidx.room.*
import com.example.gonzaloaliaga.model.Usuario
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {

    @Query("SELECT * FROM usuarios ORDER BY id DESC")
    fun getAll(): Flow<List<Usuario>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(usuario: Usuario): Long

    @Update
    suspend fun update(usuario: Usuario)

    @Delete
    suspend fun delete(usuario: Usuario)

    @Query("SELECT * FROM usuarios WHERE id = :id LIMIT 1")
    suspend fun findById(id: Long): Usuario?

    @Query("SELECT * FROM usuarios WHERE nombre = :nombre LIMIT 1")
    suspend fun findByNombre(nombre: String): Usuario?

    @Query("SELECT * FROM usuarios WHERE rol = :rol LIMIT 1")
    suspend fun findByRol(rol: String): Usuario?
}