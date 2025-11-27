package com.example.gonzaloaliaga.ui.screen.products

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gonzaloaliaga.viewmodel.CarritoViewModel
import com.example.gonzaloaliaga.viewmodel.ProductViewModel
import com.example.gonzaloaliaga.viewmodel.UsuarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    uservm: UsuarioViewModel,
    prodvm: ProductViewModel,
    cartvm: CarritoViewModel,
    navController: NavController,
    productId: String
) {
    val productos by prodvm.productos.collectAsState()
    val producto = productos.find { it.id == productId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(producto?.nombre ?: "Producto no encontrado") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("catalog") }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                    }
                }
            )
        }
    ) { padding ->

        producto?.let { p ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize()
            ) {

                // Imagen
                androidx.compose.foundation.Image(
                    painter = coil.compose.rememberAsyncImagePainter(p.img),
                    contentDescription = p.nombre,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )

                Spacer(Modifier.height(16.dp))
                Text(p.nombre, style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(8.dp))
                Text("Precio: $${p.precio}", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                Text("Categoría: ${p.categoria}")
                Spacer(Modifier.height(8.dp))
                Text(p.descripcion)
                Spacer(Modifier.height(16.dp))

                val context = LocalContext.current

                Button(
                    onClick = {
                        cartvm.agregar(p)
                        Toast.makeText(
                            context,
                            "${p.nombre} agregado al carrito",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Agregar al carrito")
                }
            }
        } ?: Box(
            Modifier.fillMaxSize().padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Text("Producto no encontrado", color = Color.Gray)
        }
    }
}
