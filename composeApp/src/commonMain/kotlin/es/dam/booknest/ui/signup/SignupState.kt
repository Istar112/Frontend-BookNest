package es.dam.booknest.ui.signup

/**
 * Esta clase es el el estado del formulario de registro con sus errores,
 * es la que controla lo que se ve.
 */
data class SignupState (
    val username: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isSignupSuccess: Boolean = false,

    // visibilidad
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,

    //errores de validacion
    val usernameError: String? = null,
    val nameError: String? = null,
    val emailError: String? = null,
    val phoneError: String? = null,
    val passwordError: String? = null,

    val signupError: String? = null,
    val successMessage: String? = null


)
