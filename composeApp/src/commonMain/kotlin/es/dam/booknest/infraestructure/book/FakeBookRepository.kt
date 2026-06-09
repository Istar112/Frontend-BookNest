package es.dam.booknest.infraestructure.book

import es.dam.booknest.model.Book
import es.dam.booknest.model.IBookRepository
import es.dam.booknest.model.ReadingStatus

class FakeBookRepository : IBookRepository {
    override suspend fun getAll(): Result<List<Book>> {
        val books = listOf(
            Book("1", "978-0140449136", "The Odyssey", "Classic", 544, "1946-01-01", true, "https://res.cloudinary.com/dccdeyylr/image/upload/q_auto/f_auto/v1779374661/odyssey_b3beqt.jpg"),
            Book("2", "978-0141439518", "Pride and Prejudice", "Romance", 480, "1813-01-28", true, "https://res.cloudinary.com/dccdeyylr/image/upload/q_auto/f_auto/v1779374661/pride_zej0gw.jpg"),
            Book("3", "978-0142437230", "Don Quixote", "Adventure", 1072, "1605-01-16", false, "https://res.cloudinary.com/dccdeyylr/image/upload/q_auto/f_auto/v1779372804/images_cui60x.jpg"),
            Book("4", "978-0451524935", "1984", "Dystopian", 328, "1949-06-08", true, "https://m.media-amazon.com/images/I/71kxa1-0mfL.jpg"),
            Book("5", "978-0743273565", "The Great Gatsby", "Classic", 180, "1925-04-10", false, "https://m.media-amazon.com/images/I/81af+MCATTL.jpg")
        )
        return Result.success(books)
    }

    override suspend fun addToProcess(bookId: Int, numPag: Int, dateStart: String): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun addToFinished(bookId: Int, finishDate: String, rating: Int): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun getReadingStatus(bookId: Int): Result<ReadingStatus> {
        return Result.success(
            ReadingStatus(
                numPag = 120,
                dateStart = "2024-01-01",
                rating = 8
            )
        )
    }

    override suspend fun updateProcess(bookId: Int, numPag: Int, dateStart: String): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun updateFinished(bookId: Int, finishDate: String, rating: Int): Result<Unit> {
        return Result.success(Unit)
    }
}
