package es.dam.booknest.model

data class Book(
     private val id: String,
    private val isbn: String,
    private val title:String,
    private val category: String,
    private val totalPages: Int,
    private val publicationDate: String,
    private val purchased: Boolean,
    private val coverImage: String
)
