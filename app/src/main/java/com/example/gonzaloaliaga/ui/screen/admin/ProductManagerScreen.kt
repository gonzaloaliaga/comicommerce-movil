package com.example.gonzaloaliaga.ui.screen.admin

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gonzaloaliaga.model.Producto
import com.example.gonzaloaliaga.data.products.ProductViewModel
import com.example.gonzaloaliaga.data.users.UsuarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductManagerScreen(uservm: UsuarioViewModel, prodvm: ProductViewModel, navController: NavController) {
    val user by uservm.currentUser.collectAsState()
    val selectedSection = remember { mutableStateOf("crear") } // "crear", "modificar", "eliminar"
    val productos by prodvm.productos.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Administrar productos") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("home") }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver al menú")
                    }
                }
            )
        }
    ) { padding ->

        Column(modifier = Modifier.padding(padding).padding(16.dp)) {

            // Row de botones para seleccionar sección
            val scrollState = rememberScrollState()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState),
                horizontalArrangement = Arrangement.spacedBy(8.dp) // espacio entre botones
            ) {
                Button(
                    onClick = { selectedSection.value = "crear" },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedSection.value == "crear") Color.Gray else MaterialTheme.colorScheme.primary
                    )
                ) { Text("Crear Producto") }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = { selectedSection.value = "modificar" },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedSection.value == "modificar") Color.Gray else MaterialTheme.colorScheme.primary
                    )
                ) { Text("Modificar Producto") }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = { selectedSection.value = "eliminar" },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedSection.value == "eliminar") Color.Gray else MaterialTheme.colorScheme.primary
                    )
                ) { Text("Eliminar Producto") }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Secciones según selección
            when (selectedSection.value) {
                "crear" -> CrearProductoSection(prodvm)
                "modificar" -> ModificarProductoSection(prodvm, productos)
                "eliminar" -> EliminarProductoSection(prodvm, productos)
            }
        }
    }
}

// Sección de creación
@Composable
fun CrearProductoSection(prodvm: ProductViewModel) {
    val form by prodvm.form.collectAsState()

    Column {
        TextField(
            value = form.nombre,
            onValueChange = { prodvm.onNombreChange(it) },
            label = { Text("Nombre") }
        )
        TextField(
            value = form.precio?.toString() ?: "",
            onValueChange = { prodvm.onPrecioChange(it.toDoubleOrNull() ?: 0.0) },
            label = { Text("Precio") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        TextField(
            value = form.descripcion,
            onValueChange = { prodvm.onDescripcionChange(it) },
            label = { Text("Descripción") }
        )
        TextField(
            value = form.categoria,
            onValueChange = { prodvm.onCategoriaChange(it) },
            label = { Text("Categoría") }
        )

        // Mostrar error directamente desde el ViewModel
        form.error?.let {
            Spacer(Modifier.height(8.dp))
            Text(it, color = Color.Red)
        }

        Spacer(modifier = Modifier.height(8.dp))

        val context = LocalContext.current
        Button(onClick = {
            prodvm.guardar()
            Toast.makeText(context, "Producto agregado", Toast.LENGTH_SHORT).show()
        }) {
            Text("Guardar Producto")
        }
    }
}

// Sección de modificación
@Composable
fun ModificarProductoSection(prodvm: ProductViewModel, productos: List<Producto>) {
    val form by prodvm.form.collectAsState()
    Column {
        LazyColumn {
            items(productos) { producto ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { prodvm.editar(producto) }
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(producto.nombre, style = MaterialTheme.typography.titleMedium)
                        Text("Precio: $${producto.precio}")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Form para editar producto seleccionado
        if (form.id != null) {
            Text("Editar Producto Seleccionado")
            TextField(
                value = form.nombre,
                onValueChange = { prodvm.onNombreChange(it) },
                label = { Text("Nombre") }
            )
            TextField(
                value = form.precio?.toString() ?: "",
                onValueChange = { prodvm.onPrecioChange(it.toDoubleOrNull() ?: 0.0) },
                label = { Text("Precio") }
            )
            TextField(
                value = form.descripcion,
                onValueChange = { prodvm.onDescripcionChange(it) },
                label = { Text("Descripción") }
            )
            TextField(
                value = form.categoria,
                onValueChange = { prodvm.onCategoriaChange(it) },
                label = { Text("Categoría") }
            )
            form.error?.let {
                Spacer(Modifier.height(8.dp))
                Text(it, color = Color.Red)
            }

            Spacer(modifier = Modifier.height(8.dp))

            val context = LocalContext.current
            Button(onClick = {
                prodvm.guardar()
                Toast.makeText(context, "Producto actualizado", Toast.LENGTH_SHORT).show()
            }) {
                Text("Actualizar Producto")
            }
        }
    }
}

// Sección de eliminación
@Composable
fun EliminarProductoSection(prodvm: ProductViewModel, productos: List<Producto>) {
    LazyColumn {
        items(productos) { producto ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(producto.nombre, style = MaterialTheme.typography.titleMedium)
                        Text("Precio: $${producto.precio}", style = MaterialTheme.typography.bodyMedium)
                    }

                    val context = LocalContext.current
                    Button(onClick = {
                        prodvm.eliminar(producto)
                        Toast.makeText(context, "Producto eliminado", Toast.LENGTH_SHORT).show()
                    }) {
                        Text("Eliminar")
                    }
                }
            }
        }
    }
}
