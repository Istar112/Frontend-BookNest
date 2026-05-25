package es.dam.booknest.infraestructure.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StatusUpdateFinishedDTO(
    @SerialName("finish_date") val finishDate: String,
    val rating: Int
)
