package es.dam.booknest.aplication.book.list

import es.dam.booknest.model.Book
import es.dam.booknest.model.IBookRepository

class GetAllBooksUseCase(
    private val bookRepository: IBookRepository
) {
    suspend operator fun invoke(): Result<List<Book>> {
        return bookRepository.getAll()
    }
}