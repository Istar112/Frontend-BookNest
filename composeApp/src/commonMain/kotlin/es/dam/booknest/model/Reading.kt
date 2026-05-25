package es.dam.booknest.model

import kotlinx.serialization.Serializable

@Serializable
data class Reading(
    val idUser: Int,
    val idBook: Int,
    val idStatus: Int,
    val readingStatus: String,
    val book: Book? = null,
    val statusDetails: ReadingStatus? = null
)
