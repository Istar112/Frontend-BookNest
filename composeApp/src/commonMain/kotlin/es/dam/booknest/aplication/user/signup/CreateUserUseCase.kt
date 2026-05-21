package es.dam.booknest.aplication.user.signup

import es.dam.booknest.model.IUserRepository
import es.dam.booknest.model.User

/**
 * Lllama al repositorio y hace la peticion al servidor
 */
class CreateUserUseCase(
    private val userRepository: IUserRepository
) {
    suspend operator fun invoke(command: CreateUserCommand) : Result<String> {
        return userRepository.add(command)
    }
}