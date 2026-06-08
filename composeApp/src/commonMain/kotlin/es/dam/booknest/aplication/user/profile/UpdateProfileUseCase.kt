package es.dam.booknest.aplication.user.profile

import es.dam.booknest.model.IUserRepository

class UpdateProfileUseCase(
    private val repository: IUserRepository
) {
    suspend operator fun invoke(username: String, newPassword: String?): Result<Unit> {
        return repository.updateProfile(username, newPassword)
    }
}