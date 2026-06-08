package es.dam.booknest.infraestructure.user

import android.content.Context

class AuthStorage(context: Context) : SessionStorage {

    private val prefs = context.getSharedPreferences("booknest_auth", Context.MODE_PRIVATE)

    override fun saveTokens(accessToken: String, refreshToken: String) {
        prefs.edit()
            .putString("access_token", accessToken)
            .putString("refresh_token", refreshToken)
            .apply()
    }

    override fun getAccessToken(): String? {
        return prefs.getString("access_token", null)
    }

    override fun getRefreshToken(): String? {
        return prefs.getString("refresh_token", null)
    }

    override fun clear() {
        prefs.edit().clear().apply()
    }
}