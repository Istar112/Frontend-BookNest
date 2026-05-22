package es.dam.booknest.infraestructure.map

import es.dam.booknest.infraestructure.dto.BookDTO
import es.dam.booknest.model.Book

fun BookDTO.toDomain(): Book = Book(
    id = id?.toString(),
    isbn = isbn,
    title = title,
    category = category,
    totalPages = totalPages,
    publicationDate = publicationDate,
    purchased = purchased,
    coverImage = coverImage,
    desired = desired
)
