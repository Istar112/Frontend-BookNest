package es.dam.booknest.aplication.book.status

import es.dam.booknest.model.IBookRepository
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class AddBookToFinishedUseCase(
    private val bookRepository: IBookRepository
) {
    suspend operator fun invoke(bookId: Int, rating: Int = 5, finishDate: String? = null): Result<Unit> {
        val dateString = if (finishDate != null && finishDate.isNotBlank()) {
            finishDate
        } else {
            val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            "${now.year}-${now.monthNumber.toString().padStart(2, '0')}-${now.dayOfMonth.toString().padStart(2, '0')}"
        }
        
        return bookRepository.addToFinished(
            bookId = bookId,
            finishDate = dateString,
            rating = rating
        )
    }
}
