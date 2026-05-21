package es.dam.booknest.model

import es.dam.booknest.aplication.user.signup.CreateUserCommand

interface IUserRepository {
    suspend fun add(command: CreateUserCommand): Result<String>
}