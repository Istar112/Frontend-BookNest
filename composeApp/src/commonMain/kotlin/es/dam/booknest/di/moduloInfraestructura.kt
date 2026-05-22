package es.dam.booknest.di

import es.dam.booknest.infraestructure.book.RepositorioRestBook
import es.dam.booknest.infraestructure.user.RepositorioRestUser
import es.dam.booknest.model.IBookRepository
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
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                })
            }
            install(Logging) {
                level = LogLevel.ALL
            }
            defaultRequest {
                contentType(ContentType.Application.Json)
                header("Accept", "application/json")
                // Token temporal para pruebas hasta que el Login esté listo // Cuando se acabe habra que hacer otro login y poner otro token quitar cuando se implemente el login con token
                header("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ2aWN0b3IiLCJ1c2VyX2lkIjoxLCJleHAiOjE3Nzk5ODE0OTN9.Zg6D1SI3JX-F7ld5JrPMhLlFWEy6IW0n6XseuMzQays")
            }
        }
    }

    // Al usar 'adb reverse tcp:8000 tcp:8000', el móvil ve al PC como 'localhost'
    val host = "localhost"
    val baseUrl = "http://$host:8000/api/v1"

    single<IUserRepository> {
        RepositorioRestUser("$baseUrl/users", get())
    }

    single<IBookRepository> {
        RepositorioRestBook("$baseUrl/books/", get())
    }
}