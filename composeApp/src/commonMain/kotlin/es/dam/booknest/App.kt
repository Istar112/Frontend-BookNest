package es.dam.booknest

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import es.dam.booknest.vista.signup.Signup

@Composable
@Preview
fun App() {
    MaterialTheme {
        // tiene que haber una vista de login y otra de registro
        Signup()
        }
    }