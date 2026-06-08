package es.dam.booknest.infraestructure.user

import es.dam.booknest.aplication.user.login.RefreshTokenUseCase

class AuthManager(
    private val refreshTokenUseCase: RefreshTokenUseCase
) {
    suspend fun refreshSession(): Boolean {
        val currentRefreshToken = SessionManager.refreshToken ?: return false

        val result = refreshTokenUseCase(currentRefreshToken)

        return result.fold(
            onSuccess = { tokenResponse ->
                SessionManager.saveTokens(
                    newAccessToken = tokenResponse.accessToken,
                    newRefreshToken = tokenResponse.refreshToken
                )
                true
            },
            onFailure = {
                SessionManager.clear()
                false
            }
        )
    }

    fun logout() {
        SessionManager.clear()
    }
}