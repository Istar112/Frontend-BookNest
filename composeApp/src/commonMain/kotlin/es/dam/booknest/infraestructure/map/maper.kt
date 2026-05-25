package es.dam.booknest.infraestructure.map

import es.dam.booknest.infraestructure.dto.BookDTO
import es.dam.booknest.infraestructure.dto.ReadingDTO
import es.dam.booknest.infraestructure.dto.ReadingStatusDTO
import es.dam.booknest.model.Book
import es.dam.booknest.model.Reading
import es.dam.booknest.model.ReadingStatus

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

fun ReadingDTO.toDomain(): Reading = Reading(
    idUser = idUser,
    idBook = idBook,
    idStatus = idStatus,
    readingStatus = readingStatus
)

fun ReadingStatusDTO.toDomain(): ReadingStatus = ReadingStatus(
    numPag = numPag,
    dateStart = dateStart,
    finishDate = finishDate,
    rating = rating
)
