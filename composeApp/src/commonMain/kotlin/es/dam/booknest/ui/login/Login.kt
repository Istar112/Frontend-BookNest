package es.dam.booknest.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Login(){
    var loginState by remember { mutableStateOf(LoginState()) }

    Box(
        Modifier.fillMaxSize().safeContentPadding()
    ){
        // Botón de Sign Up
        TextButton(
            onClick = { /* registrarse */ },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Text("Sign Up")
        }

        // centro
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = "LOGIN",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Field usuario
            OutlinedTextField(
                value = loginState.username,
                onValueChange = { loginState = loginState.copy(username = it) },
                label = { Text("Username") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // Field contraseña
            OutlinedTextField(
                value = loginState.password,
                onValueChange = { loginState = loginState.copy(password = it) },
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            // Botón LOGIN
            Button(
                onClick = { /* loguearse */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Text("LOGIN")
            }
        }

        // Botón de salir
        TextButton(
            onClick = { /* salir */ },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Text("Exit")
        }
    }
}
