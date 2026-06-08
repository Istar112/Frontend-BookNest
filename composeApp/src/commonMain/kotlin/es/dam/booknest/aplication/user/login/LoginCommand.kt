package es.dam.booknest.aplication.user.login

data class LoginCommand(
    val username: String,
    val password: String
)