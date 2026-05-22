package es.dam.booknest.infraestructure.book

import es.dam.booknest.infraestructure.dto.BookDTO
import es.dam.booknest.infraestructure.map.toDomain
import es.dam.booknest.model.Book
import es.dam.booknest.model.IBookRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode

class RepositorioRestBook(
    val url: String,
    val client: HttpClient
) : IBookRepository {
    override suspend fun getAll(): Result<List<Book>> {
        return try {
            val response = client.get(url)
            if (response.status == HttpStatusCode.OK) {
                val dtos = response.body<List<BookDTO>>()
                Result.success(dtos.map { it.toDomain() })
            } else {
                Result.failure(Exception("Error al obtener libros: ${response.status.value}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
