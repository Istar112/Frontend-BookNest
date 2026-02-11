package es.dam.booknest

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import es.dam.booknest.ui.login.Login
import es.dam.booknest.ui.signup.Signup

@Composable
fun App() {
    MaterialTheme {
        // tiene que haber una vista de login y otra de registro
        //Signup()
        Login()
        }
    }