package com.example.gonzaloaliaga

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gonzaloaliaga.ui.ProductViewModel
import com.example.gonzaloaliaga.ui.ProductViewModelFactory
import com.example.gonzaloaliaga.ui.UsuarioViewModel
import com.example.gonzaloaliaga.ui.UsuarioViewModelFactory
import com.example.gonzaloaliaga.ui.screen.HomeScreen
import com.example.gonzaloaliaga.ui.screen.LoginScreen
// import com.example.gonzaloaliaga.ui.screen.RegisterScreen


class MainActivity : ComponentActivity() {

    private val uservm: UsuarioViewModel by viewModels { UsuarioViewModelFactory(application) }
    private val prodvm: ProductViewModel by viewModels { ProductViewModelFactory(application) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(navController, startDestination = "login") {

                composable("login") {
                    LoginScreen(uservm) {
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true } // elimina login del backstack
                        }
                    }
                }
                /*
                composable("register") {
                    RegisterScreen(uservm) {
                        navController.navigate("home") {
                            popUpTo("register") { inclusive = true }
                        }
                    }
                }
                */
                composable("home") {
                    HomeScreen(uservm, prodvm)
                }
            }
        }
    }
}