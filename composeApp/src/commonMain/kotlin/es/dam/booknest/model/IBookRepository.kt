package es.dam.booknest.model

interface IBookRepository {
    suspend fun getAll(): Result<List<Book>>
    suspend fun addToProcess(bookId: Int, numPag: Int, dateStart: String): Result<Unit>
    suspend fun addToFinished(bookId: Int, finishDate: String, rating: Int): Result<Unit>
    suspend fun getReadingStatus(bookId: Int): Result<ReadingStatus>
    suspend fun updateProcess(bookId: Int, numPag: Int, dateStart: String): Result<Unit>
    suspend fun updateFinished(bookId: Int, finishDate: String, rating: Int): Result<Unit>
}