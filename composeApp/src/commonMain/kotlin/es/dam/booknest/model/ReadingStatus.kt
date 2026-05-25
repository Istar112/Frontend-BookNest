package es.dam.booknest.model

import kotlinx.serialization.Serializable

@Serializable
data class ReadingStatus(
    val numPag: Int? = null,
    val dateStart: String? = null,
    val finishDate: String? = null,
    val rating: Int? = null
)
