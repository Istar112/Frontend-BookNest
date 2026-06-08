package es.dam.booknest.aplication.user.profile

import es.dam.booknest.model.IUserRepository

class GetProfileUseCase(
    private val repository: IUserRepository
) {
    suspend operator fun invoke(): Result<String> {
        return repository.getProfile()
    }
}