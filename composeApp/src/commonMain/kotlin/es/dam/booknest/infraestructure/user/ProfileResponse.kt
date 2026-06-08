package es.dam.booknest.infraestructure.user

import kotlinx.serialization.Serializable

@Serializable
data class ProfileResponse(
    val username: String
)