package com.example.gonzaloaliaga.data.repository

import com.example.gonzaloaliaga.data.api.ProductApi
import com.example.gonzaloaliaga.data.model.Producto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow

class ProductRepository(private val api: ProductApi) {

    // Obtener TODOS los productos (manejo de HATEOAS)
    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    val productos: StateFlow<List<Producto>> = _productos

    // --- Obtener todos los productos desde la API ---
    suspend fun refrescar() {
        val resp = api.getAll()
        val lista = resp._embedded?.productoList ?: emptyList()
        _productos.value = lista
    }

    suspend fun agregar(
        nombre: String,
        precio: Double,
        descripcion: String,
        categoria: String,
        img: String
    ): Producto {

        require(nombre.isNotBlank()) { "El nombre no puede estar vacío" }
        require(precio > 0) { "El precio debe ser mayor a 0" }
        require(descripcion.isNotBlank()) { "La descripción no puede estar vacía" }
        require(categoria.isNotBlank()) { "La categoría no puede estar vacía" }
        require(img.isNotBlank()) { "La imagen no puede estar vacía" }

        val nuevo = Producto(
            nombre = nombre.trim(),
            precio = precio,
            descripcion = descripcion,
            categoria = categoria,
            img = img
        )

        val creado = api.create(nuevo)
        refrescar()
        return creado
    }

    suspend fun actualizar(
        id: String,
        nombre: String,
        precio: Double,
        descripcion: String,
        categoria: String,
        img: String
    ): Producto {

        require(id.isNotBlank()) { "ID inválido" }
        require(nombre.isNotBlank()) { "El nombre no puede estar vacío" }
        require(precio > 0) { "El precio debe ser mayor a 0" }
        require(descripcion.isNotBlank()) { "La descripción no puede estar vacía" }
        require(categoria.isNotBlank()) { "La categoría no puede estar vacía" }
        require(img.isNotBlank()) { "La imagen no puede estar vacía" }

        val actualizado = Producto(
            id = id,
            nombre = nombre.trim(),
            precio = precio,
            descripcion = descripcion,
            categoria = categoria,
            img = img
        )

        val resultado = api.update(id, actualizado)
        refrescar()
        return resultado
    }

    suspend fun eliminar(id: String) {
        require(id.isNotBlank()) { "ID inválido" }
        api.delete(id)
        refrescar()
    }

    suspend fun obtener(id: String): Producto {
        require(id.isNotBlank()) { "ID inválido" }
        return api.getById(id)
    }

    suspend fun obtenerPorCategoria(categoria: String): List<Producto> {
        require(categoria.isNotBlank()) { "La categoría no puede estar vacía" }

        val result = api.getAll()
        val list = result._embedded?.productoList ?: emptyList()

        return list.filter { it.categoria.equals(categoria, ignoreCase = true) }
    }
}