package es.dam.booknest.infraestructure.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReadingDTO(
    @SerialName("id_user") val idUser: Int,
    @SerialName("id_book") val idBook: Int,
    @SerialName("id_status") val idStatus: Int,
    @SerialName("reading_status") val readingStatus: String
)
