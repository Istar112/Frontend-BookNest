package es.dam.booknest.infraestructure.user

object SessionManager {
    private var storage: SessionStorage? = null

    var accessToken: String? = null
        private set

    var refreshToken: String? = null
        private set

    fun init(sessionStorage: SessionStorage) {
        storage = sessionStorage
        accessToken = sessionStorage.getAccessToken()
        refreshToken = sessionStorage.getRefreshToken()
    }

    fun saveTokens(newAccessToken: String, newRefreshToken: String) {
        accessToken = newAccessToken
        refreshToken = newRefreshToken
        storage?.saveTokens(newAccessToken, newRefreshToken)
    }

    fun clear() {
        accessToken = null
        refreshToken = null
        storage?.clear()
    }
}