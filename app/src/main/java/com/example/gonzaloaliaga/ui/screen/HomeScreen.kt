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
import com.example.gonzaloaliaga.ui.ProductViewModel
import com.example.gonzaloaliaga.ui.UsuarioViewModel

@Composable
fun HomeScreen(uservm: UsuarioViewModel, prodvm: ProductViewModel) {
    val user by uservm.currentUser.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Bienvenido, ${user?.nombre ?: "Usuario"}")

        if (user?.rol == "admin") {
            Button(onClick = { /* manejar productos */ }) { Text("Gestionar Productos") }
            Button(onClick = { /* manejar usuarios */ }) { Text("Gestionar Usuarios") }
        } else {
            Button(onClick = { /* ver productos */ }) { Text("Ver Productos") }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { uservm.logout() }) { Text("Cerrar sesión") }
    }
}