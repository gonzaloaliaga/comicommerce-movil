package com.example.gonzaloaliaga.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gonzaloaliaga.data.products.ProductViewModel
import com.example.gonzaloaliaga.data.users.UsuarioViewModel

@Composable
fun HomeScreen(uservm: UsuarioViewModel, prodvm: ProductViewModel, navController: NavController) {
    val user by uservm.currentUser.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Bienvenido, ${user?.nombre ?: "Usuario"}")

        if (user?.rol == "admin") { // Vista Admin
            Button(onClick = { navController.navigate("catalog") }) { Text("Ver Productos") }
            Button(onClick = { navController.navigate("cart") }) { Text("Ver Carrito") }
            Button(onClick = { navController.navigate("about") }) { Text("Sobre nosotros") }
            Button(onClick = { navController.navigate("adminScreen") }) { Text("Ir a panel de admin") }
        } else { // Vista Cliente
            Button(onClick = { navController.navigate("catalog") }) { Text("Ver Productos") }
            Button(onClick = { navController.navigate("cart") }) { Text("Ver Carrito") }
            Button(onClick = { navController.navigate("about") }) { Text("Sobre nosotros") }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            uservm.logout()
            navController.navigate("login")
        }) { Text("Cerrar sesión") }
    }
}