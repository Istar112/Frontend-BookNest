package es.dam.booknest.di

import es.dam.booknest.infraestructure.book.RestBookRepository
import es.dam.booknest.infraestructure.reading.RestReadingRepository
import es.dam.booknest.infraestructure.user.AuthManager
import es.dam.booknest.infraestructure.user.RepositorioRestUser
import es.dam.booknest.infraestructure.user.SessionManager
import es.dam.booknest.model.IBookRepository
import es.dam.booknest.model.IReadingRepository
import es.dam.booknest.model.IUserRepository
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val moduloInfraestructura = module {
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        prettyPrint = true
                        isLenient = true
                    }
                )
            }

            install(Logging) {
                level = LogLevel.ALL
            }

            defaultRequest {
                contentType(ContentType.Application.Json)
                header("Accept", "application/json")

                SessionManager.accessToken?.let { token ->
                    header("Authorization", "Bearer $token")
                }
            }
        }
    }

    single { AuthManager(get()) }

    val host = "localhost"
    //val host = "10.0.2.2"
    val baseUrl = "http://$host:8000/api/v1"

    single<IUserRepository> {
        RepositorioRestUser("$baseUrl/users", get())
    }

    single<IBookRepository> {
        RestBookRepository("$baseUrl/books/", get(), get())
    }

    single<IReadingRepository> {
        RestReadingRepository("$baseUrl/readings/", get(), get())
    }
}