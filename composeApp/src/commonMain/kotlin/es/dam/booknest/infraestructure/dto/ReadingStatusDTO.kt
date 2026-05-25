package es.dam.booknest.infraestructure.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReadingStatusDTO(
    @SerialName("num_pag") val numPag: Int? = null,
    @SerialName("date_start") val dateStart: String? = null,
    @SerialName("finish_date") val finishDate: String? = null,
    val rating: Int? = null
)
