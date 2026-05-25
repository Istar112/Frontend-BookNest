package es.dam.booknest.aplication.book.status

import es.dam.booknest.model.IBookRepository

class UpdateReadingUseCase(
    private val bookRepository: IBookRepository
) {
    suspend fun updateProcess(bookId: Int, numPag: Int, dateStart: String): Result<Unit> {
        return bookRepository.updateProcess(bookId, numPag, dateStart)
    }

    suspend fun updateFinished(bookId: Int, finishDate: String, rating: Int): Result<Unit> {
        return bookRepository.updateFinished(bookId, finishDate, rating)
    }
}
