package es.dam.booknest.vista.signup

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * El formulario para el registro la ventana (solamente)
 */
@Composable
fun Signup(

){
    Box(
        Modifier.fillMaxSize().safeContentPadding(),
        contentAlignment = Alignment.Center

    ){
        Column {
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("Username") },
            )
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("Name") },
            )
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("email") },
            )
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("phone") },
            )
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("password ") },
            )
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("password ") },
            )
        }

    }
}



