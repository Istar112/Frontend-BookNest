package es.dam.booknest.infraestructure.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StatusUpdateProcessDTO(
    @SerialName("num_pag") val numPag: Int,
    @SerialName("date_start") val dateStart: String
)

