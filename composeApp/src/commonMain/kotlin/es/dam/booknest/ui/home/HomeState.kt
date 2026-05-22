package es.dam.booknest.ui.home

import es.dam.booknest.model.Book

data class HomeState(
    val books: List<Book> = emptyList(),
    val selectedBook: Book? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)