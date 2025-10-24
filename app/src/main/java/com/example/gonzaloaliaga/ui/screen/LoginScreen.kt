package com.example.gonzaloaliaga.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.gonzaloaliaga.ui.UsuarioViewModel

@Composable
fun LoginScreen(uservm: UsuarioViewModel, onLoginSuccess: () -> Unit) {
    val form by uservm.form.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = form.nombre,
            onValueChange = { uservm.onNombreChange(it) },
            label = { Text("Nombre") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = form.password,
            onValueChange = { uservm.onPasswordChange(it) },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation()
        )
        form.error?.let { Text(it, color = Color.Red) }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { uservm.login(onLoginSuccess) }) {
            Text("Ingresar")
        }
    }
}