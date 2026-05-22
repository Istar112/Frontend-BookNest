package es.dam.booknest.model

interface IBookRepository {
    suspend fun getAll(): Result<List<Book>>
}