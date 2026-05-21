package es.dam.booknest.infraestructure

import es.dam.booknest.aplication.user.signup.CreateUserCommand
import es.dam.booknest.model.IUserRepository
import es.dam.booknest.model.User
import io.ktor.client.HttpClient
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.HttpStatusCode
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.post
import io.ktor.client.request.setBody


class RepositorioRestUser(
    private val url: String,
    private val client: HttpClient
) : IUserRepository {

    override suspend fun add(command: CreateUserCommand): Result<String> {
        return try {
            val response = client.post("${url}/signup/") {
                contentType(ContentType.Application.Json)
                setBody(command)
            }

            when (response.status) {
                HttpStatusCode.Created, HttpStatusCode.OK -> {
                    try {
                        val body = response.body<Map<String, String>>()
                        Result.success(body["message"] ?: "Usuario creado correctamente")
                    } catch (e: Exception) {
                        // Si no es un JSON con "message", devolvemos el texto plano
                        Result.success(response.bodyAsText())
                    }
                }

                else -> {
                    Result.failure(Exception("Error inesperado: ${response.status.value}"))
                }
            }
        } catch (e: ClientRequestException) {
            when (e.response.status) {
                HttpStatusCode.Conflict -> {
                    Result.failure(Exception("El usuario o el teléfono ya están registrados"))
                }

                HttpStatusCode.UnprocessableEntity -> {
                    Result.failure(Exception("Error de validación: revisa el formato de los datos"))
                }

                HttpStatusCode.BadRequest -> {
                    Result.failure(Exception("Petición incorrecta"))
                }

                else -> {
                    Result.failure(Exception("Error inesperado: ${e.response.status.value}"))
                }
            }
        } catch (e: ServerResponseException) {
            when (e.response.status) {
                HttpStatusCode.InternalServerError -> {
                    Result.failure(Exception("Error en el servidor: posible dato demasiado largo"))
                }

                else -> {
                    Result.failure(Exception("Error del servidor: ${e.response.status.value}"))
                }
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        }
    }
}