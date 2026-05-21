package es.dam.booknest.model

import kotlinx.serialization.Serializable
@Serializable
data class User(
    val id: Int,
    val username: String,
    val name: String,
    val email: String,
    val phone: String
)