package com.example.gonzaloaliaga.data.products


import androidx.room.*
import com.example.gonzaloaliaga.model.Producto
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoDao {

    @Query("SELECT * FROM productos ORDER BY id DESC")
    fun getAll(): Flow<List<Producto>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(product: Producto): Long

    @Update
    suspend fun update(product: Producto)

    @Delete
    suspend fun delete(product: Producto)

    @Query("SELECT * FROM productos WHERE id = :id LIMIT 1")
    suspend fun findById(id: Long): Producto?

    @Query("SELECT * FROM productos WHERE categoria = :categoria")
    suspend fun findByCategoria(categoria: String): Producto?
}
