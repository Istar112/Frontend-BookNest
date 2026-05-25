package es.dam.booknest.aplication.book.status

import es.dam.booknest.model.IBookRepository
import es.dam.booknest.model.ReadingStatus

class GetReadingStatusUseCase(
    private val bookRepository: IBookRepository
) {
    suspend operator fun invoke(bookId: Int): Result<ReadingStatus> {
        return bookRepository.getReadingStatus(bookId)
    }
}
