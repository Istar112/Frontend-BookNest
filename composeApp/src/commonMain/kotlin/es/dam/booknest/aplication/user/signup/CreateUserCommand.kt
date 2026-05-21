package es.dam.booknest.aplication.user.signup

import kotlinx.serialization.Serializable

@Serializable
class CreateUserCommand (
    val username: String,
    val password: String,
    val name: String,
    val email: String,
    val phone: String
)