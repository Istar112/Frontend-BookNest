package es.dam.booknest.di

import es.dam.booknest.infraestructure.RepositorioRestUser
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
            }
        }
    }

    // Tu IP local detectada: 192.168.1.9
    // Usamos esta IP para Android (tanto emulador como móvil real)
    //val host = if (getPlatform().name.contains("Android")) "192.168.1.9" else "localhost"
    val host = "localhost"
    val baseUrl = "http://$host:8000/api/v1"

    single<IUserRepository> {
        RepositorioRestUser("$baseUrl/users", get())
    }

}