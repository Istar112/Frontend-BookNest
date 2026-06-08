package es.dam.booknest.aplication.user.login

import es.dam.booknest.infraestructure.user.TokenResponse
import es.dam.booknest.model.IUserRepository

class RefreshTokenUseCase(
    private val repositorio: IUserRepository
) {
    suspend operator fun invoke(refreshToken: String): Result<TokenResponse> {
        return repositorio.refreshToken(refreshToken)
    }
}