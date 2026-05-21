package es.dam.booknest

import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import es.dam.booknest.ui.signup.Signup

import es.dam.booknest.ui.home.Home

/**
 * En esta  clase tenemos  un navHost para la navegación de la aplicación,
 * será la que se encargue de navegar entre las pantallas.
 *
 * 1. La aplicación empieza en home o en login segun el token
 * 2. Si no hay token, se muestra el login
 * 3. Si hay token, se muestra el home
 * 4. Si se pulsa el botón de registro, se muestra el registro
 * 5. Si se pulsa el botón de volver, se muestra el login de nuevo
 *
 * Las rutas se encuentran en /ui/AppRoutes.kt
 */
@Composable
fun App() {
    // navController para controlar la navegación
    val navController = rememberNavController()

    // navHost para controlar la navegación
    val navHost = NavHost(navController, startDestination = "register") {
        composable("login") {
            // login
        }
        composable("register") {
            Signup(
                onBack = {
                    navController.navigate("home") {
                        popUpTo("register") { inclusive = true }
                    }
                })
        }
        composable("home") {
            Home()
        }

    }


}