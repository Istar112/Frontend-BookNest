package es.dam.booknest.aplication.reading

import es.dam.booknest.model.IBookRepository
import es.dam.booknest.model.IReadingRepository
import es.dam.booknest.model.Reading

class GetUserReadingsUseCase(
    private val readingRepository: IReadingRepository,
    private val bookRepository: IBookRepository
) {
    suspend operator fun invoke(): Result<List<Reading>> {
        val readingsResult = readingRepository.getReadings()
        val booksResult = bookRepository.getAll()

        return if (readingsResult.isSuccess && booksResult.isSuccess) {
            val readings = readingsResult.getOrThrow()
            val books = booksResult.getOrThrow()

            val enrichedReadings = readings.map { reading ->
                val associatedBook = books.find { it.id == reading.idBook.toString() }
                reading.copy(book = associatedBook)
            }
            Result.success(enrichedReadings)
        } else {
            readingsResult
        }
    }
}
