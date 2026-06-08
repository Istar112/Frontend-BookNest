package es.dam.booknest.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.dam.booknest.aplication.user.profile.GetProfileUseCase
import es.dam.booknest.aplication.user.profile.UpdateProfileUseCase
import es.dam.booknest.infraestructure.user.SessionExpiredException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getProfileUseCase: GetProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileState())
    val uiState = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    error = null,
                    successMessage = null,
                    sessionExpired = false
                )
            }

            getProfileUseCase()
                .onSuccess { username ->
                    _uiState.update {
                        it.copy(
                            username = username,
                            isLoading = false
                        )
                    }
                }
                .onFailure { e ->
                    if (e is SessionExpiredException) {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                sessionExpired = true
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = e.message ?: "Error loading profile"
                            )
                        }
                    }
                }
        }
    }

    fun onUsernameChanged(value: String) {
        _uiState.update {
            it.copy(
                username = value,
                error = null,
                successMessage = null
            )
        }
    }

    fun onNewPasswordChanged(value: String) {
        _uiState.update {
            it.copy(
                newPassword = value,
                error = null,
                successMessage = null
            )
        }
    }

    fun onConfirmPasswordChanged(value: String) {
        _uiState.update {
            it.copy(
                confirmPassword = value,
                error = null,
                successMessage = null
            )
        }
    }

    fun saveProfile() {
        val state = _uiState.value

        if (state.username.isBlank()) {
            _uiState.update { it.copy(error = "Username cannot be empty") }
            return
        }

        if (state.newPassword.isNotBlank() && state.newPassword != state.confirmPassword) {
            _uiState.update { it.copy(error = "Passwords do not match") }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    error = null,
                    successMessage = null,
                    sessionExpired = false
                )
            }

            updateProfileUseCase(
                username = state.username,
                newPassword = state.newPassword.ifBlank { null }
            )
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            newPassword = "",
                            confirmPassword = "",
                            successMessage = "Profile updated successfully"
                        )
                    }
                }
                .onFailure { e ->
                    if (e is SessionExpiredException) {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                sessionExpired = true
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = e.message ?: "Error updating profile"
                            )
                        }
                    }
                }
        }
    }

    fun clearSessionExpired() {
        _uiState.update {
            it.copy(sessionExpired = false)
        }
    }
}