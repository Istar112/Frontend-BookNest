package es.dam.booknest.ui.profile

data class ProfileState(
    val username: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null,
    val sessionExpired: Boolean = false
)