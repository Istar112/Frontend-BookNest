package es.dam.booknest.infraestructure.user

import es.dam.booknest.aplication.user.login.LoginCommand
import es.dam.booknest.aplication.user.signup.CreateUserCommand
import es.dam.booknest.model.IUserRepository
import es.dam.booknest.model.UserStreak
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.parameters

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
                        Result.success(body["message"] ?: "User created successfully")
                    } catch (e: Exception) {
                        Result.success(response.bodyAsText())
                    }
                }

                else -> {
                    Result.failure(Exception("Unexpected error: ${response.status.value}"))
                }
            }
        } catch (e: ClientRequestException) {
            when (e.response.status) {
                HttpStatusCode.Conflict -> {
                    Result.failure(Exception("Username or phone number is already registered"))
                }

                HttpStatusCode.UnprocessableEntity -> {
                    Result.failure(Exception("Validation error: check the data format"))
                }

                HttpStatusCode.BadRequest -> {
                    Result.failure(Exception("Bad request"))
                }

                else -> {
                    Result.failure(Exception("Unexpected error: ${e.response.status.value}"))
                }
            }
        } catch (e: ServerResponseException) {
            when (e.response.status) {
                HttpStatusCode.InternalServerError -> {
                    Result.failure(Exception("Server error: possible oversized field value"))
                }

                else -> {
                    Result.failure(Exception("Server error: ${e.response.status.value}"))
                }
            }
        } catch (e: Exception) {
            Result.failure(Exception("Connection error: ${e.message}"))
        }
    }

    override suspend fun login(command: LoginCommand): Result<TokenResponse> {
        return try {
            val response = client.submitForm(
                url = "${url}/login/",
                formParameters = parameters {
                    append("username", command.username)
                    append("password", command.password)
                }
            )

            when (response.status) {
                HttpStatusCode.OK -> {
                    val body = response.body<TokenResponse>()
                    Result.success(body)
                }

                else -> {
                    Result.failure(Exception("Unexpected error: ${response.status.value}"))
                }
            }
        } catch (e: ClientRequestException) {
            when (e.response.status) {
                HttpStatusCode.Unauthorized -> {
                    Result.failure(Exception("Invalid username or password"))
                }

                HttpStatusCode.BadRequest -> {
                    Result.failure(Exception("Bad request"))
                }

                else -> {
                    Result.failure(Exception("Unexpected error: ${e.response.status.value}"))
                }
            }
        } catch (e: ServerResponseException) {
            Result.failure(Exception("Server error: ${e.response.status.value}"))
        } catch (e: Exception) {
            Result.failure(Exception("Connection error: ${e.message}"))
        }
    }

    override suspend fun refreshToken(refreshToken: String): Result<TokenResponse> {
        return try {
            val response = client.post("${url}/refresh/") {
                contentType(ContentType.Application.Json)
                setBody(RefreshTokenRequest(refreshToken = refreshToken))
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    val body = response.body<TokenResponse>()
                    Result.success(body)
                }

                else -> {
                    Result.failure(Exception("Unexpected error while refreshing token: ${response.status.value}"))
                }
            }
        } catch (e: ClientRequestException) {
            when (e.response.status) {
                HttpStatusCode.Unauthorized -> {
                    Result.failure(Exception("Invalid or expired refresh token"))
                }

                HttpStatusCode.BadRequest -> {
                    Result.failure(Exception("Bad request"))
                }

                else -> {
                    Result.failure(Exception("Unexpected error: ${e.response.status.value}"))
                }
            }
        } catch (e: ServerResponseException) {
            Result.failure(Exception("Server error: ${e.response.status.value}"))
        } catch (e: Exception) {
            Result.failure(Exception("Connection error: ${e.message}"))
        }
    }

    override suspend fun getProfile(): Result<String> {
        return try {
            val response = client.get("${url}/me/")

            when (response.status) {
                HttpStatusCode.OK -> {
                    val body = response.body<ProfileResponse>()
                    Result.success(body.username)
                }

                HttpStatusCode.Unauthorized -> {
                    Result.failure(SessionExpiredException())
                }

                else -> {
                    val errorBody = response.bodyAsText()
                    Result.failure(Exception("Unexpected error loading profile (${response.status.value}): $errorBody"))
                }
            }
        } catch (e: ClientRequestException) {
            when (e.response.status) {
                HttpStatusCode.Unauthorized -> {
                    Result.failure(SessionExpiredException())
                }

                else -> {
                    Result.failure(Exception("Request error: ${e.response.status.value}"))
                }
            }
        } catch (e: ServerResponseException) {
            Result.failure(Exception("Server error: ${e.response.status.value}"))
        } catch (e: Exception) {
            Result.failure(Exception("Connection error: ${e.message}"))
        }
    }

    override suspend fun updateProfile(username: String, newPassword: String?): Result<Unit> {
        return try {
            val response = client.put("${url}/me/") {
                contentType(ContentType.Application.Json)
                setBody(
                    UpdateProfileRequest(
                        username = username,
                        newPassword = newPassword
                    )
                )
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    Result.success(Unit)
                }

                HttpStatusCode.Unauthorized -> {
                    Result.failure(SessionExpiredException())
                }

                else -> {
                    val errorBody = response.bodyAsText()
                    Result.failure(Exception("Unexpected error updating profile (${response.status.value}): $errorBody"))
                }
            }
        } catch (e: ClientRequestException) {
            when (e.response.status) {
                HttpStatusCode.Unauthorized -> {
                    Result.failure(SessionExpiredException())
                }

                HttpStatusCode.Conflict -> {
                    Result.failure(Exception("Username is already in use"))
                }

                HttpStatusCode.BadRequest -> {
                    Result.failure(Exception("Bad request"))
                }

                else -> {
                    Result.failure(Exception("Request error: ${e.response.status.value}"))
                }
            }
        } catch (e: ServerResponseException) {
            Result.failure(Exception("Server error: ${e.response.status.value}"))
        } catch (e: Exception) {
            Result.failure(Exception("Connection error: ${e.message}"))
        }
    }

    override suspend fun getUserStreak(): Result<UserStreak> {
        return try {
            val response = client.get("${url}/streak/")

            when (response.status) {
                HttpStatusCode.OK -> {
                    val body = response.body<UserStreak>()
                    Result.success(body)
                }

                HttpStatusCode.Unauthorized -> {
                    Result.failure(SessionExpiredException())
                }

                else -> {
                    val errorBody = response.bodyAsText()
                    Result.failure(Exception("Unexpected error loading streak (${response.status.value}): $errorBody"))
                }
            }
        } catch (e: ClientRequestException) {
            when (e.response.status) {
                HttpStatusCode.Unauthorized -> {
                    Result.failure(SessionExpiredException())
                }

                else -> {
                    Result.failure(Exception("Request error: ${e.response.status.value}"))
                }
            }
        } catch (e: ServerResponseException) {
            Result.failure(Exception("Server error: ${e.response.status.value}"))
        } catch (e: Exception) {
            Result.failure(Exception("Connection error: ${e.message}"))
        }
    }
}