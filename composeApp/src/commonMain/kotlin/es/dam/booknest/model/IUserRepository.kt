package es.dam.booknest.model

import es.dam.booknest.aplication.user.login.LoginCommand
import es.dam.booknest.aplication.user.signup.CreateUserCommand
import es.dam.booknest.infraestructure.user.TokenResponse

interface IUserRepository {
    suspend fun add(command: CreateUserCommand): Result<String>
    suspend fun login(command: LoginCommand): Result<TokenResponse>
    suspend fun refreshToken(refreshToken: String): Result<TokenResponse>
    suspend fun getProfile(): Result<String>
    suspend fun updateProfile(username: String, newPassword: String?): Result<Unit>
    suspend fun getUserStreak(): Result<UserStreak>
}