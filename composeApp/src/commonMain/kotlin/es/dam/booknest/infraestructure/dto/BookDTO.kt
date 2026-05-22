package es.dam.booknest.infraestructure.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookDTO(
    val id: Int? = null,
    val isbn: String,
    val title: String,
    val category: String,
    @SerialName("total_pages")
    val totalPages: Int,
    @SerialName("publication_date")
    val publicationDate: String,
    val purchased: Boolean,
    @SerialName("cover_image")
    val coverImage: String,
    val desired: Boolean = false
)
