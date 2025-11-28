package com.example.gonzaloaliaga.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gonzaloaliaga.data.formstate.ProductFormState
import com.example.gonzaloaliaga.data.repository.ProductRepository
import com.example.gonzaloaliaga.data.model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductViewModel(private val repo: ProductRepository) : ViewModel() {

    // -----------------------------
    // LISTA DE PRODUCTOS (Flow del repo)
    // -----------------------------
    val productos = repo.productos

    init {
        viewModelScope.launch {
            repo.refrescar()
        }
    }

    fun refrescarDesdeUI() = viewModelScope.launch {
        repo.refrescar()
    }

    // -----------------------------
    // FORMULARIO
    // -----------------------------
    private val _form = MutableStateFlow(ProductFormState())
    val form: StateFlow<ProductFormState> = _form.asStateFlow()

    fun editar(product: Producto?) {
        _form.value = if (product == null) {
            ProductFormState()
        } else {
            ProductFormState(
                id = product.id,
                nombre = product.nombre,
                precio = product.precio,
                descripcion = product.descripcion,
                categoria = product.categoria,
                img = product.img
            )
        }
    }

    fun onNombreChange(v: String)        { _form.update { it.copy(nombre = v) } }
    fun onPrecioChange(v: Double)        { _form.update { it.copy(precio = v) } }
    fun onDescripcionChange(v: String)    { _form.update { it.copy(descripcion = v) } }
    fun onCategoriaChange(v: String)      { _form.update { it.copy(categoria = v) } }
    fun onImgChange(v: String)            { _form.update { it.copy(img = v) } }
    fun limpiarError()                    { _form.update { it.copy(error = null) } }

    // -----------------------------
    // GUARDAR (CREAR O EDITAR)
    // -----------------------------
    fun guardar(onSuccess: () -> Unit = {}) = viewModelScope.launch {
        try {
            val f = _form.value

            val precio = f.precio ?: throw IllegalArgumentException("Precio inválido")
            val nombre = f.nombre.trim()
            val descripcion = f.descripcion
            val categoria = f.categoria
            val img = f.img

            if (f.id == null) {
                // CREAR
                repo.agregar(
                    nombre = nombre,
                    precio = precio,
                    descripcion = descripcion,
                    categoria = categoria,
                    img = img
                )
            } else {
                // ACTUALIZAR
                repo.actualizar(
                    id = f.id,
                    nombre = nombre,
                    precio = precio,
                    descripcion = descripcion,
                    categoria = categoria,
                    img = img
                )
            }

            editar(null)
            onSuccess()

        } catch (e: Exception) {
            _form.update { it.copy(error = e.message ?: "Error desconocido") }
        }
    }

    // -----------------------------
    // ELIMINAR
    // -----------------------------
    fun eliminar(product: Producto) = viewModelScope.launch {
        try {
            product.id?.let { repo.eliminar(it) }
        } catch (e: Exception) {
            _form.update { it.copy(error = e.message ?: "Error desconocido") }
        }
    }

    // OBTENER UN PRODUCTO
    private val _productoSeleccionado = MutableStateFlow<Producto?>(null)
    val productoSeleccionado: StateFlow<Producto?> = _productoSeleccionado

    fun cargarProducto(id: String) {
        viewModelScope.launch {
            val p = repo.obtener(id)
            _productoSeleccionado.value = p
        }
    }
}
