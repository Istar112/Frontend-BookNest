package es.dam.booknest.infraestructure.user

import io.ktor.client.plugins.ClientRequestException
import io.ktor.http.HttpStatusCode

suspend fun <T> executeWithAutoRefresh(
    authManager: AuthManager,
    block: suspend () -> T
): T {
    return try {
        block()
    } catch (e: ClientRequestException) {
        if (e.response.status == HttpStatusCode.Unauthorized) {
            val refreshed = authManager.refreshSession()
            if (refreshed) {
                block()
            } else {
                throw SessionExpiredException()
            }
        } else {
            throw e
        }
    }
}