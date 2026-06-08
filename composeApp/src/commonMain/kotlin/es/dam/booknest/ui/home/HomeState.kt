package es.dam.booknest.ui.home

import es.dam.booknest.model.Book

data class HomeState(
    val yourBooks: List<Book> = emptyList(),
    val recommendedBooks: List<Book> = emptyList(),
    val selectedBook: Book? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null,
    val sessionExpired: Boolean = false,
    val streakDays: Int = 0,
    val readingDays: List<Int> = emptyList()
)