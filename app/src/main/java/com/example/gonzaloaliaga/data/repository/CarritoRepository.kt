package com.example.gonzaloaliaga.data.repository

import com.example.gonzaloaliaga.data.cart.CarritoConProducto
import com.example.gonzaloaliaga.data.cart.CartItemEntity
import com.example.gonzaloaliaga.data.dao.CartDao
import kotlinx.coroutines.flow.Flow

class CarritoRepository(private val dao: CartDao) {

    fun obtenerCarrito(usuarioId: Long): Flow<List<CarritoConProducto>> =
        dao.getCarrito(usuarioId)

    suspend fun agregarAlCarrito(usuarioId: Long, productoId: Long) {
        val existente = dao.findItem(usuarioId, productoId)
        if (existente != null) {
            dao.update(existente.copy(cantidad = existente.cantidad + 1))
        } else {
            dao.insert(
                CartItemEntity(
                    id = 0,
                    usuarioId = usuarioId,
                    productoId = productoId,
                    cantidad = 1
                )
            )
        }
    }

    suspend fun restarDelCarrito(usuarioId: Long, productoId: Long) {
        val existente = dao.findItem(usuarioId, productoId)
        if (existente != null) {
            if (existente.cantidad > 1) {
                dao.update(existente.copy(cantidad = existente.cantidad - 1))
            } else {
                dao.delete(existente)
            }
        }
    }

    suspend fun eliminar(usuarioId: Long, productoId: Long) {
        val existente = dao.findItem(usuarioId, productoId)
        if (existente != null) dao.delete(existente)
    }

    suspend fun vaciar(usuarioId: Long) {
        dao.limpiarCarrito(usuarioId)
    }
}