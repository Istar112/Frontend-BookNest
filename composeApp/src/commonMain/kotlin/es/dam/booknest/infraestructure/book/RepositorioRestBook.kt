package es.dam.booknest.infraestructure.book

import es.dam.booknest.infraestructure.dto.BookDTO
import es.dam.booknest.infraestructure.dto.ReadingStatusDTO
import es.dam.booknest.infraestructure.dto.StatusUpdateFinishedDTO
import es.dam.booknest.infraestructure.dto.StatusUpdateProcessDTO
import es.dam.booknest.infraestructure.map.toDomain
import es.dam.booknest.model.Book
import es.dam.booknest.model.IBookRepository
import es.dam.booknest.model.ReadingStatus
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess

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

    override suspend fun addToProcess(bookId: Int, numPag: Int, dateStart: String): Result<Unit> {
        return try {
            val response = client.post("$url$bookId/status/process") {
                setBody(StatusUpdateProcessDTO(numPag, dateStart))
            }
            if (response.status.isSuccess()) {
                Result.success(Unit)
            } else {
                val errorBody = response.bodyAsText()
                Result.failure(Exception("Error (${response.status.value}): $errorBody"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addToFinished(bookId: Int, finishDate: String, rating: Int): Result<Unit> {
        return try {
            val response = client.post("$url$bookId/status/finished") {
                setBody(StatusUpdateFinishedDTO(finishDate, rating))
            }
            if (response.status.isSuccess()) {
                Result.success(Unit)
            } else {
                val errorBody = response.bodyAsText()
                Result.failure(Exception("Error (${response.status.value}): $errorBody"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getReadingStatus(bookId: Int): Result<ReadingStatus> {
        return try {
            val response = client.get("$url$bookId/status")
            if (response.status == HttpStatusCode.OK) {
                val dto = response.body<ReadingStatusDTO>()
                Result.success(dto.toDomain())
            } else {
                val errorBody = response.bodyAsText()
                Result.failure(Exception("Error al obtener estado (${response.status.value}): $errorBody"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateProcess(bookId: Int, numPag: Int, dateStart: String): Result<Unit> {
        return try {
            val response = client.put("$url$bookId/status/process") {
                setBody(StatusUpdateProcessDTO(numPag, dateStart))
            }
            if (response.status.isSuccess()) {
                Result.success(Unit)
            } else {
                val errorBody = response.bodyAsText()
                Result.failure(Exception("Error al actualizar (${response.status.value}): $errorBody"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateFinished(bookId: Int, finishDate: String, rating: Int): Result<Unit> {
        return try {
            val response = client.put("$url$bookId/status/finished") {
                setBody(StatusUpdateFinishedDTO(finishDate, rating))
            }
            if (response.status.isSuccess()) {
                Result.success(Unit)
            } else {
                val errorBody = response.bodyAsText()
                Result.failure(Exception("Error al actualizar (${response.status.value}): $errorBody"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
