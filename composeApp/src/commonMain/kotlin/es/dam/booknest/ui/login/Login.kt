package es.dam.booknest.ui.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.dam.booknest.ui.theme.ErrorRed
import es.dam.booknest.ui.theme.InkBlack
import es.dam.booknest.ui.theme.LeatherBrown
import es.dam.booknest.ui.theme.OldPaper
import es.dam.booknest.ui.theme.SoftPaper
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun Login(
    modifier: Modifier = Modifier,
    onLoginSuccess: () -> Unit,
    onGoToRegister: () -> Unit
) {
    val vm: LoginViewModel = koinViewModel()
    val uiState by vm.uiState.collectAsState()

    var showErrorDialog by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.loginSuccess) {
        if (uiState.loginSuccess) {
            onLoginSuccess()
        }
    }

    LaunchedEffect(uiState.loginError) {
        if (uiState.loginError != null) {
            showErrorDialog = true
            delay(2500)
            showErrorDialog = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(OldPaper)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .safeContentPadding(),
            contentAlignment = Alignment.Center
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(bottom = 24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoStories,
                            contentDescription = null,
                            tint = LeatherBrown,
                            modifier = Modifier.size(56.dp)
                        )
                        Text(
                            text = "WELCOME BACK",
                            style = TextStyle(
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = InkBlack,
                                letterSpacing = 1.sp
                            )
                        )
                        Text(
                            text = "Continue your reading journey",
                            style = TextStyle(
                                fontSize = 14.sp,
                                color = LeatherBrown.copy(alpha = 0.8f),
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                }

                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(SoftPaper)
                            .border(
                                1.5.dp,
                                LeatherBrown.copy(alpha = 0.2f),
                                RoundedCornerShape(16.dp)
                            )
                            .padding(24.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

                            OutlinedTextField(
                                value = uiState.username,
                                onValueChange = { vm.onUsernameChanged(it) },
                                label = { Text("Username") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                textStyle = TextStyle(color = InkBlack),
                                isError = uiState.usernameError != null,
                                supportingText = {
                                    uiState.usernameError?.let { Text(it) }
                                },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = LeatherBrown,
                                    unfocusedBorderColor = LeatherBrown.copy(alpha = 0.4f),
                                    focusedTextColor = InkBlack,
                                    unfocusedTextColor = InkBlack,
                                    focusedLabelColor = LeatherBrown,
                                    unfocusedLabelColor = LeatherBrown.copy(alpha = 0.6f),
                                    errorLabelColor = ErrorRed,
                                    errorSupportingTextColor = ErrorRed,
                                    cursorColor = LeatherBrown
                                )
                            )

                            OutlinedTextField(
                                value = uiState.password,
                                onValueChange = { vm.onPasswordChanged(it) },
                                label = { Text("Password") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                textStyle = TextStyle(color = InkBlack),
                                visualTransformation = if (uiState.isPasswordVisible) {
                                    VisualTransformation.None
                                } else {
                                    PasswordVisualTransformation()
                                },
                                isError = uiState.passwordError != null,
                                supportingText = {
                                    uiState.passwordError?.let { Text(it) }
                                },
                                trailingIcon = {
                                    IconButton(onClick = { vm.togglePasswordVisibility() }) {
                                        Icon(
                                            imageVector = if (uiState.isPasswordVisible) {
                                                Icons.Default.VisibilityOff
                                            } else {
                                                Icons.Default.Visibility
                                            },
                                            contentDescription = null,
                                            tint = LeatherBrown
                                        )
                                    }
                                },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = LeatherBrown,
                                    unfocusedBorderColor = LeatherBrown.copy(alpha = 0.4f),
                                    focusedTextColor = InkBlack,
                                    unfocusedTextColor = InkBlack,
                                    focusedLabelColor = LeatherBrown,
                                    unfocusedLabelColor = LeatherBrown.copy(alpha = 0.6f),
                                    errorLabelColor = ErrorRed,
                                    errorSupportingTextColor = ErrorRed,
                                    cursorColor = LeatherBrown
                                )
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Button(
                                onClick = { vm.login() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                shape = RoundedCornerShape(12.dp),
                                enabled = !uiState.isLoading,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = LeatherBrown,
                                    contentColor = Color.White,
                                    disabledContainerColor = LeatherBrown.copy(alpha = 0.3f),
                                    disabledContentColor = Color.White.copy(alpha = 0.5f)
                                )
                            ) {
                                Text(
                                    if (uiState.isLoading) "SIGNING IN..." else "LOGIN",
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            OutlinedButton(
                                onClick = { onGoToRegister() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, LeatherBrown.copy(alpha = 0.5f))
                            ) {
                                Text("CREATE ACCOUNT", color = LeatherBrown)
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(20.dp)) }
            }
        }

        if (showErrorDialog) {
            AlertDialog(
                onDismissRequest = { showErrorDialog = false },
                containerColor = OldPaper,
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Error, null, tint = ErrorRed, modifier = Modifier.size(28.dp))
                        Spacer(Modifier.padding(horizontal = 8.dp))
                        Text("LOGIN ERROR", color = ErrorRed, fontWeight = FontWeight.Bold)
                    }
                },
                text = {
                    Text(
                        uiState.loginError ?: "Something went wrong while logging in.",
                        color = InkBlack
                    )
                },
                confirmButton = {
                    TextButton(onClick = { showErrorDialog = false }) {
                        Text("RETRY", color = LeatherBrown, fontWeight = FontWeight.Bold)
                    }
                },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.border(2.dp, ErrorRed, RoundedCornerShape(16.dp))
            )
        }
    }
}