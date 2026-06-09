package es.dam.booknest.infraestructure.reading

import es.dam.booknest.infraestructure.dto.ReadingDTO
import es.dam.booknest.infraestructure.map.toDomain
import es.dam.booknest.infraestructure.user.AuthManager
import es.dam.booknest.infraestructure.user.executeWithAutoRefresh
import es.dam.booknest.model.IReadingRepository
import es.dam.booknest.model.Reading
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode

class RestReadingRepository(
    private val url: String,
    private val client: HttpClient,
    private val authManager: AuthManager
) : IReadingRepository {

    override suspend fun getReadings(): Result<List<Reading>> {
        return try {
            val response = executeWithAutoRefresh(authManager) {
                client.get(url)
            }

            if (response.status == HttpStatusCode.OK) {
                val dtos = response.body<List<ReadingDTO>>()
                Result.success(dtos.map { it.toDomain() })
            } else {
                val errorBody = response.bodyAsText()
                Result.failure(Exception("Error fetching readings (${response.status.value}): $errorBody"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}