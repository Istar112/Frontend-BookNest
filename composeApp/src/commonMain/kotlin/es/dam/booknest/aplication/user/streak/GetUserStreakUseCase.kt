package es.dam.booknest.aplication.user.streak

import es.dam.booknest.model.IUserRepository
import es.dam.booknest.model.UserStreak


class GetUserStreakUseCase(private val IUserRepository: IUserRepository) {
    suspend operator fun invoke(): Result<UserStreak> {
        return IUserRepository.getUserStreak()
    }
}