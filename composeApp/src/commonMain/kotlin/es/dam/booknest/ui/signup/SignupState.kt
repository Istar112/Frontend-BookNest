package es.dam.booknest.ui.signup

//Este estado controla la vista del formulario
data class SignupState (
    val username: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val password: String = "",
    val confirmPassword: String = ""

    //errores de validacion
)
