package es.dam.booknest.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.dam.booknest.aplication.user.login.LoginCommand
import es.dam.booknest.aplication.user.login.LoginUseCase
import es.dam.booknest.infraestructure.user.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginState())
    val uiState = _uiState.asStateFlow()

    fun onUsernameChanged(username: String) {
        val error = if (username.isBlank()) "Username cannot be empty" else null
        _uiState.update { it.copy(username = username, usernameError = error) }
    }

    fun onPasswordChanged(password: String) {
        val error = if (password.isBlank()) "Password cannot be empty" else null
        _uiState.update { it.copy(password = password, passwordError = error) }
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    private fun validate(): Boolean {
        val state = _uiState.value
        val usernameError = if (state.username.isBlank()) "Required" else null
        val passwordError = if (state.password.isBlank()) "Required" else null

        _uiState.update {
            it.copy(
                usernameError = usernameError,
                passwordError = passwordError
            )
        }

        return usernameError == null && passwordError == null
    }

    fun login() {
        if (!validate()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, loginError = null) }

            val result = loginUseCase(
                LoginCommand(
                    username = _uiState.value.username,
                    password = _uiState.value.password
                )
            )

            result
                .onSuccess { token ->
                    SessionManager.saveTokens(
                        newAccessToken = token.accessToken,
                        newRefreshToken = token.refreshToken
                    )

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            loginSuccess = true
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            loginError = error.message ?: "Login failed"
                        )
                    }
                }
        }
    }
}