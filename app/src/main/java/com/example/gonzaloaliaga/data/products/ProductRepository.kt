package com.example.gonzaloaliaga.data.products
import com.example.gonzaloaliaga.model.Producto
import com.example.gonzaloaliaga.model.Usuario
import kotlinx.coroutines.flow.Flow

class ProductRepository(private val dao: ProductoDao) {

    val productos: Flow<List<Producto>> = dao.getAll()

    suspend fun agregar(nombre: String, precio: Double, descripcion: String, categoria: String) {
        require(nombre.isNotBlank()) { "El nombre no puede estar vacío" }
        require(precio >= 0) { "El precio no puede ser negativo" }
        require(nombre.isNotBlank()) { "El nombre no puede estar vacío" }
        dao.insert(Producto(nombre = nombre.trim(), precio = precio, descripcion = descripcion, categoria = categoria))
    }

    suspend fun actualizar(id: Long, nombre: String, precio: Double, descripcion: String, categoria: String) {
        require(id > 0) { "Id inválido" }
        require(nombre.isNotBlank()) { "El nombre no puede estar vacío" }
        require(precio >= 0) { "El precio no puede ser negativo" }
        require(descripcion.isNotBlank()) { "La descripción no puede estar vacía" }
        require(categoria.isNotBlank()) { "La categoría no puede estar vacía" }
        dao.update(Producto(id = id, nombre = nombre.trim(), precio = precio, descripcion = descripcion, categoria = categoria))
    }

    suspend fun eliminar(product: Producto) = dao.delete(product)
    suspend fun obtener(id: Long) = dao.findById(id)
    suspend fun obtenerPorCategoria(categoria: String) = dao.findByCategoria(categoria)
}
