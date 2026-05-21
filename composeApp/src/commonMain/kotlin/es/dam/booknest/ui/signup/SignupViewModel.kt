package es.dam.booknest.ui.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.dam.booknest.aplication.user.signup.CreateUserCommand
import es.dam.booknest.aplication.user.signup.CreateUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignupViewModel(
    private val createUserUseCase: CreateUserUseCase
): ViewModel(){
    private val _uiState = MutableStateFlow(SignupState())
    val uiState = _uiState.asStateFlow()

    private val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    fun onUsernameChanged(username: String){
        val error = if (username.isBlank()) "El usuario no puede estar vacío" else null
        _uiState.update { it.copy(username = username, usernameError = error) }
    }

    fun onNameChanged(name: String){
        val error = if (name.isBlank()) "El nombre no puede estar vacío" else null
        _uiState.update { it.copy(name = name, nameError = error) }
    }

    fun onEmailChanged(email: String) {
        val error = when {
            email.isBlank() -> "El email no puede estar vacío"
            !emailRegex.matches(email) -> "Email no válido"
            else -> null
        }
        _uiState.update { it.copy(email = email, emailError = error) }
    }

    fun onPhoneChanged(phone: String){
        val error = if (phone.length != 9) "Teléfono no válido" else null
        _uiState.update { it.copy(phone = phone, phoneError = error) }
    }

    fun onPasswordChanged(password: String){
        _uiState.update { currentState ->
            val error = when {
                password.length < 6 -> "Mínimo 6 caracteres"
                currentState.confirmPassword.isNotEmpty() && password != currentState.confirmPassword -> "Las contraseñas no coinciden"
                else -> ""
            }
            currentState.copy(password = password, passwordError = error)
        }
    }

    fun onConfirmPasswordChanged(confirmPassword: String) {
        _uiState.update { currentState ->
            val error = if (confirmPassword != currentState.password) "Las contraseñas no coinciden" else ""
            currentState.copy(confirmPassword = confirmPassword, passwordError = error)
        }
    }

    private fun validateAll(): Boolean {
        val s = _uiState.value
        val uError = if (s.username.isBlank()) "Requerido" else null
        val nError = if (s.name.isBlank()) "Requerido" else null
        val eError = when {
            s.email.isBlank() -> "Requerido"
            !emailRegex.matches(s.email) -> "Email inválido"
            else -> null
        }
        val pError = if (s.password.length < 6) "Mínimo 6 caracteres" else if (s.password != s.confirmPassword) "No coinciden" else ""

        _uiState.update { 
            it.copy(
                usernameError = uError,
                nameError = nError,
                emailError = eError,
                passwordError = pError
            )
        }

        return uError == null && nError == null && eError == null && pError.isEmpty()
    }

    fun signup(){
        if (!validateAll()) return

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    signupError = null,
                    isSignupSuccess = false
                )
            }
            
            val currentState = _uiState.value

            val command = CreateUserCommand(
                username = currentState.username,
                name = currentState.name,
                email = currentState.email,
                phone = currentState.phone,
                password = currentState.password
            )
            val resultado = createUserUseCase(command)

            if (resultado.isSuccess){
                _uiState.update {
                    it.copy(
                        isSignupSuccess = true,
                        successMessage = resultado.getOrNull(),
                        signupError = null,
                        passwordError = ""
                    )
                }
            } else {
                val errormsg = resultado.exceptionOrNull()?.message ?: "Error desconocido"
                _uiState.update {
                    it.copy(
                        isSignupSuccess = false,
                        signupError = errormsg
                    )
                }
            }
        }
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun toggleConfirmPasswordVisibility() {
        _uiState.update { it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible) }
    }
}
