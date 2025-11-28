package com.example.gonzaloaliaga.ui.screen.products

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gonzaloaliaga.ui.components.ProductCard
import com.example.gonzaloaliaga.viewmodel.CarritoViewModel
import com.example.gonzaloaliaga.viewmodel.ProductViewModel
import com.example.gonzaloaliaga.viewmodel.UsuarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    uservm: UsuarioViewModel,
    prodvm: ProductViewModel,
    cartvm: CarritoViewModel,
    navController: NavController
) {
    LaunchedEffect(Unit) {
        prodvm.refrescarDesdeUI()
    }

    val productos by prodvm.productos.collectAsState()
    val productosPorCategoria = productos.groupBy { it.categoria }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Catálogo de productos") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("home") }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                    }
                }
            )
        }
    ) { padding ->

        LazyColumn(Modifier.padding(padding).padding(16.dp)) {

            productosPorCategoria.forEach { (categoria, lista) ->
                item {
                    Text(
                        text = categoria,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                items(lista) { producto ->
                    ProductCard(producto, cartvm, navController)
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}
