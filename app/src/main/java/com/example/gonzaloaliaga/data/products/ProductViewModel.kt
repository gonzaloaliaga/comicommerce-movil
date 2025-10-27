package com.example.gonzaloaliaga.data.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gonzaloaliaga.model.Producto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductViewModel(private val repo: ProductRepository): ViewModel() {

    // Lista observable desde la UI
    val productos: StateFlow<List<Producto>> =
        repo.productos.stateIn(viewModelScope, SharingStarted.Companion.WhileSubscribed(5_000), emptyList())

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
                categoria = product.categoria
            )
        }
    }

    fun onNombreChange(v: String) { _form.update { it.copy(nombre = v) } }
    fun onPrecioChange(v: Double) { _form.update { it.copy(precio = v) } }

    fun onDescripcionChange(v: String) { _form.update { it.copy(descripcion = v) } }

    fun onCategoriaChange(v: String) { _form.update { it.copy(categoria = v) } }
    fun limpiarError() { _form.update { it.copy(error = null) } }

    fun guardar(oAlFinal: () -> Unit = {}) = viewModelScope.launch {
        try {
            val f = _form.value
            val precio = f.precio ?: throw IllegalArgumentException("Precio inválido")
            val descripcion = f.descripcion
            val categoria = f.categoria

            if (f.id == null) {
                repo.agregar(f.nombre, precio, descripcion, categoria)
            } else {
                repo.actualizar(f.id, f.nombre, precio, descripcion, categoria)
            }
            editar(null)
            oAlFinal()
        } catch (e: Exception) {
            _form.update { it.copy(error = e.message ?: "Error desconocido") }
        }
    }

    fun eliminar(product: Producto) = viewModelScope.launch {
        repo.eliminar(product)
    }
}