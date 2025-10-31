package com.example.gonzaloaliaga.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gonzaloaliaga.data.cart.CarritoConProducto
import com.example.gonzaloaliaga.data.repository.CarritoRepository
import com.example.gonzaloaliaga.model.Producto
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CarritoViewModel(
    private val repository: CarritoRepository,
    private val uservm: UsuarioViewModel
) : ViewModel() {

    private val _carrito = MutableStateFlow<List<CarritoConProducto>>(emptyList())
    val carrito: StateFlow<List<CarritoConProducto>> = _carrito.asStateFlow()

    private var carritoJob: Job? = null

    init {
        viewModelScope.launch {
            uservm.currentUser.collect { user ->
                // Cancelar la colección anterior si existía
                carritoJob?.cancel()
                _carrito.value = emptyList() // limpiar carrito anterior

                if (user != null) {
                    carritoJob = viewModelScope.launch {
                        repository.obtenerCarrito(user.id).collect { items ->
                            _carrito.value = items
                        }
                    }
                }
            }
        }
    }

    fun agregar(producto: Producto) = viewModelScope.launch {
        val user = uservm.currentUser.value ?: return@launch
        repository.agregarAlCarrito(user.id, producto.id)
    }

    fun restar(producto: Producto) = viewModelScope.launch {
        val user = uservm.currentUser.value ?: return@launch
        repository.restarDelCarrito(user.id, producto.id)
    }

    fun eliminar(producto: Producto) = viewModelScope.launch {
        val user = uservm.currentUser.value ?: return@launch
        repository.eliminar(user.id, producto.id)
    }

    fun vaciar() = viewModelScope.launch {
        val user = uservm.currentUser.value ?: return@launch
        repository.vaciar(user.id)
    }

    fun total(): Double = carrito.value.sumOf { it.precio * it.cantidad }
}