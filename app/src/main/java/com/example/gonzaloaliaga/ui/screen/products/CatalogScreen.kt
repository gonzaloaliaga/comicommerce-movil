package com.example.gonzaloaliaga.ui.screen.products

import com.example.gonzaloaliaga.ui.components.ProductCard
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gonzaloaliaga.data.products.ProductViewModel
import com.example.gonzaloaliaga.data.users.UsuarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(uservm: UsuarioViewModel, prodvm: ProductViewModel, navController: NavController) {
    val user by uservm.currentUser.collectAsState()
    val productos by prodvm.productos.collectAsState()
    val productosPorCategoria = productos.groupBy { it.categoria }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Catálogo de productos") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("home") }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver al menú")
                    }
                }
            )
        }
    ) { padding ->

        LazyColumn(modifier = Modifier.padding(padding).padding(16.dp)) {
            productosPorCategoria.forEach { (categoria, lista) ->
                item {
                    Text(
                        text = categoria,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                items(lista) { producto ->
                    ProductCard(producto, navController)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

    }
}