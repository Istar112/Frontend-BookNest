package es.dam.booknest.aplication.book.status

import es.dam.booknest.model.IBookRepository
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class AddBookToProcessUseCase(
    private val bookRepository: IBookRepository
) {
    suspend operator fun invoke(bookId: Int, numPag: Int = 0, dateStart: String? = null): Result<Unit> {
        val dateString = if (dateStart != null && dateStart.isNotBlank()) {
            dateStart
        } else {
            val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            "${now.year}-${now.monthNumber.toString().padStart(2, '0')}-${now.dayOfMonth.toString().padStart(2, '0')}"
        }
        
        return bookRepository.addToProcess(
            bookId = bookId,
            numPag = numPag,
            dateStart = dateString
        )
    }
}
