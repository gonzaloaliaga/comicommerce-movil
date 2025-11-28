package com.example.gonzaloaliaga.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.gonzaloaliaga.viewmodel.CarritoViewModel
import com.example.gonzaloaliaga.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingCartScreen(
    cartvm: CarritoViewModel,
    prodvm: ProductViewModel,
    navController: NavController
) {
    val carrito by cartvm.carrito.collectAsState()
    val productos by prodvm.productos.collectAsState()

    if (carrito == null) {
        Scaffold { padding ->
            Box(
                modifier = Modifier.padding(padding).fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Cargando carrito...")
            }
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tu Carrito") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("home") }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->

        Column(modifier = Modifier.padding(padding).padding(16.dp)) {

            if (carrito!!.items.isEmpty()) {
                Text("Tu carrito está vacío.")
                return@Column
            }

            LazyColumn {

                items(carrito!!.items) { itemCarrito ->

                    val producto = productos.find { it.id == itemCarrito.productoId }

                    if (producto == null) {
                        Text("Producto no encontrado: ${itemCarrito.productoId}")
                        return@items
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            // Imagen con COIL
                            AsyncImage(
                                model = producto.img,
                                contentDescription = producto.nombre,
                                modifier = Modifier
                                    .size(80.dp)
                                    .padding(4.dp)
                            )

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(producto.nombre, style = MaterialTheme.typography.titleMedium)
                                Text("Cantidad: ${itemCarrito.cantidad}")
                                Text("Total: $${producto.precio * itemCarrito.cantidad}")
                            }

                            Column {

                                IconButton(
                                    onClick = { cartvm.agregar(producto.id!!) }
                                ) {
                                    Icon(Icons.Default.KeyboardArrowUp, "Sumar")
                                }

                                IconButton(
                                    onClick = { cartvm.disminuir(producto.id!!) }
                                ) {
                                    Icon(Icons.Default.KeyboardArrowDown, "Restar")
                                }

                                IconButton(
                                    onClick = { cartvm.disminuir(producto.id!!) }
                                ) {
                                    Icon(Icons.Default.Delete, "Eliminar")
                                }
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            val total = carrito!!.items.sumOf { item ->
                val p = productos.find { it.id == item.productoId }
                if (p != null) p.precio * item.cantidad else 0.0
            }

            Text("Total: $${total}", style = MaterialTheme.typography.titleLarge)

            Spacer(Modifier.height(8.dp))

            Button(onClick = { cartvm.vaciar() }) {
                Text("Vaciar Carrito")
            }
        }
    }
}