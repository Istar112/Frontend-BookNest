package es.dam.booknest.infraestructure.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateProfileRequest(
    val username: String,
    @SerialName("new_password")
    val newPassword: String? = null
)